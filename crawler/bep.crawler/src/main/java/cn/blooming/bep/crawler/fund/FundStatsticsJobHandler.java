package cn.blooming.bep.crawler.fund;
import cn.blooming.bep.crawler.model.service.fund.FundStatsticsHandlerService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 更新同类均值调度处理器
 */
@JobHandler(value = "fundStatsticsJobHandler")
@Component
public class FundStatsticsJobHandler extends IJobHandler {

    private static final Logger logger= LoggerFactory.getLogger(FundStatsticsJobHandler.class);
    @Autowired
    private FundStatsticsHandlerService fundStatsticsHandlerService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        logger.debug("开始执行更新同类均值调度处理器");
        String resultMessage = fundStatsticsHandlerService.start();
        return new ReturnT<>(200, resultMessage);
    }

}
