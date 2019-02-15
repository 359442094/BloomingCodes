package cn.blooming.bep.crawler.model.service.ext;
import java.util.Date;

public interface QueryFundHistoryService {
    Date selectGetMaxDate(String code);
}
