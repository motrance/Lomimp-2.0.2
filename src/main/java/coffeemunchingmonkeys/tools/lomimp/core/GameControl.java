package coffeemunchingmonkeys.tools.lomimp.core;

import coffeemunchingmonkeys.tools.lomimp.bricks.*;
import coffeemunchingmonkeys.tools.lomimp.connector.ConnectEuroJackpot;
import coffeemunchingmonkeys.tools.lomimp.connector.ConnectLotto;
import coffeemunchingmonkeys.tools.lomimp.connector.ConnectViking;
import coffeemunchingmonkeys.tools.lomimp.connector.IConnector;
import coffeemunchingmonkeys.tools.lomimp.stats.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * Lomimp
 * core.Fetcher
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class GameControl {
    //Fields
    LogProvider log;
    Settings settings;

    public GameControl(LogProvider log, Settings settings) {
        this.log = log;
        this.settings = settings;
    }

    /** 
     * @param game
     * @param stats
     * @param fetchNumbersFromWeb
     * @return boolean
     */
    public boolean fetch(Game game, Boolean stats, Boolean fetchNumbersFromWeb) {
        boolean success = true;
        Storage storage = new Storage(this.settings, this.log);
        if(storage.connect()) {
            log.writeInfo("Connected to db");

            //Create Excel Sheet
            XSSFWorkbook workBook = new XSSFWorkbook();
            DocManager docManager = new DocManager(workBook, this.log, settings);
            FileIO fileIO = new FileIO(log);
            String sheetName = "";
            ArrayList<Row> rows = new ArrayList<>();
            StatsManager statsManager = null;
            Map<String, List<StatsDataHolder>> statsDataHolderMap = new HashMap<>();
            if(game == Game.ALL3) {
                sheetName = "Games";
                for(Game selectedGame :Game.values()) {
                   if(selectedGame == Game.ALL3) {
                    } else {
                        log.writeInfo("Processing: " + selectedGame.displayName());
                        ArrayList<Row> fetchedRows = fetchNumbers(selectedGame.displayName().toString(), fetchNumbersFromWeb, storage);
                        rows = fetchedRows;
                        docManager.createNumbersSheet(rows, selectedGame.displayName());
                        if(stats) {
                            statsManager = new StatsManager(selectedGame.displayName(), storage, workBook, this.log, this.settings);
                            List<StatsDataHolder> tempStatsDataHolders = statsManager.createStatsOnPairs();
                            statsDataHolderMap.put(selectedGame.displayName(), tempStatsDataHolders);
                        }
                    }
                }
            } else {
                log.writeInfo("Processing: " + game.displayName());
                rows = fetchNumbers(game.displayName(), fetchNumbersFromWeb, storage);
                docManager.createNumbersSheet(rows, game.displayName());

                if(stats) {
                    statsManager = new StatsManager(game.displayName(), storage, workBook, this.log, this.settings);
                    List<StatsDataHolder> tempStatsDataHolders = statsManager.createStatsOnPairs();
                    statsDataHolderMap.put(game.displayName(), tempStatsDataHolders);
                }
                
                sheetName = game.displayName();
            }

            if(success) {
                if(stats && statsManager != null) {
                    docManager.createStatNumberSheets(statsDataHolderMap);

                    statsManager.createStaticStats();
                    ArrayList<BigInteger> vikingPairsStatic = statsManager.getVikingPairsStatic();
                    ArrayList<BigInteger> euroPairsStatic = statsManager.getEuroPairsStatic();
                    ArrayList<BigInteger> lottoPairsStatic = statsManager.getLottoPairsStatic();

                    docManager.createInfoSheet(vikingPairsStatic, euroPairsStatic, lottoPairsStatic);
                }

                try {
                    if(fileIO.write(workBook, sheetName)) {
                        log.writeInfo("Excel sheet created");
                    } else {
                        log.writeInfo("Unable to create Excel sheet");
                        success = false;
                    }
                } catch (Exception e) {
                    log.writeException(e);
                    success = false;
                }
                if (fileIO.close(workBook)) {
                    log.writeDebug("Excel sheet closed");
                } else {
                    log.writeInfo("Unable to close Excel sheet");
                    success = false;
                };
                log.writeInfo("Process completed successfully");
            } else {
                log.writeInfo("Process ended with errors");
            }
        } else {
            log.writeInfo("Unable to connect to db");
            success = false;
        }
        if(success && storage.disconnect()) {
                log.writeDebug("Disconnected from db");
        } else {
            log.writeDebug("Unable to disconnect from db");
        }
        return success;
    }

    /** 
     * @param selectedGame
     * @param fetchNumbersFromWeb
     * @param storage
     * @param docManager
     * @return ArrayList<Row>
     */
    private ArrayList<Row> fetchNumbers(String selectedGame, Boolean fetchNumbersFromWeb, Storage storage) {
        //Fetch numbers from local db
        ArrayList<Row> rows = storage.readAll(selectedGame);

        log.writeInfo("Rows fetched from DB: " + rows.size());

        //Fetch numbers from web
        if(fetchNumbersFromWeb) {
            Boolean fullFetch = rows != null && rows.isEmpty();

            //Full fetch if DB is empty, otherwise current year only;
            ArrayList<Row> latestRows = fetchRowsFromWeb(selectedGame, fullFetch);

            log.writeInfo("Rows fetched online: " + (latestRows != null?latestRows.size():0));

            if(!fullFetch) {
                ArrayList<Row> deltaRows = rowsDeltaCheck(rows, latestRows);
                //persist delta rows in local db
                storage.write(selectedGame, deltaRows);
            } else {
                storage.write(selectedGame, latestRows);
            }
            rows = storage.readAll(selectedGame);
        }
        return rows;
    }

    /** 
     * Compare rows in latest rows and existing rows, and return arrayList with delta
     * 
     * @param rows
     * @param latestRows
     * @return ArrayList<Row>
     */
    public ArrayList<Row> rowsDeltaCheck(ArrayList<Row> rows, ArrayList<Row> latestRows) {
        ArrayList<Row> deltaRows = new ArrayList<Row>();

        SortedMap<String, Row> sortedMap = new TreeMap<String, Row>();

        for(Row row : rows) {
            String mapKey = row.getYear() + "-" + row.getCw() + "-" + row.getDate();
            sortedMap.put(mapKey, row);
        }

        for(Row row : latestRows) {
            String mapKey = row.getYear() + "-" + row.getCw() + "-" + row.getDate();

            Row tmpRow = sortedMap.get(mapKey);
            if(tmpRow == null) {
                deltaRows.add(row);
            }
        }
        return deltaRows;
    }

    /** 
     * full: true for all winning numbers back from 2013, false for current year only.
     * 
     * @param selectedGame
     * @param full
     * @return ArrayList<Row>
     */
    public ArrayList<Row> fetchRowsFromWeb(String selectedGame, Boolean full) {
        ArrayList<Row> latestRows = new ArrayList<Row>();
        SettingsSet settingsSet = settings.getSettings(selectedGame);
        IConnector connector = null;
        switch(selectedGame) {
            case "Viking":
                connector = new ConnectViking(log, settingsSet);
                break;
            case "EuroJackpot":
                connector = new ConnectEuroJackpot(log, settingsSet);
                break;
            case "Lotto":
                connector = new ConnectLotto(log, settingsSet);
                break;
        }

        int responseCode = 0;
        if(connector != null) {
            responseCode = connector.connect(full);

            latestRows = connector.getRows();
        }
        log.writeDebug("Response code from web service: " + responseCode);

        return latestRows;
    }
}
