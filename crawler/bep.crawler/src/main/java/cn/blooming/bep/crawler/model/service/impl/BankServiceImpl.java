package cn.blooming.bep.crawler.model.service.impl;

import cn.blooming.bep.crawler.model.entity.Bank;
import cn.blooming.bep.crawler.model.entity.BankExample;
import cn.blooming.bep.crawler.model.mapper.BankMapper;
import cn.blooming.bep.crawler.model.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankMapper bankMapper;

    @Override
    public List<Bank> selectByExample(BankExample example) {
        return bankMapper.selectByExample(example);
    }
}
