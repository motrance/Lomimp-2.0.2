# Lomimp-2.0.2
Lomimp-2.0.2

MYSQL database is required for this application.

Database:

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
