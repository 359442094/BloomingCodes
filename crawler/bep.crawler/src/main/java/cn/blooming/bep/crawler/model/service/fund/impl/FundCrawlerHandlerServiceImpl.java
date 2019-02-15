package cn.blooming.bep.crawler.model.service.fund.impl;

import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.CrawlerUrl;
import cn.blooming.bep.crawler.model.service.fund.FundCrawlerHandlerService;
import cn.blooming.bep.crawler.model.util.JsoupUtil;
import cn.blooming.bep.crawler.model.util.ReptileUtil;
import cn.blooming.bep.crawler.model.util.TimeUtil;
import cn.blooming.bep.fund.api.QueryAllBankFundAPI;
import cn.blooming.bep.fund.model.QueryAllBankFundRequest;
import cn.blooming.bep.fund.model.QueryAllBankFundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FundCrawlerHandlerServiceImpl implements FundCrawlerHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(FundCrawlerHandlerServiceImpl.class);

    private String[] filters = null;

    @Autowired
    private QueryAllBankFundAPI queryAllBankFundAPI;
    @Autowired
    private JsoupUtil jsoupUtil;

    public String[] getFilters() {
        return filters;
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }
    /**
     * 获取过滤codes信息
     * */
    private void init(){
        QueryAllBankFundRequest request = new QueryAllBankFundRequest();
        request.setClientId(ClientInfo.CLIENT_ID);
        request.setStaffUsername(ClientInfo.STAFF_USERNAME);
        request.setVersion(ClientInfo.VERSION);
        QueryAllBankFundResponse response = queryAllBankFundAPI.process(request);
        this.filters = response.getFundCodes();
    }

    @Override
    public String startFilters() throws Exception {
        init();
        //获取爬取内容
        //开始时间
        long startMillis = System.currentTimeMillis();
        //xxl-job显示添加结果
        StringBuffer resultMessage = new StringBuffer();

        logger.debug("请耐心等待结果...");

        //2.爬取基金公司内容
        /*只扫描基金代码集合中的基金*/
        logger.debug("正在爬取基金公司内容[只添加codes中存在的数据]...");
        //3.基金公司内容
        logger.debug("正在解析基金公司内容...");
        Map<String, String> maps = jsoupUtil.analysisNewFundHouse(filters, resultMessage);
        logger.debug("基金公司解析完毕，结果maps为:"+maps);

        //4.基金详情内容 80106677:http://fund.eastmoney.com//Company/80106677.html
        logger.debug("正在爬取基金公司详情...");
        List<String> codes = jsoupUtil.analysisNewFund(filters, maps, resultMessage);
        logger.debug("基金公司详情解析完毕，过滤之后codes为:"+codes);

        //5.爬取基金更多历史净值 这里设置查询开始日期-结束日期之间的分页数据  默认查询全部分页数据
        logger.debug("正在爬取基金历史净值...");
        jsoupUtil.netValueOfHistory(codes, null, null, resultMessage);

        //结束时间
        long endMillis = System.currentTimeMillis();

        //统计用时
        String log = TimeUtil.endTime(endMillis, startMillis);

        StringBuffer append = resultMessage.append(log);

        return append.toString();
    }

    @Override
    public String startAll() throws Exception {
        //获取爬取内容
        init();
        //开始时间
        long startMillis = System.currentTimeMillis();
        //xxl-job显示添加结果
        StringBuffer resultMessage = new StringBuffer();

        logger.debug("开始爬取:" + CrawlerUrl.CRAWLER_START_URL);
        logger.debug("请耐心等待结果...");

        //2.爬取基金公司内容
        logger.debug("正在爬取基金公司内容[扫描并爬取全部]...");
        String content = ReptileUtil.startReptile(CrawlerUrl.CRAWLER_START_URL);
        //3.基金公司内容
        logger.debug("正在解析基金公司内容...");
        Map<String, String> maps = jsoupUtil.analysisOldFundHouse(content, resultMessage);
        /*Map<String,String> maps=new HashMap<>();
        maps.put("80452130","http://fund.eastmoney.com//Company/80452130.html");*/

        //4.基金详情内容 80106677:http://fund.eastmoney.com//Company/80106677.html
        logger.debug("正在爬取基金公司详情...");
        List<String> codes = jsoupUtil.analysisOldFund(maps, resultMessage);

        //5.爬取基金更多历史净值 这里设置查询开始日期-结束日期之间的分页数据  默认查询全部分页数据
        logger.debug("正在爬取基金历史净值...");
        jsoupUtil.netValueOfHistory(codes, null, null, resultMessage);

        //结束时间
        long endMillis = System.currentTimeMillis();

        //统计用时
        String log = TimeUtil.endTime(endMillis, startMillis);

        StringBuffer append = resultMessage.append(log);

        return append.toString();
    }

}
