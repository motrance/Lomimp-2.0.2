package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import coffeemunchingmonkeys.tools.lomimp.core.*;
import coffeemunchingmonkeys.tools.lomimp.stats.*;

/**
 *
 * Lomimp
 * bricks.DocManager
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class DocManager extends SpreadSheetHelper {
    //Fields
    LogProvider log;
    Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
    Settings settings;
    int maxNumberOfNumbers = 0;
    int maxNumberOfExtras = 0;

    public DocManager(XSSFWorkbook workBook, LogProvider log, Settings settings) {
        this.log = log;
        this.workBook = workBook;
        this.settings = settings;
        setupFontsAndStyles();
    }

    /** 
     * @param rows
     * @param displayName
     */
    // Create Sheet sets
    public void createNumbersSheet(ArrayList<Row> rows, String displayName) {
        if(settings != null && settings.getSettings(displayName) != null) {
            XSSFSheet newSheet = this.workBook.createSheet(displayName);
            createNumbersSheetHeader(rows, newSheet);
            createNumbersSheetContent(rows, newSheet, displayName, settings.getSettings(displayName));

            autoSizeColumns(newSheet);
        }
    }

    /** 
     * @param statsDataHolderMap
     * @param settings
     */
    public void createStatNumberSheets(Map<String, List<StatsDataHolder>> statsDataHolderMap) {
        for(Map.Entry<String, List<StatsDataHolder>> entry : statsDataHolderMap.entrySet()) {
            String displayName = entry.getKey();
            SettingsSet settingsSet = settings.getSettings(displayName);
            List<StatsDataHolder> statsDataHolders = entry.getValue();

            if (settingsSet != null && statsDataHolders != null && !statsDataHolders.isEmpty()) {
                XSSFSheet numberSheet = this.workBook.createSheet(displayName + " Stats");
                createStatNumberSheetHeader(numberSheet, settingsSet);
                createStatNumberSheetContent(statsDataHolders, numberSheet, settingsSet);
                autoSizeColumns(numberSheet);

                SettingsSet extraSettingsSet = settings.getSettings(displayName);
                if(extraSettingsSet != null && extraSettingsSet.getExtrasize() > 0) {
                    XSSFSheet extraSheet = this.workBook.createSheet(displayName + " Extra Stats");
                    createStatExtraSheetHeader(extraSheet, settingsSet, statsDataHolders);
                    createStatExtraSheetContent(statsDataHolders, extraSheet, settingsSet);
                    autoSizeColumns(extraSheet);
                }
            } else {
                log.writeInfo("Unable to create stats sheets for " + displayName + ". No data available.");
            }
        }
    }

    /** 
     * @param vikingPairsStatic
     * @param euroPairsStatic
     * @param lottoPairsStatic
     */
    public void createInfoSheet(ArrayList<BigInteger> vikingPairsStatic, ArrayList<BigInteger> euroPairsStatic, ArrayList<BigInteger> lottoPairsStatic) {
        XSSFSheet sheet = this.workBook.createSheet("Info");
        if(vikingPairsStatic != null && euroPairsStatic != null && lottoPairsStatic != null && vikingPairsStatic.size() > 0 && euroPairsStatic.size() > 0 && lottoPairsStatic.size() > 0) {
            int rowCounter = 0;

            XSSFRow topRow = sheet.createRow(rowCounter++);
            XSSFCell cell0 = topRow.createCell(0);
            cell0.setCellValue("Static Stats");
            cell0.setCellStyle(style6);
            XSSFCell cell1 = topRow.createCell(1);
            cell1.setCellValue("Possible combinations");
            cell1.setCellStyle(style6);

            //Viking static stats
            XSSFRow tmpVikingRow = sheet.createRow(rowCounter++);
            XSSFCell tempCellViking = tmpVikingRow.createCell(0);
            tempCellViking.setCellValue("Viking");
            for(int counter = 0; counter < vikingPairsStatic.size(); counter++) {
                XSSFRow tmpRow = sheet.createRow(rowCounter++);
                XSSFCell tempCell1 = tmpRow.createCell(0);
                XSSFCell tempCell2 = tmpRow.createCell(1);

                if(counter < vikingPairsStatic.size() - 2) {
                    tempCell1.setCellValue((counter + 1) + " number" + (counter + 1 > 1 ? "s" : ""));
                    tempCell2.setCellStyle(style15);
                    tempCell2.setCellValue(vikingPairsStatic.get(counter).doubleValue());
                }
                
                if(counter == vikingPairsStatic.size() - 2) {
                    tempCell1.setCellValue("Extra ");
                    tempCell2.setCellStyle(style15);
                    tempCell2.setCellValue(vikingPairsStatic.get(counter).doubleValue());
                }
                
                if(counter == vikingPairsStatic.size() - 1) {
                    tempCell1.setCellStyle(style4);
                    tempCell2.setCellStyle(style16);
                    tempCell1.setCellValue("Full row");
                    tempCell2.setCellValue(vikingPairsStatic.get(counter).doubleValue());
                }
            }

            //Insert blank row
            sheet.createRow(rowCounter++);

            //EuroJackpot static stats
            XSSFRow tmpEuroRow = sheet.createRow(rowCounter++);
            XSSFCell tempCellEuro = tmpEuroRow.createCell(0);
            tempCellEuro.setCellValue("EuroJackpot");
            int extraCounterPrefix = 1;
            for(int counter = 0; counter < euroPairsStatic.size(); counter++) {
                XSSFRow tmpRow = sheet.createRow(rowCounter++);
                XSSFCell tempCell1 = tmpRow.createCell(0);
                XSSFCell tempCell2 = tmpRow.createCell(1);

                if(counter < euroPairsStatic.size() - 3) {
                    tempCell1.setCellValue((counter + 1) + " number" + (counter + 1 > 1 ? "s" : ""));
                    tempCell2.setCellStyle(style15);
                    tempCell2.setCellValue(euroPairsStatic.get(counter).doubleValue());
                }
                
                if(counter >= euroPairsStatic.size() - 3 && counter < euroPairsStatic.size() - 1) {
                    tempCell1.setCellValue(extraCounterPrefix++ + " star" + ((extraCounterPrefix - 1) > 1 ? "s" : ""));
                    tempCell2.setCellStyle(style15);
                    tempCell2.setCellValue(euroPairsStatic.get(counter).doubleValue());
                }
                
                if(counter == euroPairsStatic.size() - 1) {
                    tempCell1.setCellStyle(style4);
                    tempCell2.setCellStyle(style16);
                    tempCell1.setCellValue("Full row");
                    tempCell2.setCellValue(euroPairsStatic.get(counter).doubleValue());
                }
            }

            //Insert blank row
            sheet.createRow(rowCounter++);

            //EuroJackpot static stats
            XSSFRow tmpLottoRow = sheet.createRow(rowCounter++);
            XSSFCell tempCellLotto = tmpLottoRow.createCell(0);
            tempCellLotto.setCellValue("Lotto");
            for(int counter = 0; counter < lottoPairsStatic.size(); counter++) {
                XSSFRow tmpRow = sheet.createRow(rowCounter++);
                XSSFCell tempCell1 = tmpRow.createCell(0);
                XSSFCell tempCell2 = tmpRow.createCell(1);

                if(counter < lottoPairsStatic.size() - 1) {
                    tempCell1.setCellValue((counter + 1) + " number" + (counter + 1 > 1 ? "s" : ""));
                    tempCell2.setCellStyle(style15);
                    tempCell2.setCellValue(lottoPairsStatic.get(counter).doubleValue());
                }
                
                if(counter == lottoPairsStatic.size() - 1) {
                    tempCell1.setCellStyle(style4);
                    tempCell2.setCellStyle(style16);
                    tempCell1.setCellValue("Full row");
                    tempCell2.setCellValue(lottoPairsStatic.get(counter).doubleValue());
                }
            }

            autoSizeColumns(sheet);
        } else {
            XSSFRow topRow = sheet.createRow(0);
            XSSFCell celTmp = topRow.createCell(0);
            celTmp.setCellValue("No static data available");
            log.writeInfo("Unable to create info sheet. No static data available...");
        }
    }

    /** 
     * @param rows
     * @param sheet
     */
    public void createNumbersSheetHeader(ArrayList<Row> rows, XSSFSheet sheet) {
        if(sheet != null) {
            XSSFRow row = sheet.createRow(0);
            maxNumberOfNumbers = 0;
            maxNumberOfExtras = 0;

            for(int rowsCounter = 0; rowsCounter < rows.size(); rowsCounter++) {
                Row tmpRow = rows.get(rowsCounter);
                int num = tmpRow.getNumbersSize();
                int etx = tmpRow.getExtrasSize();
                if(num > maxNumberOfNumbers) {
                    maxNumberOfNumbers = num;
                }
                if(etx > maxNumberOfExtras) {
                    maxNumberOfExtras = etx;
                }
            }

            int columnIndex = 0;

            XSSFCell cell1 = row.createCell(columnIndex++);
            cell1.setCellValue("Week");
            cell1.setCellStyle(style1);

            XSSFCell cell2 = row.createCell(columnIndex++);
            cell2.setCellValue("Date");
            cell2.setCellStyle(style3);

            for(int numbersCounter = 0; numbersCounter < maxNumberOfNumbers; numbersCounter++) {
                XSSFCell newCell = row.createCell(columnIndex++);
                newCell.setCellValue((numbersCounter + 1) + ".");
                newCell.setCellStyle(style5);
            }

            for(int extrasCounter = 0; extrasCounter < maxNumberOfExtras; extrasCounter++) {
                XSSFCell newCell = row.createCell(columnIndex++);
                newCell.setCellValue("E" + (extrasCounter + 1));
                newCell.setCellStyle(style7);
            }
        }
    }

    /** 
     * @param rows
     * @param sheet
     */
    public void createNumbersSheetContent(ArrayList<Row> rows, XSSFSheet sheet, String displayName, SettingsSet settingsSet) {
        if(sheet != null) {
            int rowNumber = 1;

            for(Row row : rows) {
                XSSFRow excelRow = sheet.createRow(rowNumber++);

                int columnIndex = 0;
                XSSFCell cell1 = excelRow.createCell(columnIndex++);
                cell1.setCellValue(row.getCw());
                cell1.setCellStyle(style2);

                XSSFCell cell2 = excelRow.createCell(columnIndex++);
                cell2.setCellValue(row.getDate());
                cell2.setCellStyle(style4);

                String[] numbers = row.numbersToString().replace("{", "").replace("}", "").replace("[", "").replace("]", " ").trim().split(" ");
                for(int numbersCounter = 0; numbersCounter < numbers.length; numbersCounter++) {
                    XSSFCell newCell = excelRow.createCell(columnIndex++);
                    newCell.setCellValue(numbers[numbersCounter]);
                    newCell.setCellStyle(style6);
                }

                /*
                String[] extras = row.extrasToString().replace("{", "").replace("}", "").replace("[", "").replace("]", " ").trim().split(" ");
                for(int extrasCounter = 0; extrasCounter < extras.length; extrasCounter++) {
                    XSSFCell newCell = excelRow.createCell(columnIndex++);
                    newCell.setCellValue(extras[extrasCounter]);
                    newCell.setCellStyle(style8);
                }
                */
                if(settingsSet != null && settingsSet.getExtrasize() > 0) {
                    String[] extras = row.extrasToString().replace("{", "").replace("}", "").replace("[", "").replace("]", " ").trim().split(" ");
                    for(int extrasCounter = 0; extrasCounter < extras.length; extrasCounter++) {
                        XSSFCell newCell = excelRow.createCell(columnIndex++);
                        newCell.setCellValue(extras[extrasCounter]);
                        newCell.setCellStyle(style8);
                    }
                }
                    
            }
        }
    }

    /** 
     * @param sheet
     * @param settingsSet
     */
    public void createStatNumberSheetHeader(XSSFSheet sheet, SettingsSet settingsSet) {
        if(sheet != null && settingsSet != null) {
            int maxPairSize = settingsSet.getNumberSize();
            int rowCounter = 0;

            XSSFRow topRow = sheet.createRow(rowCounter++);

            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int topRowStyleIndex = (pairSizeCounter * 2) - 2;
                    if(topRowStyleIndex > styles.size()) {
                        topRowStyleIndex = 0;
                    }
                    XSSFCellStyle topRowStyle = styles.get(topRowStyleIndex);

                    XSSFCell tempTopRowCell = topRow.createCell(columnIndex++);
                    tempTopRowCell.setCellStyle(topRowStyle);
                    tempTopRowCell.setCellValue(pairSizeCounter + " number" + (pairSizeCounter > 1 ? "s" : ""));

                    for(int tmpCellCounter = 1; tmpCellCounter < (1 + pairSizeCounter) * 3; tmpCellCounter++) {
                        XSSFCell emptyCell = topRow.createCell(columnIndex++);
                        emptyCell.setCellValue("");
                    }
                }
            } catch (Exception e) {
                log.writeError("Error while creating toprow in number stats sheet: " + e.getMessage());
            }

            XSSFRow yearRow = sheet.createRow(rowCounter++);
            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                            int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);
                    //XSSFCellStyle borderedYearRowStyle = createThickBorderStyle(yearRowStyle);

                    XSSFCellStyle yearRowLeftStyle = createThickBorderStyle(yearRowStyle, true, true, true, false);
                    XSSFCellStyle yearRowMiddleStyle = createThickBorderStyle(yearRowStyle, true, true, false, false);
                    XSSFCellStyle yearRowRightStyle = createThickBorderStyle(yearRowStyle, true, true, false, true);

                    XSSFCell tempYearRowCell1 = yearRow.createCell(columnIndex++);
                    tempYearRowCell1.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell1.setCellValue("1 Year");

                    int startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }

                    XSSFCell tempYearRowCell2 = yearRow.createCell(columnIndex++);
                    tempYearRowCell2.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell2.setCellValue("3 Years");

                    startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }

                    XSSFCell tempYearRowCell3 = yearRow.createCell(columnIndex++);
                    tempYearRowCell3.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell3.setCellValue("All");

                    startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }                 
                }
            } catch (Exception e) {
                log.writeError("Error while creating toprow in number stats sheet: " + e.getMessage());
            }
            
            XSSFRow countRow = sheet.createRow(rowCounter++);
            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);
                    XSSFCellStyle yearRowLeftStyle = createThickBorderStyle(yearRowStyle, true, true, true, false);
                    XSSFCellStyle yearRowMiddleStyle = createThickBorderStyle(yearRowStyle, true, true, false, false);
                    XSSFCellStyle yearRowRightStyle = createThickBorderStyle(yearRowStyle, true, true, false, true);
                    for(int countTo3 = 1; countTo3 <= 3; countTo3++) {
                        XSSFCell tempYearRowCell1 = countRow.createCell(columnIndex++);
                        tempYearRowCell1.setCellStyle(yearRowLeftStyle);
                        tempYearRowCell1.setCellValue("#");

                        int startColumnIndex = columnIndex;
                        int numbers = 1;
                        for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                            XSSFCell countCell = countRow.createCell(columnIndex++);
                            if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                                countCell.setCellStyle(yearRowRightStyle);
                            } else {
                                countCell.setCellStyle(yearRowMiddleStyle);
                            }
                            countCell.setCellValue(numbers++ + ".");
                        }
                    }         
                }
            } catch (Exception e) {
                log.writeError("Error while creating yearRow in number stats sheet: " + e.getMessage());
            }
        }
    }

    /** 
     * @param statsDataHolders
     * @param sheet
     * @param settingsSet
     */
    public void createStatNumberSheetContent(List<StatsDataHolder> statsDataHolders, XSSFSheet sheet, SettingsSet settingsSet) {
        if(sheet != null && settingsSet != null && statsDataHolders != null && !statsDataHolders.isEmpty()) {
            int maxPairSize = settingsSet.getNumberSize();
            try {
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);

                    @SuppressWarnings("unchecked")
                    Map<Integer, Map<List<Integer>, Integer>>[] sections = new Map[3];
                    for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                        if (sectionIndex < statsDataHolders.size()) {
                            sections[sectionIndex] = statsDataHolders.get(sectionIndex).getNumberCombCounts();
                        } else {
                            sections[sectionIndex] = new HashMap<>();
                        }
                    }

                    int blockStartColumn = 0;
                    for (int previousSize = 1; previousSize < pairSizeCounter; previousSize++) {
                        blockStartColumn += (1 + previousSize) * 3;
                    }

                    for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                        Map<List<Integer>, Integer> combMap = sections[sectionIndex].get(pairSizeCounter);
                        if (combMap != null && !combMap.isEmpty()) {
                            List<Map.Entry<List<Integer>, Integer>> numberPairs = combMap.entrySet().stream()
                                .sorted((a, b) -> {
                                    int countCompare = b.getValue().compareTo(a.getValue());
                                    if (countCompare != 0) {
                                        return countCompare;
                                    }
                                    List<Integer> keyA = a.getKey();
                                    List<Integer> keyB = b.getKey();
                                    int minSize = Math.min(keyA.size(), keyB.size());
                                    for (int i = 0; i < minSize; i++) {
                                        int valueCompare = keyA.get(i).compareTo(keyB.get(i));
                                        if (valueCompare != 0) {
                                            return valueCompare;
                                        }
                                    }
                                    return Integer.compare(keyA.size(), keyB.size());
                                })
                                .collect(Collectors.toList());

                            int sectionWidth = 1 + pairSizeCounter;
                            int sectionStartColumn = blockStartColumn + (sectionIndex * sectionWidth);

                            for (int entryIndex = 0; entryIndex < numberPairs.size(); entryIndex++) {
                                Map.Entry<List<Integer>, Integer> entry = numberPairs.get(entryIndex);
                                int rowIndex = 3 + entryIndex;
                                XSSFRow tmpRow = sheet.getRow(rowIndex);
                                if (tmpRow == null) {
                                    tmpRow = sheet.createRow(rowIndex);
                                }

                                int columnIndex = sectionStartColumn;
                                XSSFCell tempCell1 = tmpRow.createCell(columnIndex++);
                                tempCell1.setCellValue(entry.getValue());
                                tempCell1.setCellStyle(yearRowStyle);

                                for (Integer number : entry.getKey()) {
                                    XSSFCell numberCell = tmpRow.createCell(columnIndex++);
                                    numberCell.setCellValue(number);
                                    //numberCell.setCellStyle(yearRowStyle);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.writeError("Error while creating number stats sheet: " + e.getMessage());
            }
        }
    }

    /** 
     * @param sheet
     * @param settingsSet
     * @param statsDataHolders
     */
    public void createStatExtraSheetHeader(XSSFSheet sheet, SettingsSet settingsSet, List<StatsDataHolder> statsDataHolders) {
        if(sheet != null && settingsSet != null) {
            int maxPairSize = getMaxExtraSize(settingsSet, statsDataHolders);
            int rowCounter = 0;

            XSSFRow topRow = sheet.createRow(rowCounter++);

            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int topRowStyleIndex = (pairSizeCounter * 2) - 2;
                    if(topRowStyleIndex > styles.size()) {
                        topRowStyleIndex = 0;
                    }
                    XSSFCellStyle topRowStyle = styles.get(topRowStyleIndex);

                    XSSFCell tempTopRowCell = topRow.createCell(columnIndex++);
                    tempTopRowCell.setCellStyle(topRowStyle);
                    tempTopRowCell.setCellValue(pairSizeCounter + " extra" + (pairSizeCounter > 1 ? "s" : ""));

                    for(int tmpCellCounter = 1; tmpCellCounter < (1 + pairSizeCounter) * 3; tmpCellCounter++) {
                        XSSFCell emptyCell = topRow.createCell(columnIndex++);
                        emptyCell.setCellValue("");
                    }
                }
            } catch (Exception e) {
                log.writeError("Error while creating top row in extra stats sheet: " + e.getMessage());
            }

            XSSFRow yearRow = sheet.createRow(rowCounter++);
            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);
                    XSSFCellStyle yearRowLeftStyle = createThickBorderStyle(yearRowStyle, true, true, true, false);
                    XSSFCellStyle yearRowMiddleStyle = createThickBorderStyle(yearRowStyle, true, true, false, false);
                    XSSFCellStyle yearRowRightStyle = createThickBorderStyle(yearRowStyle, true, true, false, true);

                    XSSFCell tempYearRowCell1 = yearRow.createCell(columnIndex++);
                    tempYearRowCell1.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell1.setCellValue("1 Year");

                    int startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }

                    XSSFCell tempYearRowCell2 = yearRow.createCell(columnIndex++);
                    tempYearRowCell2.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell2.setCellValue("3 Years");

                    startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }

                    XSSFCell tempYearRowCell3 = yearRow.createCell(columnIndex++);
                    tempYearRowCell3.setCellStyle(yearRowLeftStyle);
                    tempYearRowCell3.setCellValue("All");

                    startColumnIndex = columnIndex;
                    for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                        XSSFCell emptyCell = yearRow.createCell(columnIndex++);
                        if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                            emptyCell.setCellStyle(yearRowRightStyle);
                        } else {
                            emptyCell.setCellStyle(yearRowMiddleStyle);
                        }
                        emptyCell.setCellValue("");
                    }
                }
            } catch (Exception e) {
                log.writeError("Error while creating year row in extra stats sheet: " + e.getMessage());
            }

            XSSFRow countRow = sheet.createRow(rowCounter++);
            try {
                int columnIndex = 0;
                for(int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);
                    XSSFCellStyle yearRowLeftStyle = createThickBorderStyle(yearRowStyle, true, true, true, false);
                    XSSFCellStyle yearRowMiddleStyle = createThickBorderStyle(yearRowStyle, true, true, false, false);
                    XSSFCellStyle yearRowRightStyle = createThickBorderStyle(yearRowStyle, true, true, false, true);

                    for(int countTo3 = 1; countTo3 <= 3; countTo3++) {
                        XSSFCell tempYearRowCell1 = countRow.createCell(columnIndex++);
                        tempYearRowCell1.setCellStyle(yearRowLeftStyle);
                        tempYearRowCell1.setCellValue("#");

                        int startColumnIndex = columnIndex;
                        int numbers = 1;
                        for(int tmpCellCounter = startColumnIndex; tmpCellCounter < startColumnIndex + pairSizeCounter; tmpCellCounter++) {
                            XSSFCell countCell = countRow.createCell(columnIndex++);
                            if (tmpCellCounter == startColumnIndex + pairSizeCounter - 1) {
                                countCell.setCellStyle(yearRowRightStyle);
                            } else {
                                countCell.setCellStyle(yearRowMiddleStyle);
                            }
                            countCell.setCellValue(numbers++ + ".");
                        }
                    }
                }
            } catch (Exception e) {
                log.writeError("Error while creating count row in extra stats sheet: " + e.getMessage());
            }
        }
    }

    /** 
     * @param statsDataHolders
     * @param sheet
     * @param settingsSet
     */
    public void createStatExtraSheetContent(List<StatsDataHolder> statsDataHolders, XSSFSheet sheet, SettingsSet settingsSet) {
        if(sheet != null && settingsSet != null && statsDataHolders != null && !statsDataHolders.isEmpty()) {
            int maxPairSize = getMaxExtraSize(settingsSet, statsDataHolders);
            try {
                @SuppressWarnings("unchecked")
                Map<Integer, Map<List<Integer>, Integer>>[] sections = new Map[3];
                for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                    if (sectionIndex < statsDataHolders.size()) {
                        sections[sectionIndex] = statsDataHolders.get(sectionIndex).getExtraCombCounts();
                    } else {
                        sections[sectionIndex] = new HashMap<>();
                    }
                }

                for (int pairSizeCounter = 1; pairSizeCounter <= maxPairSize; pairSizeCounter++) {
                    int yearRowStyleIndex = (pairSizeCounter * 2) - 1;
                    if(yearRowStyleIndex > styles.size()) {
                        yearRowStyleIndex = 0;
                    }
                    XSSFCellStyle yearRowStyle = styles.get(yearRowStyleIndex);

                    int sectionWidth = 1 + pairSizeCounter;
                    int blockStartColumn = 0;
                    for (int previousSize = 1; previousSize < pairSizeCounter; previousSize++) {
                        blockStartColumn += (1 + previousSize) * 3;
                    }

                    for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                        Map<List<Integer>, Integer> combMap = sections[sectionIndex].get(pairSizeCounter);
                        if (combMap == null || combMap.isEmpty()) {
                            continue;
                        }

                        List<Map.Entry<List<Integer>, Integer>> extraPairs = combMap.entrySet().stream()
                            .sorted((a, b) -> {
                                int countCompare = b.getValue().compareTo(a.getValue());
                                if (countCompare != 0) {
                                    return countCompare;
                                }
                                List<Integer> keyA = a.getKey();
                                List<Integer> keyB = b.getKey();
                                int minSize = Math.min(keyA.size(), keyB.size());
                                for (int i = 0; i < minSize; i++) {
                                    int valueCompare = keyA.get(i).compareTo(keyB.get(i));
                                    if (valueCompare != 0) {
                                        return valueCompare;
                                    }
                                }
                                return Integer.compare(keyA.size(), keyB.size());
                            })
                            .collect(Collectors.toList());

                        int sectionStartColumn = blockStartColumn + (sectionIndex * sectionWidth);
                        for (int entryIndex = 0; entryIndex < extraPairs.size(); entryIndex++) {
                            Map.Entry<List<Integer>, Integer> entry = extraPairs.get(entryIndex);
                            int rowIndex = 3 + entryIndex;
                            XSSFRow tmpRow = sheet.getRow(rowIndex);
                            if (tmpRow == null) {
                                tmpRow = sheet.createRow(rowIndex);
                            }

                            int columnIndex = sectionStartColumn;
                            XSSFCell tempCell1 = tmpRow.createCell(columnIndex++);
                            tempCell1.setCellValue(entry.getValue());
                            tempCell1.setCellStyle(yearRowStyle);

                            for (Integer number : entry.getKey()) {
                                XSSFCell numberCell = tmpRow.createCell(columnIndex++);
                                numberCell.setCellValue(number);
                            }
                        }
                    }

                    blockStartColumn += sectionWidth * sections.length;
                }
            } catch (Exception e) {
                log.writeError("Error while creating extra stats sheet: " + e.getMessage());
            }
        }
    }

    /** 
     * @param baseStyle
     * @param top
     * @param bottom
     * @param left
     * @param right
     * @return XSSFCellStyle
     */
    public XSSFCellStyle createThickBorderStyle(XSSFCellStyle baseStyle, boolean top, boolean bottom, boolean left, boolean right) {
        XSSFCellStyle borderedStyle = this.workBook.createCellStyle();
        borderedStyle.cloneStyleFrom(baseStyle);
        if (top) {
            borderedStyle.setBorderTop(BorderStyle.THICK);
        }
        if (bottom) {
            borderedStyle.setBorderBottom(BorderStyle.THICK);
        }
        if (left) {
            borderedStyle.setBorderLeft(BorderStyle.THICK);
        }
        if (right) {
            borderedStyle.setBorderRight(BorderStyle.THICK);
        }
        return borderedStyle;
    }

    /** 
     * @param settingsSet
     * @param statsDataHolders
     * @return int
     */
    public int getMaxExtraSize(SettingsSet settingsSet, List<StatsDataHolder> statsDataHolders) {
        int maxSize = settingsSet != null ? settingsSet.getExtrasize() : 0;
        if (statsDataHolders != null) {
            for (StatsDataHolder holder : statsDataHolders) {
                if (holder != null && holder.getExtraCombCounts() != null) {
                    for (Integer size : holder.getExtraCombCounts().keySet()) {
                        if (size != null && size > maxSize) {
                            maxSize = size;
                        }
                    }
                }
            }
        }
        return maxSize;
    }

    /** 
     * @param sheet
     */
    public void autoSizeColumns(XSSFSheet sheet) {
        if(sheet != null) {
            finish(sheet);
            /*
            int WidthNumberPairs = 32000;
            int WidthStarPairs = 6000;

            finish(numbersSheet);

            numbersSheet.setColumnWidth(9, WidthNumberPairs);
            numbersSheet.setColumnWidth(10, WidthStarPairs);
             */
        }
    }
}
