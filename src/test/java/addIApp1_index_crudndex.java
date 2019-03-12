import com.baidu.entity.Book;
import com.baidu.impl.BookDaoImpl;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addIApp1_index_crudndex {
    /**
     * 3.lucene索引库维护（一）添加索引
     * 索引库：
     * 1. 索引域： 存储的是索引数据。
     * 2. 文档域： 存储的是文档数据。
     *
     * 添加索引：
     * 只要添加文档对象，会同时生成索引域、文档域。
     */
    private static final String PATH = "D:\\library\\index";

    @Test
    public void addIndex() throws Exception {
        //1.采集数据
        List<Book> bookList = new BookDaoImpl().findAllBooks();
        //2.转换为文档对象
        List<Document> documentList = new ArrayList<>();
        for (Book book:bookList){
            //创建文档对象
            Document doc = new Document();
            doc.add(new StringField("id",book.getId()+"",Field.Store.YES));
            doc.add(new TextField("bookname",book.getBookname(),Field.Store.YES));
            doc.add(new DoubleField("price",book.getPrice(),Field.Store.YES));
            doc.add(new StoredField("pic",book.getPic()));
            doc.add(new TextField("bookdesc",book.getBookDesc(),Field.Store.NO));
            documentList.add(doc);

        }
        //3.分析器
        Analyzer analyzer = new IKAnalyzer();
        //4.索引器
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        //每次重新创建索引库
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //5.索引库目录
        Directory d = FSDirectory.open(new File(PATH));
        //6.索引库操作对象
        IndexWriter indexWriter = new IndexWriter(d,conf);
        //遍历文档
        documentList.forEach(doc ->{

            try {
                //7.把文档写入索引库(索引域,文档域都会写入数据)
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //8.释放资源
        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void deleteByTerm() throws Exception {
        //创建索引库操作对象,操作索引库
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(PATH)),new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer()));
        //根据term条件进行删除
        //条件对象:new term(字段,字段的值);
        indexWriter.deleteDocuments(new Term("bookname","lucene"));
 	//123
        //释放资源
        indexWriter.commit();
        indexWriter.close();
    }

}
