package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

/** An object that provides utility methods for making queries on the
 *  Google NGrams dataset (or a subset thereof).
 *
 *  An NGramMap stores pertinent data from a "words file" and a "counts
 *  file". It is not a map in the strict sense, but it does provide additional
 *  functionality.
 *
 *  @author Josh Hug
 */
public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    private HashMap<String, TimeSeries> freq;
    private TimeSeries wordCount;

    public NGramMap(String wordsFilename, String countsFilename) {
        In allWords = new In(wordsFilename);
        freq = new HashMap<String, TimeSeries>();
        wordCount = new TimeSeries();
        String[] eachLine;
        while (allWords.hasNextLine()) {
            eachLine = allWords.readLine().split("[\t]+");
            Integer yearlyCounts = Integer.parseInt(eachLine[1]);
            Integer eachCount = Integer.parseInt(eachLine[2]);
            TimeSeries newTS = new TimeSeries();
            if (freq.containsKey(eachLine[0])) {
                freq.get(eachLine[0]).put(yearlyCounts, (double) eachCount);
            } else {
                newTS.clear();
                newTS.put(yearlyCounts, (double) eachCount);
                freq.put(eachLine[0], newTS);
            }
        }
        In allCounts = new In(countsFilename);
        String newString;
        String[] newLines;
        while (allCounts.hasNextLine()) {
            newString = allCounts.readLine();
            newLines = newString.split(",");
            Integer a = Integer.parseInt(newLines[0]);
            Double b = Double.parseDouble(newLines[1]);
            wordCount.put(a, b);
        }
    }

    /** Provides the history of WORD. The returned TimeSeries should be a copy,
     *  not a link to this NGramMap's TimeSeries. In other words, changes made
     *  to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word) {
        TimeSeries oldTS = freq.get(word);
        TimeSeries newTS = new TimeSeries();
        newTS.putAll(oldTS);
        return newTS;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     *  returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     *  changes made to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries newTS = new TimeSeries(freq.get(word), startYear, endYear);
        return newTS;
    }

    /** Returns a defensive copy of the total number of words recorded per year in all volumes. */
    public TimeSeries totalCountHistory() {
        return wordCount;
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD compared to
     *  all words recorded in that year. */
    public TimeSeries weightHistory(String word) {
        TimeSeries aWord = freq.get(word);
        return aWord.dividedBy(wordCount);
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     *  and ENDYEAR, inclusive of both ends. */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries newTSa = countHistory(word, startYear, endYear);
        TimeSeries newTSb = totalCountHistory();
        return newTSa.dividedBy(newTSb);
    }

    /** Returns the summed relative frequency per year of all words in WORDS. */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries cumulative = new TimeSeries();
        for (String word : words) {
            cumulative = cumulative.plus(weightHistory(word));
        }
        return cumulative;
    }

    /** Provides the summed relative frequency per year of all words in WORDS
     *  between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     *  this time frame, ignore it rather than throwing an exception. */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries newTS = new TimeSeries(summedWeightHistory(words), startYear, endYear);
        return newTS;
    }

}
