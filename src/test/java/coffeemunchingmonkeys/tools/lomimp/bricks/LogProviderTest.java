package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JTextArea;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class LogProviderTest {

    private final Path logPath = Paths.get("logs/log.txt");

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

        String content = Files.readString(logPath, StandardCharsets.UTF_8);
        Assert.assertFalse(content.contains(marker));
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
        Assert.assertTrue(content.contains("Info"));
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
}
