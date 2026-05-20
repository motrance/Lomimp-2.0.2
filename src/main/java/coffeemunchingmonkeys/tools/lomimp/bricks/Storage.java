package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.sql.*;
import java.util.ArrayList;

import coffeemunchingmonkeys.tools.lomimp.core.*;

/**
 *
 * Lomimp
 * bricks.Storage
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class Storage extends DBHelper {
    //Fields
    Row latestRow;

    public Storage(Settings settings, LogProvider log) {
        this.log = log;
        this.settings = settings;
        dbUser = this.settings.getDdbUser();
        dbPass = this.settings.getDbPass();
        dbHost = this.settings.getDbHost();
    }

    /** 
     * @return boolean
     */
    public boolean connect() {
        boolean state = true;

        try{
            conn = DriverManager.getConnection(this.dbHost, this.dbUser, this.dbPass);
        }
        catch(Exception e) {
            state = false;
            log.writeException(e);
        }

        return state;
    }

    /** 
     * @return boolean
     */
    public boolean disconnect() {
        boolean state = true;

        try{
            conn.close();
        }
        catch(Exception e) {
            state = false;
            log.writeException(e);
        }

        return state;
    }

    /** 
     * @param selectedGame
     * @return ArrayList<Row>
     */
    public ArrayList<Row> readAll(String selectedGame) {
        String selectedGameString = selectedGame.toLowerCase();
        String query = "SELECT * FROM " + selectedGameString + " ORDER BY DATE DESC";
        ArrayList<Row> rows = new ArrayList<Row>();

        try {
            Statement st = this.conn.createStatement();

            ResultSet rs = st.executeQuery(query);
            {
                while (rs.next()) {
                    Row row = new Row();
                    //rs.next();
                    row.setDate(rs.getString("date")); 
                    row.setYear(rs.getInt("year"));
                    row.setCw(rs.getInt("cw"));

                    int numbersSize = rs.getInt("numberssize");
                    int extrasSize = rs.getInt("extrassize");

                    for(int numbersCounter = 0; numbersCounter < numbersSize; numbersCounter++) {
                        //row.addNumber(rs.getString("num" + (numbersCounter + 1)));
                        row.addNumber(rs.getInt("num" + (numbersCounter + 1)));
                    }
                    for(int extrasCounter = 0; extrasCounter < extrasSize; extrasCounter++) {
                        //row.addExtra(rs.getString("extra" + (extrasCounter + 1)));
                        row.addExtra(rs.getInt("extra" + (extrasCounter + 1)));
                    }
                    rows.add(row);
                }
            }
            st.close();
        }
        catch (Exception e) {
            log.writeException(e);
        }

        return rows;
    }

    /**
     *
     * @param selectedGame  - "Viking", "EuroJackpot", "Lotto"
     * @param date - for example 2021-01-01
     *
     * @return ArrayList of rows from db lomimp in table lotto, viking or eurojackpot, from <Date> to now
     */
    public ArrayList<Row> readFromDate(String selectedGame, String date) {
        String selectedGameString = selectedGame.toLowerCase();
        String query = "SELECT * FROM " + selectedGameString + 
                    " WHERE STR_TO_DATE(date, '%Y-%m-%d') >= ? " +
                    "AND STR_TO_DATE(date, '%Y-%m-%d') <= CURDATE() " +
                    "ORDER BY date DESC, id DESC";
        ArrayList<Row> rows = new ArrayList<Row>();

        try (PreparedStatement pst = this.conn.prepareStatement(query)) {
            pst.setString(1, date);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Row row = new Row();
                row.setDate(rs.getString("date")); 
                row.setYear(rs.getInt("year"));
                row.setCw(rs.getInt("cw"));

                int numbersSize = rs.getInt("numberssize");
                int extrasSize = rs.getInt("extrassize");

                for(int numbersCounter = 0; numbersCounter < numbersSize; numbersCounter++) {
                    //row.addNumber(rs.getString("num" + (numbersCounter + 1)));
                    row.addNumber(rs.getInt("num" + (numbersCounter + 1)));
                }
                for(int extrasCounter = 0; extrasCounter < extrasSize; extrasCounter++) {
                    //row.addExtra(rs.getString("extra" + (extrasCounter + 1)));
                    row.addExtra(rs.getInt("extra" + (extrasCounter + 1)));
                }
                rows.add(row);
            }
        }
        catch (Exception e) {
            log.writeException(e);
        }

        return rows;
    }

    /**
     *
     * @param selectedGame - "Viking", "EuroJackpot", "Lotto"
     * @param rows - ArrayList of rows to be stored in db lomimp
     */
    public int write(String selectedGame, ArrayList<Row> rows) {
        String selectedGameString = selectedGame.toLowerCase();

        Integer linesStored = 0;

        if(!rows.isEmpty()) {
            for(int rowCounter = rows.size() - 1; rowCounter >= 0; rowCounter--) {
                Row row = rows.get(rowCounter);
                try {
                    if(!row.getNumbers().isEmpty() && row.getExtras() != null) {
                        latestRow = row;

                        ArrayList<Integer> numbers = row.getNumbers();
                        ArrayList<Integer> extras = row.getExtras();
                        int numbersSize = numbers.size();
                        int extrasSize = extras.size();

                        String query = "insert into " + selectedGameString + " (date, year, cw, numberssize, extrassize";
                        String values = ") values ( ?, ?, ?, ?, ?";
                        for(int numbersCounter = 0; numbersCounter < numbersSize; numbersCounter++) {
                            query += ", num" + (numbersCounter + 1);
                            values += ", ?";
                        }

                        for(int extrasCounter = 0; extrasCounter < extrasSize; extrasCounter++) {
                            query += ", extra" + (extrasCounter + 1);
                            values += ", ?";
                        }

                        query += values + ")";

                        int indexCounter = 1;
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(indexCounter++, row.getDate());
                        preparedStmt.setInt(indexCounter++, row.getYear());
                        preparedStmt.setInt(indexCounter++, row.getCw());
                        preparedStmt.setInt(indexCounter++, numbersSize);
                        preparedStmt.setInt(indexCounter++, extrasSize);

                        for(int numbersCounter = 0; numbersCounter < numbersSize; numbersCounter++) {
                            preparedStmt.setString(indexCounter++, "" + numbers.get(numbersCounter));
                        }
                        for(int extrasCounter = 0; extrasCounter < extrasSize; extrasCounter++) {
                            preparedStmt.setString(indexCounter++, "" + extras.get(extrasCounter));
                        }
                        linesStored = linesStored + preparedStmt.executeUpdate();
                    }
                }
                catch(Exception e) {
                    log.writeDebug("Error in latestRow: " + latestRow.getDate() );
                    log.writeException(e);
                }
            }
        }

        log.writeInfo("Lines stored in db: " + linesStored);

        return linesStored;
    }

    /**
     *
     * @param selectedGame - "Viking", "EuroJackpot", "Lotto"
     */
    public void cleanUpAfterTest(String selectedGame) {
        String selectedGameString = selectedGame.toLowerCase();
        int numRowsAffected = 0;
        try {
            Statement sqlStatement = conn.createStatement();
            numRowsAffected = sqlStatement.executeUpdate("delete from " + selectedGameString + " where date = '9999-99-99'");
        }
        catch(Exception e) {
            log.writeDebug("Error cleaning up after test");
            log.writeException(e);
        }

        log.writeDebug("Cleaned up after test. Rows deleted: " + numRowsAffected);
    }
}
