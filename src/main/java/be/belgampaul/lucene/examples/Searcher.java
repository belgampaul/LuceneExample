package be.belgampaul.lucene.examples;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author ikka
 * @date: 08.10.2015.
 */
public class Searcher {
  public static void main(String[] args) {
    try (Analyzer analyzer = new EnglishAnalyzer();
         FSDirectory indexDir = NIOFSDirectory.open(Paths.get(Constants.luceneIndexLocation));
         IndexReader indexReader = DirectoryReader.open(indexDir)
    ) {
      IndexSearcher indexSearcher = new IndexSearcher(indexReader);
      BooleanQuery.Builder builder = new BooleanQuery.Builder();
      QueryParser queryParser = new QueryParser("text", analyzer);
      Query textFilter = queryParser.parse("state");
      Query amountFilter = NumericRangeQuery.newLongRange("amount", 1000L, 1000L, true, true);
      Query amountFilter2 = NumericRangeQuery.newLongRange("amount", 2000L, 2000L, true, true);
      builder.add(textFilter, BooleanClause.Occur.MUST);

      BooleanQuery.Builder builder2 = new BooleanQuery.Builder();
      builder2.add(amountFilter, BooleanClause.Occur.SHOULD);
      builder2.add(amountFilter2, BooleanClause.Occur.SHOULD);

//      builder.add(builder2.build(), BooleanClause.Occur.MUST);
      BooleanQuery query = builder.build();
      //      BooleanQuery booleanQuery = (BooleanQuery) builder;
      //      booleanQuery.
      TopDocs results = indexSearcher.search(query, 1000);
      System.out.println("Number of hits: " + results.scoreDocs.length);


      //      Field id = new LongField("id", 1L, LongField.TYPE_STORED);
//      Field text = new TextField("text", "LockFactory implementations are singletons now and have no state. They only need to implement one method: makeLock(Directory dir, String name).", Field.Store.NO);
      //      doc.add(id);
      //      doc.add(text);
//      Term id = new Term("id", "1");
//      indexReader.deleteDocuments(id);
      //      indexReader.updateDocument(id, doc);
      //  indexReader.deleteAll();

//      indexReader.commit();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
