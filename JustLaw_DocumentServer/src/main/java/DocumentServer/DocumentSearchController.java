package DocumentServer;

import DocumentServer.Models.Case;
import DocumentServer.Models.SearchResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DocumentSearchController {

    @RequestMapping(value = "/test/suggestions")
    List<String> getAutocompleteSuggestions(String searchString) {
        return Arrays.asList("suggestion 1", "suggestion 2");
    }

    // No idea why Spring isn't seeing one of the two request params if they're both params. Switch one to a path variable.
    @RequestMapping(value = "/{caseId}/case", method = GET)
    Case getCase(@PathVariable String caseId, @RequestParam String searchString) throws IOException {
        Case case_ = new Case();
        case_.caseId = caseId;
        case_.tags = new ArrayList<>();

        case_.fullTextHtml = getHighlightedFullText(caseId, searchString);
        case_.generatedHeadnotesHtml = getHighlightedSummaryText(caseId, searchString);

        return case_;
    }

    private String getHighlightedFullText(String caseId, String searchString) throws IOException {
        String line;
        List<String> allLines = new ArrayList<>();
        String prefix = "C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\text\\";
        try (
                InputStream fis = new FileInputStream(prefix + caseId);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
        }

        String catted = allLines.stream().collect(Collectors.joining(""));
        return highlight(catted, Arrays.asList(searchString.split(" ")));
    }

    private String getHighlightedSummaryText(String caseId, String searchString) throws IOException {
        try {
            String line;
            List<String> allLines = new ArrayList<>();
            String prefix = "C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\summary\\";
            try (
                    InputStream fis = new FileInputStream(prefix + caseId);
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    allLines.add(line);
                }
            }
            if (allLines.isEmpty()) return "";

            String catted = allLines.stream().collect(Collectors.joining(""));
            return highlight(catted, Arrays.asList(searchString.split(" ")));
        }
        catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = "/search", method = GET)
    public List<SearchResult> search(@RequestParam String searchString) throws Exception {
        List<String> matchingDocs = getMatchingDocs(searchString)
                .stream()
                .map(x -> Arrays.asList(x.split("\\\\")))
                .map(x -> x.get(x.size()-1))
                .collect(Collectors.toList());

        List<SearchResult> results = new ArrayList<>();
        for (String docid : matchingDocs) {
            SearchResult result = new SearchResult();
            result.caseId = docid;
            result.tags = new ArrayList<>();
            String line;

            List<String> allLines = new ArrayList<>();
            String prefix = "C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\text\\";
            try (
                    InputStream fis = new FileInputStream(prefix + docid);
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    allLines.add(line);
                }
            }

            List<String> keywords = Arrays.asList(searchString.split(" "));

            for (String keyword : keywords) {
                allLines = allLines.stream().filter(x -> x.contains(keyword)).collect(Collectors.toList());
            }

            String bestLine = allLines
                    .stream()
                    .sorted((x0, x1) -> x0.length() > x1.length() ? 1 : -1)
                    .findFirst()
                    .orElseGet(() -> "");

            result.previewTextHtml = highlight(bestLine, keywords);
            results.add(result);
        }

        return results;
    }

    List<String> getMatchingDocs(String searchString) throws Exception {
        if (!indexed) {
            setupIndex();
            indexed = true;
        }

        int hits = 100;
        Searcher searcher = new Searcher();
        return searcher.searchIndex(searchString, hits);
    }

    void setupIndex() throws Exception {
        File indexDir = new File("C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\index");
        File dataDir = new File("C:\\Users\\dgadal\\IdeaProjects\\Just\\JustDocumentServer\\src\\main\\resources\\raw-text\\text");
        String suffix = "txt";
        FileIndexer indexer = new FileIndexer();
        TimeUnit.SECONDS.sleep(5);
        int numIndex = indexer.index(indexDir, dataDir, suffix);
        //System.out.println("Number of total files indexed:  " + numIndex);
    }

    private String highlight(String s, List<String> keywords) {
        String pre = "<div class=\"highlighted\">";
        String post = "<\\div>";
        return Arrays.stream(s.split(" ")).map(w -> keywords.stream().anyMatch(k -> w.contains(k)) ? pre + w + post : w).collect(Collectors.joining(" "));
    }

    private static boolean indexed = false;
}
