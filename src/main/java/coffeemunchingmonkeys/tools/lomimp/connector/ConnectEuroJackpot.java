package coffeemunchingmonkeys.tools.lomimp.connector;

import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.bricks.SettingsSet;
import coffeemunchingmonkeys.tools.lomimp.core.Row;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Lomimp
 * connector.ConnectEuroJackpot
 * 
 * @version 2.0.2
 * @since 2026-05-15
*/
public class ConnectEuroJackpot extends ConnectorCore implements IConnector {
    public ConnectEuroJackpot(LogProvider log, SettingsSet settingsSet) {
        super(log);

        initialYear = settingsSet.getInitialYear();
        baseString = settingsSet.getUrl();
    }

    /** 
     * @param full
     * @return int
     */
    //connect to web page
    // full: true for all winning numbers back to first year of the game, false for current year only.
    public int connect(Boolean full) {
        Integer code = 200;

        log.writeDebug("Connector: Connecting to webservice");
        log.writeDebug("Connector: Full fetch from webservice: " + full);

        try {
            for (int counter = currentYear; counter >= (full ? initialYear : currentYear); counter--) {
                url = baseString + counter;
                ArrayList<Row> rowsYear = new ArrayList<Row>();

                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);

                int tmpCode = response.getStatusLine().getStatusCode();

                if (tmpCode != 200) {
                    code = tmpCode;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                Boolean running = true;

                Row row = new Row();

                while (running) {
                    String currentLine = br.readLine();
                    if (currentLine == null) {
                        running = false;
                    } else {
                        if (!row.getDate().isEmpty() && row.getNumbersSize() > 0) {
                            rowsYear.add((Row) row.clone());
                            row = new Row();
                        }

                        if (currentLine.contains("<a href=\"/da/resultater/")) {
                            currentLine = currentLine.trim();
                            currentLine = currentLine.substring(28, 38);

                            row.setDate(currentLine);

                            try {
                                Date parsedDate = null;

                                if (currentLine.contains("-") && currentLine.length() >= 10) {
                                    String[] dateLineArray = currentLine.split("-");

                                    if (dateLineArray != null && dateLineArray.length == 3) {
                                        String modifiedDateLineString = dateLineArray[2] + "-" + dateLineArray[1] + "-" + dateLineArray[0];

                                        row.setDate(modifiedDateLineString);

                                        parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDateLineString);
                                    }
                                }

                                if (parsedDate == null) {
                                    parsedDate = new SimpleDateFormat("dd-MM-yyyy").parse(currentLine);
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(parsedDate);

                                row.setYear(calendar.get(Calendar.YEAR));

                                row.setCw(calendar.get(Calendar.WEEK_OF_YEAR));

                                while(!currentLine.contains("</ul>")) {
                                    if(currentLine != null) {
                                        if (!row.getDate().isEmpty() && currentLine.trim().contains("<li class=\"ball\"><span>")) {
                                            currentLine = currentLine.trim();
                                            currentLine = currentLine.substring(23, 25).replace("<", "");

                                            row.addNumber(currentLine);
                                        }

                                        if (!row.getDate().isEmpty() && currentLine.contains("<li class=\"euro\"><span>")) {
                                            currentLine = currentLine.trim();
                                            currentLine = currentLine.substring(23, 25).replace("<", "");

                                            row.addExtra(currentLine);
                                        }
                                    }
                                    currentLine = br.readLine();
                                }
                            } catch (Exception ignore) {
                            }
                        }
                    }
                }

                rows.addAll(rowsYear);
            }
        } catch (Exception e) {
            log.writeException(e);

            if(code == 200) {
                code = -1;
            }
        }
        return code;
    }
}
