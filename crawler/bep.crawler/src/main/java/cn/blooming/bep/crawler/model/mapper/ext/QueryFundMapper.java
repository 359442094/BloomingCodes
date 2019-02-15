package cn.blooming.bep.crawler.model.mapper.ext;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface QueryFundMapper {

    String getTypeMean(@Param("dateType") String dateType,@Param("date") String date,@Param("type") String type);

    List<Map> getFundStatsticsMap();

    String getAverageByDateAndType(@Param("date") String date, @Param("type")String type);  //*

    Date selectGetMaxUpdateTime(@Param("fund_house_code") String fund_house_code);

    List<String> selectGetFundTypes();
}