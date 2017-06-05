package DocumentServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
    public List<String> searchIndex(String queryStr, int maxHits) throws Exception {
        File indexDir = new File("C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\index");
        Directory directory = FSDirectory.open(indexDir);
        @SuppressWarnings("deprecation")
        IndexSearcher searcher = new IndexSearcher(directory);
        @SuppressWarnings("deprecation")
        QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer());
        Query query = parser.parse(queryStr);
        TopDocs topDocs = searcher.search(query, maxHits);

        ScoreDoc[] hits = topDocs.scoreDocs;
        List<String> docids = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
            docids.add(d.get("filename"));
        }

        System.out.println("Found " + hits.length);
        return docids;
    }
}
