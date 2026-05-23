package coffeemunchingmonkeys.tools.lomimp.stats;

import coffeemunchingmonkeys.tools.lomimp.bricks.Settings;
import coffeemunchingmonkeys.tools.lomimp.bricks.SettingsSet;
import coffeemunchingmonkeys.tools.lomimp.core.Row;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class PairsHelperTest {

    @Test
    public void createPairsGeneratesCombinationsForNumbersAndExtras() {
        PairsHelper helper = createHelper();
        Row row = createRow(2025, 5, List.of(1), List.of(2));

        helper.createPairs(new ArrayList<>(List.of(row)));

        Assert.assertTrue(row.getNumberPairs().stream().anyMatch(pair -> "1 ".equals(pair.toString())));
        Assert.assertTrue(row.getExtraPairs().stream().anyMatch(pair -> "2 ".equals(pair.toString())));
        Assert.assertTrue(helper.allNumberPairsString.contains("[5.2025]"));
        Assert.assertTrue(helper.allNumberPairsString.contains("(1)"));
        Assert.assertTrue(helper.allExtraPairsString.contains("[5.2025]"));
        Assert.assertTrue(helper.allExtraPairsString.contains("(2)"));
    }

    @Test
    public void createPairsNReturnsAllCombinationsForRequestedSize() {
        PairsHelper helper = createHelper();

        ArrayList<Pair> combinations = helper.createPairsN(2, new ArrayList<>(List.of(1, 2, 3)));

        Assert.assertEquals(3, combinations.size());
        Assert.assertEquals("1 2 ", combinations.get(0).toString());
        Assert.assertEquals("1 3 ", combinations.get(1).toString());
        Assert.assertEquals("2 3 ", combinations.get(2).toString());
    }

    @Test
    public void zeroMapReturnsSortedMapWithZeroValues() {
        PairsHelper helper = createHelper();
        ArrayList<Pair> pairs = new ArrayList<>();
        pairs.add(createPair(2, 3));
        pairs.add(createPair(1));

        SortedMap<String, Integer> map = helper.zeroMap(pairs);

        Assert.assertEquals(List.of("1 ", "2 3 "), new ArrayList<>(map.keySet()));
        Assert.assertEquals(Integer.valueOf(0), map.get("1 "));
        Assert.assertEquals(Integer.valueOf(0), map.get("2 3 "));
    }

    @Test
    public void getEuroCalculateStaticPairsReturnsCombinatoricsForGameSettings() {
        PairsHelper helper = createHelper();

        ArrayList<BigInteger> results = helper.getEuroCalculateStaticPairs(2, 5, 1, 2);

        Assert.assertEquals(List.of(
                BigInteger.valueOf(5),
                BigInteger.valueOf(10),
                BigInteger.valueOf(2)
        ), results);
    }

    @Test
    public void getVikingStaticPairsAddsFullRowCombination() {
        PairsHelper helper = createHelper();

        ArrayList<BigInteger> results = helper.getVikingStaticStats();

        Assert.assertEquals(List.of(
                comb(48, 1),
                comb(48, 2),
                comb(48, 3),
                comb(48, 4),
                comb(48, 5),
                comb(48, 6),
                comb(10, 1),
                comb(10, 2),
                BigInteger.valueOf(450)
        ), results);
    }

    @Test
    public void getLottoStaticPairsAddsFullRowCombination() {
        PairsHelper helper = createHelper();

        ArrayList<BigInteger> results = helper.getLottoStaticPairs();

        Assert.assertEquals(List.of(
                comb(36, 1),
                comb(36, 2),
                comb(36, 3),
                comb(36, 4),
                comb(36, 5),
                comb(36, 6),
                comb(36, 7),
                comb(36, 7)
        ), results);
    }

    @Test
    public void getEuroStaticPairsAddsFullRowCombination() {
        PairsHelper helper = createHelper();

        ArrayList<BigInteger> results = helper.getEuroStaticPairs();

        Assert.assertEquals(List.of(
                comb(50, 1),
                comb(50, 2),
                comb(50, 3),
                comb(50, 4),
                comb(50, 5),
                comb(10, 1),
                comb(10, 2),
                comb(50, 5).multiply(comb(10, 2))
        ), results);
    }

    private PairsHelper createHelper() {
        Settings settings = new Settings();
        setField(settings, "vikingSettings", createSettingsSet(6, 48, 2, 10));
        setField(settings, "euroJackpotSettings", createSettingsSet(5, 50, 2, 10));
        setField(settings, "lottoSettings", createSettingsSet(7, 36, 0, 0));
        return new PairsHelper(settings);
    }

    private SettingsSet createSettingsSet(int numberSize, int numberMax, int extraSize, int extraMax) {
        SettingsSet settingsSet = new SettingsSet();
        setField(settingsSet, "numberSize", numberSize);
        setField(settingsSet, "numberMax", numberMax);
        setField(settingsSet, "extrasize", extraSize);
        setField(settingsSet, "extraMax", extraMax);
        return settingsSet;
    }

    private Row createRow(int year, int cw, List<Integer> numbers, List<Integer> extras) {
        Row row = new Row();
        row.setYear(year);
        row.setCw(cw);
        for (Integer number : numbers) {
            row.addNumber(number);
        }
        for (Integer extra : extras) {
            row.addExtra(extra);
        }
        return row;
    }

    private Pair createPair(int... members) {
        Pair pair = new Pair();
        for (int member : members) {
            pair.addMember(member);
        }
        return pair;
    }

    private BigInteger comb(int n, int k) {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            result = result.multiply(BigInteger.valueOf(n - k + i)).divide(BigInteger.valueOf(i));
        }
        return result;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
