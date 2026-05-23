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

public class ConnectVikingTest {
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
    public void connectParsesRowsFromVikingHtml() {
        String base = "http://localhost:" + port + "/resultatarkiv-";

        ConnectViking connector = createConnector(base, 2025, 2024);

        int result = connector.connect(false);

        Assert.assertEquals(200, result);
        Assert.assertEquals(1, requestedPaths.size());
        Assert.assertEquals("/resultatarkiv-2025", requestedPaths.get(0));

        ArrayList<Row> rows = connector.getRows();
        Assert.assertEquals(3, rows.size());

        Row first = rows.get(0);
        Assert.assertEquals("2025-05-24", first.getDate());
        Assert.assertEquals(Integer.valueOf(2025), first.getYear());
        Assert.assertEquals(Integer.valueOf(21), first.getCw());
        Assert.assertEquals(List.of(1, 2, 3, 4, 5, 6), first.getNumbers());
        Assert.assertEquals(List.of(7, 8), first.getExtras());

        Row second = rows.get(1);
        Assert.assertEquals("2025-05-23", second.getDate());
        Assert.assertEquals(Integer.valueOf(2025), second.getYear());
        Assert.assertEquals(Integer.valueOf(21), second.getCw());
        Assert.assertEquals(List.of(9, 10, 11, 12, 13, 14), second.getNumbers());
        Assert.assertEquals(List.of(15, 16), second.getExtras());

        Row third = rows.get(2);
        Assert.assertEquals("2025-05-22", third.getDate());
        Assert.assertEquals(Integer.valueOf(2025), third.getYear());
        Assert.assertEquals(Integer.valueOf(21), third.getCw());
        Assert.assertEquals(List.of(17, 18, 19, 20, 21, 22), third.getNumbers());
        Assert.assertEquals(List.of(23, 24), third.getExtras());
    }

    @Test
    public void connectFullFetchRequestsAllYearsInRange() {
        String base = "http://localhost:" + port + "/resultatarkiv-";

        ConnectViking connector = createConnector(base, 2025, 2024);

        int result = connector.connect(true);

        Assert.assertEquals(200, result);
        Assert.assertEquals(List.of("/resultatarkiv-2025", "/resultatarkiv-2024"), requestedPaths);
        Assert.assertEquals(6, connector.getRows().size());
    }

    @Test
    public void connectReturnsHttpStatusCodeWhenResponseIsNotOk() {
        String base = "http://localhost:" + port + "/broken-";

        ConnectViking connector = createConnector(base, 2025, 2024);

        int result = connector.connect(false);

        Assert.assertEquals(500, result);
        Assert.assertEquals(List.of("/broken-2025"), requestedPaths);
    }

    private ConnectViking createConnector(String base, int currentYear, int initialYear) {
        SettingsSet settingsSet = new SettingsSet();
        setField(settingsSet, "initialYear", initialYear);
        setField(settingsSet, "url", base);

        ConnectViking connector = new ConnectViking(new LogProvider(), settingsSet);
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
