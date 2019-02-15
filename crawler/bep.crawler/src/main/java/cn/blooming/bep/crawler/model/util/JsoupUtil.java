package cn.blooming.bep.crawler.model.util;

import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.crawler.CrawlerUrl;
import cn.blooming.bep.crawler.ErrorCode;
import cn.blooming.bep.crawler.model.entity.*;
import cn.blooming.bep.crawler.model.service.FundHistoryService;
import cn.blooming.bep.crawler.model.service.FundHouseService;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.service.ext.QueryFundHistoryService;
import cn.blooming.bep.crawler.model.service.ext.QueryFundService;
import cn.blooming.bep.crawler.model.service.ext.impl.QueryFundHistoryServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 解析HTML工具类
 */
@Component
public class JsoupUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    private static Map<String, String> fundMap = new HashMap<>();

    @Autowired
    private QueryFundHistoryService queryFundHistoryService;

    @Autowired
    private FundHistoryService fundHistoryService;

    @Autowired
    private QueryFundService queryFundService;

    @Autowired
    private FundHouseService fundHouseService;

    @Autowired
    private FundService fundService;

    //记录下标
    private int index = 0;
    //
    private int updateNumber=0;


    /**
     * 解析并过滤基金代码页面中的基金代码、基金公司名称
     *
     * @param HtmlConntent 基金公司html代码
     * @return maps key : 基金公司代码 ,value 基金公司详情url
     */
    public Map<String, String> analysisOldFundHouse(String HtmlConntent, StringBuffer resultMessage) {
        //记录本次添加基金公司数据成功次数
        int successfulNumber = 0;
        //记录本次添加基金公司数据失败次数
        int failedNumber = 0;

        Map<String, String> maps = new HashMap<>();
        Document document = Jsoup.parse(HtmlConntent);
        //提取基金公司一览表中 包含基金代码(链接)、基金公司名称的信息、url
        Elements as = document.select(".td-align-left a");

        //应添加基金公司数量
        int shouldNumber = 0;
        //忽略基金公司数量
        int ignoreNumber = 0;
        //记录下标
        int index = 0;

        logger.debug("--------------------------------基金公司日志信息开始--------------------------------");
        shouldNumber = as.size();
        for (Element a : as) {
            //获取url参数
            String href = a.attr("href");
            //获取文字内容
            String name = a.text();
            String code = href.substring(href.lastIndexOf("/") + 1, href.indexOf("."));
            String url = CrawlerUrl.CRAWLER_FUND_URL_PREFIX + href;
            FundHouseExample fundHouseExample = new FundHouseExample();
            if (!StringUtils.isEmpty(code)) {
                fundHouseExample.createCriteria().andCodeEqualTo(code);
            }
            index++;
            logger.debug("已获取到第" + index + "个基金公司[" + name + "]");
            //判断该公司名称是否存在
            List<FundHouse> fundHouses = fundHouseService.selectByExample(fundHouseExample);
            if (fundHouses.size() == 0) {
                //不存在则添加 存在则下一步
                try {
                    FundHouse fundHouse = new FundHouse(code, name);
                    logger.debug("--正在添加基金公司:" + fundHouse);
                    int insert = fundHouseService.insert(fundHouse);
                    if (insert > 0) {
                        successfulNumber++;
                    } else {
                        failedNumber++;
                    }
                } catch (Exception e) {
                    failedNumber++;
                    logger.error("--添加基金公司失败:" + e.getMessage());
                }
            } else {
                //已经忽略的基金公司信息
                ignoreNumber++;
                logger.debug("--当前数据库已存在基金公司[" + name + "]");
            }
            maps.put(code, url);
            fundMap.put(code, name);
        }
        String resultStr = "本次新增基金公司数据: 应添加[" + shouldNumber + "]条,实际成功[" + successfulNumber + "]条,失败[" + failedNumber + "]条，已忽略[" + ignoreNumber + "]条";
        resultMessage.append(resultStr);
        logger.debug(resultStr);
        logger.debug("--------------------------------基金公司日志信息结束--------------------------------");
        return maps;
    }

    /**
     * 只爬取codes存在的code的公司信息
     * */
    public Map<String, String> analysisNewFundHouse(String [] filters,StringBuffer resultMessage) throws IOException {
        //记录本次添加基金公司数据成功次数
        int successfulNumber = 0;
        //记录本次添加基金公司数据失败次数
        int failedNumber = 0;

        //应添加基金公司数量
        int shouldNumber = 0;
        //忽略基金公司数量
        int ignoreNumber = 0;
        //记录下标
        int index = 0;

        logger.debug("--------------------------------基金公司日志信息开始--------------------------------");

        Map<String,String> maps=new HashMap<>();
        //根据基金代码获取基金公司信息
        for (String code : filters) {
            String url = CrawlerUrl.CRAWLER_FUND_URL_PREFIX + code + CrawlerUrl.CRAWLER_URL_SUFFIX;
            String content = ReptileUtil.startReptile(url);
            Document document = Jsoup.parse(content);
            Elements select = document.select(".wrapper .merchandiseDetail .infoOfFund tbody tr td:eq(1) a:eq(1)");
            shouldNumber=filters.length;
            for (Element element : select) {
                //获取url参数
                String href = element.attr("href");

                //获取文字内容
                String name = element.text();
                String fundHouseCode =href.substring(href.lastIndexOf("/")+1, href.lastIndexOf("."));

                FundHouseExample fundHouseExample = new FundHouseExample();
                if (!StringUtils.isEmpty(name)) {
                    fundHouseExample.createCriteria().andNameEqualTo(name);
                }
                index++;
                logger.debug("已获取到第" + index + "个基金公司[" + name + "]");
                //判断该公司名称是否存在
                List<FundHouse> fundHouses = fundHouseService.selectByExample(fundHouseExample);
                if (fundHouses.size() == 0) {
                    //不存在则添加 存在则下一步
                    try {
                        FundHouse fundHouse = new FundHouse(fundHouseCode, name);
                        logger.debug("--正在添加基金公司:" + fundHouse);
                        int insert = fundHouseService.insert(fundHouse);
                        if (insert > 0) {
                            successfulNumber++;
                        } else {
                            failedNumber++;
                        }
                    } catch (Exception e) {
                        failedNumber++;
                        logger.error("--添加基金公司失败:" + e.getMessage());
                    }
                } else {
                    //已经忽略的基金公司信息
                    ignoreNumber++;
                    logger.debug("--当前数据库已存在基金公司[" + name + "]");
                }

                FundHouse fundHouse = new FundHouse();
                fundHouse.setCode(fundHouseCode);
                fundHouse.setName(name);

                maps.put(fundHouseCode, href);
                fundMap.put(fundHouseCode, name);

            }
        }
        String resultStr = "本次新增基金公司数据: 应添加[" + shouldNumber + "]条,实际成功[" + successfulNumber + "]条,失败[" + failedNumber + "]条，已忽略[" + ignoreNumber + "]条";
        resultMessage.append(resultStr);
        logger.debug(resultStr);
        logger.debug("--------------------------------基金公司日志信息结束--------------------------------");
        return maps;
    }

    private List<Integer> successfulNumbers=new ArrayList<>();
    /**
     * 添加基金产品数据
     * 解析并过滤基金公司详情页面(基金表)
     * 修改后
     * @param maps key : 基金公司代码 ,value 基金公司详情url
     * @return codes : 基金产品代码集合
     */
    public List<String> analysisNewFund(String[] filters,Map<String, String> maps, StringBuffer resultMessage) throws Exception {
        successfulNumbers.clear();
        Map<String,Object> mapsResult=new HashMap<>();

        //封装待添加Fund数据
        List<String> codes = new ArrayList<>();

        Set<Map.Entry<String, String>> entries = maps.entrySet();

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        String BasedType = null;
        //场内式基金 html元素ID
        String parentBasedId = "#CNFundNetCon tbody tr td:eq(3)";
        String chind1BasedId = "#CNFundNetCon .fund-name-code";
        String chind2BasedId = "CNFundNetCon";
        //开放式基金 html元素ID
        String parentOpenId = "#kfsFundNetWrap tbody tr td:eq(3)";
        String chind1OpenId = "#kfsFundNetWrap .fund-name-code";
        String chind2OpenId = "kfsFundNetWrap";

        //应添加基金产品数量
        int shouldNumber = 0;

        //添加成功次数
        int successfulNumber=0;

        //修改次数
        int updateNumber=0;

        logger.debug("--------------------------------基金产品日志信息开始--------------------------------");
        for (Map.Entry<String, String> entry : entries) {
            String code = entry.getKey();
            //获得数据库最大日期
            Date maxUpdateTime = queryFundService.selectGetMaxUpdateTime(code);
            //开始爬取基金公司详情页面
            String url = entry.getValue();
            String conntent = ReptileUtil.startReptile(url);
            //解析爬取的html文档
            Document document = Jsoup.parse(conntent);

            logger.debug("已获取到基金公司[" + fundMap.get(code) + "]基金产品列表");

            //应添加基金产品总数
            shouldNumber = getFundDetailsSize(filters);

            //修改次数
            //场内式基金更新日期
            updateNumber=fundNewDetails(successfulNumbers,mapsResult,filters,index, document, BasedType, year, maxUpdateTime, calendar, code, codes, parentBasedId, chind1BasedId, chind2BasedId);
            //开放式基金更新日期
            updateNumber=fundNewDetails(successfulNumbers,mapsResult,filters,index, document, BasedType, year, maxUpdateTime, calendar, code, codes, parentOpenId, chind1OpenId, chind2OpenId);

        }

        List<Integer> successfulNumbers= (List<Integer>) mapsResult.get("successfulNumbers");
        for (Integer s : successfulNumbers) {
            successfulNumber+=s;
        }

        String resultStr = "本次新增基金产品数据:应添加[" + shouldNumber + "]条,实际添加成功[" + successfulNumber + "]条,添加失败[" + mapsResult.get("failedNumber") + "]条,更新成功["+updateNumber+"],,更新失败["+mapsResult.get("updateErrorNumber")+"],忽略[" + mapsResult.get("ignoreNumber") + "]条";
        resultMessage.append(resultStr);
        logger.debug(resultStr);
        logger.debug("--------------------------------基金产品日志信息结束--------------------------------");
        return codes;
    }


    /**
     * 添加基金产品数据
     * 解析并过滤基金公司详情页面(基金表)
     *
     * @param maps key : 基金公司代码 ,value 基金公司详情url
     * @return codes : 基金产品代码集合
     */
    public List<String> analysisOldFund(Map<String, String> maps, StringBuffer resultMessage) throws Exception {
        successfulNumbers.clear();
        Map<String,Object> mapResult=new HashMap<>();

        //记录本次添加基金产品数据成功次数
        int successfulNumber = 0;

        //记录本次添加基金产品数据失败次数
        int failedNumber = 0;

        //封装待添加Fund数据
        List<String> codes = new ArrayList<>();

        Set<Map.Entry<String, String>> entries = maps.entrySet();

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        String BasedType = null;
        //场内式基金 html元素ID
        String parentBasedId = "#CNFundNetCon tbody tr td:eq(3)";
        String chind1BasedId = "#CNFundNetCon .fund-name-code";
        String chind2BasedId = "CNFundNetCon";
        //开放式基金 html元素ID
        String parentOpenId = "#kfsFundNetWrap tbody tr td:eq(3)";
        String chind1OpenId = "#kfsFundNetWrap .fund-name-code";
        String chind2OpenId = "kfsFundNetWrap";

        //应添加基金产品数量
        int shouldNumber = 0;
        //忽略基金产品数量
        int ignoreNumber = 0;
        //更新产品数量
        //int updateNumber=0;

        logger.debug("--------------------------------基金产品日志信息开始--------------------------------");
        for (Map.Entry<String, String> entry : entries) {
            String code = entry.getKey();
            //获得数据库最大日期
            Date maxUpdateTime = queryFundService.selectGetMaxUpdateTime(code);
            //开始爬取基金公司详情页面
            String url = entry.getValue();
            String conntent = ReptileUtil.startReptile(url);
            //解析爬取的html文档
            Document document = Jsoup.parse(conntent);

            logger.debug("已获取到基金公司[" + fundMap.get(code) + "]基金产品列表");

            //应添加基金产品总数
            shouldNumber += getFundDetailsSize(document, parentBasedId);
            shouldNumber += getFundDetailsSize(document, parentOpenId);

            //场内式基金更新日期
            ignoreNumber += fundOldDetails(successfulNumbers,mapResult,index, document, BasedType, year, maxUpdateTime, calendar, code,  codes, parentBasedId, chind1BasedId, chind2BasedId);
            //开放式基金更新日期
            ignoreNumber += fundOldDetails(successfulNumbers,mapResult,index, document, BasedType, year, maxUpdateTime, calendar, code,  codes, parentOpenId, chind1OpenId, chind2OpenId);

        }
        List<Integer> successfulNumbers= (List<Integer>) mapResult.get("successfulNumbers");
        for (Integer s : successfulNumbers) {
            successfulNumber+=s;
        }

        String resultStr = "本次新增基金产品数据:应添加[" + shouldNumber + "]条,实际成功" + successfulNumber + "条,失败" + failedNumber + "条,忽略[" + ignoreNumber + "]条";
        resultMessage.append(resultStr);
        logger.debug(resultStr);
        logger.debug("--------------------------------基金产品日志信息结束--------------------------------");
        return codes;
    }


    /**
     * 获取基金集合中的数量
     */
    private int getFundDetailsSize(String [] filters) throws Exception {
        return filters.length;
    }

    /**
     * 获取基金公司旗下基金产品数量
     */
    private int getFundDetailsSize(Document document, String parentId) throws Exception {
        Elements results = document.select(parentId);
        return results.size();
    }

    /**
     * 处理部分基金详情 [只获取filters中存在的相关基金内容]
     */
    private int fundNewDetails(List<Integer> successfulNumbers,Map<String,Object> mapResult,String [] filters,int index, Document document, String BasedType, int year, Date maxUpdateTime, Calendar calendar, String code, List<String> codes, String parentId, String chind1Id, String chind2Id) throws Exception {
        //记录本次添加基金产品数据成功次数
        int successfulNumber = 0;
        //记录本次添加基金产品数据失败次数
        int failedNumber = 0;
        //忽略次数
        int ignoreNumber=0;
        //修改失败次数
        int updateErrorNumber=0;

        Elements results = document.select(parentId);
        String fundName = new String();

        for (int i = 0; i < results.size(); i++) {
            index++;
            Fund fund = new Fund();
            BasedType = document.select(chind1Id).get(i + 1).select(".code").text();
            //过滤filters中不存在的数据
            for (String filter : filters) {
                //判断网站的基金跟需要的基金是否一致
                if(!StringUtils.isEmpty(filter)&&filter.equals(BasedType)){
                    //将基金代码添加至集合中
                    codes.add(BasedType);
                    if (!StringUtils.isEmpty(results.get(i).text()) && results.get(i).text().indexOf("-") != -1 && results.get(i).text().indexOf("/") == -1) {
                        //判断数据库的更新时间跟网站的更新时间是否一致
                        String time = year + "-" + results.get(0).text();

                        fundName = getFundName(document, fund, i, chind2Id);
                        //不一致的时候
                        if (maxUpdateTime == null || !DateFormatUtils.format(maxUpdateTime, "yyyy-MM-dd").equals(time)) {
                            //判断基金代码是否存在与数据库
                            FundExample fundExample = new FundExample();
                            if (!StringUtils.isEmpty(BasedType)) {
                                fundExample.createCriteria().andCodeEqualTo(BasedType);
                            }

                            logger.debug("--第" + index + "个基金产品[" + fundName + "]");

                            List<Fund> fundList = fundService.selectByExample(fundExample);

                            if (!"-".equals(results.get(i).text())) {
                                java.sql.Date date = java.sql.Date.valueOf(year + "-" + results.get(i).text());
                                Date nowDate = calendar.getTime();
                                if (!date.before(nowDate)) {
                                    fund.setUpdatedTime(java.sql.Date.valueOf((year - 1) + "-" + results.get(i).text()));
                                } else {
                                    fund.setUpdatedTime(date);
                                }
                            } else {
                                fund.setUpdatedTime(null);
                            }
                            fund.setFundHouseCode(code);    //基金公司代码
                            fund.setCode(BasedType);         //基金代码
                            //获取场内基金所有信息
                            opeEndedFund(document, fund, i, chind2Id);

                            if (fundList.size() == 0) {
                                //添加数据库
                                try {
                                    int insert = fundService.insert(fund);
                                    if (insert > 0) {
                                        successfulNumber++;
                                    } else {
                                        failedNumber++;
                                    }
                                } catch (Exception e) {
                                    failedNumber++;
                                    logger.error("添加基金信息失败:" + e.getMessage());
                                }
                            } else {
                                logger.debug("--数据库已存在  正在更新基金信息");
                                try {
                                    //更新数据库基金信息
                                    int update = fundService.updateByPrimaryKeySelective(fund);
                                    if (update > 0) {
                                        updateNumber++;
                                        logger.debug("--[" + fund + "]");
                                        logger.debug("更新结果:更新成功");
                                    } else {
                                        updateErrorNumber++;
                                        logger.error("更新结果:更新失败");
                                    }
                                } catch (Exception e) {
                                    logger.error("--[" + fund + "]\t更新失败" + e.getMessage());
                                }
                            }
                        } else {
                            ignoreNumber++;
                            //基金产品数据库已存在
                            logger.debug("--基金产品数据库已存在[" + fundName + "]");
                        }
                    } else {
                        ignoreNumber++;
                        //基金产品数据库已存在
                        logger.debug("--更新时间一致[" + fundName + "]");
                    }
                }
            }
        }
        successfulNumbers.add(successfulNumber);
        mapResult.put("successfulNumbers",successfulNumbers);
        mapResult.put("failedNumber",failedNumber);
        mapResult.put("ignoreNumber",ignoreNumber);
        mapResult.put("updateErrorNumber",updateErrorNumber);

        return updateNumber;
    }

    /**
     * 处理全部基金详情 [不需要过滤]
     */
    private int fundOldDetails(List<Integer> successfulNumbers,Map<String,Object> mapResult,int index, Document document, String BasedType, int year, Date maxUpdateTime, Calendar calendar, String code, List<String> codes, String parentId, String chind1Id, String chind2Id) throws Exception {
        //记录本次添加基金产品数据成功次数
        int successfulNumber = 0;
        //记录本次添加基金产品数据失败次数
        int failedNumber = 0;
        //忽略次数
        int ignoreNumber=0;
        //修改失败次数
        int updateErrorNumber=0;
        Elements results = document.select(parentId);

        //int ignoreNumber = 0;
        String fundName = new String();
        for (int i = 0; i < results.size(); i++) {
            index++;
            Fund fund = new Fund();
            BasedType = document.select(chind1Id).get(i + 1).select(".code").text();

            //for (String filter : filters) {
                //if(!StringUtils.isEmpty(filter)&&filter.equals(BasedType)){
                    //将基金代码添加至集合中
                    codes.add(BasedType);

                    if (!StringUtils.isEmpty(results.get(i).text()) && results.get(i).text().indexOf("-") != -1 && results.get(i).text().indexOf("/") == -1) {
                        //判断数据库的更新时间跟网站的更新时间是否一致
                        String time = year + "-" + results.get(0).text();

                        fundName = getFundName(document, fund, i, chind2Id);
                        //不一致的时候
                        if (maxUpdateTime == null || !DateFormatUtils.format(maxUpdateTime, "yyyy-MM-dd").equals(time)) {
                            //判断基金代码是否存在与数据库
                            FundExample fundExample = new FundExample();
                            if (!StringUtils.isEmpty(BasedType)) {
                                fundExample.createCriteria().andCodeEqualTo(BasedType);
                            }

                            logger.debug("--第" + index + "个基金产品[" + fundName + "]");

                            List<Fund> fundList = fundService.selectByExample(fundExample);

                            if (!"-".equals(results.get(i).text())) {
                                java.sql.Date date = java.sql.Date.valueOf(year + "-" + results.get(i).text());
                                Date nowDate = calendar.getTime();
                                if (!date.before(nowDate)) {
                                    fund.setUpdatedTime(java.sql.Date.valueOf((year - 1) + "-" + results.get(i).text()));
                                } else {
                                    fund.setUpdatedTime(date);
                                }
                            } else {
                                fund.setUpdatedTime(null);
                            }
                            fund.setFundHouseCode(code);    //基金公司代码
                            fund.setCode(BasedType);         //基金代码
                            //获取场内基金所有信息
                            opeEndedFund(document, fund, i, chind2Id);

                            if (fundList.size() == 0) {
                                //添加数据库
                                try {
                                    int insert = fundService.insert(fund);
                                    if (insert > 0) {
                                        successfulNumber++;
                                    } else {
                                        failedNumber++;
                                    }
                                } catch (Exception e) {
                                    failedNumber++;
                                    logger.error("添加基金信息失败:" + e.getMessage());
                                }
                            } else {
                                //只要是过滤列表中存在的 就更新数据 否则直接添加作为临时数据
                                if(BasedType.equals(fund.getCode())){
                                    logger.debug("--数据库已存在  正在更新基金信息");
                                    try {
                                        //更新数据库基金信息
                                        int update =fundService.updateByPrimaryKeySelective(fund);
                                        if (update > 0) {
                                            updateNumber++;
                                            ignoreNumber++;
                                            logger.debug("--[" + fund + "]");
                                            logger.debug("更新结果:更新成功");
                                        } else {
                                            logger.error("更新结果:更新失败");
                                        }
                                    } catch (Exception e) {
                                        logger.error("--[" + fund + "]\t更新失败" + e.getMessage());
                                    }
                                }
                            }
                        } else {
                            ignoreNumber++;
                            //基金产品数据库已存在
                            logger.debug("--基金产品数据库已存在[" + fundName + "]");
                        }
                    } else {
                        ignoreNumber++;
                        //基金产品数据库已存在
                        logger.debug("--更新时间一致[" + fundName + "]");
                    }
                }
            //}
        //}
        successfulNumbers.add(successfulNumber);
        mapResult.put("successfulNumbers",successfulNumbers);
        /*mapResult.put("failedNumber",failedNumber);
        mapResult.put("ignoreNumber",ignoreNumber);
        mapResult.put("updateErrorNumber",updateErrorNumber);*/
        return ignoreNumber;
    }

    /**
     * 获取正在处理的基金产品名称
     */
    private String getFundName(Document document, Fund fund, int i, String idName) throws Exception {
        //基金代码、名称
        String fundName = document.select("#" + idName + " .fund-name-code").get(i + 1).select(".name").text();
        return fundName;
    }

    /**
     * 获取开放、场内式基金数据
     *
     * @param document 基金公司页面文档对象
     * @param fund     基金产品对象
     * @param i        循环下标
     */
    private void opeEndedFund(Document document, Fund fund, int i, String idName) throws Exception {
        //基金代码、名称
        String fundName = document.select("#" + idName + " .fund-name-code").get(i + 1).select(".name").text();
        fund.setName(fundName);  //基金名称
        //基金类型
        String type = document.select("#" + idName + " tbody tr td:eq(2)").get(i).text();
        String fundType = isFundType(type);
        fund.setType(fundType);
        //单位净值
        String net_value = document.select("#" + idName + " .number:eq(4)").get(i).text();
        if (!StringUtils.isEmpty(net_value) && net_value.indexOf("%") == -1) {
            if (!"-".equals(net_value)) {
                fund.setNetValue(new BigDecimal(net_value));
            } else {
                logger.error("[" + fundName + "]基金单位净值为空");
                fund.setNetValue(null);
            }
        }
        //累计净值
        String total_value = document.select("#" + idName + "  .number:eq(5)").get(i).text();
        if (!StringUtils.isEmpty(total_value) && total_value.indexOf("%") == -1) {
            if (!"-".equals(total_value)) {
                fund.setTotalValue(new BigDecimal(total_value));
            } else {
                logger.error("[" + fundName + "]基金累计净值为空");
                fund.setTotalValue(null);
            }
        }

        //开始继续收集基金详细页面
        String content = ReptileUtil.startReptile(CrawlerUrl.CRAWLER_FUND_URL_PREFIX + fund.getCode() + CrawlerUrl.CRAWLER_URL_SUFFIX);
        Document fundDocument = Jsoup.parse(content);
        Elements select1 = fundDocument.select(".dataOfFund .dataItem01 #gz_gsz");
        for (Element element : select1) {
            if (!"--".equals(element.text())) {
                fund.setRateNetValue(new BigDecimal(element.text()));
            } else {
                fund.setRateNetValue(null);
            }
        }

        //增加 近一周、今年来 基金数据
        Elements newly = fundDocument.select("#increaseAmount_stage tbody tr:eq(1)");
        for (Element element : newly) {
            //近一周
            String text = element.child(1).text();
            if (!"--".equals(text) && text.indexOf("%") != -1) {
                String rate_7days = text.substring(0, text.indexOf("%"));
                fund.setRate7days(new BigDecimal(rate_7days));
            } else {
                logger.error("[" + fundName + "]基金近一周收益率为空");
                fund.setRate7days(null);
            }
            //今年来
            String text1 = element.child(5).text();
            if (!"--".equals(text1) && text1.indexOf("%") != -1) {
                String rate_current_year = text1.substring(0, text1.indexOf("%"));
                fund.setRateCurrentYear(new BigDecimal(rate_current_year));
            } else {
                logger.error("[" + fundName + "]基金今年来收益率为空");
                fund.setRateCurrentYear(null);
            }
        }


        //rate_month 上一月收益率
        Elements months = fundDocument.select(".dataOfFund .dataItem01 dd:eq(2)");
        for (Element month : months) {
            if (!"--".equals(month.child(1).text()) && month.child(1).text().indexOf("%") != -1) {
                String rate_month = month.child(1).text().substring(0, month.child(1).text().indexOf("%"));
                fund.setRateMonth(new BigDecimal(rate_month));
            } else {
                logger.error("[" + fundName + "]基金上一月收益率为空");
                fund.setRateMonth(null);
            }
        }

        //rate_half 	半年度收益率
        Elements halfs = fundDocument.select(".dataOfFund .dataItem03 dd:eq(2)");
        for (Element half : halfs) {
            if (!"--".equals(half.child(1).text()) && half.child(1).text().indexOf("%") != -1) {
                String rate_half = half.child(1).text().substring(0, half.child(1).text().indexOf("%"));
                fund.setRateHalf(new BigDecimal(rate_half));
            } else {
                logger.error("[" + fundName + "]基金半年度收益率为空");
                fund.setRateHalf(null);
            }
        }


        //rate_quarter 	上一季度收益率
        Elements quarters = fundDocument.select(".dataOfFund .dataItem02 dd:eq(2)");
        for (Element quarter : quarters) {
            if (!"--".equals(quarter.child(1).text()) && quarter.child(1).text().indexOf("%") != -1) {
                String rate_month = quarter.child(1).text().substring(0, quarter.child(1).text().indexOf("%"));
                fund.setRateQuarter(new BigDecimal(rate_month));
            } else {
                logger.error("[" + fundName + "]基金上一季度收益率为空");
                fund.setRateQuarter(null);
            }
        }


        //rate_three_year 三年收益率
        Elements three_years = fundDocument.select(".dataOfFund .dataItem02 dd:eq(3)");
        for (Element three_year : three_years) {
            if (!"--".equals(three_year.child(1).text()) && three_year.child(1).text().indexOf("%") != -1) {
                String rate_month = three_year.child(1).text().substring(0, three_year.child(1).text().indexOf("%"));
                fund.setRateThreeYear(new BigDecimal(rate_month));
            } else {
                logger.error("[" + fundName + "]基金三年收益率为空");
                fund.setRateThreeYear(null);
            }
        }
        //rate_year 一年收益率
        Elements rate_years = fundDocument.select(".dataOfFund .dataItem01 dd:eq(3)");
        for (Element dd : rate_years) {
            if (!"--".equals(dd.child(1).text()) && dd.child(1).text().indexOf("%") != -1) {
                String rate_month = dd.child(1).text().substring(0, dd.child(1).text().indexOf("%"));
                fund.setRateYear(new BigDecimal(rate_month));
            } else {
                logger.error("[" + fundName + "]基金一年收益率为空");
                fund.setRateYear(null);
            }
        }

        //自成立日起的收益率
        Elements rateStarts = fundDocument.select(".dataOfFund .dataItem03 dd:eq(3)");
        for (Element dd : rateStarts) {
            if (!"--".equals(dd.child(1).text()) && dd.child(1).text().indexOf("%") != -1) {
                String rate_month = dd.child(1).text().substring(0, dd.child(1).text().indexOf("%"));
                fund.setRateStart(new BigDecimal(rate_month));
            } else {
                logger.error("[" + fundName + "]基金自成立日起收益率为空");
                fund.setRateStart(null);
            }
        }

        //rate_five_year  五年收益率为null

        //start_date 	成立日
        Elements select = fundDocument.select(".fundInfoItem .infoOfFund tbody tr:eq(1)");
        for (Element element : select) {
            String text = element.child(0).text();
            if (text.indexOf("：") != -1) {
                String risk_level = text.trim().substring(text.trim().indexOf("：") + 1, text.trim().length());
                if (!"--".equals(risk_level)) {
                    fund.setStartDate(java.sql.Date.valueOf(risk_level));
                } else {
                    logger.error("[" + fundName + "]基金成立日时间为空");
                    fund.setStartDate(null);
                }
            }
        }

        //risk_level 	风险等级 R1 R2 R3 R4 R5
        Elements elements = fundDocument.select(".fundInfoItem .infoOfFund tbody tr:eq(0)");
        for (Element element : elements) {
            String text = element.child(0).text();
            if (text.indexOf("|") != -1) {
                String risk_level = text.trim().substring(text.trim().indexOf("|") + 1, text.trim().length());
                String trim = risk_level.trim();
                String risk_leve = null;
                switch (trim) {
                    case "  低风险":
                        risk_leve = "R1";
                        break;
                    case "  中低风险":
                        risk_leve = "R2";
                        break;
                    case "  中风险":
                        risk_leve = "R3";
                        break;
                    case "  中高风险":
                        risk_leve = "R4";
                        break;
                    case "  高风险":
                        risk_leve = "R5";
                        break;
                    default:
                        logger.error("未知风险:" + risk_leve);
                        break;
                }
                fund.setRiskLevel(risk_leve);
            }
        }
        //scale 		基金规模 (单位万)
        Elements scales = fundDocument.select(".fundInfoItem .infoOfFund tbody tr:eq(0)");
        for (Element element : scales) {
            String text = element.child(1).text();
            if (text.indexOf("：") != -1) {
                String risk_level = text.trim().substring(text.trim().indexOf("：") + 1, text.trim().indexOf("亿"));
                if (!"--".equals(risk_level)) {
                    fund.setScale(new BigDecimal(risk_level.trim()));
                } else {
                    logger.error("[" + fundName + "]基金规模为空");
                    fund.setScale(null);
                }
            }
        }

        //level 		基金评级
        Elements levels = fundDocument.select(".fundInfoItem .infoOfFund tbody tr:eq(1) td:eq(2)");
        for (Element element : levels) {
            String level = null;
            String levelStr = element.child(2).attr("class");
            switch (levelStr) {
                case "jjpj1":
                    level = "1级";
                    break;
                case "jjpj2":
                    level = "2级";
                    break;
                case "jjpj3":
                    level = "3级";
                    break;
                case "jjpj4":
                    level = "4级";
                    break;
                case "jjpj5":
                    level = "5级";
                    break;
                default:
                    level = "暂无评级";
                    break;
            }
            fund.setLevel(level);
        }
        logger.debug("正在添加基金产品:" + fund);
    }

    public static String isFundType(String type){
        switch (type) {
            case "股票型":
                type = "stock";
                break;
            case "混合型":
                type = "mix";
                break;
            case "混合-FOF":
                type = "fof";
                break;
            case "理财型":
                type = "bond";
                break;
            case "债券型":
                type = "bond";
                break;
            case "定开债券":
                type = "bond";
                break;
            case "债券指数":
                type = "bond";
                break;
            case "QDII-指数":
                type = "index";
                break;
            case "股票指数":
                type = "index";
                break;
            case "etf联接基金":
                type = "etf";
                break;
            case "ETF-场内":
                type = "etf";
                break;
            case "联接基金":
                type = "etf";
                break;
            case "货币型":
                type = "mmf";
                break;
            case "QDII":
                type = "qdii";
                break;
            case "QDII-ETF":
                type = "qdii";
                break;
            case "保本型":
                type = "gf";
                break;
            case "固定收益":
                type = "sf";
                break;
            case "分级杠杆":
                type = "sf";
                break;
            case "其他创新":
                type = "sf";
                break;
            default:
                logger.error("未知类型:" + type);
                break;
        }
        return type;
    }

    /**
     * 解析更多历史净值页面
     * 添加历史净值数据
     *
     * @param codes     基金代码集合
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public void netValueOfHistory(List<String> codes, java.sql.Date startDate, java.sql.Date endDate, StringBuffer resultMessage) throws Exception {
        //记录本次添加历史净值数据成功次数
        int successfulNumber = 0;

        //记录本次添加历史净值数据失败次数
        int failedNumber = 0;

        //应添加基金历史净值数量
        int shouldNumber = 0;

        //忽略基金历史净值数量
        int ignoreNumber = 0;

        //当前页码
        int pageIndex = 1;
        //页面大小
        int pageSize = 20;
        logger.debug("--------------------------------基金历史净值日志信息开始--------------------------------");
        for (String code : codes) {
            //设置历史净值页面请求头 开始爬取指定基金代码下历史净值信息
            String content = ReptileUtil.headersFund_History(code, pageIndex, pageSize, startDate, endDate);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
            if (jsonObject != null) {
                //获取网页最近更新的时间
                JSONArray lsjzList = jsonObject.getJSONObject("Data").getJSONArray("LSJZList");
                if (lsjzList.size() != 0) {
                    Object firstObj = lsjzList.get(0);
                    JSONObject first = (JSONObject) JSONObject.parse(firstObj.toString());
                    //网站最新更新时间
                    Date firstDate =java.sql.Date.valueOf(first.get("FSRQ").toString());
                    //数据库最近修改时间
                    Date maxDate =queryFundHistoryService.selectGetMaxDate(code); //java.sql.Date.valueOf("2019-01-10");

                    //获取分页数据
                    int totalCount = (int) jsonObject.get("TotalCount");
                    //记录应添加总数
                    shouldNumber += totalCount;
                    pageIndex = (int) jsonObject.get("PageIndex");
                    pageSize = (int) jsonObject.get("PageSize");
                    //指定code数据库最近更新日期不存在 或者 时间不一致
                    if (maxDate == null || !DateFormatUtils.format(maxDate, "yyyy-MM-dd").equals(DateFormatUtils.format(firstDate, "yyyy-MM-dd"))) {
                        //计算总页数
                        int pages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
                        //从总页数开始循环爬取历史净值数据
                        for (int pIndex = pages; pIndex >= 0; pIndex--) {
                            String history = ReptileUtil.headersFund_History(code, pIndex, pageSize, startDate, endDate);
                            JSONArray jsonArray = JsonUtil.getJSONArrayByHtmlJSON(history);
                            for (Object o : jsonArray) {
                                JSONObject info = (JSONObject) JSONObject.parse(o.toString());
                                //从网页返回JSON数据中提取历史净值信息
                                FundHistory fundHistory = new FundHistory();
                                if (!StringUtils.isEmpty(code)) {
                                    fundHistory.setCode(code);
                                } else {
                                    logger.error("[" + code + "]基金历史code为空");
                                    fundHistory.setCode(null);
                                }
                                if (!StringUtils.isEmpty(java.sql.Date.valueOf(info.get("FSRQ").toString()))) {
                                    fundHistory.setDate(java.sql.Date.valueOf(info.get("FSRQ").toString()));
                                } else {
                                    logger.error("[" + code + "]基金历史时间为空");
                                    fundHistory.setDate(null);
                                }
                                //过滤非最新数据
                                if(!StringUtils.isEmpty(fundHistory.getDate())&&DateFormatUtils.format(firstDate, "yyyy-MM-dd").equals(DateFormatUtils.format(fundHistory.getDate(), "yyyy-MM-dd"))){
                                    //判断该数据库是否存在
                                    FundHistoryExample fundHistoryExample = new FundHistoryExample();
                                    fundHistoryExample.createCriteria()
                                            .andDateEqualTo(fundHistory.getDate())
                                            .andCodeEqualTo(code)
                                    ;
                                    List<FundHistory> fundHistories1 = fundHistoryService.selectByExample(fundHistoryExample);
                                    //判断该记录是否重复 不重复则添加
                                    if (fundHistories1.size() < 1) {
                                        if (!StringUtils.isEmpty(info.get("DWJZ").toString())) {
                                            fundHistory.setNetValue(new BigDecimal(info.get("DWJZ").toString()));
                                        } else {
                                            logger.error("[" + code + "]基金历史单位净值为空");
                                            fundHistory.setNetValue(null);
                                        }
                                        if (!StringUtils.isEmpty(info.get("LJJZ").toString())) {
                                            fundHistory.setTotalValue(new BigDecimal(info.get("LJJZ").toString()));
                                        } else {
                                            logger.error("[" + code + "]基金历史累计净值为空");
                                            fundHistory.setTotalValue(null);
                                        }

                                        if (StringUtils.isEmpty(fundHistory.getNetValue()) || StringUtils.isEmpty(fundHistory.getTotalValue())) {
                                            ignoreNumber++;
                                            continue;
                                        }

                                        if (!StringUtils.isEmpty(info.get("JZZZL").toString())) {
                                            fundHistory.setRateNetValue(new BigDecimal(info.get("JZZZL").toString()));
                                        } else {
                                            //日增长率为空时
                                            logger.debug("正在计算基金产品:" + code + "的 " + fundHistory.getDate() + " 日增长率(日增长率为空)");
                                            BigDecimal rateNetValue = fundHistoryRateNetValue(code, fundHistory.getDate());
                                            fundHistory.setRateNetValue(rateNetValue);
                                        }
                                        try {
                                            logger.debug("[" + code + "-" + fundHistory.getDate() + "]正在添加基金历史净值:" + fundHistory);
                                            int insert = fundHistoryService.insert(fundHistory);
                                            if (insert > 0) {
                                                successfulNumber++;
                                            } else {
                                                failedNumber++;
                                            }
                                        } catch (Exception e) {
                                            failedNumber++;
                                            logger.error("添加基金历史净值[" + code + "-" + fundHistory.getDate() + " ]失败:" + e.getMessage());
                                        }
                                    } else {
                                        if (pIndex != 0) {
                                            ignoreNumber++;
                                            logger.error("--数据库已存在历史净值数据[" + code + "-" + fundHistory.getDate() + " ] 序号:" + ignoreNumber);
                                        }
                                    }
                                }else{
                                    //非最新数据
                                    if (pIndex != 0) {
                                        ignoreNumber++;
                                    }
                                }
                            }
                        }
                    } else {
                        //网页更新时间与数据库一致
                        ignoreNumber += totalCount;
                        logger.error("--[" + code + " ]网页更新时间与数据库一致");
                    }
                }
            } else {
                //获取网页最近更新的时间
                /*ignoreNumber++;
                logger.error("--["+code+"]获取网页最近更新的时间失败");*/
            }
        }
        logger.debug("--------------------------------基金历史净值日志信息结束--------------------------------");
        String resultStr = "本次新增基金历史净值数据:应添加[" + shouldNumber + "]条,实际成功[" + successfulNumber + "]条,失败" + failedNumber + "条,忽略[" + ignoreNumber + "]条";
        resultMessage.append(resultStr);
        logger.debug(resultStr);
    }

    /**
     * 计算日增长率
     */
    private static BigDecimal fundHistoryRateNetValue(String code, Date date) throws IOException {
        //总记录数
        int totalCount = ReptileUtil.getTotalCountFundHistory(code);

        String fundHistoryJSON = ReptileUtil.headersFund_History(code, 1, totalCount, null, null);

        BigDecimal rateNetValue = null;
        if (!StringUtils.isEmpty(fundHistoryJSON)) {
            JSONArray jsonArray = JsonUtil.getJSONArrayByHtmlJSON(fundHistoryJSON);
            String format = DateFormatUtils.format(date, "yyyy-MM-dd");
            for (int i = 0; i < jsonArray.size(); i++) {
                //当前JSON数据行
                JSONObject info = (JSONObject) jsonArray.get(i);
                if (info == null) {
                    throw new BEPServiceException(ErrorCode.FUNDHISTORY_JSON_NOT_FOUND, "基金代码为[" + code + "]的[" + date + "]时间段JSON INFO数据为空");
                }
                Object fsrq = info.get("FSRQ");
                if (StringUtils.isEmpty(fsrq)) {
                    throw new BEPServiceException(ErrorCode.FUNDHISTORY_JSON_NOT_FOUND, "基金代码为[" + code + "]的[" + date + "]时间段JSON DATE数据为空");
                }
                if (format.equals(fsrq.toString())) {
                    //json数据非最后一条
                    if (i + 1 < jsonArray.size()) {
                        //获取已经排序好的下一条JSON数据
                        JSONObject nextJSON = (JSONObject) jsonArray.get(i + 1);
                        Object jzzzl = info.get("JZZZL");
                        if (StringUtils.isEmpty(jzzzl)) {
                            //计算为空的日增长率
                            Object dwjz = info.get("DWJZ");
                            Object nextDWJZ = nextJSON.get("DWJZ");
                            if (StringUtils.isEmpty(nextDWJZ) || StringUtils.isEmpty(dwjz)) {
                                rateNetValue = new BigDecimal(0);
                            } else {
                                double netValue = new BigDecimal(dwjz.toString()).doubleValue();
                                double nextNetValue = new BigDecimal(nextDWJZ.toString()).doubleValue();
                                //日增长率=（当天净值-前一日净值）/前一日净值 *100
                                double result = (netValue - nextNetValue) / nextNetValue * 100;
                                rateNetValue = new BigDecimal(result);
                            }
                        } else {
                            rateNetValue = new BigDecimal(jzzzl.toString());
                        }
                        //json数据最后一条
                    } else {
                        Object jzzzl = info.get("JZZZL");
                        if (StringUtils.isEmpty(jzzzl)) {
                            rateNetValue = new BigDecimal(0);
                        } else {
                            rateNetValue = new BigDecimal(jzzzl.toString());
                        }
                    }
                }
            }
        } else {
            rateNetValue = new BigDecimal(0);
        }
        return rateNetValue;
    }

    /**
     * 去除数据后面的%
     * */
    public static String wipeOff(String content){
        if(!StringUtils.isEmpty(content)&&content.indexOf("%")!=-1){
            return content.substring(0,content.indexOf("%"));
        }
        return null;
    }

    /**
     * 获取所有基金产品Code值
     * */
    public static List<String> getCodeAll() throws IOException {
        List<String> codeList=new ArrayList<>();
        String url="http://fund.eastmoney.com/allfund.html";
        String content = ReptileUtil.startReptile(url);
        Document document = Jsoup.parse(content);
        Elements codesElements = document.select("#code_content .num_box .num_right li");
        for (Element codesElement : codesElements) {
            Elements codes = codesElement.select("a:eq(0)");
            //去除内容为空的li元素
            if(codes.text()!=null&&!"".equals(codes.text())){
                String href = codes.attr("href");
                String code = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
                codeList.add(code);
            }
        }
        return codeList;
    }

}
