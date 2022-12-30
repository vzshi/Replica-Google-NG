package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNetReader wnr;
    private NGramMap ngm;

    public HyponymsHandler(WordNetReader reader, NGramMap map) {
        wnr = reader;
        ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        ArrayList<String> commonStrs = wnr.bstTraversal(words.get(0));

        if (k > 0) {
            if (words.size() > 1) {
                for (String word : words) {
                    commonStrs.retainAll(wnr.bstTraversal(word));
                    if (commonStrs.size() == 0) {
                        return commonStrs.toString();
                    }
                }
            }
            TreeMap<Double, String> sortedKeys = new TreeMap<>();
            for (String word : commonStrs) {
                TimeSeries wordTS = ngm.countHistory(word, startYear, endYear);
                if (wordTS.isEmpty()) {
                    continue;
                } else {
                    double sum = 0.0;
                    for (int i = startYear; i <= endYear; i++) {
                        if (wordTS.get(i) != null) {
                            sum += wordTS.get(i);
                        }
                    }
                    sortedKeys.put(sum, word);
                }
            }
            ArrayList<String> finalString = new ArrayList<>(sortedKeys.values());
            while (finalString.size() > k) {
                finalString.remove(0);
            }
            Collections.sort(finalString);
            return finalString.toString();
        } else {
            if (words.size() > 1) {
                for (String word : words) {
                    commonStrs.retainAll(wnr.bstTraversal(word));
                    if (commonStrs.size() == 0) {
                        return commonStrs.toString();
                    }
                }
            }
            Collections.sort(commonStrs);
            return commonStrs.toString();
        }
    }
}
