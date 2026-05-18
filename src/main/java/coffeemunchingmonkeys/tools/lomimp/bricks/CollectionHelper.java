package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Lomimp
 * bricks.CollectionHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class CollectionHelper {
    /**
     *
     * @param map <String, Integer>
     * @return
     */
    public static Map<String, Integer> sortIntegerMap(Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     *
     * @param map <String, String>
     * @return
     */
    public static Map<String, String> sortStringMap(Map<String, String> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, String>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
