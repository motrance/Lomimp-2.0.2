package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DBHelperTest {

    @Test
    public void dbTableCreatesExpectedSqlStatement() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);

        helper.dbTable("test_table");

        verify(stmt).executeUpdate("CREATE TABLE test_table ( id INTEGER NOT NULL AUTO_INCREMENT,  date VARCHAR(45) NOT NULL,  year INTEGER NOT NULL,  cw INTEGER NOT NULL,  numberssize INTEGER NOT NULL,  extrassize INTEGER NOT NULL,  num1 INTEGER NOT NULL,  num2 INTEGER NOT NULL,  num3 INTEGER NOT NULL,  num4 INTEGER NOT NULL,  num5 INTEGER NOT NULL,  num6 INTEGER,  num7 INTEGER,  num8 INTEGER,  num9 INTEGER,  extra1 INTEGER,  extra2 INTEGER,  extra3 INTEGER,  extra4 INTEGER,  PRIMARY KEY ( id ), UNIQUE KEY id_UNIQUE ( id ))ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8mb4");
    }

    @Test
    public void dbTableTruncateCreatesExpectedSqlStatement() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);

        helper.dbTableTruncate("test_table");

        verify(stmt).executeUpdate("TRUNCATE TABLE test_table");
    }

    @Test
    public void dbTableDropCreatesExpectedSqlStatement() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);

        helper.dbTableDrop("test_table");

        verify(stmt).executeUpdate("DROP TABLE test_table");
    }

    @Test
    public void dbTableLogsExceptionWhenSqlFails() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);
        SQLException failure = new SQLException("boom");
        when(stmt.executeUpdate(org.mockito.ArgumentMatchers.anyString())).thenThrow(failure);

        helper.dbTable("test_table");

        verify(helper.log).writeException(failure);
    }

    @Test
    public void dbTableTruncateLogsExceptionWhenSqlFails() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);
        SQLException failure = new SQLException("boom");
        when(stmt.executeUpdate(org.mockito.ArgumentMatchers.anyString())).thenThrow(failure);

        helper.dbTableTruncate("test_table");

        verify(helper.log).writeException(failure);
    }

    @Test
    public void dbTableDropLogsExceptionWhenSqlFails() throws Exception {
        TestDBHelper helper = createHelper();
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        helper.conn = conn;

        when(conn.createStatement()).thenReturn(stmt);
        SQLException failure = new SQLException("boom");
        when(stmt.executeUpdate(org.mockito.ArgumentMatchers.anyString())).thenThrow(failure);

        helper.dbTableDrop("test_table");

        verify(helper.log).writeException(failure);
    }

    private TestDBHelper createHelper() {
        TestDBHelper helper = new TestDBHelper();
        helper.log = mock(LogProvider.class);
        return helper;
    }

    private static class TestDBHelper extends DBHelper {
    }
}
