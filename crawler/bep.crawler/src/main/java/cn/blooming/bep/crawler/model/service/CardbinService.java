package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.Cardbin;

public interface CardbinService {
    Cardbin selectByPrimaryKey(String cardbin);
}
