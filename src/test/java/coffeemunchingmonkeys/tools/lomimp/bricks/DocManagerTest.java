package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import coffeemunchingmonkeys.tools.lomimp.stats.StatsDataHolder;

public class DocManagerTest {

    @Test
    public void testCreateStatExtraSheetHeaderAndContent() {
        LogProvider logProvider = new LogProvider();
        logProvider.setLogLevel(LogLevel.None);
        logProvider.setPrintToConsole(false);

        Settings settings = new Settings();
        XSSFWorkbook workbook = new XSSFWorkbook();
        DocManager docManager = new DocManager(workbook, logProvider, settings);

        SettingsSet settingsSet = new SettingsSet();
        settingsSet.numberSize = 0;
        settingsSet.extrasize = 2;

        List<StatsDataHolder> statsDataHolders = Arrays.asList(
                createStatsDataHolder("Viking-1Year", 1, 1),
                createStatsDataHolder("Viking-3Years", 2, 2),
                createStatsDataHolder("Viking-All", 3, 3)
        );

        XSSFSheet sheet = workbook.createSheet("Viking Extra Stats");
        docManager.createStatExtraSheetHeader(sheet, settingsSet, statsDataHolders);
        docManager.createStatExtraSheetContent(statsDataHolders, sheet, settingsSet);

        XSSFRow headerRow = sheet.getRow(0);
        Assert.assertNotNull("Header row must exist", headerRow);
        Assert.assertEquals("1 extra", headerRow.getCell(0).getStringCellValue());
        Assert.assertEquals("2 extras", headerRow.getCell(6).getStringCellValue());

        XSSFRow yearRow = sheet.getRow(1);
        Assert.assertNotNull("Year header row must exist", yearRow);
        Assert.assertEquals("1 Year", yearRow.getCell(0).getStringCellValue());
        Assert.assertEquals("3 Years", yearRow.getCell(2).getStringCellValue());
        Assert.assertEquals("All", yearRow.getCell(4).getStringCellValue());
        Assert.assertEquals("1 Year", yearRow.getCell(6).getStringCellValue());
        Assert.assertEquals("3 Years", yearRow.getCell(9).getStringCellValue());
        Assert.assertEquals("All", yearRow.getCell(12).getStringCellValue());

        XSSFRow dataRow = sheet.getRow(3);
        Assert.assertNotNull("Data row must exist", dataRow);

        // 1 extra, 1 Year section
        Assert.assertEquals(1.0, dataRow.getCell(0).getNumericCellValue(), 0.0);
        Assert.assertEquals(5.0, dataRow.getCell(1).getNumericCellValue(), 0.0);

        // 2 extras, 1 Year section
        Assert.assertEquals(1.0, dataRow.getCell(6).getNumericCellValue(), 0.0);
        Assert.assertEquals(3.0, dataRow.getCell(7).getNumericCellValue(), 0.0);
        Assert.assertEquals(4.0, dataRow.getCell(8).getNumericCellValue(), 0.0);

        // 2 extras, 3 Years section
        Assert.assertEquals(2.0, dataRow.getCell(9).getNumericCellValue(), 0.0);
        Assert.assertEquals(3.0, dataRow.getCell(10).getNumericCellValue(), 0.0);
        Assert.assertEquals(4.0, dataRow.getCell(11).getNumericCellValue(), 0.0);

        // 2 extras, All section
        Assert.assertEquals(3.0, dataRow.getCell(12).getNumericCellValue(), 0.0);
        Assert.assertEquals(3.0, dataRow.getCell(13).getNumericCellValue(), 0.0);
        Assert.assertEquals(4.0, dataRow.getCell(14).getNumericCellValue(), 0.0);
    }

    @Test
    public void testCreateStatNumberSheetHeaderAndContent() {
        LogProvider logProvider = new LogProvider();
        logProvider.setLogLevel(LogLevel.None);
        logProvider.setPrintToConsole(false);
        
        Settings settings = new Settings();
        XSSFWorkbook workbook = new XSSFWorkbook();
        DocManager docManager = new DocManager(workbook, logProvider, settings);

        SettingsSet settingsSet = new SettingsSet();
        settingsSet.numberSize = 2;
        settingsSet.extrasize = 0;

        StatsDataHolder holder1 = createNumberStatsDataHolder("Viking-1Year", 1, 2);
        StatsDataHolder holder2 = createNumberStatsDataHolder("Viking-3Years", 3, 4);
        StatsDataHolder holder3 = createNumberStatsDataHolder("Viking-All", 5, 6);

        List<StatsDataHolder> statsDataHolders = Arrays.asList(holder1, holder2, holder3);

        XSSFSheet sheet = workbook.createSheet("Viking Stats");
        docManager.createStatNumberSheetHeader(sheet, settingsSet);
        docManager.createStatNumberSheetContent(statsDataHolders, sheet, settingsSet);

        XSSFRow headerRow = sheet.getRow(0);
        Assert.assertNotNull(headerRow);
        Assert.assertEquals("1 number", headerRow.getCell(0).getStringCellValue());
        XSSFRow dataRow = sheet.getRow(3);
        Assert.assertNotNull(dataRow);
        Assert.assertEquals(1.0, dataRow.getCell(0).getNumericCellValue(), 0.0);
    }

    private StatsDataHolder createStatsDataHolder(String game, int extra1Count, int extra2Count) {
        Map<Integer, Map<List<Integer>, Integer>> extraCombCounts = new HashMap<>();
        Map<List<Integer>, Integer> oneExtra = new HashMap<>();
        oneExtra.put(Collections.singletonList(5), extra1Count);
        extraCombCounts.put(1, oneExtra);

        Map<List<Integer>, Integer> twoExtras = new HashMap<>();
        twoExtras.put(Arrays.asList(3, 4), extra2Count);
        extraCombCounts.put(2, twoExtras);

        return new StatsDataHolder(game, new HashMap<>(), extraCombCounts);
    }

    private StatsDataHolder createNumberStatsDataHolder(String game, int firstNumber, int secondNumber) {
        Map<Integer, Map<List<Integer>, Integer>> numberCombCounts = new HashMap<>();
        Map<List<Integer>, Integer> oneNumber = new HashMap<>();
        oneNumber.put(Collections.singletonList(firstNumber), 1);
        numberCombCounts.put(1, oneNumber);
        Map<List<Integer>, Integer> twoNumbers = new HashMap<>();
        twoNumbers.put(Arrays.asList(secondNumber, secondNumber + 1), 1);
        numberCombCounts.put(2, twoNumbers);
        return new StatsDataHolder(game, numberCombCounts, new HashMap<>());
    }
}
