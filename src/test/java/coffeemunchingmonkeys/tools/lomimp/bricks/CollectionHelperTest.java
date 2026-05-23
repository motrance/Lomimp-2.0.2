package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class CollectionHelperTest {

    @Test
    public void sortIntegerMapOrdersEntriesByValueDescending() {
        Map<String, Integer> input = new LinkedHashMap<>();
        input.put("alpha", 2);
        input.put("beta", 5);
        input.put("gamma", 1);

        Map<String, Integer> result = CollectionHelper.sortIntegerMap(input);

        Assert.assertEquals("beta", result.keySet().toArray()[0]);
        Assert.assertEquals("alpha", result.keySet().toArray()[1]);
        Assert.assertEquals("gamma", result.keySet().toArray()[2]);
    }

    @Test
    public void sortIntegerMapPreservesValuesAndUsesLinkedHashMap() {
        Map<String, Integer> input = new LinkedHashMap<>();
        input.put("first", 3);
        input.put("second", 3);

        Map<String, Integer> result = CollectionHelper.sortIntegerMap(input);

        Assert.assertTrue(result instanceof LinkedHashMap);
        Assert.assertEquals(Integer.valueOf(3), result.get("first"));
        Assert.assertEquals(Integer.valueOf(3), result.get("second"));
    }

    @Test
    public void sortStringMapOrdersEntriesByValueDescending() {
        Map<String, String> input = new LinkedHashMap<>();
        input.put("alpha", "zebra");
        input.put("beta", "apple");
        input.put("gamma", "melon");

        Map<String, String> result = CollectionHelper.sortStringMap(input);

        Assert.assertEquals("alpha", result.keySet().toArray()[0]);
        Assert.assertEquals("gamma", result.keySet().toArray()[1]);
        Assert.assertEquals("beta", result.keySet().toArray()[2]);
    }

    @Test
    public void sortStringMapPreservesValuesAndUsesLinkedHashMap() {
        Map<String, String> input = new LinkedHashMap<>();
        input.put("first", "one");
        input.put("second", "two");

        Map<String, String> result = CollectionHelper.sortStringMap(input);

        Assert.assertTrue(result instanceof LinkedHashMap);
        Assert.assertEquals("one", result.get("first"));
        Assert.assertEquals("two", result.get("second"));
    }
}
