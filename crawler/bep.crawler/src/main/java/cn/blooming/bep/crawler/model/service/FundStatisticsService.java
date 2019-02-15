package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.FundStatistics;
import cn.blooming.bep.crawler.model.entity.FundStatisticsExample;

import java.util.List;

public interface FundStatisticsService {

    List<FundStatistics> selectByExample(FundStatisticsExample example);

    int insert(FundStatistics fundStatistics);

}
