package cn.blooming.bep.crawler.model.lucene.ik;

import org.apache.lucene.analysis.Analyzer;

public class IKAnalyzer4Lucene7 extends Analyzer {
    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * IK分词器Lucene  Analyzer接口实现类
     *
     * 默认细粒度切分算法
     */
    public IKAnalyzer4Lucene7() {
        this(false);
    }

    /**
     * IK分词器Lucene Analyzer接口实现类
     *
     * @param useSmart 当为true时，分词器进行智能切分
     */
    public IKAnalyzer4Lucene7(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

//    /**
//     * 重载Analyzer接口，构造分词组件
//     */
//    @Override
//    protected Analyzer.TokenStreamComponents createComponents(String fieldName, final Reader in) {
//        Tokenizer _IKTokenizer = new IKTokenizer(in, this.useSmart());
//        return new Analyzer.TokenStreamComponents(_IKTokenizer);
//    }

    /**
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenizer4Lucene7 tokenizer4Lucene7 = new IKTokenizer4Lucene7(this.useSmart);
        return new TokenStreamComponents(tokenizer4Lucene7);
    }
}
