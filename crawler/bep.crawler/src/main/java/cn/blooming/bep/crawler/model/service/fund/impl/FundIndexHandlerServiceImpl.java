package cn.blooming.bep.crawler.model.service.fund.impl;

import cn.blooming.bep.crawler.LuceneUrl;
import cn.blooming.bep.crawler.model.entity.Fund;
import cn.blooming.bep.crawler.model.entity.FundExample;
import cn.blooming.bep.crawler.model.entity.FundHouse;
import cn.blooming.bep.crawler.model.entity.FundHouseExample;
import cn.blooming.bep.crawler.model.lucene.ik.IKAnalyzer4Lucene7;
import cn.blooming.bep.crawler.model.service.FundHouseService;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.service.fund.FundIndexHandlerService;
import cn.blooming.bep.crawler.model.util.LuceneUtil;
import cn.blooming.bep.crawler.model.util.TimeUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 创建索引业务逻辑层
 * */
@Service
public class FundIndexHandlerServiceImpl implements FundIndexHandlerService {

    private static final Logger logger= LoggerFactory.getLogger(FundIndexHandlerServiceImpl.class);

    //自定义分词器
    private static final Analyzer ikAnalyzer4Lucene7=new IKAnalyzer4Lucene7(true);
    //默认分词器
    private static final Analyzer standardAnalyzer=new StandardAnalyzer();

    @Autowired
    private FundHouseService fundHouseService;
    @Autowired
    private FundService fundService;

    /**
     * 写入基金索引(不包含关键词)
     * */
    @Override
    public String witer() throws Exception {
        //使用默认分词器
        return startWiterFundIndex(false,standardAnalyzer);
    }

    /**
     * 写入基金全部索引(包含关键词)
     * */
    @Override
    public String witerAll() throws Exception {
        //使用自定义分词器
        return startWiterFundIndex(true,ikAnalyzer4Lucene7);
    }

    private String startWiterFundIndex(boolean isKey,Analyzer analyzer) throws Exception {
        //开始时间
        long startMillis = System.currentTimeMillis();

        FundExample fundExample=new FundExample();

        List<Fund> funds = fundService.selectByExample(fundExample);

        FundHouseExample fundHouseExample=new FundHouseExample();
        List<FundHouse> fundHouses = fundHouseService.selectByExample(fundHouseExample);

        logger.debug("正在写入基金索引至->"+LuceneUrl.LUCENE_DEFAULT_PATH);
        //写入索引
        LuceneUtil.witerFundIndex(isKey,LuceneUrl.LUCENE_DEFAULT_PATH,fundHouses,funds,analyzer);

        //结束时间
        long endMillis = System.currentTimeMillis();

        //统计用时
        String log = TimeUtil.endTime(endMillis, startMillis);

        logger.debug("写入成功:"+log);

        return log;
    }

}
