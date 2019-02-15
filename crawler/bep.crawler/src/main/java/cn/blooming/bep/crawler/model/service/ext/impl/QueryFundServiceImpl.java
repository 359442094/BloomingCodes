package cn.blooming.bep.crawler.model.service.ext.impl;

import cn.blooming.bep.crawler.model.mapper.ext.QueryFundMapper;
import cn.blooming.bep.crawler.model.service.ext.QueryFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class QueryFundServiceImpl implements QueryFundService {

    @Autowired
    private QueryFundMapper queryFundMapper;

    @Override
    public List<String> getFundTypes() {
        return queryFundMapper.selectGetFundTypes();
    }

    @Override
    public List<Map> getFundStatsticsMap() {
        return queryFundMapper.getFundStatsticsMap();
    }

    @Override
    public String getAverageByDateAndType(String date, String type) {
        return queryFundMapper.getAverageByDateAndType(date,type);
    }

    @Override
    public Date selectGetMaxUpdateTime(String fund_house_code) {
        return queryFundMapper.selectGetMaxUpdateTime(fund_house_code);
    }
}
