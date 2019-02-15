package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.Cardbin;
import cn.blooming.bep.crawler.model.mapper.CardbinMapper;
import cn.blooming.bep.crawler.model.service.CardbinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardbinServiceImpl implements CardbinService {

    @Autowired
    private CardbinMapper cardbinMapper;

    @Override
    public Cardbin selectByPrimaryKey(String cardbin) {
        return cardbinMapper.selectByPrimaryKey(cardbin);
    }

}
