package coffeemunchingmonkeys.tools.lomimp.connector;

import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.bricks.SettingsSet;
import coffeemunchingmonkeys.tools.lomimp.core.Row;
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
import java.util.List;

public class ConnectLottoTest {
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
    public void connectParsesRowsFromLottoHtml() {
        String base = "http://localhost:" + port + "/resultatarkiv-";

        ConnectLotto connector = createConnector(base, 2025, 2024);

        int result = connector.connect(false);

        Assert.assertEquals(200, result);
        Assert.assertEquals(1, requestedPaths.size());
        Assert.assertEquals("/resultatarkiv-2025", requestedPaths.get(0));

        ArrayList<Row> rows = connector.getRows();
        Assert.assertEquals(3, rows.size());

        Row first = rows.get(0);
        Assert.assertEquals("2025-02-01", first.getDate());
        Assert.assertEquals(Integer.valueOf(2025), first.getYear());
        Assert.assertEquals(Integer.valueOf(5), first.getCw());
        Assert.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), first.getNumbers());

        Row second = rows.get(1);
        Assert.assertEquals("2025-02-08", second.getDate());
        Assert.assertEquals(Integer.valueOf(2025), second.getYear());
        Assert.assertEquals(Integer.valueOf(6), second.getCw());
        Assert.assertEquals(List.of(8, 9, 10, 11, 12, 13, 14), second.getNumbers());

        Row third = rows.get(2);
        Assert.assertEquals("2025-02-15", third.getDate());
        Assert.assertEquals(Integer.valueOf(2025), third.getYear());
        Assert.assertEquals(Integer.valueOf(7), third.getCw());
        Assert.assertEquals(List.of(15, 16, 17, 18, 19, 20, 21), third.getNumbers());
    }

    @Test
    public void connectFullFetchRequestsAllYearsInRange() {
        String base = "http://localhost:" + port + "/resultatarkiv-";

        ConnectLotto connector = createConnector(base, 2025, 2024);

        int result = connector.connect(true);

        Assert.assertEquals(200, result);
        Assert.assertEquals(List.of("/resultatarkiv-2025", "/resultatarkiv-2024"), requestedPaths);
        Assert.assertEquals(6, connector.getRows().size());
    }

    @Test
    public void connectReturnsHttpStatusCodeWhenResponseIsNotOk() {
        String base = "http://localhost:" + port + "/broken-";

        ConnectLotto connector = createConnector(base, 2025, 2024);

        int result = connector.connect(false);

        Assert.assertEquals(500, result);
        Assert.assertEquals(List.of("/broken-2025"), requestedPaths);
    }

    private ConnectLotto createConnector(String base, int currentYear, int initialYear) {
        SettingsSet settingsSet = new SettingsSet();
        setField(settingsSet, "initialYear", initialYear);
        setField(settingsSet, "url", base);

        ConnectLotto connector = new ConnectLotto(new LogProvider(), settingsSet);
        connector.currentYear = currentYear;
        return connector;
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

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        requestedPaths.add(path);

        if (path.equals("/broken-2025")) {
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
            return;
        }

        byte[] body = buildResponse().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private String buildResponse() {
        return "<html><body>\n"
            + "<div class=\"date\">mandag 01. februar 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball  medium ball\">01</li>\n"
            + "<li class=\"ball  medium ball\">02</li>\n"
            + "<li class=\"ball  medium ball\">03</li>\n"
            + "<li class=\"ball  medium ball\">04</li>\n"
            + "<li class=\"ball  medium ball\">05</li>\n"
            + "<li class=\"ball  medium ball\">06</li>\n"
            + "<li class=\"ball  medium ball\">07</li>\n"
            + "</ul>\n"
            + "<div class=\"date\">mandag 08. februar 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball  medium ball\">08</li>\n"
            + "<li class=\"ball  medium ball\">09</li>\n"
            + "<li class=\"ball  medium ball\">10</li>\n"
            + "<li class=\"ball  medium ball\">11</li>\n"
            + "<li class=\"ball  medium ball\">12</li>\n"
            + "<li class=\"ball  medium ball\">13</li>\n"
            + "<li class=\"ball  medium ball\">14</li>\n"
            + "</ul>\n"
            + "<div class=\"date\">mandag 15. februar 2025</div>\n"
            + "<ul>\n"
            + "<li class=\"ball  medium ball\">15</li>\n"
            + "<li class=\"ball  medium ball\">16</li>\n"
            + "<li class=\"ball  medium ball\">17</li>\n"
            + "<li class=\"ball  medium ball\">18</li>\n"
            + "<li class=\"ball  medium ball\">19</li>\n"
            + "<li class=\"ball  medium ball\">20</li>\n"
            + "<li class=\"ball  medium ball\">21</li>\n"
            + "</ul>\n"
            + "</body></html>";
    }
}