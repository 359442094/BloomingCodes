package cn.blooming.bep.crawler.data.controller;
import java.util.*;
import cn.blooming.bep.common.api.dataenum.FundType;
import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.ErrorCode;
import cn.blooming.bep.crawler.LuceneUrl;
import cn.blooming.bep.crawler.model.entity.*;
import cn.blooming.bep.crawler.model.lucene.ik.IKAnalyzer4Lucene7;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.convert.FundConvert;
import cn.blooming.bep.crawler.model.util.LuceneUtil;
import cn.blooming.bep.fund.api.QueryFundProductAPI;
import cn.blooming.bep.fund.model.FundProduct;
import cn.blooming.bep.fund.model.QueryFundProductRequest;
import cn.blooming.bep.fund.model.QueryFundProductResponse;
import com.github.pagehelper.Page;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.blooming.bep.data.api.SearchFundsAPI;
import cn.blooming.bep.data.api.model.SearchFundsRequest;
import cn.blooming.bep.data.api.model.SearchFundsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"查询基金列表"})
public class SearchFundsController implements SearchFundsAPI {

    private static final Logger log= LoggerFactory.getLogger(SearchFundsController.class);

	@Autowired
    private QueryFundProductAPI queryFundProductAPI;
    @Autowired
    private FundService fundService;

	@Override
    @ResponseBody
	@RequestMapping(path = "/fund/search",method = RequestMethod.POST)
	@ApiOperation(value = "查询基金列表", notes = "查询基金列表")
	public SearchFundsResponse process(SearchFundsRequest request) throws Exception {
        log.debug("查询基金列表:"+request);

        List<String> codes=new ArrayList<>();
        QueryFundProductRequest queryFundProductRequest=new QueryFundProductRequest();
        queryFundProductRequest.setClientId(ClientInfo.CLIENT_ID);                 //中奥物业zawy
        queryFundProductRequest.setStaffUsername(ClientInfo.STAFF_USERNAME);       //API_USER
        queryFundProductRequest.setVersion(ClientInfo.VERSION);
        queryFundProductRequest.setPage(request.getPage());
        queryFundProductRequest.setPageSize(10000);

        //查询起始行
        Integer page=request.getPage();
        //查询数据行数
        Integer pageSize=request.getPageSize();
        FundExample example=new FundExample();

        String type="";
        if(!StringUtils.isEmpty(request.getType())){
            type=request.getType().getValue();
            queryFundProductRequest.setPrdType(type);
        }

        QueryFundProductResponse process = queryFundProductAPI.process(queryFundProductRequest);
        List<FundProduct> list = process.getList();
        for (FundProduct product : list) {
            codes.add(product.getPrdCode());
        }
        String sort=request.getSort();
        if(!StringUtils.isEmpty(sort)){
            switch (sort){
                case "sinceStartup":
                    sort="rate_start";
                    break;
                case "year0":
                    sort="rate_current_year";
                    break;
                case "day0":
                    sort="rate_net_value";
                    break;
                case "month1":
                    sort="rate_month";
                    break;
                case "month3":
                    sort="rate_quarter";
                    break;
                case "month6":
                    sort="rate_half";
                    break;
                case "year1":
                    sort="rate_year";
                    break;
                case "value":
                case "day7":
                default:
                    sort="rate_7days";
                    break;
            }
        }

        //排序类型
        String sortOrder=request.getSortOrder();
        if(StringUtils.isEmpty(sortOrder)){
            sortOrder="desc";
        }
        example.setOrderByClause(sort +" " +sortOrder);

        //基金类型
        FundType types=request.getType();

        FundExample.Criteria criteria = example.createCriteria();
        criteria.andCodeIn(codes);
        if(!StringUtils.isEmpty(types)){
            criteria.andTypeEqualTo(types.getValue());
        }
        SearchFundsResponse searchFundsResponse=new SearchFundsResponse();
        //1.增加模糊查询 使用lucene全文检索
        if(!StringUtils.isEmpty(request.getSearch())){
            List<cn.blooming.bep.data.api.model.Fund> funds=new ArrayList<>();
            List<String> indexTypes=new ArrayList<>();
            String search = request.getSearch();
            //判断用户输入类型 数字、中文、字母
            String searchType = LuceneUtil.isSearch(search);
            //添加数据所在域名称
            switch (searchType){
                case "number":
                    FundExample fundExample=new FundExample();
                    fundExample.createCriteria().andCodeLike("%"+ search +"%");
                    return searchByLike(fundExample, page, pageSize);
                case "chinese":
                    //按关键词显示联想结果
                    //indexTypes.add("key");
                    indexTypes.add("fundNameAll");
                    indexTypes.add("fundName");
                    indexTypes.add("fundHouseNameAll");
                    indexTypes.add("fundHouseName");
                    break;
                case "letter":
                    indexTypes.add("quanpin");
                    indexTypes.add("jianpin");
                    break;
                default:
                    //其它
                    log.error("用户输入类型未能识别:"+searchType);
                    throw new BEPServiceException(ErrorCode.USER_INPUT_TYPE_NOT_RECOGNIZED,"用户输入类型未能识别");
            }
            //精确检索
            for (String indexType : indexTypes) {
                log.debug("正在按["+indexType+":"+search+"]精确检索");
                Query query = LuceneUtil.doQuery(indexType,search);
                funds = queryIndex(indexType, query, search, searchFundsResponse, page, pageSize, funds);
                if(funds.size()!=0){
                    log.debug("精确检索匹配完毕");
                    break;
                }
            }
            log.debug("精确检索匹配出的基金相关的记录总数:" + funds.size());
            log.debug("精确检索匹配出的基金内容:" + funds);

            if(funds.size()==0){
                //模糊检索
                for (String indexType : indexTypes) {
                    log.debug("正在按["+indexType+":"+search+"]模糊检索");
                    Analyzer analyzer=new IKAnalyzer4Lucene7(true);
                    //模糊检索
                    Query query=LuceneUtil.doQuery(indexType+":"+search,analyzer);
                    funds = queryIndex(indexType, query, search, searchFundsResponse, page, pageSize, funds);
                    if(funds.size()!=0){
                        log.debug("模糊检索匹配完毕");
                        break;
                    }
                }
                log.debug("模糊检索匹配出的基金相关的记录总数:" + funds.size());
                log.debug("模糊检索匹配出的基金内容:" + funds);
                if(funds.size()==0){
                    FundExample fundExample=new FundExample();
                    fundExample.createCriteria().andNameLike("%" +search+ "%");
                    return searchByLike(fundExample,page,pageSize);
                }else{
                    searchFundsResponse.setFunds(funds);
                }
            }else{
                searchFundsResponse.setFunds(funds);
            }
            return searchFundsResponse;
        }

        if(StringUtils.isEmpty(types)){
            searchFundsResponse.setFunds(null);
            return searchFundsResponse;
        }else{
            return searchByLike(example,page,pageSize);
        }

	}

