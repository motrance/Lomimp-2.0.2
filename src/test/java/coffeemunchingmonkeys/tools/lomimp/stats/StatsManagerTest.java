package coffeemunchingmonkeys.tools.lomimp.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.bricks.Settings;
import coffeemunchingmonkeys.tools.lomimp.bricks.Storage;
import coffeemunchingmonkeys.tools.lomimp.core.Row;

public class StatsManagerTest {

    private static class TestStorage extends Storage {
        private final ArrayList<Row> rows;
        private int readAllCount = 0;
        private int readFromDateCount = 0;
        private String readFromDateDate;

        public TestStorage(Settings settings, LogProvider log, ArrayList<Row> rows) {
            super(settings, log);
            this.rows = rows;
        }

        @Override
        public ArrayList<Row> readAll(String selectedGame) {
            readAllCount++;
            return rows;
        }

        @Override
        public ArrayList<Row> readFromDate(String selectedGame, String date) {
            readFromDateCount++;
            readFromDateDate = date;
            return rows;
        }
    }

    private static class SilentLogProvider extends LogProvider {
        @Override
        public void writeDebug(String msg) {
            // no-op
        }

        @Override
        public void writeInfo(String msg) {
            // no-op
        }

        @Override
        public void writeWarning(String msg) {
            // no-op
        }

        @Override
        public void writeError(String msg) {
            // no-op
        }

        @Override
        public void writeException(Exception e) {
            // no-op
        }
    }

    @Test
    public void testCreateStatsOnPairsReturnsThreeStatsDataHolders() {
        ArrayList<Row> rows = new ArrayList<>();
        rows.add(createRow(new int[] {1, 2, 3}, new int[] {4, 5}));
        rows.add(createRow(new int[] {1, 3, 4}, new int[] {5, 6}));

        Settings settings = new Settings();
        SilentLogProvider logProvider = new SilentLogProvider();
        TestStorage storage = new TestStorage(settings, logProvider, rows);

        StatsManager manager = new StatsManager("Viking", storage, new XSSFWorkbook(), logProvider, settings);
        List<StatsDataHolder> holders = manager.createStatsOnPairs();

        Assert.assertEquals(3, holders.size());

        for (StatsDataHolder holder : holders) {
            Assert.assertEquals("Viking", holder.getGame());
            Assert.assertNotNull(holder.getNumberCombCounts());
            Assert.assertNotNull(holder.getExtraCombCounts());
        }

        Assert.assertEquals(1, storage.readAllCount);
        Assert.assertEquals(0, storage.readFromDateCount);
        Assert.assertNull(storage.readFromDateDate);
    }

    @Test
    public void testCreateStatsOnPairsCountsNumberAndExtraCombinations() {
        ArrayList<Row> rows = new ArrayList<>();
        rows.add(createRow(new int[] {1, 2, 3}, new int[] {4, 5}));
        rows.add(createRow(new int[] {1, 3, 4}, new int[] {5, 6}));

        Settings settings = new Settings();
        SilentLogProvider logProvider = new SilentLogProvider();
        TestStorage storage = new TestStorage(settings, logProvider, rows);

        StatsManager manager = new StatsManager("Viking", storage, new XSSFWorkbook(), logProvider, settings);
        StatsDataHolder holder = manager.createStatsOnPairs().get(0);

        Assert.assertEquals(3, holder.getNumberCombCounts().size());
        Assert.assertEquals(2, holder.getNumberCombCounts().get(1).get(Arrays.asList(1)).intValue());
        Assert.assertEquals(1, holder.getNumberCombCounts().get(1).get(Arrays.asList(2)).intValue());
        Assert.assertEquals(2, holder.getNumberCombCounts().get(1).get(Arrays.asList(3)).intValue());
        Assert.assertEquals(1, holder.getNumberCombCounts().get(1).get(Arrays.asList(4)).intValue());

        Assert.assertEquals(2, holder.getNumberCombCounts().get(2).get(Arrays.asList(1, 3)).intValue());
        Assert.assertEquals(1, holder.getNumberCombCounts().get(2).get(Arrays.asList(1, 2)).intValue());
        Assert.assertEquals(1, holder.getNumberCombCounts().get(2).get(Arrays.asList(3, 4)).intValue());
        Assert.assertEquals(5, holder.getNumberCombCounts().get(2).size());

        Assert.assertEquals(1, holder.getNumberCombCounts().get(3).get(Arrays.asList(1, 2, 3)).intValue());
        Assert.assertEquals(1, holder.getNumberCombCounts().get(3).get(Arrays.asList(1, 3, 4)).intValue());

        Assert.assertEquals(2, holder.getExtraCombCounts().size());
        Assert.assertEquals(1, holder.getExtraCombCounts().get(2).get(Arrays.asList(4, 5)).intValue());
        Assert.assertEquals(1, holder.getExtraCombCounts().get(2).get(Arrays.asList(5, 6)).intValue());
        Assert.assertEquals(2, holder.getExtraCombCounts().get(1).get(Arrays.asList(5)).intValue());
    }

    private Row createRow(int[] numbers, int[] extras) {
        Row row = new Row();
        for (int number : numbers) {
            row.addNumber(number);
        }
        for (int extra : extras) {
            row.addExtra(extra);
        }
        return row;
    }
}
