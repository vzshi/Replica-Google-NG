package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap ngm;
    public HistoryTextHandler(NGramMap map) {
        ngm = map;
    }

    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        TimeSeries newTS = new TimeSeries();

        for (String word : words) {
            newTS.putAll(ngm.weightHistory(word, startYear, endYear));
            response += word + ": " + newTS.toString() + "\n";
            newTS.clear();
        }
        return response;
    }

}
