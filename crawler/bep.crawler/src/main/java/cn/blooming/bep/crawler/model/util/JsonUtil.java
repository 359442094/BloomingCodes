package cn.blooming.bep.crawler.model.util;

import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.data.api.model.Fund;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 处理JSON数据
 * */
public class JsonUtil {

    private static final Logger logger= LoggerFactory.getLogger(JsonUtil.class);

    public static JSONArray getJSONArrayByHtmlJSON(String fundHistoryJSON){
        JSONObject jsonObject =(JSONObject) JSONObject.parse(fundHistoryJSON);
        JSONObject data = jsonObject.getJSONObject("Data");
        if(data==null){
            logger.error("Data JSON 为空");
            throw new BEPServiceException("BC0004","Data JSON 为空");
        }
       JSONArray jsonArray =data.getJSONArray("LSJZList");
        if(jsonArray==null){
            logger.error("LSJZList JSON 为空");
            throw new BEPServiceException("BC0005","LSJZList JSON 为空");
        }
        return jsonArray;
    }

    public static Fund apiFundByJSONObject(JSONObject object){
        Fund fund = new Fund();
        if(!StringUtils.isEmpty(object.get("totalValue"))){
            fund.setTotalValue(new BigDecimal(object.get("totalValue").toString()));
        }
        if(!StringUtils.isEmpty(object.get("updatedTime"))){
            fund.setUpdatedTime(new Date((Long) object.get("updatedTime")));
        }
        if(!StringUtils.isEmpty(object.get("code"))){
            fund.setCode(object.get("code").toString());
        }
        if(!StringUtils.isEmpty(object.get("rate7days"))){
            fund.setRate7days(new BigDecimal(object.get("rate7days").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateCurrentYear"))){
            fund.setRateCurrentYear(new BigDecimal(object.get("rateCurrentYear").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateNetValue"))){
            fund.setRateNetValue(new BigDecimal(object.get("rateNetValue").toString()));
        }
        if(!StringUtils.isEmpty(object.get("riskLevel"))){
            fund.setRiskLevel(object.get("riskLevel").toString());
        }
        if(!StringUtils.isEmpty(object.get("level"))){
            fund.setLevel(object.get("level").toString());
        }
        if(!StringUtils.isEmpty(object.get("netValue"))){
            fund.setNetValue(new BigDecimal(object.get("netValue").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateMonth"))){
            fund.setRateMonth(new BigDecimal(object.get("rateMonth").toString()));
        }
        if(!StringUtils.isEmpty(object.get("fundHouseCode"))){
            fund.setFundHouseCode(object.get("fundHouseCode").toString());
        }
        if(!StringUtils.isEmpty(object.get("scale"))){
            fund.setScale(new BigDecimal(object.get("scale").toString()));
        }
        if(!StringUtils.isEmpty(object.get("type"))){
            fund.setType(object.get("type").toString());
        }
        if(!StringUtils.isEmpty(object.get("rateYear"))){
            fund.setRateYear(new BigDecimal(object.get("rateYear").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateThreeYear"))){
            fund.setRateThreeYear(new BigDecimal(object.get("rateThreeYear").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateHalf"))){
            fund.setRateHalf(new BigDecimal(object.get("rateHalf").toString()));
        }
        if(!StringUtils.isEmpty(object.get("name"))){
            fund.setName(object.get("name").toString());
        }
        if(!StringUtils.isEmpty(object.get("rateQuarter"))){
            fund.setRateQuarter(new BigDecimal(object.get("rateQuarter").toString()));
        }
        if(!StringUtils.isEmpty(object.get("rateStart"))){
            fund.setRateStart(new BigDecimal(object.get("rateStart").toString()));
        }
        if(!StringUtils.isEmpty(object.get("startDate"))){
            fund.setStartDate(new Date((Long) object.get("startDate")));
        }
        return fund;
    }

}
