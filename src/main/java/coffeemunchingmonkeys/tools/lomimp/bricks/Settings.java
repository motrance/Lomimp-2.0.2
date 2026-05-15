package coffeemunchingmonkeys.tools.lomimp.bricks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/***
 *
 * Lomimp
 * bricks.Settings
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class Settings {
    // Fields
    LogProvider log;
    SettingsSet vikingSettings;
    SettingsSet euroJackpotSettings;
    SettingsSet lottoSettings;
    String dbUser;
    String dbPass;
    String dbHost;
    Integer logLevel = 0;

    public Settings() {
    }

    public Settings(LogProvider log) {
        this.log = log;
        loadFromJson("settings.json");
    }

    /** 
     * @param filePath
     */
    private void loadFromJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(filePath));
            
            this.logLevel = root.get("LOGLEVEL").asInt();

            this.dbUser = root.get("DB").get("DBUSER").asText();
            this.dbPass = root.get("DB").get("DBPASS").asText();
            this.dbHost = root.get("DB").get("DBHOST").asText();

            this.vikingSettings = parseSettings(root.get("GAMES").get("VIKING"));
            this.euroJackpotSettings = parseSettings(root.get("GAMES").get("EUROJACKPOT"));
            this.lottoSettings = parseSettings(root.get("GAMES").get("LOTTO"));
        } catch (IOException e) {
            log.writeError("Failed to load settings from JSON file.");
            log.write(e);
        }
    }

    /** 
     * @param node
     * @return SettingsSet
     */
    private SettingsSet parseSettings(JsonNode node) {
        SettingsSet set = new SettingsSet();

        set.initialYear = node.get("initialYear").asInt();
        set.url = node.get("url").asText();
        set.numberSize = node.get("numberSize").asInt();
        set.numberMax = node.get("numberMax").asInt();
        set.extrasize = node.get("extrasize").asInt();
        set.extraMax = node.get("extraMax").asInt();

        return set;
    }

    /** 
     * @param selectedGame
     * @return SettingsSet
     */
    public SettingsSet getSettings(String selectedGame) {
        switch(selectedGame) {
            case "Viking":
                return this.vikingSettings;
            case "EuroJackpot":
                return this.euroJackpotSettings;
            case "Lotto":
                return this.lottoSettings;
            default:
                log.writeError("Unknown game: " + selectedGame);
                return null;
        }
    }

    /** 
     * @return logLevel 
     */
    public Integer getLogLevel () {
        return this.logLevel;
    }

    /** 
     * @return dbUser
     */
    public String getDdbUser() {
        return this.dbUser;
    }

    /** 
     * @return dbPass
     */
    public String getDbPass() {
        return this.dbPass;
    }

    /** 
     * @return dbHost
     */
    public String getDbHost() {
        return this.dbHost;
    }
}


/*
Example of settings.json file:
{	
	"LOGLEVEL":5,
	"DB": {
		"DBUSER": "xxxxxx",
		"DBPASS": "xxxxxx",
		"DBHOST": "jdbc:mysql://localhost:3306/lomimp?serverTimezone=Europe/Copenhagen"
	},
	"GAMES":{
		"VIKING":{
				"initialYear":2012,
				"url":"https://danskelotto.com/viking-lotto/vindertal/arkiv-",
				"numberSize":6,
				"numberMax":48,
				"extrasize":1,
				"extraMax":5
		},
		"EUROJACKPOT":{
				"initialYear":2012,
				"url":"https://www.euro-jackpot.net/da/resultatarkiv-",
				"numberSize":5,
				"numberMax":50,
				"extrasize":2,
				"extraMax":12
		},
		"LOTTO":{
				"initialYear":1990,
				"url":"https://danskelotto.com/lotto/vindertal/arkiv-",
				"numberSize":7,
				"numberMax":36,
				"extrasize":0,
				"extraMax":0
		}
	}
}
*/