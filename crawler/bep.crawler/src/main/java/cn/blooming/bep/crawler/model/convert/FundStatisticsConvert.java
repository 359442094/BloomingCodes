package cn.blooming.bep.crawler.model.convert;

import cn.blooming.bep.data.api.model.FundStatistics;

public class FundStatisticsConvert {
    public static FundStatistics crawlerFundStatisticsConvertApiFundStatistics(cn.blooming.bep.crawler.model.entity.FundStatistics crawlerFundStatistics){
        FundStatistics apiFundStatistics = new FundStatistics();
        apiFundStatistics.setDate(crawlerFundStatistics.getDate());
        apiFundStatistics.setUpdated(crawlerFundStatistics.getUpdated());
        apiFundStatistics.setAverage(crawlerFundStatistics.getAverage());
        apiFundStatistics.setFundType(crawlerFundStatistics.getFundType());
        apiFundStatistics.setRate(crawlerFundStatistics.getRate());
        return apiFundStatistics;
    }
}
