package coffeemunchingmonkeys.tools.lomimp.stats;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import coffeemunchingmonkeys.tools.lomimp.core.Row;
import coffeemunchingmonkeys.tools.lomimp.bricks.*;

/***
 *
 * Lomimp
 * stats.PairsHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class PairsHelper {
    //Fields
    Settings settings;
    String allNumberPairsString = "";
    String allExtraPairsString = "";

    public PairsHelper(Settings settings) {
        this.settings = settings;
    }

    /** 
     * @return ArrayList<Integer>
     */
    public ArrayList<BigInteger> getVikingStaticStats() {
        ArrayList<BigInteger> theList = new ArrayList<BigInteger>();

        SettingsSet settingsSet = this.settings.getSettings("Viking");
        int size = settingsSet.getNumberSize();
        int max = settingsSet.getNumberMax();
        int extraSize = settingsSet.getExtrasize();
        int extraMax = settingsSet.getExtraMax();

        theList = getEuroCalculateStaticPairs(size, max, extraSize, extraMax);
        
        BigInteger fr1 = theList.get(theList.size() - 2);
        BigInteger fr2 = theList.get(theList.size() - 1);

        BigInteger fullrow = fr1.multiply(fr2);
        theList.add(fullrow);
        return theList;
    }

    /** 
     * @return ArrayList<BigInteger>
     */
    public ArrayList<BigInteger> getEuroStaticPairs() {
        ArrayList<BigInteger> theList = new ArrayList<BigInteger>();

        SettingsSet settingsSet = this.settings.getSettings("EuroJackpot");
        int size = settingsSet.getNumberSize();
        int max = settingsSet.getNumberMax();
        int extraSize = settingsSet.getExtrasize();
        int extraMax = settingsSet.getExtraMax();

        theList = getEuroCalculateStaticPairs(size, max, extraSize, extraMax);

        BigInteger fr1 = theList.get(theList.size() - 3);
        BigInteger fr2 = theList.get(theList.size() - 1);

        BigInteger fullrow = fr1.multiply(fr2);
        theList.add(fullrow);
        return theList;
    }

    /** 
     * @return ArrayList<BigInteger>
     */
    public ArrayList<BigInteger> getLottoStaticPairs() {
        ArrayList<BigInteger> theList = new ArrayList<BigInteger>();

        SettingsSet settingsSet = this.settings.getSettings("Lotto");
        int size = settingsSet.getNumberSize();
        int max = settingsSet.getNumberMax();
        int extraSize = settingsSet.getExtrasize();
        int extraMax = settingsSet.getExtraMax();

        theList = getEuroCalculateStaticPairs(size, max, extraSize, extraMax);

        BigInteger fullrow = theList.get(theList.size() - 1);
        theList.add(fullrow);
        return theList;
    }

    /** 
     * @param size
     * @param max
     * @param extraSize
     * @param extraMax
     * @return ArrayList<BigInteger>
     */
    public ArrayList<BigInteger> getEuroCalculateStaticPairs(int size, int max, int extraSize, int extraMax) {
        ArrayList<BigInteger> theList = new ArrayList<BigInteger>();

        for(int counter = 1; counter <= size; counter++) {
            theList.add(calculateSize(counter, max));
        }
        
        for(int counter = 1; counter <= extraSize; counter++) {
            theList.add(calculateSize(counter, extraMax));
        }
        return theList;
    }

    /** 
     * @param n
     * @param max
     * @return BigInteger
     */
    private BigInteger calculateSize(int n, int max) {
        if (n < 0 || max < 0 || n > max) {
            throw new IllegalArgumentException("Invalid input");
        }

        BigInteger factMax = calculateFactorial(max);
        BigInteger factMaxMinusN = calculateFactorial(max - n);
        BigInteger factN = calculateFactorial(n);
        return factMax
                .divide(factMaxMinusN)
                .divide(factN);
                //.subtract(factN);
    }

    /** 
     * @param number
     * @return BigInteger
     */
    private BigInteger calculateFactorial(int number) {
        BigInteger result = BigInteger.ONE;

        for (int i = 2; i <= number; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /** 
     * @param rows
     */
    public void createPairs(ArrayList<Row> rows) {
        for(Row row: rows) {
            ArrayList<Pair> numberPairs = new ArrayList<Pair>();
            ArrayList<Pair> extraPairs = new ArrayList<Pair>();

            ArrayList<Integer> numbers = new ArrayList<Integer>();
            ArrayList<Integer> extras = new ArrayList<Integer>();

            numbers = row.getNumbers();
            extras = row.getExtras();

            for(int SizeCounter = 1; SizeCounter <= numbers.size(); SizeCounter++) {
                ArrayList<Pair> numberPairsN =  createPairsN(SizeCounter, numbers);
                numberPairs.addAll(numberPairsN);
                row.setNumberPairs(numberPairsN);
            }

            for(int SizeCounter = 1; SizeCounter <= extras.size(); SizeCounter++) {
                ArrayList<Pair> extrasPairsN =  createPairsN(SizeCounter, extras);
                extraPairs.addAll(extrasPairsN);
                row.setExtraPairs(extrasPairsN);
            }
            ArrayList<Pair> extraPairs1 =  createPairsN(1, extras);
            ArrayList<Pair> extraPairs2 =  createPairsN(2, extras);

            extraPairs.addAll(extraPairs1);
            extraPairs.addAll(extraPairs2);

            row.setNumberPairs(numberPairs);
            row.setExtraPairs(extraPairs);

            String cwString = "["  + row.getCw() + "." + row.getYear() + "]";

            allNumberPairsString = allNumberPairsString + cwString;

            for(Pair pair: numberPairs) {
                allNumberPairsString = allNumberPairsString + "(" + pair.toString().trim().replace(" ", ",") + ")";
            }

            allExtraPairsString = allExtraPairsString + cwString;

            for(Pair pair: extraPairs) {
                allExtraPairsString = allExtraPairsString + "(" + pair.toString().trim().replace(" ", ",") + ")";
            }
        }
    }

    /** 
     * @param n
     * @param numbers
     * @return ArrayList<Pair>
     */
    public ArrayList<Pair> createPairsN(Integer n, ArrayList<Integer> numbers) {
        ArrayList<Pair> pairs = new ArrayList<Pair>();
        generateCombinations(numbers, n, 0, new ArrayList<Integer>(), pairs);
        return pairs;
    }

    /** 
     * @param numbers
     * @param n
     * @param start
     * @param current
     * @param pairs
     */
    private void generateCombinations(ArrayList<Integer> numbers, int n, int start, ArrayList<Integer> current, ArrayList<Pair> pairs) {
        if (current.size() == n) {
            Pair pair = new Pair();
            for (Integer s : current) {
                pair.addMember(s);
            }
            pairs.add(pair);
            return;
        }
        for (int i = start; i < numbers.size(); i++) {
            current.add(numbers.get(i));
            generateCombinations(numbers, n, i + 1, current, pairs);
            current.remove(current.size() - 1);
        }
    }
    
    /** 
     * @param pairs
     * @return SortedMap<String, Integer>
     */
    public SortedMap<String, Integer> zeroMap(ArrayList<Pair> pairs) {
        SortedMap<String, Integer> sortedMap = new TreeMap<String, Integer>();

        for(Pair pair: pairs) {
            sortedMap.put(pair.toString(), 0);
        }
        return sortedMap;
    }
}
