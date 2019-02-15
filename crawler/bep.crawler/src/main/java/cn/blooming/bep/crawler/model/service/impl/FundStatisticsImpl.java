package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.FundStatistics;
import cn.blooming.bep.crawler.model.entity.FundStatisticsExample;
import cn.blooming.bep.crawler.model.mapper.FundStatisticsMapper;
import cn.blooming.bep.crawler.model.service.FundStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundStatisticsImpl implements FundStatisticsService {

    @Autowired
    private FundStatisticsMapper fundStatisticsMapper;

    @Override
    public List<FundStatistics> selectByExample(FundStatisticsExample example) {
        return fundStatisticsMapper.selectByExample(example);
    }

    @Override
    public int insert(FundStatistics fundStatistics) {
        return fundStatisticsMapper.insert(fundStatistics);
    }
}
