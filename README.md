# Lomimp-2.0.2
Lomimp-2.0.2

- Database
- Configuration

Database:
A MYSQL database is required for this application.

Database MySql
CREATE DATABASE `lomimp` /*!40100 DEFAULT CHARACTER SET utf8mb4 */ /*!80016 DEFAULT ENCRYPTION='N' */;

Tables:
CREATE TABLE `eurojackpot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` varchar(45) NOT NULL,
  `year` int NOT NULL,
  `cw` int NOT NULL,
  `numberssize` int NOT NULL,
  `extrassize` int NOT NULL,
  `num1` int NOT NULL,
  `num2` int NOT NULL,
  `num3` int NOT NULL,
  `num4` int NOT NULL,
  `num5` int NOT NULL,
  `num6` int,
  `num7` int,
  `num8` int,
  `num9` int,
  `num10` int,
  `extra1` int NOT NULL,
  `extra2` int,
  `extra3` int,
  `extra4` int,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `viking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` varchar(45) NOT NULL,
  `year` int NOT NULL,
  `cw` int NOT NULL,
  `numberssize` int NOT NULL,
  `extrassize` int NOT NULL,
  `num1` int NOT NULL,
  `num2` int NOT NULL,
  `num3` int NOT NULL,
  `num4` int NOT NULL,
  `num5` int NOT NULL,
  `num6` int,
  `num7` int,
  `num8` int,
  `num9` int,
  `num10` int,
  `extra1` int NOT NULL,
  `extra2` int,
  `extra3` int,
  `extra4` int,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `lotto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` varchar(45) NOT NULL,
  `year` int NOT NULL,
  `cw` int NOT NULL,
  `numberssize` int NOT NULL,
  `extrassize` int NOT NULL,
  `num1` int NOT NULL,
  `num2` int NOT NULL,
  `num3` int NOT NULL,
  `num4` int NOT NULL,
  `num5` int NOT NULL,
  `num6` int,
  `num7` int,
  `num8` int,
  `num9` int,
  `num10` int,
  `extra1` int,
  `extra2` int,
  `extra3` int,
  `extra4` int,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8mb4;

# Loglevel - 0 for no log, 1 for info, 2 for errors, 3 for warnings, 4 for exceptions and 5 for debug

Configuration:
A configuraton file, "Settings.json" places in the folder "resources", is required.

Example of settings.json file:
{	
	"LOGLEVEL":4,
  "OUTPUTPATH": "",
	"LOGPATH": "logs/",
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