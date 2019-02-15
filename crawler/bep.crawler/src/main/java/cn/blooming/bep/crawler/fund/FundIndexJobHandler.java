package cn.blooming.bep.crawler.fund;
import cn.blooming.bep.crawler.model.service.fund.FundIndexHandlerService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 更新基金的Lucene索引
 * */
@JobHandler(value = "fundIndexJobHandler")
@Component
public class FundIndexJobHandler extends IJobHandler {

    private static final Logger logger= LoggerFactory.getLogger(FundIndexJobHandler.class);
    @Autowired
    private FundIndexHandlerService fundIndexHandlerService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        logger.debug("开始执行更新基金的Lucene索引");
        //写入基金索引(不包含关键词)
        String resultMessage = fundIndexHandlerService.witer();
        //写入基金索引(包含关键词)
        //String resultMessage = fundIndexHandlerService.witerAll();
        return new ReturnT<>(200,"执行结束:"+resultMessage);
    }

}
