package coffeemunchingmonkeys.tools.lomimp.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class StatsDataHolderTest {

    @Test
    public void constructorStoresProvidedValues() {
        Map<Integer, Map<List<Integer>, Integer>> numberCombCounts = new HashMap<>();
        Map<Integer, Map<List<Integer>, Integer>> extraCombCounts = new HashMap<>();

        Map<List<Integer>, Integer> numberYearCounts = new HashMap<>();
        numberYearCounts.put(List.of(1, 2, 3), 4);
        numberCombCounts.put(2025, numberYearCounts);

        Map<List<Integer>, Integer> extraYearCounts = new HashMap<>();
        extraYearCounts.put(List.of(7, 8), 2);
        extraCombCounts.put(2025, extraYearCounts);

        StatsDataHolder holder = new StatsDataHolder("Lotto", numberCombCounts, extraCombCounts);

        Assert.assertEquals("Lotto", holder.getGame());
        Assert.assertSame(numberCombCounts, holder.getNumberCombCounts());
        Assert.assertSame(extraCombCounts, holder.getExtraCombCounts());
        Assert.assertEquals(Integer.valueOf(4), holder.getNumberCombCounts().get(2025).get(List.of(1, 2, 3)));
        Assert.assertEquals(Integer.valueOf(2), holder.getExtraCombCounts().get(2025).get(List.of(7, 8)));
    }

    @Test
    public void settersUpdateStoredValues() {
        Map<Integer, Map<List<Integer>, Integer>> initialNumberCounts = new HashMap<>();
        Map<Integer, Map<List<Integer>, Integer>> initialExtraCounts = new HashMap<>();
        StatsDataHolder holder = new StatsDataHolder("Viking", initialNumberCounts, initialExtraCounts);

        Map<Integer, Map<List<Integer>, Integer>> updatedNumberCounts = new HashMap<>();
        Map<List<Integer>, Integer> updatedNumberYearCounts = new HashMap<>();
        updatedNumberYearCounts.put(List.of(3, 4, 5), 6);
        updatedNumberCounts.put(2024, updatedNumberYearCounts);

        Map<Integer, Map<List<Integer>, Integer>> updatedExtraCounts = new HashMap<>();
        Map<List<Integer>, Integer> updatedExtraYearCounts = new HashMap<>();
        updatedExtraYearCounts.put(List.of(9, 10), 1);
        updatedExtraCounts.put(2024, updatedExtraYearCounts);

        holder.setGame("EuroJackpot");
        holder.setNumberCombCounts(updatedNumberCounts);
        holder.setExtraCombCounts(updatedExtraCounts);

        Assert.assertEquals("EuroJackpot", holder.getGame());
        Assert.assertSame(updatedNumberCounts, holder.getNumberCombCounts());
        Assert.assertSame(updatedExtraCounts, holder.getExtraCombCounts());
        Assert.assertEquals(Integer.valueOf(6), holder.getNumberCombCounts().get(2024).get(List.of(3, 4, 5)));
        Assert.assertEquals(Integer.valueOf(1), holder.getExtraCombCounts().get(2024).get(List.of(9, 10)));
    }
}
