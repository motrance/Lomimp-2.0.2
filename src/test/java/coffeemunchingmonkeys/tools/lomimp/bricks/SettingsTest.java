package coffeemunchingmonkeys.tools.lomimp.bricks;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SettingsTest {

    private final Path settingsPath = Paths.get("resources/Settings.json");

    @After
    public void cleanupSettingsFile() throws Exception {
        Files.deleteIfExists(settingsPath);
    }

    @Test
    public void constructorLoadsJsonSettingsIntoAccessors() throws Exception {
        writeSettingsJson();
        LogProvider log = mock(LogProvider.class);

        Settings settings = new Settings(log);

        Assert.assertEquals(Integer.valueOf(2), settings.getLogLevel());
        Assert.assertEquals("db-user", settings.getDdbUser());
        Assert.assertEquals("db-pass", settings.getDbPass());
        Assert.assertEquals("jdbc:mysql://localhost:3306/lomimp", settings.getDbHost());

        SettingsSet viking = settings.getSettings("Viking");
        Assert.assertNotNull(viking);
        Assert.assertEquals(2025, viking.getInitialYear());
        Assert.assertEquals("https://example.test/viking", viking.getUrl());
        Assert.assertEquals(6, viking.getNumberSize());
        Assert.assertEquals(48, viking.getNumberMax());
        Assert.assertEquals(2, viking.getExtrasize());
        Assert.assertEquals(10, viking.getExtraMax());

        SettingsSet euro = settings.getSettings("EuroJackpot");
        Assert.assertNotNull(euro);
        Assert.assertEquals(2024, euro.getInitialYear());
        Assert.assertEquals("https://example.test/euro", euro.getUrl());
        Assert.assertEquals(5, euro.getNumberSize());
        Assert.assertEquals(50, euro.getNumberMax());
        Assert.assertEquals(2, euro.getExtrasize());
        Assert.assertEquals(10, euro.getExtraMax());

        SettingsSet lotto = settings.getSettings("Lotto");
        Assert.assertNotNull(lotto);
        Assert.assertEquals(2023, lotto.getInitialYear());
        Assert.assertEquals("https://example.test/lotto", lotto.getUrl());
        Assert.assertEquals(7, lotto.getNumberSize());
        Assert.assertEquals(36, lotto.getNumberMax());
        Assert.assertEquals(0, lotto.getExtrasize());
        Assert.assertEquals(0, lotto.getExtraMax());

        verify(log, org.mockito.Mockito.never()).writeError(org.mockito.ArgumentMatchers.anyString());
        verify(log, org.mockito.Mockito.never()).writeException(org.mockito.ArgumentMatchers.any(Exception.class));
    }

    @Test
    public void getSettingsUnknownGameLogsErrorAndReturnsNull() throws Exception {
        writeSettingsJson();
        LogProvider log = mock(LogProvider.class);

        Settings settings = new Settings(log);

        Assert.assertNull(settings.getSettings("Unknown"));
        verify(log).writeError("Unknown game: Unknown");
    }

    private void writeSettingsJson() throws Exception {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("LOGLEVEL", 2);

        Map<String, Object> db = new LinkedHashMap<>();
        db.put("DBUSER", "db-user");
        db.put("DBPASS", "db-pass");
        db.put("DBHOST", "jdbc:mysql://localhost:3306/lomimp");
        root.put("DB", db);

        Map<String, Object> games = new LinkedHashMap<>();
        games.put("VIKING", buildGameSettings(2025, "https://example.test/viking", 6, 48, 2, 10));
        games.put("EUROJACKPOT", buildGameSettings(2024, "https://example.test/euro", 5, 50, 2, 10));
        games.put("LOTTO", buildGameSettings(2023, "https://example.test/lotto", 7, 36, 0, 0));
        root.put("GAMES", games);

        ObjectMapper mapper = new ObjectMapper();
        Files.writeString(settingsPath, mapper.writeValueAsString(root), StandardCharsets.UTF_8);
    }

    private Map<String, Object> buildGameSettings(int initialYear, String url, int numberSize, int numberMax, int extraSize, int extraMax) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("initialYear", initialYear);
        settings.put("url", url);
        settings.put("numberSize", numberSize);
        settings.put("numberMax", numberMax);
        settings.put("extrasize", extraSize);
        settings.put("extraMax", extraMax);
        return settings;
    }
}
