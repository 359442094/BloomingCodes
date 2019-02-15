package cn.blooming.bep.crawler.fund;
import cn.blooming.bep.crawler.model.service.fund.FundCrawlerHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
/**
 * 爬虫调度处理器
 * */
@JobHandler(value="fundCrawlerJobHandler")
@Component
public class FundCrawlerJobHandler extends IJobHandler {

	private static final Logger logger= LoggerFactory.getLogger(FundCrawlerJobHandler.class);
	@Autowired
	private FundCrawlerHandlerService fundCrawlerHandlerService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		logger.debug("开始执行爬虫调度处理器");
		//爬取全部基金数据
        String resultMessage= fundCrawlerHandlerService.startAll();
        //根据codes过滤只爬取部分基金数据
        //String resultMessage=fundCrawlerHandlerService.startFilters();
		return new ReturnT(200,resultMessage);
	}

}
