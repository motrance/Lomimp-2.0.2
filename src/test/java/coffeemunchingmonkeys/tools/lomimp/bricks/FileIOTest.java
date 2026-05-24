package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FileIOTest {

    @Test
    public void writeCreatesXlsxFileAndReturnsTrue() throws Exception {
        Path tempDir = Files.createTempDirectory("fileio-write-");
        Path outputPath = tempDir.resolve("export");
        XSSFWorkbook workbook = createWorkbook();
        FileIO fileIO = new FileIO(new LogProvider());

        try {
            boolean result = fileIO.write(workbook, outputPath.toString());

            Assert.assertTrue(result);
            Assert.assertTrue(Files.exists(outputPath.resolveSibling("export.xlsx")));
            Assert.assertTrue(Files.size(outputPath.resolveSibling("export.xlsx")) > 0);
        } finally {
            workbook.close();
            deleteRecursively(tempDir);
        }
    }

    @Test
    public void writAndCloseWriteCreatesFileAndClosesWorkbook() throws Exception {
        Path tempDir = Files.createTempDirectory("fileio-writandclose-");
        Path outputPath = tempDir.resolve("closed-export");
        XSSFWorkbook workbook = createWorkbook();
        FileIO fileIO = new FileIO(new LogProvider());

        try {
            boolean result = fileIO.writAndCloseWrite(workbook, outputPath.toString());

            Assert.assertTrue(result);
            Assert.assertTrue(Files.exists(outputPath.resolveSibling("closed-export.xlsx")));
            Assert.assertTrue(Files.size(outputPath.resolveSibling("closed-export.xlsx")) > 0);
        } finally {
            deleteRecursively(tempDir);
        }
    }

    @Test
    public void closeReturnsTrueAfterSuccessfulWrite() throws Exception {
        Path tempDir = Files.createTempDirectory("fileio-close-");
        Path outputPath = tempDir.resolve("close-export");
        XSSFWorkbook workbook = createWorkbook();
        FileIO fileIO = new FileIO(new LogProvider());

        try {
            Assert.assertTrue(fileIO.write(workbook, outputPath.toString()));
            Assert.assertTrue(fileIO.close(workbook));
            Assert.assertTrue(Files.exists(outputPath.resolveSibling("close-export.xlsx")));
        } finally {
            deleteRecursively(tempDir);
        }
    }

    @Test
    public void writeReturnsFalseAndLogsExceptionForInvalidPath() throws Exception {
        Path tempDir = Files.createTempDirectory("fileio-error-");
        XSSFWorkbook workbook = createWorkbook();
        LogProvider log = mock(LogProvider.class);
        FileIO fileIO = new FileIO(log);

        try {
            boolean result = fileIO.write(workbook, tempDir.resolve("missing-dir").resolve("broken").toString());

            Assert.assertFalse(result);
            verify(log).writeException(any(Exception.class));
        } finally {
            workbook.close();
            deleteRecursively(tempDir);
        }
    }

    private XSSFWorkbook createWorkbook() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TestSheet");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("value");
        return workbook;
    }

    private void deleteRecursively(Path path) throws Exception {
        if (path == null || !Files.exists(path)) {
            return;
        }

        Files.walk(path)
                .sorted((first, second) -> second.compareTo(first))
                .forEach(file -> {
                    try {
                        Files.deleteIfExists(file);
                    } catch (Exception ignored) {
                    }
                });
    }
}
