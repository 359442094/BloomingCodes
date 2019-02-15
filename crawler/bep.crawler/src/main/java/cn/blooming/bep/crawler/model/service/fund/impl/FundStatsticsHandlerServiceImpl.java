package cn.blooming.bep.crawler.model.service.fund.impl;

import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.model.entity.*;
import cn.blooming.bep.crawler.model.service.FundHistoryService;
import cn.blooming.bep.crawler.model.service.FundHouseService;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.service.FundStatisticsService;
import cn.blooming.bep.crawler.model.service.ext.QueryFundService;
import cn.blooming.bep.crawler.model.service.fund.FundStatsticsHandlerService;
import cn.blooming.bep.crawler.model.util.TimeUtil;
import cn.blooming.bep.fund.api.QueryAllBankFundAPI;
import cn.blooming.bep.fund.model.QueryAllBankFundRequest;
import cn.blooming.bep.fund.model.QueryAllBankFundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundStatsticsHandlerServiceImpl implements FundStatsticsHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(FundStatsticsHandlerServiceImpl.class);
    //当前时间
    private static final Date endDate = new Date();

    @Autowired
    private FundStatisticsService fundStatisticsService;

    @Autowired
    private QueryFundService queryFundService;

    @Override
    public String start() throws Exception {
        //开始时间
        long startMillis = System.currentTimeMillis();
        //成功同类均值数据条数
        int SuccessfulCount = 0;
        //失败同类均值数据条数
        int FailureCount = 0;
        //应添加同类均值数据数量
        int shouldNumber = 0;
        //忽略同类均值数据数量
        int ignoreNumber = 0;
        logger.debug("--------------------------------基金同类均值日志信息开始--------------------------------");
        logger.debug("正在收集数据...");

        //获取需要爬取的均值数据列表
        //从历史净值中查询计算的均值数据
        List<Map> averageMap = queryFundService.getFundStatsticsMap();
        shouldNumber=averageMap.size();
        for (Map map : averageMap) {
            //时间
            String date = map.get("date").toString();
            //当天同类均值
            double average = Double.valueOf(map.get("value").toString());
            //类型
            String type = map.get("type").toString();

            //判断数据库是否存在
            FundStatisticsExample fundStatisticsExample=new FundStatisticsExample();
            fundStatisticsExample.createCriteria()
                    .andDateEqualTo(java.sql.Date.valueOf(date)).andFundTypeEqualTo(type);
            List<FundStatistics> fundStatistics1 = fundStatisticsService.selectByExample(fundStatisticsExample);
            int statisticsCount = fundStatistics1.size();
            if (statisticsCount < 1) {
                String rateStr = queryFundService.getAverageByDateAndType(date, type);
                if (!StringUtils.isEmpty(rateStr)) {
                    //前一天累计均值
                    double rate = Double.valueOf(rateStr);
                    //前一天相比的增长率
                    double lv = (average - rate) / rate * 100;

                    //近一周均值
//                    String average7days = queryFundMapper.getTypeMean("average7days", date, type);
//                    String average1month = queryFundMapper.getTypeMean("average1month", date, type);
//                    String average3month = queryFundMapper.getTypeMean("average3month", date, type);
//                    String average6month = queryFundMapper.getTypeMean("average6month", date, type);
//                    String average1year = queryFundMapper.getTypeMean("average1year", date, type);
//                    String average2year = queryFundMapper.getTypeMean("average2year", date, type);
//                    String average3year = queryFundMapper.getTypeMean("average3year", date, type);

                    FundStatistics fundStatistics = new FundStatistics();
                    fundStatistics.setDate(java.sql.Date.valueOf(date));
                    fundStatistics.setAverage(new BigDecimal(average));
                    fundStatistics.setRate(new BigDecimal(lv));
                    fundStatistics.setUpdated(endDate);
                    fundStatistics.setFundType(type);

//                    fundStatistics.setAverage7days(new BigDecimal(average7days));
//                    fundStatistics.setAverage1month(new BigDecimal(average1month));
//                    fundStatistics.setAverage3month(new BigDecimal(average3month));
//                    fundStatistics.setAverage6month(new BigDecimal(average6month));
//                    fundStatistics.setAverage1year(new BigDecimal(average1year));
//                    fundStatistics.setAverage2year(new BigDecimal(average2year));
//                    fundStatistics.setAverage3year(new BigDecimal(average3year));

                    logger.debug("开始添加" + endDate + "同类均值数据:" + fundStatistics);
                    try {
                        int insert = fundStatisticsService.insert(fundStatistics);
                        if (insert > 0) {
                            SuccessfulCount++;
                        } else {
                            FailureCount++;
                        }
                    } catch (Exception e) {
                        FailureCount++;
                        logger.error("添加" + endDate + "同类均值数据失败:" + e.getMessage());
                    }
                }else{
                    //数据库已存在当天同类均值数据
                    ignoreNumber++;
                    logger.debug("数据库已存在"+endDate+"同类均值数据");
                }
            }else{
                //数据库已存在该同类均值数据
                ignoreNumber++;
                logger.debug("数据库已存在"+endDate+"同类均值数据");
            }
        }


        //结束时间
        long endMillis = System.currentTimeMillis();

        //统计用时
        String log = TimeUtil.endTime(endMillis, startMillis);
        logger.debug("--------------------------------基金同类均值日志信息结束--------------------------------");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("本次添加同类均值数据结果:应添加"+shouldNumber+"条,实际成功" + SuccessfulCount + "条,失败" + FailureCount + "条,忽略"+ignoreNumber+"条");
        stringBuffer.append(log);
        logger.debug(stringBuffer.toString());

        return stringBuffer.toString();
    }
    /**
     * 爬取同类均值、计算当天均值
     * */
    /*private FundStatistics fundStatisticsProcessor(String url) throws IOException {
        String content = ReptileUtil.startReptile(url);
        Document document = Jsoup.parse(content);
        //获取同类均值数据
        Elements statisticsElements = document.select("#increaseAmount_stage table tbody tr:eq(2)");
        FundStatistics fundStatistics=new FundStatistics();
        for (Element statisticsElement : statisticsElements) {
            //近一周
            Elements average7days = statisticsElement.select("td:eq(1)");
            System.out.println("近一周：");
            for (Element average7day : average7days) {
                String text = average7day.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage7days(new BigDecimal(wipeOff));
                }
            }
            //近一月
            Elements average1months = statisticsElement.select("td:eq(2)");
            System.out.println("近一月：");
            for (Element average1month : average1months) {
                String text = average1month.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage1month(new BigDecimal(wipeOff));
                }
            }
            //近三月
            Elements average3months = statisticsElement.select("td:eq(3)");
            System.out.println("近三月：");
            for (Element average3month : average3months) {
                String text = average3month.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage3month(new BigDecimal(wipeOff));
                }
            }
            //近六月
            Elements average6months = statisticsElement.select("td:eq(4)");
            System.out.println("近六月：");
            for (Element average6month : average6months) {
                String text = average6month.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage6month(new BigDecimal(wipeOff));
                }
            }
            //今年来 暂时跳过 td:eq(5)

            //近一年
            Elements average1years = statisticsElement.select("td:eq(6)");
            System.out.println("近一年：");
            for (Element average1year : average1years) {
                String text = average1year.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage1year(new BigDecimal(wipeOff));
                }
            }
            //近俩年
            Elements average2years = statisticsElement.select("td:eq(7)");
            System.out.println("近俩年：");
            for (Element average2year : average2years) {
                String text = average2year.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage2year(new BigDecimal(wipeOff));
                }
            }
            //近三年
            Elements average3years = statisticsElement.select("td:eq(8)");
            System.out.println("近三年：");
            for (Element average3year : average3years) {
                String text = average3year.text();
                String wipeOff = JsoupUtil.wipeOff(text);
                if(!StringUtils.isEmpty(wipeOff)){
                    fundStatistics.setAverage3year(new BigDecimal(wipeOff));
                }
            }

            //同类均值数据更新时间
            Elements updatedElements = document.select("#jdzfDate");
            for (Element updatedElement : updatedElements) {
                String text = updatedElement.text();
                if(!StringUtils.isEmpty(text)){
                    fundStatistics.setUpdated(java.sql.Date.valueOf(text));
                }
            }
        }

        //基金类型
        Elements typeElements = document.select(".fundInfoItem .infoOfFund table tbody tr:eq(0) td:eq(0) a");
        for (Element typeElement : typeElements) {
            String type = typeElement.text();
            String fundType = JsoupUtil.isFundType(type);
            System.out.println(fundType);
        }

        return fundStatistics;

    }*/

}
