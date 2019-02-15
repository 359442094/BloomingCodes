package cn.blooming.bep.crawler.model.convert;

import cn.blooming.bep.data.api.model.Fund;

/**
 * 转换成data api 的 Fund
 * */
public class FundConvert {

    public static Fund crawlerFundConvertApiFund(cn.blooming.bep.crawler.model.entity.Fund crawlerFund){
        Fund apiFund=new Fund();
        apiFund.setRate7days(crawlerFund.getRate7days());
        apiFund.setRateCurrentYear(crawlerFund.getRateCurrentYear());
        apiFund.setUpdatedTime(crawlerFund.getUpdatedTime());
        apiFund.setType(crawlerFund.getType());
        apiFund.setTotalValue(crawlerFund.getTotalValue());
        apiFund.setStartDate(crawlerFund.getStartDate());
        apiFund.setScale(crawlerFund.getScale());
        apiFund.setRiskLevel(crawlerFund.getRiskLevel());
        apiFund.setRateYear(crawlerFund.getRateYear());
        apiFund.setRateThreeYear(crawlerFund.getRateThreeYear());
        apiFund.setRateStart(crawlerFund.getRateStart());
        apiFund.setRateQuarter(crawlerFund.getRateQuarter());
        apiFund.setRateNetValue(crawlerFund.getRateNetValue());
        apiFund.setRateMonth(crawlerFund.getRateMonth());
        apiFund.setRateHalf(crawlerFund.getRateHalf());
        apiFund.setRateFiveYear(crawlerFund.getRateFiveYear());
        apiFund.setNetValue(crawlerFund.getNetValue());
        apiFund.setName(crawlerFund.getName());
        apiFund.setLevel(crawlerFund.getLevel());
        apiFund.setFundHouseCode(crawlerFund.getFundHouseCode());
        apiFund.setCode(crawlerFund.getCode());
        return apiFund;
    }

}
