package cn.blooming.bep.crawler.data.controller;
import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.convert.FundConvert;
import cn.blooming.bep.data.api.QueryFundDataAPI;
import cn.blooming.bep.data.api.model.Fund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.blooming.bep.data.api.model.QueryFundDataRequest;
import cn.blooming.bep.data.api.model.QueryFundDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"查询单项基金数据"})
public class QueryFundDataController implements QueryFundDataAPI {

    private static final Logger log= LoggerFactory.getLogger(QueryFundDataController.class);

    @Autowired
    private FundService fundService;

    @Override
    @ResponseBody
    @RequestMapping(path = "/fund/queryFund", method = {RequestMethod.POST})
    @ApiOperation(value = "查询单项基金数据", notes = "查询单项基金数据")
    public QueryFundDataResponse process(QueryFundDataRequest request) {
        request.setVersion(ClientInfo.VERSION);
        log.debug("查询单项基金数据:"+request);
        Fund apiFund = null;
        if (!StringUtils.isEmpty(request) && !StringUtils.isEmpty(request.getCode())) {
            cn.blooming.bep.crawler.model.entity.Fund crawlerFund = fundService.selectByPrimaryKey(request.getCode());
            if (!StringUtils.isEmpty(crawlerFund)) {
                apiFund= FundConvert.crawlerFundConvertApiFund(crawlerFund);
            }
        }
        QueryFundDataResponse queryFundDataResponse = new QueryFundDataResponse();
        queryFundDataResponse.setFund(apiFund);
        log.debug("返回结果[QueryFundDataResponse]:"+apiFund);
        return queryFundDataResponse;
    }

}
