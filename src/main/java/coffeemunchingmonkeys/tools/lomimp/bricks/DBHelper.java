package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/***
 *
 * Lomimp
 * bricks.DBHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public abstract class DBHelper {
    //Fields
    Settings settings;
    LogProvider log;
    Connection conn;
    String dbUser = "";
    String dbPass = "";
    String dbHost = "";
    
    public void dbTable(String table) {
        try(
            Statement stmt = conn.createStatement();) {
                String sql = "CREATE TABLE " + table +
                " ( id INTEGER NOT NULL AUTO_INCREMENT, " +
                " date VARCHAR(45) NOT NULL, " +
                " year INTEGER NOT NULL, " +
                " cw INTEGER NOT NULL, " +
                " numberssize INTEGER NOT NULL, " +
                " extrassize INTEGER NOT NULL, " +
                " num1 INTEGER NOT NULL, " +
                " num2 INTEGER NOT NULL, " +
                " num3 INTEGER NOT NULL, " +
                " num4 INTEGER NOT NULL, " +
                " num5 INTEGER NOT NULL, " +
                " num6 INTEGER, " +
                " num7 INTEGER, " +
                " num8 INTEGER, " +
                " num9 INTEGER, " +
                " extra1 INTEGER, " +
                " extra2 INTEGER, " +
                " extra3 INTEGER, " +
                " extra4 INTEGER, " +
                " PRIMARY KEY ( id )," +
                " UNIQUE KEY id_UNIQUE ( id ))" +
                "ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8mb4";
                stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.write(e);
        }
    }

    public void dbTableTruncate(String table) {
        try(
            Statement stmt = conn.createStatement();) {
                String sql = "TRUNCATE TABLE " + table;
                stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.write(e);
        }
    }

    public void dbTableDrop(String table) {        
        try(
            Statement stmt = conn.createStatement();) {
                String sql = "DROP TABLE " + table;
                stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.write(e);
        }
    }
}
