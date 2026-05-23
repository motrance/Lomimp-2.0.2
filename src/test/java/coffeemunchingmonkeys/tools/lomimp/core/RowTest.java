package coffeemunchingmonkeys.tools.lomimp.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RowTest {

    @Test
    public void addNumberAndAddExtraIgnoreInvalidAndDuplicateValues() {
        Row row = new Row();

        row.addNumber(1);
        row.addNumber(1);
        row.addNumber(2);
        row.addNumber(0);
        row.addNumber(-5);
        row.addNumber("3");
        row.addNumber("3");
        row.addNumber("bad");

        row.addExtra(4);
        row.addExtra(4);
        row.addExtra(5);
        row.addExtra(0);
        row.addExtra(-1);
        row.addExtra("6");
        row.addExtra("6");
        row.addExtra("invalid");

        Assert.assertEquals(List.of(1, 2, 3), row.getNumbers());
        Assert.assertEquals(List.of(4, 5, 6), row.getExtras());
    }

    @Test
    public void sortOrdersNumbersAndExtrasAscending() {
        Row row = new Row();
        row.addNumber(5);
        row.addNumber(1);
        row.addNumber(3);
        row.addExtra(9);
        row.addExtra(2);
        row.addExtra(7);

        row.sort();

        Assert.assertEquals(List.of(1, 3, 5), row.getNumbers());
        Assert.assertEquals(List.of(2, 7, 9), row.getExtras());
    }

    @Test
    public void compareRowReturnsTrueForMatchingRowsAndFalseForDifferentRows() {
        Row first = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));
        Row matching = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));
        Row different = createRow("2025-01-02", 2025, 2, List.of(1, 2, 4), List.of(4, 6));

        Assert.assertTrue(first.compareRow(matching));
        Assert.assertFalse(first.compareRow(different));
        Assert.assertFalse(first.compareRow(null));
    }

    @Test
    public void compareRowReturnsFalseWhenSizesDiffer() {
        Row first = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));
        Row differentSize = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4));

        Assert.assertFalse(first.compareRow(differentSize));
    }

    @Test
    public void cloneCreatesIndependentRowInstanceWithSameValues() throws Exception {
        Row original = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));

        Row cloned = (Row) original.clone();

        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getDate(), cloned.getDate());
        Assert.assertEquals(original.getYear(), cloned.getYear());
        Assert.assertEquals(original.getCw(), cloned.getCw());
        Assert.assertEquals(original.getNumbers(), cloned.getNumbers());
        Assert.assertEquals(original.getExtras(), cloned.getExtras());
        Assert.assertEquals(original.numbersToString(), cloned.numbersToString());
        Assert.assertEquals(original.extrasToString(), cloned.extrasToString());
    }

    @Test
    public void stringRepresentationsReflectCurrentState() {
        Row row = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));

        Assert.assertEquals("{[01][02][03]}", row.numbersToString());
        Assert.assertEquals("{[04][05]}", row.extrasToString());
        Assert.assertEquals("{[01][02][03]}{[04][05]}", row.toString());
    }

    @Test
    public void setNumbersAndSetExtrasReplaceExistingCollections() {
        Row row = createRow("2025-01-01", 2025, 1, List.of(1, 2), List.of(3, 4));

        ArrayList<Integer> numbers = new ArrayList<>(List.of(7, 8));
        ArrayList<Integer> extras = new ArrayList<>(List.of(9));

        row.setNumbers(numbers);
        row.setExtras(extras);
        row.setNumbersString("updated");
        row.setExtrasString("updated");

        Assert.assertEquals(numbers, row.getNumbers());
        Assert.assertEquals(extras, row.getExtras());
        Assert.assertEquals("updated", row.getNumbersString());
        Assert.assertEquals("updated", row.getExtrasString());
    }

    private Row createRow(String date, int year, int cw, List<Integer> numbers, List<Integer> extras) {
        Row row = new Row();
        row.setDate(date);
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
}
