package coffeemunchingmonkeys.tools.lomimp.stats;

import java.util.List;
import java.util.Map;

/**
 *
 * Lomimp
 * stats.StatsDataHolder
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class StatsDataHolder {
    String game;
    Map<Integer, Map<List<Integer>, Integer>> numberCombCounts;
    Map<Integer, Map<List<Integer>, Integer>> extraCombCounts;

    public StatsDataHolder(String game, Map<Integer, Map<List<Integer>, Integer>> numberCombCounts, Map<Integer, Map<List<Integer>, Integer>> extraCombCounts) {
        this.game = game;
        this.numberCombCounts = numberCombCounts;
        this.extraCombCounts = extraCombCounts;
    }

    /** 
     * @return String
     */
    public String getGame() {
        return game;
    }

    /** 
     * @return Map<Integer, Map<List<Integer>, Integer>>
     */
    public Map<Integer, Map<List<Integer>, Integer>> getNumberCombCounts() {
        return numberCombCounts;
    }

    /** 
     * @return Map<Integer, Map<List<Integer>, Integer>>
     */
    public Map<Integer, Map<List<Integer>, Integer>> getExtraCombCounts() {
        return extraCombCounts;
    }

    /** 
     * @param game
     */
    public void setGame(String game) {
        this.game = game;
    }

    /** 
     * @param numberCombCounts
     */
    public void setNumberCombCounts(Map<Integer, Map<List<Integer>, Integer>> numberCombCounts) {
        this.numberCombCounts = numberCombCounts;
    }

    /** 
     * @param extraCombCounts
     */
    public void setExtraCombCounts(Map<Integer, Map<List<Integer>, Integer>> extraCombCounts) {
        this.extraCombCounts = extraCombCounts;
    }
}
