package coffeemunchingmonkeys.tools.lomimp.stats;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class DateHelperTest {

    @Test
    public void testAddDaysWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 1, 1);
        String result = helper.AddDays(5, baseDate);
        Assert.assertEquals("2026-01-06", result);
    }

    @Test
    public void testSubtractDaysWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 1, 10);
        String result = helper.SubtractDays(7, baseDate);
        Assert.assertEquals("2026-01-03", result);
    }

    @Test
    public void testAddWeeksWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 2, 1);
        String result = helper.AddWeeks(2, baseDate);
        Assert.assertEquals("2026-02-15", result);
    }

    @Test
    public void testSubtractWeeksWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 2, 15);
        String result = helper.SubtractWeeks(1, baseDate);
        Assert.assertEquals("2026-02-08", result);
    }

    @Test
    public void testAddMonthsWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 1, 31);
        String result = helper.AddMonths(1, baseDate);
        Assert.assertEquals("2026-02-28", result);
    }

    @Test
    public void testSubtractMonthsWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2026, 3, 31);
        String result = helper.SubtractMonths(1, baseDate);
        Assert.assertEquals("2026-02-28", result);
    }

    @Test
    public void testAddYearsWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2024, 2, 29);
        String result = helper.AddYears(1, baseDate);
        Assert.assertEquals("2025-02-28", result);
    }

    @Test
    public void testSubtractYearsWithBaseDate() {
        DateHelper helper = new DateHelper();
        LocalDate baseDate = LocalDate.of(2025, 2, 28);
        String result = helper.SubtractYears(1, baseDate);
        Assert.assertEquals("2024-02-28", result);
    }
}
