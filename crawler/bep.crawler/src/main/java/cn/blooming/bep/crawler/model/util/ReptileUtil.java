package cn.blooming.bep.crawler.model.util;
import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.crawler.CrawlerUrl;
import cn.blooming.bep.crawler.ErrorCode;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 爬虫工具类
 */
public class ReptileUtil {

    private static final Logger logger= LoggerFactory.getLogger(ReptileUtil.class);
    private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    /**
     * 开始爬取指定页面返回HTML内容字符串数据
     * @param url 爬取入口
     * @return 爬取结果数据
     */
    public static String startReptile(String url) throws IOException {
        //忽略html异常
        ignoreHtmlException(webClient);
        //获取指定路径页面
        HtmlPage page = webClient.getPage(url);
        //关闭WebClient对象
        closeWebClient(webClient);
        //爬取结果数据
        return page.getWebResponse().getContentAsString();
    }
    /**
     * 爬取带请求头的页面
     * */
    public static String startReptile(WebRequest webRequest) throws IOException {
        //忽略html异常
        ignoreHtmlException(webClient);
        //获取指定路径页面
        Page page = webClient.getPage(webRequest);
        //关闭WebClient对象
        closeWebClient(webClient);
        //爬取结果数据
        return page.getWebResponse().getContentAsString();
    }

    /**
     * 返回历史净值所需要的WebRequest
     * */
    public static WebRequest startReptileGetWebRequest(String url, String code) throws MalformedURLException {
        String refer = CrawlerUrl.CRAWLER_FUNDHISTORY_URL_PREFIX + code + CrawlerUrl.CRAWLER_URL_SUFFIX;
        URL link = new URL(url);
        WebRequest request = new WebRequest(link);
        request.setCharset("UTF-8");
        /*request.setProxyHost("api.fund.eastmoney.com"); //61.152.229.180
        request.setProxyPort(80);*/
        request.setAdditionalHeader("Referer", refer);//设置请求报文头里的refer字段
        //设置请求报文头里的User-Agent字段
        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        return request;
    }

    /**
     * 忽略html异常
     * */
    private static void ignoreHtmlException(WebClient webClient){
        //忽略js异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //关闭js
        webClient.getOptions().setJavaScriptEnabled(false);
        //关闭css
        webClient.getOptions().setCssEnabled(false);
    }

    /**
     * 关闭WebClient对象
     * */
    public static void closeWebClient(WebClient webClient){
        webClient.close();
    }

    /**
     * 截取返回结果成JSON字符串处理
     * */
    public static String convertJSON(String requestStr){
        if(!StringUtils.isEmpty(requestStr)&&requestStr.indexOf("(")!=-1&&requestStr.indexOf(")")!=-1){
            return requestStr.substring(requestStr.indexOf("(") + 1, requestStr.indexOf(")"));
        }
        logger.error("截取["+requestStr+"]失败");
        throw new BEPServiceException(ErrorCode.INTERCEPTION_ERROR,"截取["+requestStr+"]失败");
    }

    /**
     * 历史净值页面
     *      爬取指定页数、时间段
     * @param code      基金代码
     * @param pageIndex 当前页码
     * @param pageSize  页面大小
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 历史净值爬取响应结果
     */

    public static String headersFund_History(String code, int pageIndex, int pageSize, java.sql.Date startDate, java.sql.Date endDate) throws IOException {
        String url;
        if (startDate != null && endDate != null) {
            url = CrawlerUrl.CRAWLER_FUNDHISTORY_HEADERS_URL_PREFIX+"&fundCode=" + code + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&startDate=" + startDate + "&endDate=" + endDate + CrawlerUrl.CRAWLER_FUNDHISTORY_HEADERS_URL_SUFFIX;//1545881787608想采集的网址
        } else {
            url = CrawlerUrl.CRAWLER_FUNDHISTORY_HEADERS_URL_PREFIX+"&fundCode=" + code + "&pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&startDate=&endDate"+CrawlerUrl.CRAWLER_FUNDHISTORY_HEADERS_URL_SUFFIX;//1545881787608想采集的网址
        }
        //获取历史净值需要设置的参数
        WebRequest webRequest = ReptileUtil.startReptileGetWebRequest(url, code);

        String requestStr = ReptileUtil.startReptile(webRequest);

        if(StringUtils.isEmpty(requestStr)){
            return null;
        }

        return ReptileUtil.convertJSON(requestStr);
    }

    /**
     * 获取历史净值指定code总记录数
     * */
    public static int getTotalCountFundHistory(String code) throws IOException {
        String fundHistoryJSON = ReptileUtil.headersFund_History(code, 1, 1, null, null);
        JSONObject parse =(JSONObject) JSONObject.parse(fundHistoryJSON);
        if(parse!=null){
            //总记录数
            Object count = parse.get("TotalCount");
            if(!StringUtils.isEmpty(count)&&!"0".equals(count.toString())){
                return Integer.valueOf(count.toString());
            }
        }
        logger.error("历史净值["+code+"]总记录数为空");
        throw new BEPServiceException(ErrorCode.FUNDHISTORY_TOTAL_NOT_FOUND,"历史净值["+code+"]总记录数为空");
    }
}
