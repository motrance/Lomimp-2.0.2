package coffeemunchingmonkeys.tools.lomimp.core;

import coffeemunchingmonkeys.tools.lomimp.stats.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 *
 * //https://www.euro-jackpot.net/da/resultatarkiv-2024
 * //https://www.euro-jackpot.net/da/resultatarkiv-2012
 *
 * //https://danskelotto.com/viking-lotto/vindertal/arkiv-2024
 * //https://danskelotto.com/viking-lotto/vindertal/arkiv-2012
 *
 * //https://danskelotto.com/lotto/vindertal/arkiv-2024
 * //https://danskelotto.com/lotto/vindertal/arkiv-1990
 */

/**
 *
 * Lomimp
 * core.Row
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class Row implements Cloneable {
    //Fields
    private String date = "";
    private Integer year = 0;
    private Integer cw = 0;
    private ArrayList<Integer> numbers = new ArrayList<Integer>();
    private ArrayList<Integer> extras = new ArrayList<Integer>();
    private ArrayList<Pair> numberPairs = new ArrayList<Pair>();
    private ArrayList<Pair> extraPairs = new ArrayList<Pair>();
    String numbersString = "";
    String extrasString = "";

    /**
     *
     * @param number
     */
    public void addNumber(Integer number)
    {
        try {
            if(number > 0 && !check(number, numbersString)) {
                numbers.add(number);
                numbersString = createArrayString(numbers);
            }
        } catch(Exception ignore) {}
    }

    /**
     *
     * @param number
     */
    public void addNumber(String number)
    {
        try {
            Integer numberInt = Integer.parseInt(number);
            if(numberInt > 0 && !check(numberInt, numbersString)) {
                numbers.add(numberInt);
                numbersString = createArrayString(numbers);
            }
        } catch(Exception ignore) {}
    }

    /**
     *
     * @param extra
     */
    public void addExtra(String extra)
    {
        try {
            Integer extraInt = Integer.parseInt(extra);
            if(extraInt > 0 && !check(extraInt, extrasString))
            {
                extras.add(extraInt);
                extrasString = createArrayString(extras);
            }
        } catch(Exception ignore) {}
    }

    /**
     *
     * @param extra
     */
    public void addExtra(Integer extra)
    {
        try {
            if(extra > 0 && !check(extra, extrasString))
            {
                extras.add(extra);
                extrasString = createArrayString(extras);
            }
        } catch(Exception ignore) {}
    }

    /** 
     * @param checkRow
     * @return boolean
     */
    public boolean compareRow(Row checkRow) {
        boolean state = true;

        if(checkRow != null && numbers.size() == checkRow.getNumbersSize() && extras.size() == checkRow.getExtrasSize()) {

            String tmpNumberCompareString = createArrayString(checkRow.numbers);
            String tmpExtraCompareString = createArrayString(checkRow.extras);

            if(!tmpNumberCompareString.equalsIgnoreCase(numbersString)) {
                state = false;
            }

            if(!tmpExtraCompareString.equalsIgnoreCase(extrasString)) {
                state = false;
            }
        }
        else
        {
            state = false;
        }
        return state;
    }

    /**
     *
     * @param arrayList
     * @return String
     */
    public String createArrayString(ArrayList<Integer> arrayList) {
        String returnString = "{";

        for(Integer tmpNumber: arrayList) {
            String tmpString = "" + tmpNumber;

            if(tmpString.length() < 2) {
                tmpString = "0" + tmpString;
            }

            returnString = returnString + "[" + tmpString + "]";
        }
        returnString = returnString + "}";
        return returnString;
    }

    /**
     *
     * @param number
     * @param numbers
     * @return boolean
     */
    public boolean check(Integer number, String numbers) {
        boolean state = false;

        String paddedNumber = number < 10 ? "0" + number : String.valueOf(number);
        String inputRegexString = "\\{(\\[[0-9]+\\])*\\[" + paddedNumber + "\\](\\[[0-9]+\\])*\\}";

        state = Pattern.matches(inputRegexString, numbers);
        return state;
    }

    /**
     *
     */
    public void sort() {
        Collections.sort(numbers);
        Collections.sort(extras);
    }

    /** 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /** 
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /** 
     * @return Integer
     */
    public Integer getYear() {
        return year;
    }

    /** 
     * @param year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /** 
     * @return Integer
     */
    public Integer getCw() {
        return cw;
    }

    /** 
     * @param cw
     */
    public void setCw(Integer cw) {
        this.cw = cw;
    }

    /** 
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    /** 
     * @param numbers
     */
    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    /** 
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getExtras() {
        return extras;
    }

    /** 
     * @return ArrayList<Pair>
     */
    public ArrayList<Pair> getNumberPairs() {
        return this.numberPairs;
    }

    /** 
     * @param numberPairs
     */
    public void setNumberPairs(ArrayList<Pair> numberPairs) {
        this.numberPairs = numberPairs;
    }

    /** 
     * @return ArrayList<Pair>
     */
    public ArrayList<Pair> getExtraPairs() {
        return this.extraPairs;
    }

    /** 
     * @param extraPairs
     */
    public void setExtraPairs(ArrayList<Pair> extraPairs) {
        this.extraPairs = extraPairs;
    }
    
    /** 
     * @param extras
     */
    public void setExtras(ArrayList<Integer> extras) {
        this.extras = extras;
    }

    /** 
     * @return String
     */
    public String getNumbersString() {
        return numbersString;
    }

    /** 
     * @param numbersString
     */
    public void setNumbersString(String numbersString) {
        this.numbersString = numbersString;
    }

    /** 
     * @return String
     */
    public String getExtrasString() {
        return extrasString;
    }

    /** 
     * @param extrasString
     */
    public void setExtrasString(String extrasString) {
        this.extrasString = extrasString;
    }

    /** 
     * @return int
     */
    public int getNumbersSize() {
        return numbers.size();
    }

    /** 
     * @return int
     */
    public int getExtrasSize() {
        return extras.size();
    }

    /**
     *
     * @return String
     */
    public String numbersToString() {
        String returnString = "";

        returnString = createArrayString(numbers);
        return returnString;
    }

    /**
     *
     * @return String
     */
    public String extrasToString() {
        String returnString = "";

        returnString = createArrayString(extras);
        return returnString;
    }

    /**
     *
     * @return Object
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        String returnString = "";

        returnString = createArrayString(numbers) + createArrayString(extras);

        return returnString;
    }
}
