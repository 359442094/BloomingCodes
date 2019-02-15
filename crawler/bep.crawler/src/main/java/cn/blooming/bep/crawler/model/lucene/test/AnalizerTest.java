package cn.blooming.bep.crawler.model.lucene.test;

import cn.blooming.bep.crawler.model.lucene.ik.IKAnalyzer4Lucene7;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalizerTest {
    //String key,TokenStream tokenStream
    private static void doToken() throws IOException {
        String chineseText = "华夏成长";
        //IKAnalyzer 智能切分
        Analyzer analyzer = new IKAnalyzer4Lucene7(true);
        TokenStream tokenStream = analyzer.tokenStream("content", chineseText);
        System.out.println("IKAnalyzer中文分词器 智能切分，中文分词效果：");
        //doToken(chineseText,tokenStream);

        tokenStream.reset();
        CharTermAttribute cta = tokenStream.getAttribute(CharTermAttribute.class);

        List<Document> documents = new ArrayList<>();

        while (tokenStream.incrementToken()) {
            System.out.print(cta.toString() + "|");
            Field fieldKey = new TextField("key", cta.toString(), Field.Store.YES);
            Document document = new Document();
            document.add(fieldKey);
            documents.add(document);
        }

        Directory directory = FSDirectory.open(new File("D:/lucene/index2").toPath());
        analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        for (Document document : documents) {
            writer.addDocument(document);
        }

        writer.commit();

        System.out.println();
        tokenStream.end();
        tokenStream.close();

    }

    /*public static void main(String[] args) throws Exception{
        *//*String chineseText = "华夏成长";
        //IKAnalyzer 智能切分
        Analyzer analyzer = new IKAnalyzer4Lucene7(true);
        TokenStream tokenStream = analyzer.tokenStream("content", chineseText);
        System.out.println("IKAnalyzer中文分词器 智能切分，中文分词效果：");*//*
        //doToken();

        //LuceneUtil.witerKeyIndex("华夏成长");
    }*/
}
