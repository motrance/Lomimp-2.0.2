package coffeemunchingmonkeys.tools.lomimp.core;

import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.bricks.Settings;
import coffeemunchingmonkeys.tools.lomimp.bricks.SettingsSet;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameControlTest {
    private HttpServer server;
    private int port;
    private final List<String> requestedPaths = new ArrayList<>();

    @Before
    public void setUp() throws IOException {
        requestedPaths.clear();
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
        server.createContext("/", this::handleRequest);
        server.start();
    }

    @After
    public void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void rowsDeltaCheckReturnsOnlyMissingRows() {
        GameControl gameControl = createGameControl();

        Row existing = createRow("2025-01-01", 2025, 1, List.of(1, 2, 3), List.of(4, 5));
        Row latestNew = createRow("2025-01-08", 2025, 2, List.of(6, 7, 8), List.of(9, 10));

        ArrayList<Row> rows = new ArrayList<>();
        rows.add(existing);

        ArrayList<Row> latestRows = new ArrayList<>();
        latestRows.add(existing);
        latestRows.add(latestNew);

        ArrayList<Row> deltaRows = gameControl.rowsDeltaCheck(rows, latestRows);

        Assert.assertEquals(1, deltaRows.size());
        Assert.assertEquals("2025-01-08", deltaRows.get(0).getDate());
        Assert.assertEquals(Integer.valueOf(2025), deltaRows.get(0).getYear());
        Assert.assertEquals(Integer.valueOf(2), deltaRows.get(0).getCw());
    }

    @Test
    public void fetchRowsFromWebReturnsRowsFromConnector() {
        GameControl gameControl = createGameControl();

        ArrayList<Row> rows = gameControl.fetchRowsFromWeb("Viking", false);

        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(1, requestedPaths.size());
        Assert.assertEquals("/resultatarkiv-" + Calendar.getInstance().get(Calendar.YEAR), requestedPaths.get(0));

        Row first = rows.get(0);
        Assert.assertEquals("2025-05-24", first.getDate());
        Assert.assertEquals(Integer.valueOf(2025), first.getYear());
        Assert.assertEquals(Integer.valueOf(21), first.getCw());
        Assert.assertEquals(List.of(1, 2, 3, 4, 5, 6), first.getNumbers());
        Assert.assertEquals(List.of(7, 8), first.getExtras());
    }

    @Test
    public void fetchRowsFromWebFullFetchRequestsAllYearsInRange() {
        GameControl gameControl = createGameControl();

        ArrayList<Row> rows = gameControl.fetchRowsFromWeb("Viking", true);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int expectedRequests = currentYear - 2024 + 1;
        Assert.assertEquals(expectedRequests, requestedPaths.size());

        ArrayList<String> expectedPaths = new ArrayList<>();
        for (int year = currentYear; year >= 2024; year--) {
            expectedPaths.add("/resultatarkiv-" + year);
        }
        Assert.assertEquals(expectedPaths, requestedPaths);
        Assert.assertEquals(expectedRequests * 3, rows.size());
    }

    private GameControl createGameControl() {
        Settings settings = new Settings();
        setField(settings, "vikingSettings", createSettingsSet());
        LogProvider log = new LogProvider();
        log.setPrintToConsole(false);
        return new GameControl(log, settings);
    }

    private SettingsSet createSettingsSet() {
        SettingsSet settingsSet = new SettingsSet();
        setField(settingsSet, "initialYear", 2024);
        setField(settingsSet, "url", "http://localhost:" + port + "/resultatarkiv-");
        return settingsSet;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        requestedPaths.add(path);

        byte[] body = buildResponse().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private String buildResponse() {
        return "<html><body>\n"
            + "<div class=\"date\">mandag 24. maj 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball viking medium ball\">1</li>\n"
            + "<li class=\"ball viking medium ball\">2</li>\n"
            + "<li class=\"ball viking medium ball\">3</li>\n"
            + "<li class=\"ball viking medium ball\">4</li>\n"
            + "<li class=\"ball viking medium ball\">5</li>\n"
            + "<li class=\"ball viking medium ball\">6</li>\n"
            + "<li class=\"ball viking medium viking-ball\">7</li>\n"
            + "<li class=\"ball viking medium tillaegstal\">8</li>\n"
            + "</ul>\n"
            + "<div class=\"date\">mandag 23. maj 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball viking medium ball\">9</li>\n"
            + "<li class=\"ball viking medium ball\">10</li>\n"
            + "<li class=\"ball viking medium ball\">11</li>\n"
            + "<li class=\"ball viking medium ball\">12</li>\n"
            + "<li class=\"ball viking medium ball\">13</li>\n"
            + "<li class=\"ball viking medium ball\">14</li>\n"
            + "<li class=\"ball viking medium viking-ball\">15</li>\n"
            + "<li class=\"ball viking medium tillaegstal\">16</li>\n"
            + "</ul>\n"
            + "<div class=\"date\">mandag 22. maj 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball viking medium ball\">17</li>\n"
            + "<li class=\"ball viking medium ball\">18</li>\n"
            + "<li class=\"ball viking medium ball\">19</li>\n"
            + "<li class=\"ball viking medium ball\">20</li>\n"
            + "<li class=\"ball viking medium ball\">21</li>\n"
            + "<li class=\"ball viking medium ball\">22</li>\n"
            + "<li class=\"ball viking medium viking-ball\">23</li>\n"
            + "<li class=\"ball viking medium tillaegstal\">24</li>\n"
            + "</ul>\n"
            + "</body></html>";
    }
}
