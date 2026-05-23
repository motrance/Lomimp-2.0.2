package coffeemunchingmonkeys.tools.lomimp.bricks;

import org.junit.Assert;
import org.junit.Test;

public class RegexHelperTest {

    @Test
    public void countOccurrences1CountsSplitsForEscapedLiteral() {
        Assert.assertEquals(Integer.valueOf(3), RegexHelper.countOccurrences1("alpha[beta]gamma[beta]delta[beta]", "[beta]"));
    }

    @Test
    public void countOccurrences2CountsPatternMatches() {
        Assert.assertEquals(Integer.valueOf(2), RegexHelper.countOccurrences2("abc123def123ghi", "\\d+"));
    }

    @Test
    public void countOccurrences3CountsPlainSubstringOccurrences() {
        Assert.assertEquals(Integer.valueOf(3), RegexHelper.countOccurrences3("foo bar foo baz foo", "foo"));
    }

    @Test
    public void weeksSinceLastOccurrenceMatchesCountOccurrences1Behavior() {
        Assert.assertEquals(Integer.valueOf(3), RegexHelper.weeksSinceLastOccurrence("alpha[beta]gamma[beta]delta[beta]", "[beta]"));
    }

    @Test
    public void getYearReturnsSubstringBetweenCurrentAndPreviousYears() {
        String data = "prefix [01.2025] one [02.2024] suffix";

        Assert.assertEquals("[01.2025] one ", RegexHelper.getYear(data, 2025));
    }

    @Test
    public void getPositionFindsRegexMatchStartIndex() {
        Assert.assertEquals(Integer.valueOf(3), RegexHelper.getPosition("abc[beta]def", "[beta]"));
    }

    @Test
    public void methodsReturnZeroOrNegativeOneForNullOrEmptyInputs() {
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences1(null, "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences1("", "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences1("abc", null));

        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences2(null, "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences2("", "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences2("abc", null));

        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences3(null, "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences3("", "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.countOccurrences3("abc", null));

        Assert.assertEquals(Integer.valueOf(0), RegexHelper.weeksSinceLastOccurrence(null, "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.weeksSinceLastOccurrence("", "x"));
        Assert.assertEquals(Integer.valueOf(0), RegexHelper.weeksSinceLastOccurrence("abc", null));

        Assert.assertEquals("", RegexHelper.getYear(null, 2025));
        Assert.assertEquals("", RegexHelper.getYear("", 2025));
        Assert.assertEquals("", RegexHelper.getYear("abc", 2025));

        Assert.assertEquals(Integer.valueOf(-1), RegexHelper.getPosition(null, "x"));
        Assert.assertEquals(Integer.valueOf(-1), RegexHelper.getPosition("", "x"));
        Assert.assertEquals(Integer.valueOf(-1), RegexHelper.getPosition("abc", null));
    }
}
