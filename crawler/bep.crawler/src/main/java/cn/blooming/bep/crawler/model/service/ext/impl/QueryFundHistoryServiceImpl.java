package cn.blooming.bep.crawler.model.service.ext.impl;

import cn.blooming.bep.crawler.model.mapper.ext.QueryFundHistoryMapper;
import cn.blooming.bep.crawler.model.service.ext.QueryFundHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QueryFundHistoryServiceImpl implements QueryFundHistoryService {

    @Autowired
    private QueryFundHistoryMapper queryFundHistoryMapper;

    @Override
    public Date selectGetMaxDate(String code) {
        return queryFundHistoryMapper.selectGetMaxDate(code);
    }
}
