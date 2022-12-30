package ngordnet.ngrams;

import java.util.*;

/** An object for mapping a year number (e.g. 1996) to numerical data. Provides
 *  utility methods useful for data analysis.
 *  @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     *  inclusive of both end points. */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            years.add(i);
        }
        for (Integer eachYear : ts.years()) {
            if (years.contains(eachYear)) {
                this.put(eachYear, ts.get(eachYear));
            }
        }
    }

    /** Returns all years for this TimeSeries (in any order). */
    public List<Integer> years() {
        ArrayList<Integer> allYears = new ArrayList<Integer>(this.keySet());
        return allYears;
    }

    /** Returns all data for this TimeSeries (in any order).
     *  Must be in the same order as years(). */
    public List<Double> data() {
        ArrayList<Double> allData = new ArrayList<Double>(this.values());
        return allData;
    }

    /** Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     *  each year, sum the data from this TimeSeries with the data from TS. Should return a
     *  new TimeSeries (does not modify this TimeSeries). */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries newTS = new TimeSeries();
        ArrayList<Integer> allYears = (ArrayList<Integer>) years();
        allYears.addAll(ts.years());
        for (Integer eachYear : allYears) {
            if (this.get(eachYear) == null || ts.get(eachYear) == null) {
                if (this.get(eachYear) == null) {
                    newTS.put(eachYear, ts.get(eachYear));
                } else {
                    newTS.put(eachYear, this.get(eachYear));
                }
            } else {
                newTS.put(eachYear, this.get(eachYear) + ts.get(eachYear));
            }
        }
        return newTS;
    }

    /** Returns the quotient of the value for each year this TimeSeries divided by the
     *  value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
     *  throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
     *  Should return a new TimeSeries (does not modify this TimeSeries). */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries newTS = new TimeSeries();
        for (Integer eachYear : years()) {
            if (ts.get(eachYear) == null) {
                throw new IllegalArgumentException();
            }
            if (this.get(eachYear) == null) {
                continue;
            }
            newTS.put(eachYear, this.get(eachYear) / ts.get(eachYear));
        }
        return newTS;
    }
}
