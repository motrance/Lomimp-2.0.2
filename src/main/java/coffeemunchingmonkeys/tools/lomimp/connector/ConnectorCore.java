package coffeemunchingmonkeys.tools.lomimp.connector;

import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.core.*;
import java.util.ArrayList;
import java.util.Calendar;


/*
 * Lomimp
 * connector.ConnectorCore
 * 
 * @version 2.0.2
 * @since 2026-05-15
*/
public abstract class ConnectorCore {
    //Fields
    LogProvider log;
    Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);

    String baseString = null;
    Integer initialYear = 0;
    String url;
    ArrayList<Row> rows = new ArrayList<Row>();

    public ConnectorCore(LogProvider log) {
        this.log = log;
    }

    /**
     *
     * return rows fetched from webpage
     *
     * @return rows
     */
    public ArrayList<Row> getRows() {
        return this.rows;
    }
}