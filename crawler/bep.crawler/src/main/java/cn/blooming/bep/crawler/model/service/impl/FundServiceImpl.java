package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.Fund;
import cn.blooming.bep.crawler.model.entity.FundExample;
import cn.blooming.bep.crawler.model.mapper.FundMapper;
import cn.blooming.bep.crawler.model.service.FundService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundServiceImpl implements FundService {

    @Autowired
    private FundMapper fundMapper;

    @Override
    public Page<Fund> selectByExample(FundExample example, int pageIndex, int pageSize) {
        Page<Fund> fundPage = PageHelper.startPage(pageIndex, pageSize);
        fundMapper.selectByExample(example);
        return fundPage;
    }

    @Override
    public List<Fund> selectByExample(FundExample example) {
        return fundMapper.selectByExample(example);
    }

    @Override
    public Fund selectByPrimaryKey(String code) {
        return fundMapper.selectByPrimaryKey(code);
    }

    @Override
    public int insert(Fund fund) {
        return fundMapper.insert(fund);
    }

    @Override
    public int updateByPrimaryKeySelective(Fund record) {
        return fundMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByFundCode(String fundCode) {
        FundExample fundExample=new FundExample();
        fundExample.createCriteria().andCodeEqualTo(fundCode);
        return fundMapper.deleteByExample(fundExample);
    }
}
