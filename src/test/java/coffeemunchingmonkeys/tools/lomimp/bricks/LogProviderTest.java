package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.JTextArea;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LogProviderTest {

    private final Path logPath = Paths.get("logs/log.txt");

    @Before
    public void cleanLogs() throws Exception {
        if (Files.exists(logPath)) {
            Files.delete(logPath);
        }
        for (int i = 1; i <= 20; i++) {
            Path rotated = Paths.get("logs/log." + i + ".txt");
            if (Files.exists(rotated)) {
                Files.delete(rotated);
            }
        }
        Files.createDirectories(logPath.getParent());
    }

    @After
    public void resetLogProviderState() {
        LogProvider provider = new LogProvider();
        provider.setLogLevel(LogLevel.Debug);
        provider.setPrintToConsole(true);
    }

    @Test
    public void writeInfoLogsToFileWhenLevelAllows() throws Exception {
        String marker = "info-log-marker-" + System.nanoTime();
        LogProvider provider = new LogProvider();
        provider.setLogLevel(LogLevel.Info);
        provider.setPrintToConsole(false);

        provider.writeInfo(marker);

        String content = Files.readString(logPath, StandardCharsets.UTF_8);
        Assert.assertTrue(content.contains(marker));
        Assert.assertTrue(content.contains("Info"));
    }

    @Test
    public void writeInfoDoesNotLogWhenLevelIsNone() throws Exception {
        String marker = "info-suppressed-marker-" + System.nanoTime();
        LogProvider provider = new LogProvider();
        provider.setLogLevel(LogLevel.None);
        provider.setPrintToConsole(false);

        provider.writeInfo(marker);

        if (Files.exists(logPath)) {
            String content = Files.readString(logPath, StandardCharsets.UTF_8);
            Assert.assertFalse(content.contains(marker));
        }
    }

    @Test
    public void setLogLevelIntegerUpdatesCurrentLevel() throws Exception {
        String marker = "error-level-marker-" + System.nanoTime();
        LogProvider provider = new LogProvider();
        provider.setLogLevel(2);
        provider.setPrintToConsole(false);

        provider.writeError(marker);

        String content = Files.readString(logPath, StandardCharsets.UTF_8);
        Assert.assertTrue(content.contains(marker));
        Assert.assertTrue(content.contains("Error"));
    }

    @Test
    public void setLogLevelStringUpdatesCurrentLevel() throws Exception {
        String marker = "debug-level-marker-" + System.nanoTime();
        LogProvider provider = new LogProvider();
        provider.setLogLevel("Debug");
        provider.setPrintToConsole(false);

        provider.writeDebug(marker);

        String content = Files.readString(logPath, StandardCharsets.UTF_8);
        Assert.assertTrue(content.contains(marker));
        Assert.assertTrue(content.contains("Debug"));
    }

    @Test
    public void writeInfoAppendsMessageToGui() {
        JTextArea textArea = new JTextArea();
        LogProvider provider = new LogProvider(textArea);
        provider.setLogLevel(LogLevel.Info);
        provider.setPrintToConsole(false);

        provider.writeInfo("gui-message");

        Assert.assertTrue(textArea.getText().contains("gui-message"));
    }

    @Test
    public void rotateLogFilesWhenSizeExceedsLimit() throws Exception {
        LogProvider provider = new LogProvider();
        provider.setLogLevel(LogLevel.Debug);
        provider.setPrintToConsole(false);
        provider.setLogPath("logs");
        provider.setMaxBackupFiles(5);

        char[] payload = new char[6000];
        Arrays.fill(payload, 'x');
        String message = new String(payload);

        for (int i = 0; i < 1200; i++) {
            provider.writeInfo("entry-" + i + " " + message);
        }

        Assert.assertTrue(Files.exists(Paths.get("logs/log.1.txt")));
        Assert.assertTrue(Files.exists(logPath));

        String content = Files.readString(logPath, StandardCharsets.UTF_8);
        Assert.assertTrue(content.contains("entry-1199"));
    }
}
