package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class AlphanumericSortComparatorTest {

    @Test
    public void numericOrderSortsEmbeddedNumbersNumerically() {
        List<String> values = Arrays.asList("file2", "file10", "file1");

        Collections.sort(values, AlphanumericSortComparator.NUMERICAL_ORDER);

        Assert.assertEquals(Arrays.asList("file1", "file2", "file10"), values);
    }

    @Test
    public void numericOrderTreatsLeadingZerosAsEqualForTieBreak() {
        List<String> values = Arrays.asList("item001", "item1", "item000");

        Collections.sort(values, AlphanumericSortComparator.NUMERICAL_ORDER);

        Assert.assertEquals(Arrays.asList("item000", "item001", "item1"), values);
    }

    @Test
    public void caseInsensitiveOrderIgnoresCase() {
        List<String> values = Arrays.asList("beta", "Alpha", "alpha", "Beta");

        Collections.sort(values, AlphanumericSortComparator.CASE_INSENSITIVE_NUMERICAL_ORDER);

        Assert.assertEquals(Arrays.asList("Alpha", "alpha", "beta", "Beta"), values);
    }

    @Test
    public void compareReturnsZeroForEquivalentStrings() {
        Assert.assertEquals(0, AlphanumericSortComparator.NUMERICAL_ORDER.compare("file01", "file1"));
    }

    @Test
    public void compareHandlesMixedTextAndNumbers() {
        Assert.assertTrue(AlphanumericSortComparator.NUMERICAL_ORDER.compare("a2", "a10") < 0);
        Assert.assertTrue(AlphanumericSortComparator.NUMERICAL_ORDER.compare("a10", "a2") > 0);
    }
}
