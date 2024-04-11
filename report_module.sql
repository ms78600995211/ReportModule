-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: report_module
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `json_var_definition`
--

DROP TABLE IF EXISTS `json_var_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `json_var_definition` (
  `json_var_definition_id` int(10) NOT NULL AUTO_INCREMENT,
  `report_table_id` int(10) NOT NULL,
  `variable_name` varchar(75) DEFAULT NULL,
  `order_no` int(11) NOT NULL,
  `dummy1` varchar(45) DEFAULT NULL,
  `dummy2` varchar(45) DEFAULT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  `revision_no` int(11) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`json_var_definition_id`,`revision_no`) USING BTREE,
  KEY `fk1_report_table_id` (`report_table_id`),
  CONSTRAINT `fk1_report_table_id` FOREIGN KEY (`report_table_id`) REFERENCES `report_table` (`report_table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `json_var_definition`
--

LOCK TABLES `json_var_definition` WRITE;
/*!40000 ALTER TABLE `json_var_definition` DISABLE KEYS */;
INSERT INTO `json_var_definition` VALUES (1,1,'s_no',1,' ',' ','Y',0,'2023-05-02 09:59:23',NULL,NULL),(2,1,'parameter_type',2,' ',' ','Y',0,'2023-05-02 09:59:23',NULL,NULL),(3,1,'remark',3,' ',' ','Y',0,'2023-05-02 09:59:23',NULL,NULL);
/*!40000 ALTER TABLE `json_var_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_copies`
--

DROP TABLE IF EXISTS `report_copies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `report_copies` (
  `report_copies_id` int(10) NOT NULL AUTO_INCREMENT,
  `report_table_id` int(10) NOT NULL,
  `file_name` varchar(75) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  `revision_no` int(11) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_copies_id`,`revision_no`) USING BTREE,
  UNIQUE KEY `uk_filr_name` (`file_name`),
  KEY `fk_report_table_id` (`report_table_id`),
  CONSTRAINT `fk_report_table_id` FOREIGN KEY (`report_table_id`) REFERENCES `report_table` (`report_table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_copies`
--

LOCK TABLES `report_copies` WRITE;
/*!40000 ALTER TABLE `report_copies` DISABLE KEYS */;
INSERT INTO `report_copies` VALUES (1,3,'BLE Registration Report_02June23101953.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 04:49:55',NULL,'',NULL),(2,3,'BLE Registration Report_02June23103040.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:00:41',NULL,'',NULL),(3,3,'BLE Registration Report_02June23103304.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:03:04',NULL,'',NULL),(4,3,'BLE Registration Report_02June23103417.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:04:17',NULL,'',NULL),(5,3,'BLE Registration Report_02June23103654.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:06:54',NULL,'',NULL),(6,3,'BLE Registration Report_02June23104653.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:16:53',NULL,'',NULL),(7,3,'BLE Registration Report_02June23105424.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:24:25',NULL,'',NULL),(8,3,'BLE Registration Report_02June23105540.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:25:40',NULL,'',NULL),(9,3,'BLE Registration Report_02June23110854.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:38:55',NULL,'',NULL),(10,3,'BLE Registration Report_02June23111222.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:42:23',NULL,'',NULL),(11,3,'BLE Registration Report_02June23111357.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:43:58',NULL,'',NULL),(12,3,'BLE Registration Report_02June23111532.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:45:34',NULL,'',NULL),(13,3,'BLE Registration Report_02June23111644.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:46:44',NULL,'',NULL),(14,3,'BLE Registration Report_02June23111952.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:49:52',NULL,'',NULL),(15,3,'BLE Registration Report_02June23112036.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:50:36',NULL,'',NULL),(16,3,'BLE Registration Report_02June23112425.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:54:25',NULL,'',NULL),(17,3,'BLE Registration Report_02June23112459.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 05:54:59',NULL,'',NULL),(18,3,'BLE Registration Report_02June23114440.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-02 06:14:40',NULL,'',NULL),(19,3,'BLE Registration Report_06June23032241.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\BLE Registration Report\\','Y',0,'2023-06-06 09:52:43',NULL,'',NULL),(20,3,'BLE Registration Report_06June23033754.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\','Y',0,'2023-06-06 10:07:56',NULL,'',NULL),(21,3,'BLE Registration Report_06June23034631.pdf','C:\\ssadvt_repository\\Reports\\All reports\\BLE Registration Report\\','Y',0,'2023-06-06 10:16:33',NULL,'',NULL);
/*!40000 ALTER TABLE `report_copies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_status`
--

DROP TABLE IF EXISTS `report_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `report_status` (
  `report_status_id` int(10) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) DEFAULT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  `revision_no` int(11) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_status_id`,`revision_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_status`
--

LOCK TABLES `report_status` WRITE;
/*!40000 ALTER TABLE `report_status` DISABLE KEYS */;
INSERT INTO `report_status` VALUES (1,'Request Accepted','Y',0,'2023-05-09 11:14:12',NULL,NULL),(2,'Designed','Y',0,'2023-05-09 11:14:12',NULL,NULL),(3,'Request Sent for JSON','Y',0,'2023-05-16 04:24:59',NULL,NULL),(4,'Data Received','Y',0,'2023-05-16 04:24:59',NULL,NULL),(5,'Report Generated','Y',0,'2023-05-22 06:41:45',NULL,NULL);
/*!40000 ALTER TABLE `report_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_table`
--

DROP TABLE IF EXISTS `report_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `report_table` (
  `report_table_id` int(10) NOT NULL AUTO_INCREMENT,
  `report_type_id` int(10) NOT NULL,
  `report_status_id` int(10) NOT NULL,
  `report_name` varchar(75) DEFAULT NULL,
  `sample_report_path` varchar(75) DEFAULT NULL,
  `jrxml_file_name` varchar(75) DEFAULT NULL,
  `api_json_file_path` varchar(75) DEFAULT NULL,
  `no_of_variables` int(11) NOT NULL,
  `text_file_url` varchar(255) NOT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  `revision_no` int(11) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_table_id`,`revision_no`) USING BTREE,
  KEY `fk_report_type_id` (`report_type_id`),
  KEY `report_status_id_fk` (`report_status_id`),
  CONSTRAINT `fk_report_type_id` FOREIGN KEY (`report_type_id`) REFERENCES `report_type` (`report_type_id`),
  CONSTRAINT `report_status_id_fk` FOREIGN KEY (`report_status_id`) REFERENCES `report_status` (`report_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_table`
--

LOCK TABLES `report_table` WRITE;
/*!40000 ALTER TABLE `report_table` DISABLE KEYS */;
INSERT INTO `report_table` VALUES (3,1,1,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','N',0,'2023-06-01 07:28:17','','',''),(3,1,2,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','N',1,'2023-06-01 09:04:46',NULL,'',''),(3,1,3,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','N',2,'2023-06-02 06:21:05',NULL,'',''),(3,1,4,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','N',3,'2023-06-02 06:45:21',NULL,'',''),(3,1,5,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','N',4,'2023-06-02 06:45:21',NULL,'',''),(3,1,5,'BLE Registration Report','C:\\ssadvt_repository\\Reports\\sample report\\','BLE Registration Report.jrxml','C:\\ssadvt_repository\\Reports\\Text files\\',0,'http://localhost:8082/EncryptedTokenModule/webAPI/service/getTextFileForReport','Y',5,'2023-06-02 06:45:22',NULL,'','');
/*!40000 ALTER TABLE `report_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_type`
--

DROP TABLE IF EXISTS `report_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `report_type` (
  `report_type_id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) DEFAULT NULL,
  `active` char(1) NOT NULL DEFAULT 'Y',
  `revision_no` int(11) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_type_id`,`revision_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_type`
--

LOCK TABLES `report_type` WRITE;
/*!40000 ALTER TABLE `report_type` DISABLE KEYS */;
INSERT INTO `report_type` VALUES (1,'pdf','Y',0,'2023-05-02 09:55:44',NULL,NULL),(2,'excel','Y',0,'2023-05-02 09:55:44',NULL,NULL),(3,'spreadsheet','Y',0,'2023-05-02 09:55:44',NULL,NULL);
/*!40000 ALTER TABLE `report_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-16  9:24:17
