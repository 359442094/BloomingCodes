package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.Fund;
import cn.blooming.bep.crawler.model.entity.FundExample;
import com.github.pagehelper.Page;

import java.util.List;

public interface FundService {

    Page<Fund> selectByExample(FundExample example, int pageIndex, int pageSize);

    List<Fund> selectByExample(FundExample example);

    Fund selectByPrimaryKey(String code);

    int insert(Fund fund);

    int updateByPrimaryKeySelective(Fund record);

    int deleteByFundCode(String fundCode);

}
