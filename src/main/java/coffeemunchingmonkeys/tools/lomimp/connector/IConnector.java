package coffeemunchingmonkeys.tools.lomimp.connector;

import coffeemunchingmonkeys.tools.lomimp.core.Row;
import java.util.ArrayList;

/**
 *
 * Lomimp
 * connector.IConnector
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public interface IConnector {
    public int connect(Boolean full);

    public ArrayList<Row> getRows();
}
