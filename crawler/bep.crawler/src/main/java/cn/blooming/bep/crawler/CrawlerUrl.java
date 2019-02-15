package cn.blooming.bep.crawler;

public class CrawlerUrl {
    /**
     * 爬取入口:基金公司一览表
     * */
    public static final String CRAWLER_START_URL="http://fund.eastmoney.com/company/default.html#scomname;dasc";
    /**
     * 基金详情页面路径前缀
     * */
    public static final String CRAWLER_FUND_URL_PREFIX="http://fund.eastmoney.com/";
    /**
     * 所有不带headers页面路径的后缀
     * */
    public static final String CRAWLER_URL_SUFFIX=".html";

    /**
     * 历史净值页面路径前缀
     * */
    public static final String CRAWLER_FUNDHISTORY_URL_PREFIX="http://fundf10.eastmoney.com/jjjz_";
    /**
     * 历史净值带headers的路径前缀
     * */
    public static final String CRAWLER_FUNDHISTORY_HEADERS_URL_PREFIX="http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery183009758356277391722_1545872675528";
    /**
     * 历史净值带headers的路径后缀
     * */
    public static final String CRAWLER_FUNDHISTORY_HEADERS_URL_SUFFIX="&_=1545894699525";

}
