package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.FundHistory;
import cn.blooming.bep.crawler.model.entity.FundHistoryExample;
import cn.blooming.bep.crawler.model.entity.FundHistoryKey;
import cn.blooming.bep.crawler.model.mapper.FundHistoryMapper;
import cn.blooming.bep.crawler.model.service.FundHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FundHistoryServiceImpl implements FundHistoryService {

    @Autowired
    private FundHistoryMapper fundHistoryMapper;

    @Override
    public List<FundHistory> selectByExample(FundHistoryExample example) {
        return fundHistoryMapper.selectByExample(example);
    }

    @Override
    public int insert(FundHistory fundHistory) {
        return fundHistoryMapper.insert(fundHistory);
    }

    @Override
    public int deleteByFundCode(String code) {
        FundHistoryExample fundHistoryExample=new FundHistoryExample();
        fundHistoryExample.createCriteria().andCodeEqualTo(code);
        return fundHistoryMapper.deleteByExample(fundHistoryExample);
    }

    @Override
    public List<FundHistory> selectByCode(String code) {
        FundHistoryExample fundHistoryExample=new FundHistoryExample();
        fundHistoryExample.createCriteria().andCodeEqualTo(code);
        return fundHistoryMapper.selectByExample(fundHistoryExample);
    }
}
