package cn.blooming.bep.crawler.model.mapper.ext;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

public interface QueryFundHistoryMapper {
    Date selectGetMaxDate(@Param("code") String code);
}