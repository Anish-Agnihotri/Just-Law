package DocumentServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class FileIndexer {

    public int index(File indexDir, File dataDir, String suffix) throws Exception {
        IndexWriter indexWriter= null;

        try {
            indexWriter = new IndexWriter(
                    FSDirectory.open(indexDir),
                    new SimpleAnalyzer(),
                    true,
                    IndexWriter.MaxFieldLength.LIMITED);
            indexWriter.setUseCompoundFile(false);
            indexDirectory(indexWriter, dataDir, suffix);
            //int numIndexed = indexWriter.maxDoc();
            indexWriter.optimize();
            indexWriter.close();
            //return numIndexed;
            return 0;
        }
        finally {
            indexWriter.close();
            return 0;
        }

    }

    private void indexDirectory(IndexWriter indexWriter, File dataDir, String suffix) throws IOException {
        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(indexWriter, f, suffix);
            }
            else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }

    }

    private void indexFileWithIndexWriter(IndexWriter indexWriter, File f, String suffix) throws IOException {
        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
            return;
        }

        // If you want all extensions, instead of just �java�, then you can leave out that parameter. This would
        // change the �index�, �indexDirectory�, and �indexFileWithIndexWriter� constructors to not have the parameter passed in.
        //Finally, you would want to remove:

        if (suffix!=null && !f.getName().endsWith(suffix)) {
            return;
        }

        System.out.println("Indexing file:... " + f.getCanonicalPath());

        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));

        indexWriter.addDocument(doc);
    }
}