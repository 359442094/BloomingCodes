package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.FundHouse;
import cn.blooming.bep.crawler.model.entity.FundHouseExample;
import cn.blooming.bep.crawler.model.mapper.FundHouseMapper;
import cn.blooming.bep.crawler.model.service.FundHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundHouseServiceImpl implements FundHouseService {

    @Autowired
    private FundHouseMapper fundHouseMapper;

    @Override
    public List<FundHouse> selectByExample(FundHouseExample fundHouseExample) {
        return fundHouseMapper.selectByExample(fundHouseExample);
    }

    @Override
    public int insert(FundHouse fundHouse) {
        return fundHouseMapper.insert(fundHouse);
    }

    @Override
    public int deleteByFundHouseCode(String fundHouseCode) {
        FundHouseExample fundHouseExample=new FundHouseExample();
        fundHouseExample.createCriteria().andCodeEqualTo(fundHouseCode);
        return fundHouseMapper.deleteByExample(fundHouseExample);
    }
}
