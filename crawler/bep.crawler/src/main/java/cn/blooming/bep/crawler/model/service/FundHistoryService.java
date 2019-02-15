package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.FundHistory;
import cn.blooming.bep.crawler.model.entity.FundHistoryExample;

import java.util.List;

public interface FundHistoryService {

    List<FundHistory> selectByExample(FundHistoryExample example);

    int insert(FundHistory fundHistory);

    int deleteByFundCode(String code);

    List<FundHistory> selectByCode(String code);



}
