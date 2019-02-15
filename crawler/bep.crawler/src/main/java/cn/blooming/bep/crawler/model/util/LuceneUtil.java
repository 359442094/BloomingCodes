package cn.blooming.bep.crawler.model.util;

import cn.blooming.bep.crawler.LuceneUrl;
import cn.blooming.bep.crawler.model.entity.Fund;
import cn.blooming.bep.crawler.model.entity.FundHouse;
import cn.blooming.bep.crawler.model.lucene.ik.IKAnalyzer4Lucene7;
import cn.blooming.bep.data.api.model.SearchFundsResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuceneUtil {

    private static final Logger logger = LoggerFactory.getLogger(LuceneUtil.class);
    /**
     * 写入基金内容信息
     * */
    public static void witerFundIndex(boolean isKey,String witerPath, List<FundHouse> fundHouses, List<Fund> funds, Analyzer analyzer) throws Exception {
        logger.debug("正准备写入基金索引");
        List<Document> documents = getFundDocument(isKey,fundHouses, funds);
        if(isKey){
            witerIndex(witerPath,analyzer,documents,false);
        }else{
            witerIndex(witerPath,analyzer,documents,true);
        }
    }

    /**
     * 写入索引
     * */
    public static void witerIndex(String witerPath,Analyzer analyzer,List<Document> documents,Boolean emptyFlag) throws IOException {
        // 创建IndexWriter
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        // 指定索引库的地址
        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(witerPath));
        IndexWriter writer = new IndexWriter(directory, cfg);
        //为true清除之前的索引
        if(emptyFlag){
            logger.debug("清空上次遗留的索引");
            writer.deleteAll();
        }
        // 通过IndexWriter对象将Document写入到索引库中
        for (Document document : documents) {
            writer.addDocument(document);
        }
        writer.commit();
        // 关闭writer
        writer.close();
    }

    /**
     * 写入标准分词器索引
     * */
    public static void witerKeyIndex(String witerPath,String key,Boolean emptyFlag) throws IOException, BadHanyuPinyinOutputFormatCombination {

        //IKAnalyzer 智能切分
        //分词效果:华夏成长(华夏、成长)
        Analyzer analyzer = new IKAnalyzer4Lucene7(true);
        TokenStream tokenStream = analyzer.tokenStream("key", key);

        List<Document> documents = getKeyDocument(tokenStream);
        //默认分词器
        //分词效果:华夏、成长(华、夏、成、长)
        analyzer = new StandardAnalyzer();

        logger.debug("开始写入["+key+"]的关键词索引");
        witerIndex(witerPath,analyzer,documents,emptyFlag);

        tokenStream.end();
        tokenStream.close();
    }

    /**
     * 基金关键词Document
     * */
    public static List<Document> getKeyDocument(TokenStream tokenStream) throws IOException, BadHanyuPinyinOutputFormatCombination {
        logger.debug("正在获取基金关键词Document信息");
        tokenStream.reset();
        CharTermAttribute cta = tokenStream.getAttribute(CharTermAttribute.class);
        List<Document> documents = new ArrayList<>();
        List<String> keys=new ArrayList<>();
        while (tokenStream.incrementToken()) {
            //添加关键字分词至集合
            keys.add(cta.toString());
        }
        //关键字去除重复
        List<String> keyDuplicate = removeKeyDuplicate(keys);

        for (String key : keyDuplicate) {
            Document document=new Document();

            logger.debug("正在写入关键词["+key+"]");
            Field fieldKey=new TextField("key",key, Field.Store.YES);

            document.add(fieldKey);

            documents.add(document);
        }

        return documents;
    }



    /**
     * 基金分词域Document
     * */
    public static List<Document> getFundDocument(boolean isKey,List<FundHouse> fundHouses, List<Fund> funds) throws Exception {
        logger.debug("正在获取基金分词域Document信息");
        List<Document> documents = new ArrayList<>();
        Boolean emptyFlag=true;
        Map<String,String> fundHouseMap=new HashMap<>();

        for (int i=0;i<fundHouses.size();i++) {
            if(i>0){
                emptyFlag=false;
            }
            if(isKey) {
                LuceneUtil.witerKeyIndex(LuceneUrl.LUCENE_DEFAULT_PATH, fundHouses.get(i).getName(), emptyFlag);
            }
            fundHouseMap.put(fundHouses.get(i).getCode(),fundHouses.get(i).getName());
        }

        //创建索引
        for (Fund fund:funds) {
            Document document = new Document();
            //写入关键词索引内容
            if(isKey){
                LuceneUtil.witerKeyIndex(LuceneUrl.LUCENE_DEFAULT_PATH,fund.getName(),false);
            }

            String fundHouseName=fundHouseMap.get(fund.getFundHouseCode());

            logger.debug("正在写入["+fundHouseName+"]的["+fund.getCode()+"]基金");

            logger.debug("--公司名称:" + fund.getName());
            Field fieldFundHouseNameAll=new StringField("fundHouseNameAll",fundHouseName,Field.Store.YES);
            Field fieldFundHouseName=new TextField("fundHouseName",fundHouseName, Field.Store.YES);
            logger.debug("--基金代码:" + fund.getCode());
            Field fieldFundCode = new TextField("code", fund.getCode(), Field.Store.YES);
            logger.debug("--基金名称:" + fund.getName());
            Field fieldFundNameAll=new StringField("fundNameAll",fund.getName(),Field.Store.YES);
            Field fieldFundName = new TextField("fundName",fund.getName(), Field.Store.YES);

            //只存储不创建索引
            String jsonFund = JSON.toJSONString(fund);
            logger.debug("--基金内容[只存储不创建索引]:" + jsonFund);
            Field fieldFund = new StoredField("value", jsonFund);

            //全拼
            String quanpin;
            //简拼
            String jianpin;

            //长为多音字  默认为chang 这里应该为zhang
            if ("华夏成长".equals(fund.getName())) {
                quanpin = "huaxiachengzhang";
                jianpin = "hxcz";
            } else {
                quanpin = Pinyin4jUtil.chineseToPinYinQ(fund.getName());
                jianpin = Pinyin4jUtil.chineseToPinYinJ(fund.getName());
            }

            logger.debug("--["+fund.getName()+"] 全拼:" + quanpin);
            logger.debug("--["+fund.getName()+"] 简拼:" + jianpin);

            Field fieldPinYinQuanPin = new TextField("quanpin", quanpin, Field.Store.YES);
            Field fieldPinYinJianPin = new TextField("jianpin", jianpin, Field.Store.YES);


            document.add(fieldFund);
            document.add(fieldFundCode);
            document.add(fieldFundNameAll);
            document.add(fieldFundName);
            document.add(fieldFundHouseNameAll);
            document.add(fieldFundHouseName);
            document.add(fieldPinYinQuanPin);
            document.add(fieldPinYinJianPin);

            documents.add(document);
        }

        return documents;
    }

    /**
     * 设置权重、模糊查询
     */
    public static Query doQuery(String query, Analyzer analyzer) throws ParseException {
        //设置权重
        Map<String, Float> boosts = new HashMap<String, Float>();
        boosts.put("key",4.0f);              //关键词
        boosts.put("keyValue",3.9f);        //关键词内容
        boosts.put("fundName", 3.0f);       //基金名称
        boosts.put("fundHouseName", 2.0f); //公司名称
        boosts.put("code", 1.1f);           //基金代码
        //使用QueryParser搜索时，需要指定分词器，搜索时的分词器要和索引时的分词器一致
        //指定搜索的域的名称
        //MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_CURRENT, new String[]{"name", "code"}, analyzer, boosts);
        QueryParser queryParser = new MultiFieldQueryParser(new String[]{"key","keyValue","fundName","fundHouseName", "code"}, analyzer, boosts);

        //返回
        return queryParser.parse(query);
    }

    public static Query doQuery(String fieldName, String query) throws ParseException {
        return new TermQuery(new Term(fieldName, query));
    }

    /**
     * 搜索基金索引中的内容
     */
    public static List<cn.blooming.bep.data.api.model.Fund> doFundSearch(String readPath, Query query,int pageIndex, int pageSize, SearchFundsResponse searchFundsResponse) {
        List<cn.blooming.bep.data.api.model.Fund> funds = new ArrayList<>();
        try {
            // 1、创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(readPath));
            // 指定索引库的地址
            IndexReader reader = DirectoryReader.open(directory);
            // 创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
            // 通过searcher来搜索索引库
            // 第二个参数：指定需要显示的顶部记录的N条
            TopDocs topDocs = searcher.search(query, 1000);
            // 根据查询条件匹配出的记录
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            // 根据查询条件匹配出的记录总数
            int count =(int)topDocs.totalHits;
            logger.debug("匹配出的记录总数:" + count);
            searchFundsResponse.setTotalEntries((long)count);
            int pages= count%pageSize==0?count/pageSize:count/pageSize+1;
            logger.debug("匹配出的总页数:" + pages);
            searchFundsResponse.setPages(pages);
            //分页获取索引内容
            doPageFundIndexContent(funds,searcher,scoreDocs,pageIndex,pageSize);

            //关闭资源
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return funds;
    }

    /**
     * 分页获取基金索引内容
     * */
    public static void doPageFundIndexContent(List<cn.blooming.bep.data.api.model.Fund> funds,IndexSearcher searcher,ScoreDoc[] scoreDocs,int pageIndex,int pageSize) throws IOException {
        logger.debug("匹配出的基金信息");
        //查询起始记录位置
        int begin = pageSize * (pageIndex - 1) ;

        //查询终止记录位置
        int end = Math.min(begin + pageSize, scoreDocs.length);

        //进行分页查询
        for(int i=begin;i<end;i++) {
            // 获取文档的ID
            int docID = scoreDocs[i].doc;
            // 通过ID获取文档
            Document doc = searcher.doc(docID);
            logger.debug("公司名称:" + doc.get("fundHouseName"));
            logger.debug("基金代码:" + doc.get("code"));
            logger.debug("基金名称:" + doc.get("fundName"));
            JSONObject object = (JSONObject) JSON.parse(doc.get("value"));
            logger.debug("基金内容:" + object);

            cn.blooming.bep.data.api.model.Fund fund = JsonUtil.apiFundByJSONObject(object);

            funds.add(fund);

            removeFundDuplicate(funds);
        }
    }


    /**
     * 搜索关键词索引中的内容
     */
    public static List<String> doKeySearch(String readPath, Query query,int resultNumber) {
        List<String> keys=new ArrayList<>();
        try {
            // 1、创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(readPath));
            // 指定索引库的地址
            IndexReader reader = DirectoryReader.open(directory);
            // 创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
            // 通过searcher来搜索索引库
            // 第二个参数：指定需要显示的顶部记录的N条
            TopDocs topDocs = searcher.search(query, resultNumber);

            // 根据查询条件匹配出的记录总数
            //int count = (int) topDocs.totalHits;

            // 根据查询条件匹配出的记录
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            //获取索引内容
            keys = doKeyIndexContent(searcher, scoreDocs);

            // 关闭资源
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

    /**
     * 关键词索引内容
     * */
    public static List<String> doKeyIndexContent(IndexSearcher searcher,ScoreDoc[] scoreDocs) throws IOException {
        List<String> keys=new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档的ID
            int docId = scoreDoc.doc;
            // 通过ID获取文档
            Document doc = searcher.doc(docId);
            //logger.debug("基金名称:" + doc.get("key"));
            //logger.debug("基金内容:" + doc.get("keyValue"));
            if(!doc.get("key").contains(keys.toString())){
                keys.add(doc.get("key"));
            }
        }
        return keys;

    }

    /**
     * 判断用户的输入内容是数字、还是中文、或者拼音字母
     */
    public static String isSearch(String search) {
        if (isNumer(search)) {
            //数字
            return "number";
        }else if(isChinese(search)){
            //中文
            return "chinese";
        }else if(isLetter(search)){
            //字母
            return "letter";
        }else{
            //其它
            return "其它";
        }
    }

    /**
     * JAVA自带的函数
     * 判断字符串是否全是数字
     */
    private static boolean isNumer(String search) {
        for (int i = search.length(); --i >= 0; ) {
            if (!Character.isDigit(search.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全是中文
     * */
    private static boolean isChinese(String search) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(search);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否全是字母
     * */
    private static boolean isLetter(String search){
        String reg="[a-zA-Z]+";
        return search.matches(reg);
    }

    /**
     * 去除List中重复的关键词
     * */
    public static List<String> removeKeyDuplicate(List<String> keys) {
        HashSet h = new HashSet(keys);
        keys.clear();
        keys.addAll(h);
        return keys;
    }

    /**
     * 去除List中重复的基金产品
     * */
    public static List<cn.blooming.bep.data.api.model.Fund> removeFundDuplicate(List<cn.blooming.bep.data.api.model.Fund> funds) {
        HashSet h = new HashSet(funds);
        funds.clear();
        funds.addAll(h);
        return funds;
    }

}