	/**
     * 先精确检索、再模糊检索
     * */
	private List<cn.blooming.bep.data.api.model.Fund> queryIndex(String indexType, Query query, String search, SearchFundsResponse searchFundsResponse, int page, int pageSize, List<cn.blooming.bep.data.api.model.Fund> funds) throws Exception {
            if("key".equals(indexType)){
                List<String> keySearch = LuceneUtil.doKeySearch(LuceneUrl.LUCENE_DEFAULT_PATH, query, 10000);
                List<String> keys = LuceneUtil.removeKeyDuplicate(keySearch);
                log.debug("--匹配出的关键词的记录总数:" + keys.size());
                log.debug("--匹配出的关键词:" + keys);
                if(keys.size()==0){
                    searchFundsResponse.setKeys(null);
                }else{
                    searchFundsResponse.setKeys(keys);
                }
            }else{
                searchFundsResponse.setCurrentPage(page);
                searchFundsResponse.setPageSize(pageSize);
                funds = LuceneUtil.doFundSearch(LuceneUrl.LUCENE_DEFAULT_PATH, query,page,pageSize,searchFundsResponse);
            }
            return funds;
    }

    /**
     * 按Like查询
     * */
    private SearchFundsResponse searchByLike(FundExample fundExample,int pageIndex,int pageSize) {
        //PageHelper分页
        Page<Fund> fundPage = fundService.selectByExample(fundExample, pageIndex, pageSize);
        SearchFundsResponse searchFundsResponse=new SearchFundsResponse();
        List<cn.blooming.bep.data.api.model.Fund> resultFunds=new ArrayList<>();
        for (Fund fund : fundPage.getResult()) {
            cn.blooming.bep.data.api.model.Fund apiFund = FundConvert.crawlerFundConvertApiFund(fund);
            resultFunds.add(apiFund);
        }
        if(resultFunds.size()==0){
            searchFundsResponse.setFunds(null);
        }else {
            searchFundsResponse.setFunds(resultFunds);
        }
        searchFundsResponse.setCurrentPage(fundPage.getPageNum());
        searchFundsResponse.setPages(fundPage.getPages());
        searchFundsResponse.setPageSize(fundPage.getPageSize());
        searchFundsResponse.setTotalEntries(fundPage.getTotal());
        log.debug("返回结果[SearchFundsResponse]:"+searchFundsResponse);

        return searchFundsResponse;
    }

}
