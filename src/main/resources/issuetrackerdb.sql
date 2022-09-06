/*
  CREATED BY CHUMA M'HANGO
*/

-- ------------------------------------------------------

-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: issuetrackerdb
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
                            `employee_ID` int NOT NULL AUTO_INCREMENT,
                            `title` text NOT NULL,
                            `fname` tinytext NOT NULL,
                            `mname` tinytext,
                            `lname` tinytext NOT NULL,
                            `user_ID` int NOT NULL,
                            `dob` tinytext NOT NULL,
                            `gender` text NOT NULL,
                            `phone_number` text NOT NULL,
                            `email` text,
                            PRIMARY KEY (`employee_ID`),
                            UNIQUE KEY `employee_ID_UNIQUE` (`employee_ID`),
                            KEY `user_ID_idx` (`user_ID`),
                            CONSTRAINT `user_ID` FOREIGN KEY (`user_ID`) REFERENCES `user` (`user_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Mr','Chuma','','Mhango',1,'1999-02-04','Male','0975870923','chumamhango04@gmail.com'),(5,'Ms','Gloria','Mbumwae','Mulengula',5,'2001-01-27','Female','0975649333','gloria@zesco.com');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
                             `equipment_ID` int NOT NULL AUTO_INCREMENT,
                             `name` tinytext NOT NULL,
                             `equipment_desc` varchar(45) NOT NULL,
                             `site_ID` int NOT NULL,
                             `status_ID` int DEFAULT NULL,
                             `date_inspected` tinytext,
                             `comment` mediumtext,
                             `inspector_ID` int DEFAULT NULL,
                             `supervisor_ID` text,
                             PRIMARY KEY (`equipment_ID`),
                             KEY `site_ID_idx` (`site_ID`),
                             KEY `status_ID_idx` (`status_ID`),
                             KEY `employee_ID_idx` (`inspector_ID`),
                             CONSTRAINT `employee_ID` FOREIGN KEY (`inspector_ID`) REFERENCES `employee` (`employee_ID`) ON DELETE SET NULL ON UPDATE CASCADE,
                             CONSTRAINT `site_ID` FOREIGN KEY (`site_ID`) REFERENCES `site` (`site_ID`) ON DELETE RESTRICT ON UPDATE CASCADE,
                             CONSTRAINT `status_ID` FOREIGN KEY (`status_ID`) REFERENCES `status` (`status_ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (5,'Transformer1','Step Down Transformer @ 100000MWh',3,4,'07-13-2022',NULL,NULL,'1'),(6,'Transformer1','StepDown',3,5,'07-16-2022',NULL,5,NULL);
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `name_sequence`
--

DROP TABLE IF EXISTS `name_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `name_sequence` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `name_sequence`
--

LOCK TABLES `name_sequence` WRITE;
/*!40000 ALTER TABLE `name_sequence` DISABLE KEYS */;
INSERT INTO `name_sequence` VALUES (1);
/*!40000 ALTER TABLE `name_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
                        `role_ID` int NOT NULL AUTO_INCREMENT,
                        `name` text NOT NULL,
                        PRIMARY KEY (`role_ID`),
                        UNIQUE KEY `role_ID_UNIQUE` (`role_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Supervisor'),(2,'Inspector');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site` (
                        `site_ID` int NOT NULL AUTO_INCREMENT,
                        `description` text NOT NULL,
                        `location` text NOT NULL,
                        PRIMARY KEY (`site_ID`),
                        UNIQUE KEY `site_ID_UNIQUE` (`site_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (1,'Kitwe Main Branch','Jambo Drive, Riverside, Kitwe'),(2,'Headquarters','Great East Road, Lusaka'),(3,'LivingStone, Dambwa North','Dambwa North, LivingStone'),(4,'Monze Main Branch','Monze'),(5,'Matero branch','Matero, Lusaka');
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
                          `status_ID` int NOT NULL AUTO_INCREMENT,
                          `state` text NOT NULL,
                          `status_type` text,
                          PRIMARY KEY (`status_ID`),
                          UNIQUE KEY `status_ID_UNIQUE` (`status_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'Not yet inspected',NULL),(2,'The transformer is in full working order now.','Good'),(3,'Working just fine',NULL),(4,'It is all good now.','Good'),(5,'It is not in good shape','Bad');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `user_ID` int NOT NULL AUTO_INCREMENT,
                        `role_ID` int NOT NULL,
                        `username` varchar(15) NOT NULL,
                        `password` text NOT NULL,
                        PRIMARY KEY (`user_ID`),
                        UNIQUE KEY `user_ID_UNIQUE` (`user_ID`),
                        UNIQUE KEY `username_UNIQUE` (`username`),
                        KEY `role_ID_idx` (`role_ID`),
                        KEY `username` (`username`),
                        CONSTRAINT `role_ID` FOREIGN KEY (`role_ID`) REFERENCES `role` (`role_ID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'chumamhango','$argon2id$v=19$m=15360,t=2,p=1$/KVw6DNIFwPrYPoaiQD5loWCvSIF9O5z0mJN62fJsxg$3dJWm1iK/2+w1NWLAtNcDHenV9uQ2AaDZsRKhMoAP/hMuNH6hbjNApWa0BGYKdzatho7xY0/uu4nBrvhj2eyCg'),(5,2,'gloriamulengula','$argon2id$v=19$m=15360,t=2,p=1$veHGlBfksbaB/29zSwdi6PbI5yuWErmDD19UEBRdV+8$ea70lTJOTKLuX69TMfbbe5nV8n2WY2U/r1xYx67RUjEmL3t9fSAHpti+X9sMLUCFq2wGrLNpbpx9rK+t4qs8Qw');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_sequence`
--

DROP TABLE IF EXISTS `user_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_sequence` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sequence`
--

LOCK TABLES `user_sequence` WRITE;
/*!40000 ALTER TABLE `user_sequence` DISABLE KEYS */;
INSERT INTO `user_sequence` VALUES (3);
/*!40000 ALTER TABLE `user_sequence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-22 10:48:56
