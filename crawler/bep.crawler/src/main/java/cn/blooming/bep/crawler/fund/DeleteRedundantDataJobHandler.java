package cn.blooming.bep.crawler.fund;

import cn.blooming.bep.crawler.model.service.fund.DeleteRedundantDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 删除基金相关多余数据处理器
 * */
@JobHandler(value = "deleteRedundantDataJobHandler")
@Component
public class DeleteRedundantDataJobHandler extends IJobHandler {

    private static final Logger logger= LoggerFactory.getLogger(DeleteRedundantDataJobHandler.class);
    @Autowired
    private DeleteRedundantDataService deleteRedundantDataService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        logger.debug("开始执行删除基金相关多余数据处理器");
        deleteRedundantDataService.delete();
        return new ReturnT<>(200,"执行结束");
    }
}
