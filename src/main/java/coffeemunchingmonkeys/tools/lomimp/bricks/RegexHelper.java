package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Lomimp
 * bricks.RegexHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class RegexHelper {
    //Fields

    public RegexHelper() {
    }


    //Please create unitests for "coffeemunchingmonkeys.tools.lomimp.bricks.RegexHelper"//
    /**
     *
     * @param dataInput
     * @param regexString
     * @return
     */
    public static Integer countOccurrences1(String dataInput, String regexString) {
        Integer count = 0;

        if(dataInput != null && !dataInput.isEmpty() && regexString != null && !regexString.isEmpty()) {
            String regexModifiedString = regexString.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]");

            String[] subStringArray = dataInput.split(regexModifiedString);
            count = subStringArray.length;
        }

        return count;
    }

    /**
     *
     * @param dataInput
     * @param regexString
     * @return
     */
    public static Integer countOccurrences2(String dataInput, String regexString) {
        Integer count = 0;

        if(dataInput != null && !dataInput.isEmpty() && regexString != null && !regexString.isEmpty()) {
            String regexModifiedString = regexString.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]");

            Pattern pattern = Pattern.compile(regexModifiedString);
            Matcher matcher = pattern.matcher(dataInput);

            while(matcher.find()) {
                count++;
            }
        }

        return count;
    }


    /**
     *
     * @param dataInput
     * @param searchString
     * @return<s
     */
    public static Integer countOccurrences3(String dataInput, String searchString) {
        Integer count = 0;

        if(dataInput != null && !dataInput.isEmpty() && searchString != null && !searchString.isEmpty()) {
            count = StringUtils.countMatches(dataInput, searchString);
        }

        return count;
    }

    /**
     *
     * @param dataInput
     * @param regexString
     * @return
     */
    public static Integer weeksSinceLastOccurrence(String dataInput, String regexString) {
        Integer count = 0;

        if(dataInput != null && !dataInput.isEmpty() && regexString != null && !regexString.isEmpty()) {
            String regexModifiedString = regexString.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]");

            String[] subStringArray = dataInput.split(regexModifiedString);
            count = subStringArray.length;
        }

        return count;
    }

    /**
     *
     * @param dataInput
     * @param year
     */
    public static String getYear(String dataInput, Integer year) {
        String restultString = "";

        String regexStringStart = "\\[[0-9]{2}\\." + year + "\\]";
        String regexStringStop  = "\\[[0-9]{2}\\." + (year - 1) + "\\]";

        int indexStart = -1;
        int indexStop = -1;

        if(dataInput != null && !dataInput.isEmpty() && year != null) {
            Pattern pattern = Pattern.compile(regexStringStart);
            Matcher matcher = pattern.matcher(dataInput);

            if(matcher.find()) {
                indexStart = matcher.start();
            }

            pattern = Pattern.compile(regexStringStop);
            matcher = pattern.matcher(dataInput);

            if(matcher.find()) {
                indexStop = matcher.start();
            }        

            if(indexStart < 0) {
                indexStart = 0;
            }

            if(indexStop < 0) {
                indexStop = 0;
            }

            restultString = dataInput.substring(indexStart, indexStop);
        }
        return restultString;
    }

    /**
     *
     * @param dataInput
     * @param regexString
     * @return
     */
    public static Integer getPosition(String dataInput, String regexString) {
        Integer index = -1;

        if(dataInput != null && !dataInput.isEmpty() && regexString != null && !regexString.isEmpty()) {
            String regexModifiedString = regexString.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]");

            Matcher subMatcher = Pattern.compile(regexModifiedString).matcher(dataInput);
            if(subMatcher.find()) {
                index = subMatcher.start();
            }
        }

        return index;
    }
}