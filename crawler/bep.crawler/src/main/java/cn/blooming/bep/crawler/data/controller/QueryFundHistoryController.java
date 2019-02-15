package cn.blooming.bep.crawler.data.controller;

import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.ErrorCode;
import cn.blooming.bep.crawler.model.convert.FundStatisticsConvert;
import cn.blooming.bep.crawler.model.entity.*;
import cn.blooming.bep.crawler.model.service.FundHistoryService;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.service.FundStatisticsService;
import cn.blooming.bep.data.api.QueryFundRateHistoryAPI;
import cn.blooming.bep.data.api.model.FundRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.blooming.bep.data.api.model.QueryFundHistoryRequest;
import cn.blooming.bep.data.api.model.QueryFundHistoryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = {"查询基金历史数据"})
public class QueryFundHistoryController implements QueryFundRateHistoryAPI {

    private static final Logger log= LoggerFactory.getLogger(QueryFundHistoryController.class);

    @Autowired
    private FundStatisticsService fundStatisticsService;

    @Autowired
    private FundHistoryService fundHistoryService;

    @Autowired
    private FundService fundService;

    @Override
    @ResponseBody
    @RequestMapping(path = "/fund/queryFundHistory", method = {RequestMethod.POST})
    @ApiOperation(value = "查询基金历史数据", notes = "查询基金历史数据")
    public QueryFundHistoryResponse process(QueryFundHistoryRequest request) {
        request.setVersion(ClientInfo.VERSION);
        log.debug("查询基金历史数据:"+request);
        if (StringUtils.isEmpty(request)) {
            throw new BEPServiceException(ErrorCode.FUNDHISTORY_PARAMETER_NOT_FOUND, "基金历史参数为空");
        }
        List<FundRate> fundRates = new ArrayList<>();
        FundHistoryExample fundHistoryExample = new FundHistoryExample();
        fundHistoryExample.createCriteria().andCodeEqualTo(request.getCode()).andDateBetween(request.getStart(), request.getEnd());
        fundHistoryExample.setOrderByClause(" DATE  ASC ");
        List<FundHistory> fundHistories = fundHistoryService.selectByExample(fundHistoryExample);
        for (FundHistory fundHistory : fundHistories) {
            FundRate fundRate = new FundRate();
            fundRate.setDate(fundHistory.getDate());
            fundRate.setNetValue(fundHistory.getNetValue());
            fundRate.setTotalValue(fundHistory.getTotalValue());
            fundRate.setRateNetValue(fundHistory.getRateNetValue());
            fundRates.add(fundRate);
        }
        Fund fund = fundService.selectByPrimaryKey(request.getCode());
        List<cn.blooming.bep.data.api.model.FundStatistics> fundStatistics = new ArrayList<>();
        FundStatisticsExample fundStatisticsExample = new FundStatisticsExample();
        fundStatisticsExample.createCriteria()
                .andFundTypeEqualTo(fund.getType()).andDateBetween(request.getStart(), request.getEnd());
        fundStatisticsExample.setOrderByClause(" DATE  ASC ");
        List<FundStatistics> crawlerFundStatistics = fundStatisticsService.selectByExample(fundStatisticsExample);
        for (FundStatistics crawlerFundStatistic : crawlerFundStatistics) {
            cn.blooming.bep.data.api.model.FundStatistics apiFundStatistics = FundStatisticsConvert.crawlerFundStatisticsConvertApiFundStatistics(crawlerFundStatistic);
            fundStatistics.add(apiFundStatistics);
        }
        QueryFundHistoryResponse queryFundHistoryResponse = new QueryFundHistoryResponse();
        queryFundHistoryResponse.setRates(fundRates);
        queryFundHistoryResponse.setFundStatistics(fundStatistics);
        log.debug("返回结果[QueryFundHistoryResponse]:"+queryFundHistoryResponse);
        return queryFundHistoryResponse;
    }
}
