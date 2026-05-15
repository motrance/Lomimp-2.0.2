package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * Lomimp
 * bricks.SpreadSheetHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public abstract class SpreadSheetHelper {
    //Fields
    LogProvider		log;
    XSSFWorkbook	workBook;
    XSSFCellStyle 	styleDefault;
    XSSFCellStyle 	style1;
    XSSFCellStyle 	style2;
    XSSFCellStyle 	style3;
    XSSFCellStyle  	style4;
    XSSFCellStyle  	style5;
    XSSFCellStyle  	style6;
    XSSFCellStyle  	style7;
    XSSFCellStyle  	style8;
    XSSFCellStyle  	style9;
    XSSFCellStyle  	style10;
    XSSFCellStyle  	style11;
    XSSFCellStyle  	style12;
    XSSFCellStyle  	style13;
    XSSFCellStyle  	style14;
    XSSFCellStyle  	style15;
    XSSFCellStyle  	style16;
    ArrayList<XSSFCellStyle> styles = new ArrayList<>();

    /**
     *
     */
    protected void setupFontsAndStyles() {
        //Colors
        XSSFColor yellowDk = null;
        XSSFColor yellowLt = null;
        XSSFColor lillaDk = null;
        XSSFColor lillaLt = null;
        XSSFColor blueDk = null;
        XSSFColor blueLt = null;
        XSSFColor orangeDk = null;
        XSSFColor orangeLt = null;
        XSSFColor greenDk = null;
        XSSFColor greenLt = null;
        XSSFColor grayDK = null;
        XSSFColor grayLt = null;
        XSSFColor redDK = null;
        XSSFColor redLt = null;

        try {
            yellowDk = new XSSFColor(Hex.decodeHex("acb20c"), null);
            yellowLt = new XSSFColor(Hex.decodeHex("ffff38"), null);
            lillaDk = new XSSFColor(Hex.decodeHex("bf819e"), null);
            lillaLt = new XSSFColor(Hex.decodeHex("e0c2cd"), null);
            blueDk = new XSSFColor(Hex.decodeHex("729fcf"), null);
            blueLt = new XSSFColor(Hex.decodeHex("b4c7dc"), null);
            orangeDk = new XSSFColor(Hex.decodeHex("ff972f"), null);
            orangeLt = new XSSFColor(Hex.decodeHex("ffb66c"), null);
            greenDk = new XSSFColor(Hex.decodeHex("81d41a"), null);
            greenLt = new XSSFColor(Hex.decodeHex("bbe33d"), null);
            grayDK = new XSSFColor(Hex.decodeHex("999999"), null);
            grayLt = new XSSFColor(Hex.decodeHex("d3d3d3"), null);
            redDK = new XSSFColor(Hex.decodeHex("ff4000"), null);
            redLt = new XSSFColor(Hex.decodeHex("ffa6a6"), null);
        } catch (Exception e) {
            log.write(e);
        }

        DataFormat format = this.workBook.createDataFormat();

        XSSFFont font1 = this.workBook.createFont();
        font1.setFontHeightInPoints((short)10);
        font1.setFontName("Verdana");
        font1.setItalic(false);
        font1.setStrikeout(false);
        font1.setBold(false);
        font1.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        //XSSFCellStyle styleDefault
        this.styleDefault = this.workBook.createCellStyle();
        this.styleDefault.setFont(font1);
        this.styleDefault.setVerticalAlignment(VerticalAlignment.TOP);
        this.styleDefault.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style1
        this.style1 = this.workBook.createCellStyle();
        this.style1.setFillForegroundColor(yellowDk);
        this.style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style1.setFont(font1);
        this.style1.setVerticalAlignment(VerticalAlignment.TOP);
        this.style1.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style2
        this.style2 = this.workBook.createCellStyle();
        this.style2.setFillForegroundColor(yellowLt);
        this.style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style2.setFont(font1);
        this.style2.setVerticalAlignment(VerticalAlignment.TOP);
        this.style2.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style3
        this.style3 = this.workBook.createCellStyle();
        this.style3.setFillForegroundColor(lillaDk);
        this.style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style3.setFont(font1);
        this.style3.setVerticalAlignment(VerticalAlignment.TOP);
        this.style3.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style4
        this.style4 = this.workBook.createCellStyle();
        this.style4.setFillForegroundColor(lillaLt);
        this.style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style4.setFont(font1);
        this.style4.setVerticalAlignment(VerticalAlignment.TOP);
        this.style4.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style5
        this.style5 = this.workBook.createCellStyle();
        this.style5.setFillForegroundColor(blueDk);
        this.style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style5.setFont(font1);
        this.style5.setVerticalAlignment(VerticalAlignment.TOP);
        this.style5.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style6
        this.style6 = this.workBook.createCellStyle();
        this.style6.setFillForegroundColor(blueLt);
        this.style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style6.setFont(font1);
        this.style6.setVerticalAlignment(VerticalAlignment.TOP);
        this.style6.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style7
        this.style7 = this.workBook.createCellStyle();
        this.style7.setFillForegroundColor(orangeDk);
        this.style7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style7.setFont(font1);
        this.style7.setVerticalAlignment(VerticalAlignment.TOP);
        this.style7.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style8
        this.style8 = this.workBook.createCellStyle();
        this.style8.setFillForegroundColor(orangeLt);
        this.style8.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style8.setFont(font1);
        this.style8.setVerticalAlignment(VerticalAlignment.TOP);
        this.style8.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style9
        this.style9 = this.workBook.createCellStyle();
        this.style9.setFillForegroundColor(greenDk);
        this.style9.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style9.setFont(font1);
        this.style9.setVerticalAlignment(VerticalAlignment.TOP);
        this.style9.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style10
        this.style10 = this.workBook.createCellStyle();
        this.style10.setFillForegroundColor(greenLt);
        this.style10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style10.setFont(font1);
        this.style10.setVerticalAlignment(VerticalAlignment.TOP);
        this.style10.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style11
        this.style11 = this.workBook.createCellStyle();
        this.style11.setFillForegroundColor(grayDK);
        this.style11.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style11.setFont(font1);
        this.style11.setVerticalAlignment(VerticalAlignment.TOP);
        this.style11.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style12
        this.style12 = this.workBook.createCellStyle();
        this.style12.setFillForegroundColor(grayLt);
        this.style12.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style12.setFont(font1);
        this.style12.setVerticalAlignment(VerticalAlignment.TOP);
        this.style12.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style13
        this.style13 = this.workBook.createCellStyle();
        this.style13.setFillForegroundColor(redDK);
        this.style13.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style13.setFont(font1);
        this.style13.setVerticalAlignment(VerticalAlignment.TOP);
        this.style13.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style14
        this.style14 = this.workBook.createCellStyle();
        this.style14.setFillForegroundColor(redLt);
        this.style14.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style14.setFont(font1);
        this.style14.setVerticalAlignment(VerticalAlignment.TOP);
        this.style14.setAlignment(HorizontalAlignment.LEFT);

        //XSSFCellStyle style15
        this.style15 = this.workBook.createCellStyle();
        this.style15.setFont(font1);
        this.style15.setVerticalAlignment(VerticalAlignment.TOP);
        this.style15.setDataFormat(format.getFormat("#,##0"));

        //XSSFCellStyle style16
        this.style16 = this.workBook.createCellStyle();
        this.style16.setFillForegroundColor(lillaLt);
        this.style16.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.style16.setFont(font1);
        this.style16.setVerticalAlignment(VerticalAlignment.TOP);
        this.style16.setDataFormat(format.getFormat("#,##0"));

        this.styles = new ArrayList<>(Arrays.asList(style1, style2, style3, style4, style5, style6, style7, style8, style9, style10, style11, style12, style13, style14));
    }

    /**
     *
     * @param sheet
     */
    protected void finish(XSSFSheet sheet) {
        try {
            int noOfColumns = sheet.getRow(0).getLastCellNum();

            for(int counterColumn = 0; counterColumn < noOfColumns ; counterColumn++) {
                try{
                    sheet.autoSizeColumn(counterColumn);
                } catch(Exception ignore) {}
            }
        }
        catch(Exception e) {
            log.write( e );
        }
    }

}