package coffeemunchingmonkeys.tools.lomimp.stats;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import coffeemunchingmonkeys.tools.lomimp.bricks.DocManager;
import coffeemunchingmonkeys.tools.lomimp.bricks.LogProvider;
import coffeemunchingmonkeys.tools.lomimp.bricks.Settings;
import coffeemunchingmonkeys.tools.lomimp.bricks.Storage;
import coffeemunchingmonkeys.tools.lomimp.core.Row;

/***
 *
 * Lomimp
 * stats.StatsManager
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class  StatsManager {
    //Fields
    LogProvider log;
    Settings settings;
    String selectedGame;
    Storage storage;
    XSSFWorkbook workBook;
    PairsHelper PairsHelper;
    ArrayList<BigInteger> vikingPairsStatic;
    ArrayList<BigInteger> euroPairsStatic;
    ArrayList<BigInteger> lottoPairsStatic;
    ArrayList<Row> rows = new ArrayList<>();
    
    public StatsManager(String selectedGame, Storage storage, XSSFWorkbook workBook, LogProvider log, Settings settings) {
        this.selectedGame = selectedGame;
        this.storage = storage;
        this.workBook = workBook;
        this.settings = settings;
        this.PairsHelper = new PairsHelper(settings);
        this.log = log;
    }

    /** 
     * @return List<StatsDataHolder>
     */
    public List<StatsDataHolder> createStatsOnPairs() {
        DateHelper dateHelper = new DateHelper();
        String  date1Year = dateHelper.SubtractYears(1);
        String  date3Year = dateHelper.SubtractYears(3);

        ArrayList<Row> rows1Year = fetchRowsFromDate(selectedGame, date1Year, storage, null);
        ArrayList<Row> rows3Year = fetchRowsFromDate(selectedGame, date3Year, storage, null);
        ArrayList<Row> rowsAll = fetchAllRows(selectedGame, false, storage, null);

        log.writeDebug("STATS - " + selectedGame + " - Rows fetched from DB - 1 year: " + (rows1Year != null?rows1Year.size():0));
        log.writeDebug("STATS - " + selectedGame + " - Rows fetched from DB - 3 year: " + (rows3Year != null?rows3Year.size():0));
        log.writeDebug("STATS - " + selectedGame + " - Rows fetched from DB - All: " + (rowsAll != null?rowsAll.size():0));

        List<StatsDataHolder> statsDataHolders = new ArrayList<>();

        if (rows1Year != null) {
            log.writeDebug("STATS - " + selectedGame + " - Creating statistics for the last 12 months");
            StatsDataHolder dataHolder1Year = createNumberStats(rows1Year);
            statsDataHolders.add(dataHolder1Year);
        }

        if (date3Year != null) {
            log.writeDebug("STATS - " + selectedGame + " - Creating statistics for the last 3 years");
            StatsDataHolder dataHolder3Year = createNumberStats(rows3Year);
            statsDataHolders.add(dataHolder3Year);
        }

        if (rowsAll != null) {
            log.writeDebug("STATS - " + selectedGame + " - Creating statistics from the first to the last draw");
            StatsDataHolder dataHolderAll = createNumberStats(rowsAll);
            statsDataHolders.add(dataHolderAll);
        }
        return statsDataHolders;
    }

    /** 
     * @param rows
     */
    private StatsDataHolder createNumberStats(ArrayList<Row> rows) {
        Map<Integer, Map<List<Integer>, Integer>> numberCombCounts = new HashMap<>();
        Map<Integer, Map<List<Integer>, Integer>> extraCombCounts = new HashMap<>();

        for (Row row : rows) {
            List<Integer> nums = new ArrayList<>(row.getNumbers());
            List<Integer> exts = new ArrayList<>(row.getExtras());

            // Sortér én gang
            Collections.sort(nums);
            Collections.sort(exts);

            // ===== Numbers =====
            for (int k = 1; k <= nums.size(); k++) {
                Map<List<Integer>, Integer> innerMap =
                    numberCombCounts.computeIfAbsent(k, x -> new HashMap<>());

                generateCombinations(nums, k, comb -> {
                    List<Integer> key = new ArrayList<>(comb);
                    innerMap.merge(key, 1, (a, b) -> a + b);
                });
            }

            // ===== Extras =====
            for (int k = 1; k <= exts.size(); k++) {
                Map<List<Integer>, Integer> innerMap =
                    extraCombCounts.computeIfAbsent(k, x -> new HashMap<>());

                generateCombinations(exts, k, comb -> {
                    List<Integer> key = new ArrayList<>(comb);
                    innerMap.merge(key, 1, (a, b) -> a + b);
                });
            }
        }
        StatsDataHolder dataHolder = new StatsDataHolder(selectedGame, numberCombCounts, extraCombCounts);
        return dataHolder;
    }

    /** 
     * @param list
     * @param k
     * @return void
     */
    private void generateCombinations(
        List<Integer> input,
        int k,
        Consumer<List<Integer>> consumer
    ) {
        backtrack(input, k, 0, new ArrayList<>(), consumer);
    }

    /** 
     * @param input
     * @param k
     * @param start
     * @param current
     * @param consumer
     */
    private void backtrack(
        List<Integer> input,
        int k,
        int start,
        List<Integer> current,
        Consumer<List<Integer>> consumer
    ) {
        if (current.size() == k) {
            consumer.accept(current);
            return;
        }

        for (int i = start; i < input.size(); i++) {
            current.add(input.get(i));
            backtrack(input, k, i + 1, current, consumer);
            current.remove(current.size() - 1);
        }
    }

    public void createStaticStats() {
        this.vikingPairsStatic = PairsHelper.getVikingStaticStats();
        this.euroPairsStatic = PairsHelper.getEuroStaticPairs();
        this.lottoPairsStatic = PairsHelper.getLottoStaticPairs();
    }

    /** 
     * @param selectedGame
     * @param fetchNumbersFromWeb
     * @param storage
     * @param docManager
     * @return ArrayList<Row>
     */
    private ArrayList<Row> fetchAllRows(String selectedGame, Boolean fetchNumbersFromWeb, Storage storage, DocManager docManager) {
        //Fetch numbers from local db
        ArrayList<Row> rows = storage.readAll(selectedGame);
        return rows;
    }

    /** 
     * @param selectedGame
     * @param date
     * @param storage
     * @param docManager
     * @return ArrayList<Row>
     */
    private ArrayList<Row> fetchRowsFromDate(String selectedGame, String date, Storage storage, DocManager docManager) {
        //Fetch numbers from local db
        ArrayList<Row> rows = storage.readFromDate(selectedGame, date);
        return rows;
    }

    /** 
     * @return vikingPairsStatic
     */
    public ArrayList<BigInteger> getVikingPairsStatic() {
        return this.vikingPairsStatic;
    }
    
    /** 
     * @return euroPairsStatic
     */
    public ArrayList<BigInteger> getEuroPairsStatic() {
        return this.euroPairsStatic;
    }

    /** 
     * @return lottoPairsStatic
     */
    public ArrayList<BigInteger> getLottoPairsStatic() {
        return this.lottoPairsStatic;
    }
}
