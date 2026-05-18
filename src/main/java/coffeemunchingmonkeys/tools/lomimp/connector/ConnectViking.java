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
import java.util.Locale;

/*
 * Lomimp
 * connector.ConnectViking
 * 
 * @version 2.0.2
 * @since 2026-05-15
*/
public class ConnectViking extends ConnectorCore implements IConnector {
    public ConnectViking(LogProvider log, SettingsSet settingsSet) {
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

                        if (currentLine.contains("<div class=\"date\">")) {
                            currentLine = currentLine.replaceAll("\t", "");
                            int startindex = currentLine.indexOf("<div class=\"date\">")  + 18;
                            int endIndex = currentLine.indexOf("</div>");

                            currentLine = currentLine.substring(startindex, endIndex).trim();

                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd. MMMM y", Locale.forLanguageTag("da-DK"));
                                Date parsedDate = simpleDateFormat.parse(currentLine);
                                SimpleDateFormat dateFormatNormal = new SimpleDateFormat("yyyy.MM.dd");
                                String dateStringModified = dateFormatNormal.format(parsedDate);
                                dateStringModified = dateStringModified.replaceAll("\\.", "-");

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(parsedDate);

                                row.setDate(dateStringModified);
                                row.setYear(calendar.get(Calendar.YEAR));
                                row.setCw(calendar.get(Calendar.WEEK_OF_YEAR));

                                while(currentLine != null && !currentLine.contains("</ul>")) {
                                    if (!row.getDate().isEmpty() && currentLine.trim().contains("<li class=\"ball viking medium ball\">")) {
                                        currentLine = currentLine.replaceAll("\t", "");
                                        currentLine = currentLine.replace("<li class=\"ball viking medium ball\">", "");
                                        currentLine = currentLine.replace("</li>", "");
                                        row.addNumber(currentLine);
                                    }
                                    if (!row.getDate().isEmpty() && (currentLine.trim().contains("<li class=\"ball viking medium viking-ball\">")) || (currentLine.trim().contains("<li class=\"ball viking medium tillaegstal\">"))) {
                                        currentLine = currentLine.replaceAll("\t", "");
                                        currentLine = currentLine.replace("<li class=\"ball viking medium viking-ball\">", "");
                                        currentLine = currentLine.replace("<li class=\"ball viking medium tillaegstal\">", "");
                                        currentLine = currentLine.replace("</li>", "");
                                        row.addExtra(currentLine);
                                    }
                                    currentLine = br.readLine();
                                }
                            } catch (Exception ignore) {
                                running = false;
                            }
                        }
                    }
                }

                rows.addAll(rowsYear);
            }
        } catch (Exception e) {
            log.write(e);

            if(code == 200) {
                code = -1;
            }
        }
        return code;
    }
}
