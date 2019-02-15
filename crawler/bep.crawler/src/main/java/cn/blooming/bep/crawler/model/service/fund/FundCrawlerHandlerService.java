package cn.blooming.bep.crawler.model.service.fund;
/**
 * 爬虫业务逻辑层
 * */
public interface FundCrawlerHandlerService {
    //爬取网站部分基金
    String startFilters() throws Exception;

    //爬取网站全部基金
    String startAll() throws Exception;

}
