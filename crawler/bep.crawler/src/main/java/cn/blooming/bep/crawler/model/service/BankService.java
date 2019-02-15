package cn.blooming.bep.crawler.model.service;

import cn.blooming.bep.crawler.model.entity.Bank;
import cn.blooming.bep.crawler.model.entity.BankExample;

import java.util.List;

public interface BankService {
    List<Bank> selectByExample(BankExample example);
}
