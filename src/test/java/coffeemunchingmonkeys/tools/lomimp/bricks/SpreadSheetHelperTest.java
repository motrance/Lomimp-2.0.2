package coffeemunchingmonkeys.tools.lomimp.bricks;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SpreadSheetHelperTest {

    @Test
    public void setupFontsAndStylesCreatesExpectedStylesAndList() {
        TestSpreadSheetHelper helper = createHelper();

        helper.setupFontsAndStyles();

        Assert.assertNotNull(helper.styleDefault);
        Assert.assertNotNull(helper.style1);
        Assert.assertNotNull(helper.style14);
        Assert.assertNotNull(helper.style15);
        Assert.assertNotNull(helper.style16);
        Assert.assertEquals(14, helper.styles.size());
        Assert.assertSame(helper.style1, helper.styles.get(0));
        Assert.assertSame(helper.style14, helper.styles.get(13));
        Assert.assertEquals(HorizontalAlignment.LEFT, helper.styleDefault.getAlignment());
        Assert.assertEquals(VerticalAlignment.TOP, helper.styleDefault.getVerticalAlignment());
        Assert.assertEquals(FillPatternType.SOLID_FOREGROUND, helper.style1.getFillPattern());
        Assert.assertEquals("#,##0", helper.style15.getDataFormatString());
        Assert.assertEquals("#,##0", helper.style16.getDataFormatString());
    }

    @Test
    public void finishAutosizesColumnsAndLogsExceptionsForInvalidSheet() {
        TestSpreadSheetHelper helper = createHelper();
        helper.log = mock(LogProvider.class);
        helper.workBook = new XSSFWorkbook();
        XSSFSheet sheet = helper.workBook.createSheet("test");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("header");

        helper.finish(sheet);

        Assert.assertNotNull(sheet.getColumnWidth(0));

        helper.finish(null);

        verify(helper.log).writeException(org.mockito.ArgumentMatchers.any(Exception.class));
    }

    @Test
    public void style16UsesNumberFormatting() {
        TestSpreadSheetHelper helper = createHelper();

        helper.setupFontsAndStyles();

        Assert.assertEquals("#,##0", helper.style16.getDataFormatString());
    }

    private TestSpreadSheetHelper createHelper() {
        TestSpreadSheetHelper helper = new TestSpreadSheetHelper();
        helper.workBook = new XSSFWorkbook();
        helper.log = mock(LogProvider.class);
        return helper;
    }

    private static class TestSpreadSheetHelper extends SpreadSheetHelper {
    }
}
