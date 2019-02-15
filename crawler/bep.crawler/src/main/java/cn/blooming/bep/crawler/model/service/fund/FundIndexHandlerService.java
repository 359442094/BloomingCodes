package cn.blooming.bep.crawler.model.service.fund;

public interface FundIndexHandlerService {
    //写入基金索引(不包含关键词)
    String witer() throws Exception;
    //写入基金全部索引(包含关键词)
    String witerAll() throws Exception;
}
