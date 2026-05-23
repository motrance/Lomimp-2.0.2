package coffeemunchingmonkeys.tools.lomimp.bricks;

import coffeemunchingmonkeys.tools.lomimp.core.Row;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StorageTest {

    @Test
    public void readAllMapsRowsFromDatabase() throws Exception {
        Storage storage = createStorage();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        storage.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery("SELECT * FROM viking ORDER BY DATE DESC")).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("date")).thenReturn("2025-01-01", "2025-01-02");
        when(rs.getInt("year")).thenReturn(2025, 2025);
        when(rs.getInt("cw")).thenReturn(1, 2);
        when(rs.getInt("numberssize")).thenReturn(2, 3);
        when(rs.getInt("extrassize")).thenReturn(1, 0);
        when(rs.getInt("num1")).thenReturn(1, 4);
        when(rs.getInt("num2")).thenReturn(2, 5);
        when(rs.getInt("num3")).thenReturn(6);
        when(rs.getInt("extra1")).thenReturn(7);

        ArrayList<Row> rows = storage.readAll("Viking");

        Assert.assertEquals(2, rows.size());
        Assert.assertEquals("2025-01-01", rows.get(0).getDate());
        Assert.assertEquals(Integer.valueOf(2025), rows.get(0).getYear());
        Assert.assertEquals(Integer.valueOf(1), rows.get(0).getCw());
        Assert.assertEquals(List.of(1, 2), rows.get(0).getNumbers());
        Assert.assertEquals(List.of(7), rows.get(0).getExtras());

        Assert.assertEquals("2025-01-02", rows.get(1).getDate());
        Assert.assertEquals(Integer.valueOf(2025), rows.get(1).getYear());
        Assert.assertEquals(Integer.valueOf(2), rows.get(1).getCw());
        Assert.assertEquals(List.of(4, 5, 6), rows.get(1).getNumbers());
        Assert.assertTrue(rows.get(1).getExtras().isEmpty());
        verify(stmt).executeQuery("SELECT * FROM viking ORDER BY DATE DESC");
    }

    @Test
    public void readFromDateUsesPreparedStatementAndMapsRows() throws Exception {
        Storage storage = createStorage();
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        storage.conn = conn;

        when(conn.prepareStatement("SELECT * FROM viking WHERE STR_TO_DATE(date, '%Y-%m-%d') >= ? AND STR_TO_DATE(date, '%Y-%m-%d') <= CURDATE() ORDER BY date DESC, id DESC")).thenReturn(pst);
        when(pst.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("date")).thenReturn("2025-01-01");
        when(rs.getInt("year")).thenReturn(2025);
        when(rs.getInt("cw")).thenReturn(1);
        when(rs.getInt("numberssize")).thenReturn(2);
        when(rs.getInt("extrassize")).thenReturn(1);
        when(rs.getInt("num1")).thenReturn(11);
        when(rs.getInt("num2")).thenReturn(12);
        when(rs.getInt("extra1")).thenReturn(13);

        ArrayList<Row> rows = storage.readFromDate("Viking", "2025-01-01");

        Assert.assertEquals(1, rows.size());
        Assert.assertEquals("2025-01-01", rows.get(0).getDate());
        Assert.assertEquals(List.of(11, 12), rows.get(0).getNumbers());
        Assert.assertEquals(List.of(13), rows.get(0).getExtras());
        verify(pst).setString(1, "2025-01-01");
    }

    @Test
    public void writeInsertsRowsAndUpdatesLatestRow() throws Exception {
        Storage storage = createStorage();
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        storage.conn = conn;

        when(conn.prepareStatement(anyString())).thenReturn(pst);
        when(pst.executeUpdate()).thenReturn(1);

        Row row = new Row();
        row.setDate("2025-01-01");
        row.setYear(2025);
        row.setCw(1);
        row.addNumber(1);
        row.addNumber(2);
        row.addExtra(3);
        row.addExtra(4);

        ArrayList<Row> rows = new ArrayList<>();
        rows.add(row);

        int stored = storage.write("Viking", rows);

        Assert.assertEquals(1, stored);
        Assert.assertSame(row, storage.latestRow);
        verify(pst).setString(1, "2025-01-01");
        verify(pst).setInt(2, 2025);
        verify(pst).setInt(3, 1);
        verify(pst).setInt(4, 2);
        verify(pst).setInt(5, 2);
        verify(pst).setString(6, "1");
        verify(pst).setString(7, "2");
        verify(pst).setString(8, "3");
        verify(pst).setString(9, "4");
        verify(pst).executeUpdate();
    }

    @Test
    public void cleanUpAfterTestDeletesSentinelRows() throws Exception {
        Storage storage = createStorage();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        storage.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);

        storage.cleanUpAfterTest("Viking");

        verify(stmt).executeUpdate("delete from viking where date = '9999-99-99'");
    }

    @Test
    public void disconnectClosesConnection() throws Exception {
        Storage storage = createStorage();
        Connection conn = mock(Connection.class);
        storage.conn = conn;

        Assert.assertTrue(storage.disconnect());
        verify(conn).close();
    }

    private Storage createStorage() {
        Settings settings = new Settings();
        settings.dbUser = "user";
        settings.dbPass = "pass";
        settings.dbHost = "jdbc:mysql://localhost:3306/lomimp";
        return new Storage(settings, new LogProvider());
    }
}
