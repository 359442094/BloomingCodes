package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.FundHouse;
import cn.blooming.bep.crawler.model.entity.FundHouseExample;

import java.util.List;

public interface FundHouseService {

    List<FundHouse> selectByExample(FundHouseExample fundHouseExample);

    int insert(FundHouse fundHouse);

    int deleteByFundHouseCode(String fundHouseCode);

}
