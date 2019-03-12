import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class App2_index_search {

    private static final String PATH = "D:\\library\\index";

    public static void main(String[] args) throws Exception {
        //建立分析器对象,用于分词
        Analyzer analyzer = new IKAnalyzer();
        //建立查询对象
        QueryParser queryParser = new QueryParser("bookename",analyzer);
        Query query = queryParser.parse("bookname:java");
        //建立索引库目录对象(Directory),指定索引库的位置
        Directory directory = FSDirectory.open(new File(PATH));
        IndexReader r = DirectoryReader.open(directory);

        //关键对象:创建索引库搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(r);
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("搜索结果总数:"+topDocs.totalHits);

        //获取搜索结果
        //ScoreDoc包含了文档的id,分值
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs){
            System.out.println("------------------------------------");
            //根据ScoreDoc提供的score属性,获取分值
            System.out.println("文档分值:"+scoreDoc.score);
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //获取文档域
            System.out.println("图书ID:" + doc.get("id"));
            System.out.println("图书名称:" + doc.get("bookname"));
            System.out.println("图书价格:" + doc.get("price"));
            System.out.println("图片路径:" + doc.get("pic"));
            System.out.println("图书描述:" + doc.get("bookdesc"));
        }
        //关闭读取器,释放资源
        r.close();
    }
}
