package be.belgampaul.lucene.examples;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author ikka
 * @date: 08.10.2015.
 */
public class Indexer {

  static class ValueHolder {
    final String id;
    final Long amount;
    final String text;

    public ValueHolder(String id, Long amount, String text) {
      this.id = id;
      this.amount = amount;
      this.text = text;
    }

    public Document getDocument(){
      Document doc = new Document();
      ArrayList<Field> fields = new ArrayList<>();
      fields.add(new StringField("id", this.id, Field.Store.YES));
      fields.add(new LongField("amount", this.amount, Field.Store.YES));
      fields.add(new TextField("text", this.text, Field.Store.NO));

      fields.stream().forEach(doc::add);

      return doc;
    }
  }

  public static void main(String[] args) {
    try (Analyzer analyzer = new EnglishAnalyzer();
         FSDirectory indexDir = NIOFSDirectory.open(Paths.get(Constants.luceneIndexLocation));
         IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(analyzer))
    ) {

      String text = "state";
      ValueHolder valueHolder = new ValueHolder("4", 3000L, text);
      Term termId = new Term("id", valueHolder.id);
//      indexWriter.deleteDocuments(id);
      indexWriter.updateDocument(termId, valueHolder.getDocument());
//  indexWriter.deleteAll();

      indexWriter.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
