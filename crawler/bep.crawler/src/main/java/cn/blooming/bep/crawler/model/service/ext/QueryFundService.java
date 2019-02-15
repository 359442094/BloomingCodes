package cn.blooming.bep.crawler.model.service.ext;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface QueryFundService {

    List<String> getFundTypes();

    List<Map> getFundStatsticsMap();

    String getAverageByDateAndType(String date,String type);

    Date selectGetMaxUpdateTime(@Param("fund_house_code") String fund_house_code);

}
