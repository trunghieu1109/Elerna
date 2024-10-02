-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: elerna
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assign_submissions`
--

DROP TABLE IF EXISTS `assign_submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assign_submissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `assignment_id` int NOT NULL,
  `course_id` int NOT NULL,
  `content_id` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) DEFAULT 'SubmissionName',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `assignment_id` (`assignment_id`),
  KEY `course_id` (`course_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `assign_submissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assign_submissions_ibfk_2` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`id`),
  CONSTRAINT `assign_submissions_ibfk_3` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `assign_submissions_ibfk_4` FOREIGN KEY (`content_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assign_submissions`
--

LOCK TABLES `assign_submissions` WRITE;
/*!40000 ALTER TABLE `assign_submissions` DISABLE KEYS */;
INSERT INTO `assign_submissions` VALUES (8,34,6,24,39,'2024-10-02 15:48:02','2024-10-02 15:48:02','Assignment6_Submission1_json_format.py');
/*!40000 ALTER TABLE `assign_submissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `course_id` int DEFAULT NULL,
  `content_id` int NOT NULL,
  `start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `assignments_ibfk_2` FOREIGN KEY (`content_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
INSERT INTO `assignments` VALUES (6,'AssignmentJava2',24,37,'2024-10-02 15:30:45','2024-10-02 15:30:45','2024-10-02 15:30:03','2024-10-02 15:43:19');
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_account_logs`
--

DROP TABLE IF EXISTS `bank_account_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_account_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `message_type` varchar(15) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `bank_account_logs_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `bank_accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_account_logs`
--

LOCK TABLES `bank_account_logs` WRITE;
/*!40000 ALTER TABLE `bank_account_logs` DISABLE KEYS */;
INSERT INTO `bank_account_logs` VALUES (1,2,'Payment','Account 2 pays for course 19Successfully, amount = 58.0','2024-10-01 15:33:45','2024-10-01 15:33:45'),(2,2,'Deposit','Deposit money to bank account 2, amount = 123.0, residual = 165.0','2024-10-01 15:59:35','2024-10-01 15:59:35'),(3,2,'Payment','Account 2 pays for course 20 successfully, amount = 100.0, residual = 65.0','2024-10-01 16:07:05','2024-10-01 16:07:05'),(4,2,'Deposit','Deposit money to bank account 2, amount = 123.0, residual = 188.0','2024-10-01 16:20:34','2024-10-01 16:20:34'),(5,2,'Payment','Account 2 pays for course 20 successfully, amount = 100.0, residual = 88.0','2024-10-01 16:22:22','2024-10-01 16:22:22'),(6,17,'Deposit','Deposit money to bank account 17, amount = 999.0, residual = 1099.0','2024-10-02 11:05:38','2024-10-02 11:05:38'),(7,17,'Payment','Account 17 pays for course 22 successfully, amount = 0.0, residual = 1099.0','2024-10-02 11:06:54','2024-10-02 11:06:54');
/*!40000 ALTER TABLE `bank_account_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_accounts`
--

DROP TABLE IF EXISTS `bank_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `card_holder` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `bank_accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_accounts`
--

LOCK TABLES `bank_accounts` WRITE;
/*!40000 ALTER TABLE `bank_accounts` DISABLE KEYS */;
INSERT INTO `bank_accounts` VALUES (1,40,'user3097 account','02465416512',100.00,'2024-10-01 14:41:22','2024-10-01 14:41:22'),(2,41,'user3098 account','4545345345',88.00,'2024-10-01 15:26:47','2024-10-01 16:22:22'),(3,42,'user3010 account','245345',100.00,'2024-10-02 00:23:34','2024-10-02 00:23:34'),(4,43,'Hieu Nguyen','5234523452345234',100.00,'2024-10-02 09:54:26','2024-10-02 09:54:26'),(5,44,'Dat Nguyen','56465456',100.00,'2024-10-02 09:55:27','2024-10-02 10:56:06'),(6,23,'Demo Account','1142417177',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(7,24,'Kunno Account','0039996311',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(8,25,'Kunno Account','9649259130',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(9,26,'Kunno Account','4701494347',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(10,27,'Kunno Account','0166380986',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(11,28,'Kunno Account','1665600351',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(12,29,'Kunno Account','7435242916',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(13,30,'user289 Account','9347945464',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(14,31,'user289 Account','6280134273',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(15,32,'user289 Account','1802849965',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(16,33,'User3091 Account','9568479889',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(17,34,'admin account','9120352874',1099.00,'2024-10-02 10:09:12','2024-10-02 11:05:38'),(18,35,'user3092 account','5628481060',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(19,36,'user3093 account','2544497482',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(20,37,'user3094 account','7141561200',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(21,38,'user3095 account','2686130116',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(22,39,'user3096 account','1058882368',100.00,'2024-10-02 10:09:12','2024-10-02 10:09:12'),(23,45,'Trung Trung','56356356356',100.00,'2024-10-02 11:52:44','2024-10-02 11:52:44');
/*!40000 ALTER TABLE `bank_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contents`
--

DROP TABLE IF EXISTS `contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contents` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contents`
--

LOCK TABLES `contents` WRITE;
/*!40000 ALTER TABLE `contents` DISABLE KEYS */;
INSERT INTO `contents` VALUES (35,'collision_utils.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Lesson\\Course24_collision_utils.py','2024-10-02 15:26:12','2024-10-02 15:26:12'),(36,'env_control_for_formal_experiment_violation.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Lesson\\Course24_env_control_for_formal_experiment_violation.py','2024-10-02 15:28:53','2024-10-02 15:28:53'),(37,'collision_utils.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Assignment\\Course24_collision_utils.py','2024-10-02 15:30:03','2024-10-02 15:30:03'),(38,'collision_utils.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Contest\\Course24_collision_utils.py','2024-10-02 15:30:54','2024-10-02 15:30:54'),(39,'Assignment6_Submission1_json_format.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Submission\\Assignment6_Submission1_json_format.py','2024-10-02 15:48:02','2024-10-02 15:48:02'),(40,'Contest4_Submission1_env_control_for_formal_experiment_violation.py','E:\\Exercise\\Elerna\\src\\main\\resources\\Submission\\Contest4_Submission1_env_control_for_formal_experiment_violation.py','2024-10-02 15:51:34','2024-10-02 15:51:34');
/*!40000 ALTER TABLE `contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contest_submissions`
--

DROP TABLE IF EXISTS `contest_submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contest_submissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `contest_id` int NOT NULL,
  `course_id` int NOT NULL,
  `content_id` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) DEFAULT 'SubmissionName',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `contest_id` (`contest_id`),
  KEY `course_id` (`course_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `contest_submissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `contest_submissions_ibfk_2` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`id`),
  CONSTRAINT `contest_submissions_ibfk_3` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `contest_submissions_ibfk_4` FOREIGN KEY (`content_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest_submissions`
--

LOCK TABLES `contest_submissions` WRITE;
/*!40000 ALTER TABLE `contest_submissions` DISABLE KEYS */;
INSERT INTO `contest_submissions` VALUES (7,34,4,24,40,'2024-10-02 15:51:34','2024-10-02 15:51:34','Contest4_Submission1_env_control_for_formal_experiment_violation.py');
/*!40000 ALTER TABLE `contest_submissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contests`
--

DROP TABLE IF EXISTS `contests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `course_id` int DEFAULT NULL,
  `content_id` int NOT NULL,
  `start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime NOT NULL,
  `duration` datetime NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `contests_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `contests_ibfk_2` FOREIGN KEY (`content_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contests`
--

LOCK TABLES `contests` WRITE;
/*!40000 ALTER TABLE `contests` DISABLE KEYS */;
INSERT INTO `contests` VALUES (4,'ContestJava4',24,38,'2004-11-09 18:03:06','2004-11-09 18:03:06','2024-10-02 15:46:35','2024-10-02 15:30:54','2024-10-02 15:46:35');
/*!40000 ALTER TABLE `contests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_requests`
--

DROP TABLE IF EXISTS `course_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_requests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `proposer_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `major` varchar(255) NOT NULL,
  `language` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `proposer_id` (`proposer_id`),
  CONSTRAINT `course_requests_ibfk_1` FOREIGN KEY (`proposer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_requests`
--

LOCK TABLES `course_requests` WRITE;
/*!40000 ALTER TABLE `course_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `major` varchar(255) NOT NULL,
  `duration` time DEFAULT NULL,
  `rating` decimal(10,2) DEFAULT '5.00',
  `language` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (3,'string','string','18:57:36',0.00,'string','string',0.00,'2024-09-27 14:55:11','2024-09-28 01:59:11',1),(4,'Lap Trinh Java','Information Technology','15:10:49',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-27 15:10:49','2024-09-27 15:10:49',1),(5,'Lap Trinh JavaScript','Information Technology','15:10:52',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-27 15:10:52','2024-09-27 15:10:52',1),(6,'Lap Trinh Python','Information Technology','15:10:55',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-27 15:10:55','2024-09-27 15:10:55',1),(7,'Lap Trinh Ruby','Information Technology','15:10:58',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-27 15:10:58','2024-09-27 15:10:58',1),(8,'string','string','18:57:36',0.00,'string','string',0.00,'2024-09-28 09:38:19','2024-09-28 09:46:35',1),(9,'Lap Trinh Pytho','Information Technology','10:26:47',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-28 10:26:47','2024-09-28 10:26:47',1),(10,'Lap Trinh Pythonn','Information Technology','10:51:43',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-28 10:51:43','2024-09-29 22:26:37',0),(11,'Lap Trinh Pythonnnnnn','Information Technology','10:56:26',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-28 10:56:26','2024-09-29 22:28:15',0),(12,'Lap Trinh Pythonnnnnn Java','Information Technology','11:02:03',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-28 11:02:03','2024-09-28 11:02:03',1),(13,'Lap Trinh Web','Information','18:57:36',0.00,'Spainish','string',0.00,'2024-09-30 23:37:42','2024-09-30 23:44:06',0),(14,'Lap Trinh Java1','Information Technology','23:39:35',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-30 23:39:35','2024-09-30 23:39:35',0),(15,'Lap Trinh Java2','Information Technology','23:39:40',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-09-30 23:39:40','2024-09-30 23:39:40',0),(16,'Lap Trinh Java4','Information Technology','00:07:23',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-10-01 00:07:23','2024-10-01 00:12:40',0),(17,'Lap Trinh Java5','Information Technology','00:15:07',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-10-01 00:15:07','2024-10-01 00:15:07',1),(18,'Lap Trinh Java6','Information Technology','11:26:55',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-10-01 11:26:55','2024-10-01 12:22:48',0),(19,'Lap Trinh Java Spring Boot Web','Information Technology, Java Spring Boot','18:57:36',4.50,'Spainish','This is new course for Spring Boot\'s Beginner',58.00,'2024-10-01 15:27:38','2024-10-01 15:28:59',1),(20,'Lap Trinh Java Spring Security','Information Technology, Java Spring Boot','18:57:36',4.50,'Spainish','This is new course for Spring Boot\'s Beginner',100.00,'2024-10-01 16:05:44','2024-10-01 16:06:46',1),(21,'Lap Trinh Java10','Information Technology','00:24:03',5.00,'English','This is a great course for JavaScript favour',0.00,'2024-10-02 00:24:03','2024-10-02 00:24:03',1),(22,'Lap trinh Game bang Unreal Engine','Game Developer','18:26:32',0.00,'Spainish','Game developing',15.00,'2024-10-02 01:35:22','2024-10-02 14:53:41',1),(23,'Lap trinh Game bang Unity','Game Developer','14:20:22',5.00,'English','Game Developing by Unity',0.00,'2024-10-02 14:20:22','2024-10-02 15:00:44',0),(24,'Lap trinh Java tong hop','Java','15:25:17',5.00,'English','Java mixture',0.00,'2024-10-02 15:25:18','2024-10-02 15:25:18',1);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `course_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `content_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `lesson_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `lesson_ibfk_2` FOREIGN KEY (`content_id`) REFERENCES `contents` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
INSERT INTO `lesson` VALUES (6,'LessonJava1',NULL,'2024-10-02 15:26:12','2024-10-02 15:52:36',35),(7,'LessonJava7',24,'2024-10-02 15:28:53','2024-10-02 15:40:05',36);
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privileges`
--

DROP TABLE IF EXISTS `privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `privileges` (
  `id` int NOT NULL AUTO_INCREMENT,
  `resource_id` int NOT NULL,
  `action_type` varchar(10) NOT NULL,
  `description` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `resource_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=324 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privileges`
--

LOCK TABLES `privileges` WRITE;
/*!40000 ALTER TABLE `privileges` DISABLE KEYS */;
INSERT INTO `privileges` VALUES (63,23,'view','View profile, id = 23','2024-09-26 16:01:37','2024-09-26 16:01:37','profile'),(64,23,'delete','Delete profile, id = 23','2024-09-26 16:01:37','2024-09-26 16:01:37','profile'),(65,23,'update','Update profile, id = 23','2024-09-26 16:01:37','2024-09-26 16:01:37','profile'),(66,19,'view','View team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34','team'),(67,19,'delete','Delete team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34','team'),(68,19,'update','Update team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34','team'),(69,25,'view','View profile, id = 25','2024-09-26 16:34:51','2024-09-26 16:34:51','profile'),(70,25,'delete','Delete profile, id = 25','2024-09-26 16:34:51','2024-09-26 16:34:51','profile'),(71,25,'update','Update profile, id = 25','2024-09-26 16:34:51','2024-09-26 16:34:51','profile'),(72,20,'view','View team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18','team'),(73,20,'delete','Delete team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18','team'),(74,20,'update','Update team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18','team'),(75,21,'view','View team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43','team'),(76,21,'delete','Delete team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43','team'),(77,21,'update','Update team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43','team'),(78,22,'view','View team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55','team'),(79,22,'delete','Delete team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55','team'),(80,22,'update','Update team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55','team'),(81,23,'view','View team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22','team'),(82,23,'delete','Delete team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22','team'),(83,23,'update','Update team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22','team'),(84,24,'view','View team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09','team'),(85,24,'delete','Delete team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09','team'),(86,24,'update','Update team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09','team'),(87,25,'view','View team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15','team'),(88,25,'delete','Delete team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15','team'),(89,25,'update','Update team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15','team'),(90,26,'view','View team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19','team'),(91,26,'delete','Delete team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19','team'),(92,26,'update','Update team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19','team'),(93,27,'view','View team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23','team'),(94,27,'delete','Delete team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23','team'),(95,27,'update','Update team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23','team'),(96,26,'view','View profile, id = 26','2024-09-26 17:52:28','2024-09-26 17:52:28','profile'),(97,26,'delete','Delete profile, id = 26','2024-09-26 17:52:28','2024-09-26 17:52:28','profile'),(98,26,'update','Update profile, id = 26','2024-09-26 17:52:28','2024-09-26 17:52:28','profile'),(99,28,'view','View team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42','team'),(100,28,'delete','Delete team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42','team'),(101,28,'update','Update team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42','team'),(102,27,'view','View profile, id = 27','2024-09-26 18:17:34','2024-09-26 18:17:34','profile'),(103,27,'delete','Delete profile, id = 27','2024-09-26 18:17:34','2024-09-26 18:17:34','profile'),(104,27,'update','Update profile, id = 27','2024-09-26 18:17:34','2024-09-26 18:17:34','profile'),(106,30,'view','View team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21','team'),(107,30,'delete','Delete team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21','team'),(108,30,'update','Update team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21','team'),(109,28,'view','View profile, id = 28','2024-09-27 11:37:51','2024-09-27 11:37:51','profile'),(110,28,'delete','Delete profile, id = 28','2024-09-27 11:37:51','2024-09-27 11:37:51','profile'),(111,28,'update','Update profile, id = 28','2024-09-27 11:37:51','2024-09-27 11:37:51','profile'),(112,3,'delete','Delete course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11','course'),(113,3,'update','Update course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11','course'),(114,3,'view','View course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11','course'),(115,4,'delete','Delete course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49','course'),(116,4,'update','Update course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49','course'),(117,4,'view','View course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49','course'),(118,5,'delete','Delete course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52','course'),(119,5,'update','Update course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52','course'),(120,5,'view','View course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52','course'),(121,6,'delete','Delete course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55','course'),(122,6,'update','Update course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55','course'),(123,6,'view','View course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55','course'),(124,7,'delete','Delete course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58','course'),(125,7,'update','Update course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58','course'),(126,7,'view','View course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58','course'),(127,31,'view','View team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28','team'),(128,31,'delete','Delete team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28','team'),(129,31,'update','Update team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28','team'),(130,32,'view','View team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47','team'),(131,32,'delete','Delete team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47','team'),(132,32,'update','Update team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47','team'),(133,33,'view','View team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21','team'),(134,33,'delete','Delete team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21','team'),(135,33,'update','Update team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21','team'),(136,34,'view','View team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32','team'),(137,34,'delete','Delete team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32','team'),(138,34,'update','Update team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32','team'),(139,35,'view','View team, id = 35','2024-09-27 16:39:55','2024-09-27 16:39:55','team'),(140,35,'delete','Delete team, id = 35','2024-09-27 16:39:55','2024-09-27 16:39:55','team'),(141,35,'update','Update team, id = 35','2024-09-27 16:39:56','2024-09-27 16:39:56','team'),(142,29,'view','View profile, id = 29','2024-09-28 00:34:47','2024-09-28 00:34:47','profile'),(143,29,'delete','Delete profile, id = 29','2024-09-28 00:34:47','2024-09-28 00:34:47','profile'),(144,29,'update','Update profile, id = 29','2024-09-28 00:34:47','2024-09-28 00:34:47','profile'),(145,36,'view','View team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26','team'),(146,36,'delete','Delete team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26','team'),(147,36,'update','Update team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26','team'),(148,37,'view','View team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33','team'),(149,37,'delete','Delete team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33','team'),(150,37,'update','Update team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33','team'),(151,38,'view','View team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08','team'),(152,38,'delete','Delete team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08','team'),(153,38,'update','Update team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08','team'),(154,39,'view','View team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22','team'),(155,39,'delete','Delete team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22','team'),(156,39,'update','Update team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22','team'),(157,40,'view','View team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36','team'),(158,40,'delete','Delete team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36','team'),(159,40,'update','Update team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36','team'),(160,30,'view','View profile, id = 30','2024-09-28 09:27:41','2024-09-28 09:27:41','profile'),(161,30,'delete','Delete profile, id = 30','2024-09-28 09:27:41','2024-09-28 09:27:41','profile'),(162,30,'update','Update profile, id = 30','2024-09-28 09:27:41','2024-09-28 09:27:41','profile'),(163,41,'view','View team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05','team'),(164,41,'delete','Delete team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05','team'),(165,41,'update','Update team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05','team'),(166,42,'view','View team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31','team'),(167,42,'delete','Delete team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31','team'),(168,42,'update','Update team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31','team'),(169,8,'delete','Delete course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19','course'),(170,8,'update','Update course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19','course'),(171,8,'view','View course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19','course'),(172,9,'delete','Delete course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47','course'),(173,9,'update','Update course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47','course'),(174,9,'view','View course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47','course'),(175,9,'submit','View course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47','course'),(176,31,'view','View profile, id = 31','2024-09-28 10:30:02','2024-09-28 10:30:02','profile'),(177,31,'delete','Delete profile, id = 31','2024-09-28 10:30:02','2024-09-28 10:30:02','profile'),(178,31,'update','Update profile, id = 31','2024-09-28 10:30:02','2024-09-28 10:30:02','profile'),(179,32,'view','View profile, id = 32','2024-09-28 10:35:45','2024-09-28 10:35:45','profile'),(180,32,'delete','Delete profile, id = 32','2024-09-28 10:35:45','2024-09-28 10:35:45','profile'),(181,32,'update','Update profile, id = 32','2024-09-28 10:35:45','2024-09-28 10:35:45','profile'),(182,10,'delete','Delete course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43','course'),(183,10,'update','Update course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43','course'),(184,10,'view','View course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43','course'),(185,10,'submit','View course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43','course'),(186,11,'delete','Delete course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26','course'),(187,11,'update','Update course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26','course'),(188,11,'view','View course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26','course'),(189,11,'submit','View course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26','course'),(190,12,'delete','Delete course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03','course'),(191,12,'update','Update course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03','course'),(192,12,'view','View course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03','course'),(193,12,'submit','View course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03','course'),(194,4,'delete','Delete course, id = 4','2024-09-29 18:27:09','2024-09-29 18:27:09','submission'),(195,4,'update','Update course, id = 4','2024-09-29 18:27:09','2024-09-29 18:27:09','submission'),(196,4,'view','View course, id = 4','2024-09-29 18:27:09','2024-09-29 18:27:09','submission'),(197,5,'delete','Delete course, id = 5','2024-09-29 18:27:33','2024-09-29 18:27:33','submission'),(198,5,'update','Update course, id = 5','2024-09-29 18:27:33','2024-09-29 18:27:33','submission'),(199,5,'view','View course, id = 5','2024-09-29 18:27:33','2024-09-29 18:27:33','submission'),(200,6,'delete','Delete course, id = 6','2024-09-29 21:19:38','2024-09-29 21:19:38','submission'),(201,6,'update','Update course, id = 6','2024-09-29 21:19:38','2024-09-29 21:19:38','submission'),(202,6,'view','View course, id = 6','2024-09-29 21:19:38','2024-09-29 21:19:38','submission'),(203,6,'delete','Delete course, id = 6','2024-09-29 21:24:47','2024-09-29 21:24:47','submission'),(204,6,'update','Update course, id = 6','2024-09-29 21:24:47','2024-09-29 21:24:47','submission'),(205,6,'view','View course, id = 6','2024-09-29 21:24:47','2024-09-29 21:24:47','submission'),(206,33,'view','View profile, id = 33','2024-09-30 14:48:49','2024-09-30 14:48:49','profile'),(207,33,'delete','Delete profile, id = 33','2024-09-30 14:48:49','2024-09-30 14:48:49','profile'),(208,33,'update','Update profile, id = 33','2024-09-30 14:48:49','2024-09-30 14:48:49','profile'),(209,43,'view','View team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01','team'),(210,43,'delete','Delete team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01','team'),(211,43,'update','Update team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01','team'),(212,35,'view','View profile, id = 35','2024-09-30 23:27:13','2024-09-30 23:27:13','profile'),(213,35,'delete','Delete profile, id = 35','2024-09-30 23:27:13','2024-09-30 23:27:13','profile'),(214,35,'update','Update profile, id = 35','2024-09-30 23:27:13','2024-09-30 23:27:13','profile'),(215,13,'delete','Delete course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42','course'),(216,13,'update','Update course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42','course'),(217,13,'view','View course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42','course'),(218,13,'submit','View course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42','course'),(219,14,'delete','Delete course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35','course'),(220,14,'update','Update course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35','course'),(221,14,'view','View course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35','course'),(222,14,'submit','View course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35','course'),(223,15,'delete','Delete course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40','course'),(224,15,'update','Update course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40','course'),(225,15,'view','View course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40','course'),(226,15,'submit','View course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40','course'),(227,36,'view','View profile, id = 36','2024-09-30 23:42:08','2024-09-30 23:42:08','profile'),(228,36,'delete','Delete profile, id = 36','2024-09-30 23:42:08','2024-09-30 23:42:08','profile'),(229,36,'update','Update profile, id = 36','2024-09-30 23:42:08','2024-09-30 23:42:08','profile'),(230,16,'delete','Delete course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23','course'),(231,16,'update','Update course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23','course'),(232,16,'view','View course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23','course'),(233,16,'submit','View course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23','course'),(234,17,'delete','Delete course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07','course'),(235,17,'update','Update course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07','course'),(236,17,'view','View course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07','course'),(237,17,'submit','View course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07','course'),(238,7,'delete','Delete course, id = 7','2024-10-01 00:21:14','2024-10-01 00:21:14','submission'),(239,7,'update','Update course, id = 7','2024-10-01 00:21:14','2024-10-01 00:21:14','submission'),(240,7,'view','View course, id = 7','2024-10-01 00:21:14','2024-10-01 00:21:14','submission'),(241,37,'view','View profile, id = 37','2024-10-01 09:13:39','2024-10-01 09:13:39','profile'),(242,37,'delete','Delete profile, id = 37','2024-10-01 09:13:40','2024-10-01 09:13:40','profile'),(243,37,'update','Update profile, id = 37','2024-10-01 09:13:40','2024-10-01 09:13:40','profile'),(244,38,'view','View profile, id = 38','2024-10-01 09:32:44','2024-10-01 09:32:44','profile'),(245,38,'delete','Delete profile, id = 38','2024-10-01 09:32:44','2024-10-01 09:32:44','profile'),(246,38,'update','Update profile, id = 38','2024-10-01 09:32:44','2024-10-01 09:32:44','profile'),(247,39,'view','View profile, id = 39','2024-10-01 09:35:20','2024-10-01 09:35:20','profile'),(248,39,'delete','Delete profile, id = 39','2024-10-01 09:35:20','2024-10-01 09:35:20','profile'),(249,39,'update','Update profile, id = 39','2024-10-01 09:35:20','2024-10-01 09:35:20','profile'),(250,18,'delete','Delete course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56','course'),(251,18,'update','Update course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56','course'),(252,18,'view','View course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56','course'),(253,18,'submit','View course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56','course'),(254,44,'view','View team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42','team'),(255,44,'delete','Delete team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42','team'),(256,44,'update','Update team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42','team'),(257,45,'view','View team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26','team'),(258,45,'delete','Delete team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26','team'),(259,45,'update','Update team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26','team'),(260,2,'delete','Delete transaction 2','2024-10-01 12:53:28','2024-10-01 12:53:28','transaction'),(261,2,'view','View transaction 2','2024-10-01 12:53:28','2024-10-01 12:53:28','transaction'),(262,40,'view','View profile, id = 40','2024-10-01 14:41:23','2024-10-01 14:41:23','profile'),(263,40,'delete','Delete profile, id = 40','2024-10-01 14:41:23','2024-10-01 14:41:23','profile'),(264,40,'update','Update profile, id = 40','2024-10-01 14:41:23','2024-10-01 14:41:23','profile'),(265,41,'view','View profile, id = 41','2024-10-01 15:26:49','2024-10-01 15:26:49','profile'),(266,41,'delete','Delete profile, id = 41','2024-10-01 15:26:50','2024-10-01 15:26:50','profile'),(267,41,'update','Update profile, id = 41','2024-10-01 15:26:50','2024-10-01 15:26:50','profile'),(268,19,'delete','Delete course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38','course'),(269,19,'update','Update course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38','course'),(270,19,'view','View course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38','course'),(271,19,'submit','View course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38','course'),(272,3,'delete','Delete transaction 3','2024-10-01 15:33:46','2024-10-01 15:33:46','transaction'),(273,3,'view','View transaction 3','2024-10-01 15:33:46','2024-10-01 15:33:46','transaction'),(274,20,'delete','Delete course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44','course'),(275,20,'update','Update course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44','course'),(276,20,'view','View course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44','course'),(277,20,'submit','View course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44','course'),(278,4,'delete','Delete transaction 4','2024-10-01 16:07:05','2024-10-01 16:07:05','transaction'),(279,4,'view','View transaction 4','2024-10-01 16:07:05','2024-10-01 16:07:05','transaction'),(280,5,'delete','Delete transaction 5','2024-10-01 16:22:22','2024-10-01 16:22:22','transaction'),(281,5,'view','View transaction 5','2024-10-01 16:22:22','2024-10-01 16:22:22','transaction'),(282,42,'view','View profile, id = 42','2024-10-02 00:23:36','2024-10-02 00:23:36','profile'),(283,42,'delete','Delete profile, id = 42','2024-10-02 00:23:36','2024-10-02 00:23:36','profile'),(284,42,'update','Update profile, id = 42','2024-10-02 00:23:36','2024-10-02 00:23:36','profile'),(285,21,'delete','Delete course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03','course'),(286,21,'update','Update course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03','course'),(287,21,'view','View course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03','course'),(288,21,'submit','View course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03','course'),(289,22,'delete','Delete course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22','course'),(290,22,'update','Update course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22','course'),(291,22,'view','View course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22','course'),(292,22,'submit','View course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22','course'),(293,43,'view','View profile, id = 43','2024-10-02 09:54:28','2024-10-02 09:54:28','profile'),(294,43,'delete','Delete profile, id = 43','2024-10-02 09:54:28','2024-10-02 09:54:28','profile'),(295,43,'update','Update profile, id = 43','2024-10-02 09:54:28','2024-10-02 09:54:28','profile'),(296,44,'view','View profile, id = 44','2024-10-02 09:55:28','2024-10-02 09:55:28','profile'),(297,44,'delete','Delete profile, id = 44','2024-10-02 09:55:28','2024-10-02 09:55:28','profile'),(298,44,'update','Update profile, id = 44','2024-10-02 09:55:28','2024-10-02 09:55:28','profile'),(299,6,'delete','Delete transaction 6','2024-10-02 11:06:54','2024-10-02 11:06:54','transaction'),(300,6,'view','View transaction 6','2024-10-02 11:06:54','2024-10-02 11:06:54','transaction'),(301,46,'view','View team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37','team'),(302,46,'delete','Delete team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37','team'),(303,46,'update','Update team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37','team'),(304,47,'view','View team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00','team'),(305,47,'delete','Delete team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00','team'),(306,47,'update','Update team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00','team'),(307,45,'view','View profile, id = 45','2024-10-02 11:52:46','2024-10-02 11:52:46','profile'),(308,45,'delete','Delete profile, id = 45','2024-10-02 11:52:46','2024-10-02 11:52:46','profile'),(309,45,'update','Update profile, id = 45','2024-10-02 11:52:46','2024-10-02 11:52:46','profile'),(310,23,'delete','Delete course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22','course'),(311,23,'update','Update course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22','course'),(312,23,'view','View course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22','course'),(313,23,'submit','View course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22','course'),(314,24,'delete','Delete course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18','course'),(315,24,'update','Update course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18','course'),(316,24,'view','View course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18','course'),(317,24,'submit','View course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18','course'),(318,8,'delete','Delete course, id = 8','2024-10-02 15:48:02','2024-10-02 15:48:02','submission'),(319,8,'update','Update course, id = 8','2024-10-02 15:48:02','2024-10-02 15:48:02','submission'),(320,8,'view','View course, id = 8','2024-10-02 15:48:02','2024-10-02 15:48:02','submission'),(321,7,'delete','Delete course, id = 7','2024-10-02 15:51:34','2024-10-02 15:51:34','submission'),(322,7,'update','Update course, id = 7','2024-10-02 15:51:34','2024-10-02 15:51:34','submission'),(323,7,'view','View course, id = 7','2024-10-02 15:51:34','2024-10-02 15:51:34','submission');
/*!40000 ALTER TABLE `privileges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (92,'GLOBAL_TEAM_ADD','GLOBAL ROLE ADD TEAM','2024-09-26 16:00:31','2024-09-26 16:00:31'),(93,'GLOBAL_TRANSACTION_ADD','GLOBAL ROLE ADD TRANSACTION','2024-09-26 16:00:31','2024-09-26 16:00:31'),(94,'GLOBAL_COURSE_VIEW','GLOBAL ROLE VIEW COURSE','2024-09-26 16:00:31','2024-09-26 16:00:31'),(95,'GLOBAL_COURSE_ADD','GLOBAL ROLE ADD COURSE','2024-09-26 16:00:31','2024-09-26 16:00:31'),(96,'GLOBAL_TEAM_VIEW','GLOBAL ROLE VIEW TEAM','2024-09-26 16:00:31','2024-09-26 16:00:31'),(97,'ADMIN_PROFILE_23','ADMIN of profile, id = 23','2024-09-26 16:01:37','2024-09-26 16:01:37'),(98,'SYSTEM_ADMIN_*_-1','SYSTEM_ADMIN of *, id = -1','2024-09-26 16:11:14','2024-09-26 16:11:14'),(99,'EDITOR_TEAM_19','EDITOR of team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34'),(100,'ADMIN_TEAM_19','ADMIN of team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34'),(101,'VIEWER_TEAM_19','VIEWER of team, id = 19','2024-09-26 16:20:34','2024-09-26 16:20:34'),(102,'ADMIN_PROFILE_25','ADMIN of profile, id = 25','2024-09-26 16:34:51','2024-09-26 16:34:51'),(103,'EDITOR_TEAM_20','EDITOR of team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18'),(104,'ADMIN_TEAM_20','ADMIN of team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18'),(105,'VIEWER_TEAM_20','VIEWER of team, id = 20','2024-09-26 16:35:18','2024-09-26 16:35:18'),(106,'EDITOR_TEAM_21','EDITOR of team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43'),(107,'ADMIN_TEAM_21','ADMIN of team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43'),(108,'VIEWER_TEAM_21','VIEWER of team, id = 21','2024-09-26 16:36:43','2024-09-26 16:36:43'),(109,'EDITOR_TEAM_22','EDITOR of team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55'),(110,'ADMIN_TEAM_22','ADMIN of team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55'),(111,'VIEWER_TEAM_22','VIEWER of team, id = 22','2024-09-26 16:55:55','2024-09-26 16:55:55'),(112,'EDITOR_TEAM_23','EDITOR of team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22'),(113,'ADMIN_TEAM_23','ADMIN of team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22'),(114,'VIEWER_TEAM_23','VIEWER of team, id = 23','2024-09-26 17:09:22','2024-09-26 17:09:22'),(115,'EDITOR_TEAM_24','EDITOR of team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09'),(116,'ADMIN_TEAM_24','ADMIN of team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09'),(117,'VIEWER_TEAM_24','VIEWER of team, id = 24','2024-09-26 17:15:09','2024-09-26 17:15:09'),(118,'EDITOR_TEAM_25','EDITOR of team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15'),(119,'ADMIN_TEAM_25','ADMIN of team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15'),(120,'VIEWER_TEAM_25','VIEWER of team, id = 25','2024-09-26 17:15:15','2024-09-26 17:15:15'),(121,'EDITOR_TEAM_26','EDITOR of team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19'),(122,'ADMIN_TEAM_26','ADMIN of team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19'),(123,'VIEWER_TEAM_26','VIEWER of team, id = 26','2024-09-26 17:15:19','2024-09-26 17:15:19'),(124,'EDITOR_TEAM_27','EDITOR of team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23'),(125,'ADMIN_TEAM_27','ADMIN of team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23'),(126,'VIEWER_TEAM_27','VIEWER of team, id = 27','2024-09-26 17:15:23','2024-09-26 17:15:23'),(127,'ADMIN_PROFILE_26','ADMIN of profile, id = 26','2024-09-26 17:52:28','2024-09-26 17:52:28'),(128,'EDITOR_TEAM_28','EDITOR of team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42'),(129,'ADMIN_TEAM_28','ADMIN of team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42'),(130,'VIEWER_TEAM_28','VIEWER of team, id = 28','2024-09-26 17:52:42','2024-09-26 17:52:42'),(131,'ADMIN_PROFILE_27','ADMIN of profile, id = 27','2024-09-26 18:17:34','2024-09-26 18:17:34'),(132,'EDITOR_TEAM_30','EDITOR of team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21'),(133,'ADMIN_TEAM_30','ADMIN of team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21'),(134,'VIEWER_TEAM_30','VIEWER of team, id = 30','2024-09-26 18:19:21','2024-09-26 18:19:21'),(135,'ADMIN_PROFILE_28','ADMIN of profile, id = 28','2024-09-27 11:37:51','2024-09-27 11:37:51'),(137,'ADMIN_COURSE_3','ADMIN of course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11'),(138,'EDITOR_COURSE_3','EDITOR of course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11'),(139,'VIEWER_COURSE_3','VIEWER of course, id = 3','2024-09-27 14:55:11','2024-09-27 14:55:11'),(140,'ADMIN_COURSE_4','ADMIN of course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49'),(141,'EDITOR_COURSE_4','EDITOR of course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49'),(142,'VIEWER_COURSE_4','VIEWER of course, id = 4','2024-09-27 15:10:49','2024-09-27 15:10:49'),(143,'ADMIN_COURSE_5','ADMIN of course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52'),(144,'EDITOR_COURSE_5','EDITOR of course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52'),(145,'VIEWER_COURSE_5','VIEWER of course, id = 5','2024-09-27 15:10:52','2024-09-27 15:10:52'),(146,'ADMIN_COURSE_6','ADMIN of course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55'),(147,'EDITOR_COURSE_6','EDITOR of course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55'),(148,'VIEWER_COURSE_6','VIEWER of course, id = 6','2024-09-27 15:10:55','2024-09-27 15:10:55'),(149,'ADMIN_COURSE_7','ADMIN of course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58'),(150,'EDITOR_COURSE_7','EDITOR of course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58'),(151,'VIEWER_COURSE_7','VIEWER of course, id = 7','2024-09-27 15:10:58','2024-09-27 15:10:58'),(152,'EDITOR_TEAM_31','EDITOR of team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28'),(153,'ADMIN_TEAM_31','ADMIN of team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28'),(154,'VIEWER_TEAM_31','VIEWER of team, id = 31','2024-09-27 16:26:28','2024-09-27 16:26:28'),(155,'EDITOR_TEAM_32','EDITOR of team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47'),(156,'ADMIN_TEAM_32','ADMIN of team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47'),(157,'VIEWER_TEAM_32','VIEWER of team, id = 32','2024-09-27 16:28:47','2024-09-27 16:28:47'),(158,'EDITOR_TEAM_33','EDITOR of team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21'),(159,'ADMIN_TEAM_33','ADMIN of team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21'),(160,'VIEWER_TEAM_33','VIEWER of team, id = 33','2024-09-27 16:30:21','2024-09-27 16:30:21'),(161,'EDITOR_TEAM_34','EDITOR of team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32'),(162,'ADMIN_TEAM_34','ADMIN of team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32'),(163,'VIEWER_TEAM_34','VIEWER of team, id = 34','2024-09-27 16:32:32','2024-09-27 16:32:32'),(164,'EDITOR_TEAM_35','EDITOR of team, id = 35','2024-09-27 16:39:56','2024-09-27 16:39:56'),(165,'ADMIN_TEAM_35','ADMIN of team, id = 35','2024-09-27 16:39:56','2024-09-27 16:39:56'),(166,'VIEWER_TEAM_35','VIEWER of team, id = 35','2024-09-27 16:39:56','2024-09-27 16:39:56'),(167,'ADMIN_PROFILE_29','ADMIN of profile, id = 29','2024-09-28 00:34:47','2024-09-28 00:34:47'),(168,'EDITOR_TEAM_36','EDITOR of team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26'),(169,'ADMIN_TEAM_36','ADMIN of team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26'),(170,'VIEWER_TEAM_36','VIEWER of team, id = 36','2024-09-28 00:37:26','2024-09-28 00:37:26'),(171,'EDITOR_TEAM_37','EDITOR of team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33'),(172,'ADMIN_TEAM_37','ADMIN of team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33'),(173,'VIEWER_TEAM_37','VIEWER of team, id = 37','2024-09-28 00:39:33','2024-09-28 00:39:33'),(174,'EDITOR_TEAM_38','EDITOR of team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08'),(175,'ADMIN_TEAM_38','ADMIN of team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08'),(176,'VIEWER_TEAM_38','VIEWER of team, id = 38','2024-09-28 00:47:08','2024-09-28 00:47:08'),(177,'EDITOR_TEAM_39','EDITOR of team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22'),(178,'ADMIN_TEAM_39','ADMIN of team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22'),(179,'VIEWER_TEAM_39','VIEWER of team, id = 39','2024-09-28 00:49:22','2024-09-28 00:49:22'),(180,'EDITOR_TEAM_40','EDITOR of team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36'),(181,'ADMIN_TEAM_40','ADMIN of team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36'),(182,'VIEWER_TEAM_40','VIEWER of team, id = 40','2024-09-28 00:53:36','2024-09-28 00:53:36'),(183,'ADMIN_PROFILE_30','ADMIN of profile, id = 30','2024-09-28 09:27:41','2024-09-28 09:27:41'),(184,'EDITOR_TEAM_41','EDITOR of team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05'),(185,'ADMIN_TEAM_41','ADMIN of team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05'),(186,'VIEWER_TEAM_41','VIEWER of team, id = 41','2024-09-28 09:31:05','2024-09-28 09:31:05'),(187,'EDITOR_TEAM_42','EDITOR of team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31'),(188,'ADMIN_TEAM_42','ADMIN of team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31'),(189,'VIEWER_TEAM_42','VIEWER of team, id = 42','2024-09-28 09:36:31','2024-09-28 09:36:31'),(190,'ADMIN_COURSE_8','ADMIN of course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19'),(191,'EDITOR_COURSE_8','EDITOR of course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19'),(192,'VIEWER_COURSE_8','VIEWER of course, id = 8','2024-09-28 09:38:19','2024-09-28 09:38:19'),(193,'ADMIN_COURSE_9','ADMIN of course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47'),(194,'EDITOR_COURSE_9','EDITOR of course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47'),(195,'STUDENT_COURSE_9','STUDENT of course, id = 9','2024-09-28 10:26:47','2024-09-28 10:26:47'),(196,'ADMIN_PROFILE_31','ADMIN of profile, id = 31','2024-09-28 10:30:02','2024-09-28 10:30:02'),(197,'ADMIN_PROFILE_32','ADMIN of profile, id = 32','2024-09-28 10:35:45','2024-09-28 10:35:45'),(198,'ADMIN_COURSE_10','ADMIN of course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43'),(199,'EDITOR_COURSE_10','EDITOR of course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43'),(200,'STUDENT_COURSE_10','STUDENT of course, id = 10','2024-09-28 10:51:43','2024-09-28 10:51:43'),(201,'ADMIN_COURSE_11','ADMIN of course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26'),(202,'EDITOR_COURSE_11','EDITOR of course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26'),(203,'STUDENT_COURSE_11','STUDENT of course, id = 11','2024-09-28 10:56:26','2024-09-28 10:56:26'),(204,'ADMIN_COURSE_12','ADMIN of course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03'),(205,'EDITOR_COURSE_12','EDITOR of course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03'),(206,'STUDENT_COURSE_12','STUDENT of course, id = 12','2024-09-28 11:02:03','2024-09-28 11:02:03'),(207,'ADMIN_SUBMISSION_4','ADMIN of submission, id = 4','2024-09-29 18:27:09','2024-09-29 18:27:09'),(208,'ADMIN_SUBMISSION_5','ADMIN of submission, id = 5','2024-09-29 18:27:33','2024-09-29 18:27:33'),(209,'ADMIN_SUBMISSION_6','ADMIN of submission, id = 6','2024-09-29 21:19:38','2024-09-29 21:19:38'),(210,'ADMIN_SUBMISSION_6','ADMIN of submission, id = 6','2024-09-29 21:24:47','2024-09-29 21:24:47'),(211,'ADMIN_PROFILE_33','ADMIN of profile, id = 33','2024-09-30 14:48:49','2024-09-30 14:48:49'),(212,'SYSTEM_ADMIN_*_-1','SYSTEM_ADMIN of *, id = -1','2024-09-30 16:17:54','2024-09-30 16:17:54'),(213,'EDITOR_TEAM_43','EDITOR of team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01'),(214,'ADMIN_TEAM_43','ADMIN of team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01'),(215,'VIEWER_TEAM_43','VIEWER of team, id = 43','2024-09-30 17:25:01','2024-09-30 17:25:01'),(216,'ADMIN_PROFILE_35','ADMIN of profile, id = 35','2024-09-30 23:27:13','2024-09-30 23:27:13'),(217,'ADMIN_COURSE_13','ADMIN of course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42'),(218,'EDITOR_COURSE_13','EDITOR of course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42'),(219,'STUDENT_COURSE_13','STUDENT of course, id = 13','2024-09-30 23:37:42','2024-09-30 23:37:42'),(220,'ADMIN_COURSE_14','ADMIN of course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35'),(221,'EDITOR_COURSE_14','EDITOR of course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35'),(222,'STUDENT_COURSE_14','STUDENT of course, id = 14','2024-09-30 23:39:35','2024-09-30 23:39:35'),(223,'ADMIN_COURSE_15','ADMIN of course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40'),(224,'EDITOR_COURSE_15','EDITOR of course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40'),(225,'STUDENT_COURSE_15','STUDENT of course, id = 15','2024-09-30 23:39:40','2024-09-30 23:39:40'),(226,'ADMIN_PROFILE_36','ADMIN of profile, id = 36','2024-09-30 23:42:08','2024-09-30 23:42:08'),(227,'ADMIN_COURSE_16','ADMIN of course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23'),(228,'EDITOR_COURSE_16','EDITOR of course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23'),(229,'STUDENT_COURSE_16','STUDENT of course, id = 16','2024-10-01 00:07:23','2024-10-01 00:07:23'),(230,'ADMIN_COURSE_17','ADMIN of course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07'),(231,'EDITOR_COURSE_17','EDITOR of course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07'),(232,'STUDENT_COURSE_17','STUDENT of course, id = 17','2024-10-01 00:15:07','2024-10-01 00:15:07'),(233,'ADMIN_SUBMISSION_7','ADMIN of submission, id = 7','2024-10-01 00:21:14','2024-10-01 00:21:14'),(234,'ADMIN_PROFILE_37','ADMIN of profile, id = 37','2024-10-01 09:13:40','2024-10-01 09:13:40'),(235,'ADMIN_PROFILE_38','ADMIN of profile, id = 38','2024-10-01 09:32:44','2024-10-01 09:32:44'),(236,'ADMIN_PROFILE_39','ADMIN of profile, id = 39','2024-10-01 09:35:20','2024-10-01 09:35:20'),(237,'ADMIN_COURSE_18','ADMIN of course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56'),(238,'EDITOR_COURSE_18','EDITOR of course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56'),(239,'STUDENT_COURSE_18','STUDENT of course, id = 18','2024-10-01 11:26:56','2024-10-01 11:26:56'),(240,'EDITOR_TEAM_44','EDITOR of team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42'),(241,'ADMIN_TEAM_44','ADMIN of team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42'),(242,'VIEWER_TEAM_44','VIEWER of team, id = 44','2024-10-01 11:27:42','2024-10-01 11:27:42'),(243,'EDITOR_TEAM_45','EDITOR of team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26'),(244,'ADMIN_TEAM_45','ADMIN of team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26'),(245,'VIEWER_TEAM_45','VIEWER of team, id = 45','2024-10-01 12:37:26','2024-10-01 12:37:26'),(246,'ADMIN_TRANSACTION_2','admin of transaction, id = 2','2024-10-01 12:53:28','2024-10-01 12:53:28'),(247,'ADMIN_PROFILE_40','ADMIN of profile, id = 40','2024-10-01 14:41:24','2024-10-01 14:41:24'),(248,'ADMIN_PROFILE_41','ADMIN of profile, id = 41','2024-10-01 15:26:50','2024-10-01 15:26:50'),(249,'ADMIN_COURSE_19','ADMIN of course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38'),(250,'EDITOR_COURSE_19','EDITOR of course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38'),(251,'STUDENT_COURSE_19','STUDENT of course, id = 19','2024-10-01 15:27:38','2024-10-01 15:27:38'),(252,'ADMIN_TRANSACTION_3','admin of transaction, id = 3','2024-10-01 15:33:46','2024-10-01 15:33:46'),(253,'ADMIN_COURSE_20','ADMIN of course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44'),(254,'EDITOR_COURSE_20','EDITOR of course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44'),(255,'STUDENT_COURSE_20','STUDENT of course, id = 20','2024-10-01 16:05:44','2024-10-01 16:05:44'),(256,'ADMIN_TRANSACTION_4','admin of transaction, id = 4','2024-10-01 16:07:05','2024-10-01 16:07:05'),(257,'ADMIN_TRANSACTION_5','admin of transaction, id = 5','2024-10-01 16:22:22','2024-10-01 16:22:22'),(258,'ADMIN_PROFILE_42','ADMIN of profile, id = 42','2024-10-02 00:23:36','2024-10-02 00:23:36'),(259,'ADMIN_COURSE_21','ADMIN of course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03'),(260,'EDITOR_COURSE_21','EDITOR of course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03'),(261,'STUDENT_COURSE_21','STUDENT of course, id = 21','2024-10-02 00:24:03','2024-10-02 00:24:03'),(262,'ADMIN_COURSE_22','ADMIN of course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22'),(263,'EDITOR_COURSE_22','EDITOR of course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22'),(264,'STUDENT_COURSE_22','STUDENT of course, id = 22','2024-10-02 01:35:22','2024-10-02 01:35:22'),(265,'ADMIN_PROFILE_43','ADMIN of profile, id = 43','2024-10-02 09:54:28','2024-10-02 09:54:28'),(266,'ADMIN_PROFILE_44','ADMIN of profile, id = 44','2024-10-02 09:55:28','2024-10-02 09:55:28'),(267,'ADMIN_TRANSACTION_6','admin of transaction, id = 6','2024-10-02 11:06:54','2024-10-02 11:06:54'),(268,'EDITOR_TEAM_46','EDITOR of team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37'),(269,'ADMIN_TEAM_46','ADMIN of team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37'),(270,'VIEWER_TEAM_46','VIEWER of team, id = 46','2024-10-02 11:23:37','2024-10-02 11:23:37'),(271,'EDITOR_TEAM_47','EDITOR of team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00'),(272,'ADMIN_TEAM_47','ADMIN of team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00'),(273,'VIEWER_TEAM_47','VIEWER of team, id = 47','2024-10-02 11:45:00','2024-10-02 11:45:00'),(274,'ADMIN_PROFILE_45','ADMIN of profile, id = 45','2024-10-02 11:52:46','2024-10-02 11:52:46'),(275,'ADMIN_COURSE_23','ADMIN of course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22'),(276,'EDITOR_COURSE_23','EDITOR of course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22'),(277,'STUDENT_COURSE_23','STUDENT of course, id = 23','2024-10-02 14:20:22','2024-10-02 14:20:22'),(278,'ADMIN_COURSE_24','ADMIN of course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18'),(279,'EDITOR_COURSE_24','EDITOR of course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18'),(280,'STUDENT_COURSE_24','STUDENT of course, id = 24','2024-10-02 15:25:18','2024-10-02 15:25:18'),(281,'ADMIN_SUBMISSION_8','ADMIN of submission, id = 8','2024-10-02 15:48:02','2024-10-02 15:48:02'),(282,'ADMIN_SUBMISSION_7','ADMIN of submission, id = 7','2024-10-02 15:51:34','2024-10-02 15:51:34');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_privileges`
--

DROP TABLE IF EXISTS `roles_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_privileges` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `privilege_id` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  KEY `privilege_id` (`privilege_id`),
  CONSTRAINT `roles_privileges_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `roles_privileges_ibfk_2` FOREIGN KEY (`privilege_id`) REFERENCES `privileges` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=523 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_privileges`
--

LOCK TABLES `roles_privileges` WRITE;
/*!40000 ALTER TABLE `roles_privileges` DISABLE KEYS */;
INSERT INTO `roles_privileges` VALUES (97,97,64,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(98,97,63,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(99,97,65,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(100,99,68,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(101,99,67,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(102,100,68,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(103,100,67,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(104,100,66,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(105,101,66,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(106,102,69,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(107,102,71,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(108,102,70,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(109,103,73,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(110,103,74,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(111,104,72,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(112,104,73,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(113,104,74,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(114,105,72,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(115,106,76,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(116,106,77,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(117,107,76,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(118,107,77,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(119,107,75,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(120,108,75,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(121,109,80,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(122,109,79,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(123,110,80,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(124,110,78,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(125,110,79,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(126,111,78,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(127,112,83,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(128,112,82,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(129,113,81,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(130,113,83,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(131,113,82,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(132,114,81,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(133,115,86,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(134,115,85,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(135,116,84,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(136,116,86,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(137,116,85,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(138,117,84,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(139,118,88,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(140,118,89,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(141,119,87,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(142,119,88,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(143,119,89,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(144,120,87,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(145,121,91,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(146,121,92,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(147,122,91,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(148,122,90,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(149,122,92,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(150,123,90,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(151,124,95,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(152,124,94,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(153,125,93,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(154,125,95,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(155,125,94,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(156,126,93,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(157,127,98,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(158,127,97,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(159,127,96,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(160,128,100,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(161,128,101,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(162,129,100,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(163,129,99,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(164,129,101,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(165,130,99,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(166,131,103,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(167,131,102,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(168,131,104,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(169,132,107,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(170,132,108,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(171,133,107,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(172,133,106,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(173,133,108,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(174,134,106,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(175,135,111,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(176,135,109,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(177,135,110,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(178,137,112,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(179,137,114,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(180,137,113,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(181,138,112,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(182,138,113,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(183,139,114,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(184,140,116,'2024-09-27 15:10:48','2024-09-27 15:10:48'),(185,140,117,'2024-09-27 15:10:48','2024-09-27 15:10:48'),(186,140,115,'2024-09-27 15:10:48','2024-09-27 15:10:48'),(187,141,116,'2024-09-27 15:10:49','2024-09-27 15:10:49'),(188,141,115,'2024-09-27 15:10:49','2024-09-27 15:10:49'),(189,142,117,'2024-09-27 15:10:49','2024-09-27 15:10:49'),(190,143,120,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(191,143,118,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(192,143,119,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(193,144,118,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(194,144,119,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(195,145,120,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(196,146,123,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(197,146,121,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(198,146,122,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(199,147,121,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(200,147,122,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(201,148,123,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(202,149,126,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(203,149,125,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(204,149,124,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(205,150,125,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(206,150,124,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(207,151,126,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(208,152,128,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(209,152,129,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(210,153,129,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(211,153,128,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(212,153,127,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(213,154,127,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(214,155,131,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(215,155,132,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(216,156,130,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(217,156,131,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(218,156,132,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(219,157,130,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(220,158,134,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(221,158,135,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(222,159,133,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(223,159,134,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(224,159,135,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(225,160,133,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(226,161,137,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(227,161,138,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(228,162,136,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(229,162,137,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(230,162,138,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(231,163,136,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(232,164,140,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(233,164,141,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(234,165,140,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(235,165,139,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(236,165,141,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(237,166,139,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(238,167,144,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(239,167,142,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(240,167,143,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(241,168,146,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(242,168,147,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(243,169,145,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(244,169,146,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(245,169,147,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(246,170,145,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(247,171,150,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(248,171,149,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(249,172,148,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(250,172,150,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(251,172,149,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(252,173,148,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(253,174,152,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(254,174,153,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(255,175,152,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(256,175,151,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(257,175,153,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(258,176,151,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(259,177,155,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(260,177,156,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(261,178,155,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(262,178,156,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(263,178,154,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(264,179,154,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(265,180,159,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(266,180,158,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(267,181,159,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(268,181,158,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(269,181,157,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(270,182,157,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(271,183,160,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(272,183,162,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(273,183,161,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(274,184,165,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(275,184,164,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(276,185,165,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(277,185,164,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(278,185,163,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(279,186,163,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(280,187,168,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(281,187,167,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(282,188,166,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(283,188,168,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(284,188,167,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(285,189,166,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(286,190,170,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(287,190,171,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(288,190,169,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(289,191,170,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(290,191,169,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(291,192,171,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(292,193,175,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(293,193,172,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(294,193,174,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(295,193,173,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(296,194,172,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(297,194,173,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(298,195,175,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(299,195,174,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(300,196,178,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(301,196,177,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(302,196,176,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(303,197,181,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(304,197,179,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(305,197,180,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(306,198,184,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(307,198,182,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(308,198,185,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(309,198,183,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(310,199,182,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(311,199,183,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(312,200,184,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(313,200,185,'2024-09-28 10:51:43','2024-09-28 10:51:43'),(314,201,188,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(315,201,187,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(316,201,189,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(317,201,186,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(318,202,187,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(319,202,186,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(320,203,188,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(321,203,189,'2024-09-28 10:56:26','2024-09-28 10:56:26'),(322,204,193,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(323,204,192,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(324,204,190,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(325,204,191,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(326,205,191,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(327,205,190,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(328,206,193,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(329,206,192,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(330,207,195,'2024-09-29 18:27:08','2024-09-29 18:27:08'),(331,207,194,'2024-09-29 18:27:08','2024-09-29 18:27:08'),(332,207,196,'2024-09-29 18:27:08','2024-09-29 18:27:08'),(333,208,198,'2024-09-29 18:27:32','2024-09-29 18:27:32'),(334,208,197,'2024-09-29 18:27:32','2024-09-29 18:27:32'),(335,208,199,'2024-09-29 18:27:32','2024-09-29 18:27:32'),(336,209,202,'2024-09-29 21:19:37','2024-09-29 21:19:37'),(337,209,201,'2024-09-29 21:19:37','2024-09-29 21:19:37'),(338,209,200,'2024-09-29 21:19:37','2024-09-29 21:19:37'),(339,210,203,'2024-09-29 21:24:47','2024-09-29 21:24:47'),(340,210,204,'2024-09-29 21:24:47','2024-09-29 21:24:47'),(341,210,205,'2024-09-29 21:24:47','2024-09-29 21:24:47'),(342,211,208,'2024-09-30 14:48:49','2024-09-30 14:48:49'),(343,211,207,'2024-09-30 14:48:49','2024-09-30 14:48:49'),(344,211,206,'2024-09-30 14:48:49','2024-09-30 14:48:49'),(345,213,210,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(346,213,211,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(347,214,210,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(348,214,209,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(349,214,211,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(350,215,209,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(351,216,213,'2024-09-30 23:27:13','2024-09-30 23:27:13'),(352,216,212,'2024-09-30 23:27:13','2024-09-30 23:27:13'),(353,216,214,'2024-09-30 23:27:13','2024-09-30 23:27:13'),(354,217,217,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(355,217,215,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(356,217,216,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(357,217,218,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(358,218,215,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(359,218,216,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(360,219,217,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(361,219,218,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(362,220,219,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(363,220,220,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(364,220,221,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(365,220,222,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(366,221,220,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(367,221,219,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(368,222,221,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(369,222,222,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(370,223,225,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(371,223,226,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(372,223,223,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(373,223,224,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(374,224,223,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(375,224,224,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(376,225,225,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(377,225,226,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(378,226,227,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(379,226,229,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(380,226,228,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(381,227,230,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(382,227,231,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(383,227,232,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(384,227,233,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(385,228,231,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(386,228,230,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(387,229,232,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(388,229,233,'2024-10-01 00:07:23','2024-10-01 00:07:23'),(389,230,236,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(390,230,234,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(391,230,235,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(392,230,237,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(393,231,234,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(394,231,235,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(395,232,236,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(396,232,237,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(397,233,238,'2024-10-01 00:21:14','2024-10-01 00:21:14'),(398,233,240,'2024-10-01 00:21:14','2024-10-01 00:21:14'),(399,233,239,'2024-10-01 00:21:14','2024-10-01 00:21:14'),(400,234,241,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(401,234,243,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(402,234,242,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(403,235,245,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(404,235,244,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(405,235,246,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(406,236,249,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(407,236,248,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(408,236,247,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(409,237,253,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(410,237,250,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(411,237,251,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(412,237,252,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(413,238,250,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(414,238,251,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(415,239,253,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(416,239,252,'2024-10-01 11:26:55','2024-10-01 11:26:55'),(417,240,256,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(418,240,255,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(419,241,254,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(420,241,256,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(421,241,255,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(422,242,254,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(423,243,258,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(424,243,259,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(425,244,258,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(426,244,259,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(427,244,257,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(428,245,257,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(429,246,261,'2024-10-01 12:53:27','2024-10-01 12:53:27'),(430,246,260,'2024-10-01 12:53:27','2024-10-01 12:53:27'),(431,247,263,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(432,247,264,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(433,247,262,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(434,248,267,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(435,248,266,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(436,248,265,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(437,249,270,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(438,249,269,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(439,249,271,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(440,249,268,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(441,250,269,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(442,250,268,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(443,251,270,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(444,251,271,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(445,252,273,'2024-10-01 15:33:45','2024-10-01 15:33:45'),(446,252,272,'2024-10-01 15:33:45','2024-10-01 15:33:45'),(447,253,275,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(448,253,274,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(449,253,277,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(450,253,276,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(451,254,275,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(452,254,274,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(453,255,277,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(454,255,276,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(455,256,279,'2024-10-01 16:07:04','2024-10-01 16:07:04'),(456,256,278,'2024-10-01 16:07:04','2024-10-01 16:07:04'),(457,257,280,'2024-10-01 16:22:22','2024-10-01 16:22:22'),(458,257,281,'2024-10-01 16:22:22','2024-10-01 16:22:22'),(459,258,283,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(460,258,282,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(461,258,284,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(462,259,287,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(463,259,285,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(464,259,288,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(465,259,286,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(466,260,285,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(467,260,286,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(468,261,287,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(469,261,288,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(470,262,291,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(471,262,289,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(472,262,290,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(473,262,292,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(474,263,289,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(475,263,290,'2024-10-02 01:35:21','2024-10-02 01:35:21'),(476,264,291,'2024-10-02 01:35:22','2024-10-02 01:35:22'),(477,264,292,'2024-10-02 01:35:22','2024-10-02 01:35:22'),(478,265,294,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(479,265,293,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(480,265,295,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(481,266,296,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(482,266,297,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(483,266,298,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(484,267,300,'2024-10-02 11:06:54','2024-10-02 11:06:54'),(485,267,299,'2024-10-02 11:06:54','2024-10-02 11:06:54'),(486,268,303,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(487,268,302,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(488,269,301,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(489,269,303,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(490,269,302,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(491,270,301,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(492,271,305,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(493,271,306,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(494,272,305,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(495,272,304,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(496,272,306,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(497,273,304,'2024-10-02 11:45:00','2024-10-02 11:45:00'),(498,274,308,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(499,274,307,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(500,274,309,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(501,275,312,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(502,275,313,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(503,275,311,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(504,275,310,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(505,276,311,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(506,276,310,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(507,277,312,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(508,277,313,'2024-10-02 14:20:22','2024-10-02 14:20:22'),(509,278,315,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(510,278,316,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(511,278,317,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(512,278,314,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(513,279,315,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(514,279,314,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(515,280,316,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(516,280,317,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(517,281,320,'2024-10-02 15:48:01','2024-10-02 15:48:01'),(518,281,318,'2024-10-02 15:48:01','2024-10-02 15:48:01'),(519,281,319,'2024-10-02 15:48:01','2024-10-02 15:48:01'),(520,282,322,'2024-10-02 15:51:34','2024-10-02 15:51:34'),(521,282,323,'2024-10-02 15:51:34','2024-10-02 15:51:34'),(522,282,321,'2024-10-02 15:51:34','2024-10-02 15:51:34');
/*!40000 ALTER TABLE `roles_privileges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_users_teams`
--

DROP TABLE IF EXISTS `roles_users_teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_users_teams` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `team_id` (`team_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `roles_users_teams_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `roles_users_teams_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `roles_users_teams_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=395 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_users_teams`
--

LOCK TABLES `roles_users_teams` WRITE;
/*!40000 ALTER TABLE `roles_users_teams` DISABLE KEYS */;
INSERT INTO `roles_users_teams` VALUES (84,23,NULL,92,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(85,23,NULL,96,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(86,23,NULL,95,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(87,23,NULL,94,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(88,23,NULL,93,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(89,23,NULL,97,NULL,'2024-09-26 16:01:36','2024-09-26 16:01:36'),(90,24,NULL,92,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(91,24,NULL,96,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(92,24,NULL,95,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(93,24,NULL,94,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(94,24,NULL,93,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(95,24,NULL,98,NULL,'2024-09-26 16:11:13','2024-09-26 16:11:13'),(96,24,19,NULL,NULL,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(97,24,NULL,100,NULL,'2024-09-26 16:20:33','2024-09-26 16:20:33'),(98,25,NULL,92,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(99,25,NULL,96,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(100,25,NULL,95,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(101,25,NULL,94,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(102,25,NULL,93,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(103,25,NULL,102,NULL,'2024-09-26 16:34:51','2024-09-26 16:34:51'),(104,25,20,NULL,NULL,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(105,25,NULL,104,NULL,'2024-09-26 16:35:18','2024-09-26 16:35:18'),(106,25,21,NULL,NULL,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(107,25,NULL,107,NULL,'2024-09-26 16:36:43','2024-09-26 16:36:43'),(108,25,22,NULL,NULL,'2024-09-26 16:55:54','2024-09-26 16:55:54'),(109,25,NULL,110,NULL,'2024-09-26 16:55:55','2024-09-26 16:55:55'),(110,25,23,NULL,NULL,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(111,25,NULL,113,NULL,'2024-09-26 17:09:21','2024-09-26 17:09:21'),(112,25,24,NULL,NULL,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(113,25,NULL,116,NULL,'2024-09-26 17:15:08','2024-09-26 17:15:08'),(114,25,25,NULL,NULL,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(115,25,NULL,119,NULL,'2024-09-26 17:15:14','2024-09-26 17:15:14'),(116,25,26,NULL,NULL,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(117,25,NULL,122,NULL,'2024-09-26 17:15:19','2024-09-26 17:15:19'),(118,25,27,NULL,NULL,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(119,25,NULL,125,NULL,'2024-09-26 17:15:23','2024-09-26 17:15:23'),(120,26,NULL,92,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(121,26,NULL,96,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(122,26,NULL,95,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(123,26,NULL,94,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(124,26,NULL,93,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(125,26,NULL,127,NULL,'2024-09-26 17:52:27','2024-09-26 17:52:27'),(127,26,NULL,129,NULL,'2024-09-26 17:52:42','2024-09-26 17:52:42'),(128,26,27,NULL,NULL,'2024-09-26 18:05:21','2024-09-26 18:05:21'),(129,27,NULL,92,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(130,27,NULL,96,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(131,27,NULL,95,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(132,27,NULL,94,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(133,27,NULL,93,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(134,27,NULL,131,NULL,'2024-09-26 18:17:34','2024-09-26 18:17:34'),(137,27,NULL,133,NULL,'2024-09-26 18:19:21','2024-09-26 18:19:21'),(142,26,NULL,134,NULL,'2024-09-26 18:20:21','2024-09-26 18:20:21'),(143,28,NULL,92,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(144,28,NULL,96,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(145,28,NULL,95,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(146,28,NULL,94,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(147,28,NULL,93,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(148,28,NULL,135,NULL,'2024-09-27 11:37:50','2024-09-27 11:37:50'),(149,28,NULL,137,NULL,'2024-09-27 14:55:10','2024-09-27 14:55:10'),(150,28,NULL,140,NULL,'2024-09-27 15:10:49','2024-09-27 15:10:49'),(151,28,NULL,143,NULL,'2024-09-27 15:10:52','2024-09-27 15:10:52'),(152,28,NULL,146,NULL,'2024-09-27 15:10:55','2024-09-27 15:10:55'),(153,28,NULL,149,NULL,'2024-09-27 15:10:58','2024-09-27 15:10:58'),(155,27,NULL,153,NULL,'2024-09-27 16:26:28','2024-09-27 16:26:28'),(160,27,NULL,156,NULL,'2024-09-27 16:28:47','2024-09-27 16:28:47'),(165,27,NULL,159,NULL,'2024-09-27 16:30:21','2024-09-27 16:30:21'),(170,27,NULL,162,NULL,'2024-09-27 16:32:31','2024-09-27 16:32:31'),(175,27,NULL,165,NULL,'2024-09-27 16:39:55','2024-09-27 16:39:55'),(179,29,NULL,92,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(180,29,NULL,96,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(181,29,NULL,95,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(182,29,NULL,94,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(183,29,NULL,93,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(184,29,NULL,167,NULL,'2024-09-28 00:34:46','2024-09-28 00:34:46'),(186,29,NULL,169,NULL,'2024-09-28 00:37:26','2024-09-28 00:37:26'),(191,29,NULL,172,NULL,'2024-09-28 00:39:32','2024-09-28 00:39:32'),(197,NULL,38,176,NULL,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(198,NULL,38,175,NULL,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(199,NULL,38,174,NULL,'2024-09-28 00:47:08','2024-09-28 00:47:08'),(201,29,NULL,178,NULL,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(202,NULL,39,178,NULL,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(203,NULL,39,179,NULL,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(204,NULL,39,177,NULL,'2024-09-28 00:49:22','2024-09-28 00:49:22'),(207,NULL,40,180,NULL,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(208,NULL,40,181,NULL,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(209,NULL,40,182,NULL,'2024-09-28 00:53:35','2024-09-28 00:53:35'),(210,29,NULL,151,NULL,'2024-09-28 01:25:40','2024-09-28 01:25:40'),(211,30,NULL,92,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(212,30,NULL,96,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(213,30,NULL,95,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(214,30,NULL,94,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(215,30,NULL,93,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(216,30,NULL,183,NULL,'2024-09-28 09:27:40','2024-09-28 09:27:40'),(219,NULL,41,186,NULL,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(220,NULL,41,185,NULL,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(221,NULL,41,184,NULL,'2024-09-28 09:31:04','2024-09-28 09:31:04'),(224,30,42,NULL,NULL,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(225,30,NULL,188,NULL,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(226,NULL,42,188,NULL,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(227,NULL,42,187,NULL,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(228,NULL,42,189,NULL,'2024-09-28 09:36:30','2024-09-28 09:36:30'),(229,30,NULL,190,NULL,'2024-09-28 09:38:19','2024-09-28 09:38:19'),(230,30,NULL,151,NULL,'2024-09-28 09:45:22','2024-09-28 09:45:22'),(231,30,NULL,193,NULL,'2024-09-28 10:26:47','2024-09-28 10:26:47'),(232,31,NULL,92,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(233,31,NULL,96,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(234,31,NULL,95,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(235,31,NULL,94,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(236,31,NULL,93,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(237,31,NULL,196,NULL,'2024-09-28 10:30:02','2024-09-28 10:30:02'),(238,31,NULL,195,NULL,'2024-09-28 10:30:19','2024-09-28 10:30:19'),(240,32,NULL,92,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(241,32,NULL,96,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(242,32,NULL,95,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(243,32,NULL,94,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(244,32,NULL,93,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(245,32,NULL,197,NULL,'2024-09-28 10:35:45','2024-09-28 10:35:45'),(251,30,NULL,204,NULL,'2024-09-28 11:02:03','2024-09-28 11:02:03'),(253,32,NULL,207,NULL,'2024-09-29 18:27:08','2024-09-29 18:27:08'),(254,32,NULL,208,NULL,'2024-09-29 18:27:32','2024-09-29 18:27:32'),(255,32,NULL,209,NULL,'2024-09-29 21:19:37','2024-09-29 21:19:37'),(256,32,NULL,210,NULL,'2024-09-29 21:24:47','2024-09-29 21:24:47'),(257,33,NULL,92,NULL,'2024-09-30 14:48:48','2024-09-30 14:48:48'),(258,33,NULL,96,NULL,'2024-09-30 14:48:48','2024-09-30 14:48:48'),(259,33,NULL,95,NULL,'2024-09-30 14:48:48','2024-09-30 14:48:48'),(260,33,NULL,94,NULL,'2024-09-30 14:48:48','2024-09-30 14:48:48'),(261,33,NULL,93,NULL,'2024-09-30 14:48:48','2024-09-30 14:48:48'),(262,33,NULL,211,NULL,'2024-09-30 14:48:49','2024-09-30 14:48:49'),(263,34,NULL,92,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(264,34,NULL,96,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(265,34,NULL,95,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(266,34,NULL,94,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(267,34,NULL,93,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(268,34,NULL,212,NULL,'2024-09-30 16:17:53','2024-09-30 16:17:53'),(269,33,43,NULL,NULL,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(270,33,NULL,214,NULL,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(271,NULL,43,215,NULL,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(272,NULL,43,214,NULL,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(273,NULL,43,213,NULL,'2024-09-30 17:25:00','2024-09-30 17:25:00'),(282,35,NULL,92,NULL,'2024-09-30 23:27:12','2024-09-30 23:27:12'),(283,35,NULL,96,NULL,'2024-09-30 23:27:12','2024-09-30 23:27:12'),(284,35,NULL,95,NULL,'2024-09-30 23:27:12','2024-09-30 23:27:12'),(285,35,NULL,94,NULL,'2024-09-30 23:27:12','2024-09-30 23:27:12'),(286,35,NULL,93,NULL,'2024-09-30 23:27:12','2024-09-30 23:27:12'),(287,35,NULL,216,NULL,'2024-09-30 23:27:13','2024-09-30 23:27:13'),(288,35,NULL,217,NULL,'2024-09-30 23:37:42','2024-09-30 23:37:42'),(289,35,NULL,220,NULL,'2024-09-30 23:39:35','2024-09-30 23:39:35'),(290,35,NULL,223,NULL,'2024-09-30 23:39:39','2024-09-30 23:39:39'),(291,36,NULL,92,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(292,36,NULL,96,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(293,36,NULL,95,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(294,36,NULL,94,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(295,36,NULL,93,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(296,36,NULL,226,NULL,'2024-09-30 23:42:07','2024-09-30 23:42:07'),(300,35,NULL,230,NULL,'2024-10-01 00:15:06','2024-10-01 00:15:06'),(301,36,NULL,233,NULL,'2024-10-01 00:21:14','2024-10-01 00:21:14'),(302,35,NULL,232,NULL,'2024-10-01 02:01:49','2024-10-01 02:01:49'),(303,37,NULL,92,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(304,37,NULL,96,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(305,37,NULL,95,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(306,37,NULL,94,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(307,37,NULL,93,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(308,37,NULL,234,NULL,'2024-10-01 09:13:39','2024-10-01 09:13:39'),(309,38,NULL,92,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(310,38,NULL,96,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(311,38,NULL,95,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(312,38,NULL,94,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(313,38,NULL,93,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(314,38,NULL,235,NULL,'2024-10-01 09:32:43','2024-10-01 09:32:43'),(315,39,NULL,92,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(316,39,NULL,96,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(317,39,NULL,95,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(318,39,NULL,94,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(319,39,NULL,93,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(320,39,NULL,236,NULL,'2024-10-01 09:35:19','2024-10-01 09:35:19'),(321,39,NULL,232,NULL,'2024-10-01 11:11:19','2024-10-01 11:11:19'),(323,39,44,NULL,NULL,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(324,39,NULL,241,NULL,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(325,NULL,44,242,NULL,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(326,NULL,44,240,NULL,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(327,NULL,44,241,NULL,'2024-10-01 11:27:42','2024-10-01 11:27:42'),(329,38,44,NULL,NULL,'2024-10-01 11:34:31','2024-10-01 11:34:31'),(330,38,NULL,242,NULL,'2024-10-01 11:34:31','2024-10-01 11:34:31'),(331,37,44,NULL,NULL,'2024-10-01 11:41:26','2024-10-01 11:41:26'),(332,37,NULL,242,NULL,'2024-10-01 11:41:26','2024-10-01 11:41:26'),(333,38,45,NULL,NULL,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(334,38,NULL,244,NULL,'2024-10-01 12:37:26','2024-10-01 12:37:26'),(335,39,45,NULL,NULL,'2024-10-01 12:38:25','2024-10-01 12:38:25'),(336,39,NULL,245,NULL,'2024-10-01 12:38:25','2024-10-01 12:38:25'),(337,39,NULL,246,NULL,'2024-10-01 12:53:27','2024-10-01 12:53:27'),(338,40,NULL,92,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(339,40,NULL,96,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(340,40,NULL,95,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(341,40,NULL,94,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(342,40,NULL,93,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(343,40,NULL,247,NULL,'2024-10-01 14:41:23','2024-10-01 14:41:23'),(344,41,NULL,92,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(345,41,NULL,96,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(346,41,NULL,95,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(347,41,NULL,94,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(348,41,NULL,93,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(349,41,NULL,248,NULL,'2024-10-01 15:26:49','2024-10-01 15:26:49'),(350,41,NULL,249,NULL,'2024-10-01 15:27:38','2024-10-01 15:27:38'),(351,41,NULL,252,NULL,'2024-10-01 15:33:45','2024-10-01 15:33:45'),(352,41,NULL,251,NULL,'2024-10-01 15:34:45','2024-10-01 15:34:45'),(353,41,NULL,253,NULL,'2024-10-01 16:05:44','2024-10-01 16:05:44'),(354,41,NULL,256,NULL,'2024-10-01 16:07:04','2024-10-01 16:07:04'),(355,41,NULL,257,NULL,'2024-10-01 16:22:22','2024-10-01 16:22:22'),(356,42,NULL,92,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(357,42,NULL,96,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(358,42,NULL,95,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(359,42,NULL,94,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(360,42,NULL,93,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(361,42,NULL,258,NULL,'2024-10-02 00:23:35','2024-10-02 00:23:35'),(362,42,NULL,259,NULL,'2024-10-02 00:24:03','2024-10-02 00:24:03'),(363,42,NULL,262,NULL,'2024-10-02 01:35:22','2024-10-02 01:35:22'),(364,43,NULL,92,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(365,43,NULL,96,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(366,43,NULL,95,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(367,43,NULL,94,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(368,43,NULL,93,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(369,43,NULL,265,NULL,'2024-10-02 09:54:27','2024-10-02 09:54:27'),(370,44,NULL,92,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(371,44,NULL,96,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(372,44,NULL,95,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(373,44,NULL,94,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(374,44,NULL,93,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(375,44,NULL,266,NULL,'2024-10-02 09:55:28','2024-10-02 09:55:28'),(376,34,NULL,267,NULL,'2024-10-02 11:06:54','2024-10-02 11:06:54'),(377,34,46,NULL,NULL,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(378,34,NULL,269,NULL,'2024-10-02 11:23:36','2024-10-02 11:23:36'),(383,45,NULL,92,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(384,45,NULL,96,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(385,45,NULL,95,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(386,45,NULL,94,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(387,45,NULL,93,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(388,45,NULL,274,NULL,'2024-10-02 11:52:45','2024-10-02 11:52:45'),(390,34,NULL,255,NULL,'2024-10-02 14:37:46','2024-10-02 14:37:46'),(392,34,NULL,278,NULL,'2024-10-02 15:25:17','2024-10-02 15:25:17'),(393,34,NULL,281,NULL,'2024-10-02 15:48:02','2024-10-02 15:48:02'),(394,34,NULL,282,NULL,'2024-10-02 15:51:34','2024-10-02 15:51:34');
/*!40000 ALTER TABLE `roles_users_teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (19,'Lap Trinh C#','2024-09-26 16:20:34','2024-09-26 16:20:34',1),(20,'Lap Trinh JavaScript','2024-09-26 16:35:18','2024-09-26 16:35:18',0),(21,'Lap Trinh Java','2024-09-26 16:36:43','2024-09-26 16:52:47',0),(22,'Lap Trinh Java and C++-Deleted','2024-09-26 16:55:55','2024-09-26 16:56:07',0),(23,'Lap Trinh Java and C++','2024-09-26 17:09:22','2024-09-26 17:09:22',1),(24,'Lap Trinh Java and C#','2024-09-26 17:15:09','2024-09-26 17:15:09',1),(25,'Lap Trinh Java and JavaScript','2024-09-26 17:15:15','2024-09-26 17:15:15',1),(26,'Lap Trinh Java and Python','2024-09-26 17:15:19','2024-09-26 17:15:19',1),(27,'Lap Trinh Java and Ruby','2024-09-26 17:15:23','2024-09-26 17:15:23',1),(28,'Lap Trinh Kunno','2024-09-26 17:52:42','2024-09-26 17:52:42',1),(29,'Lap Trinh Kunno Java','2024-09-26 18:17:59','2024-09-26 18:17:59',1),(30,'Lap Trinh Kunno JavaScript','2024-09-26 18:19:21','2024-09-26 18:19:21',1),(31,'Lap Trinh Kunno Ruby','2024-09-27 16:26:28','2024-09-27 16:26:28',1),(32,'Lap Trinh Kunno Python','2024-09-27 16:28:47','2024-09-27 16:28:47',1),(33,'Lap Trinh Kunno Lara','2024-09-27 16:30:21','2024-09-27 16:30:21',1),(34,'Lap Trinh Kunno Lar','2024-09-27 16:32:32','2024-09-27 16:32:32',1),(35,'Lap Trinh Kunno la','2024-09-27 16:39:55','2024-09-27 16:39:55',1),(36,'Lap Trinh Kunno Javaa','2024-09-28 00:37:26','2024-09-28 00:37:26',1),(37,'Lap Trinh Kunno JavaSc','2024-09-28 00:39:33','2024-09-28 00:39:33',1),(38,'Lap Trinh Kunno JavaScr','2024-09-28 00:47:08','2024-09-28 00:47:08',1),(39,'Lap Trinh Kunno JavaScrip','2024-09-28 00:49:22','2024-09-28 00:49:22',1),(40,'Lap Trinh Kunno JavaScriptt','2024-09-28 00:53:36','2024-09-28 00:53:36',1),(41,'Lap Trinh Kunno Py','2024-09-28 09:31:05','2024-09-28 09:31:05',1),(42,'Lap Trinh Kunno Pyt-Deleted','2024-09-28 09:36:30','2024-09-28 09:36:40',0),(43,'Lap Trinh Kunno Kunno-Deleted','2024-09-30 17:25:01','2024-09-30 17:34:35',0),(44,'Lap Trinh JavaScript Beginner','2024-10-01 11:27:42','2024-10-01 11:27:42',1),(45,'Lap Trinh JavaScript Senior','2024-10-01 12:37:26','2024-10-01 12:37:26',1),(46,'Lap trinh Game','2024-10-02 11:23:36','2024-10-02 11:23:36',1),(47,'Lap trinh','2024-10-02 11:45:00','2024-10-02 11:45:00',1);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams_courses`
--

DROP TABLE IF EXISTS `teams_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams_courses` (
  `team_id` int NOT NULL,
  `coures_id` int NOT NULL,
  PRIMARY KEY (`team_id`,`coures_id`),
  KEY `coures_id` (`coures_id`),
  CONSTRAINT `teams_courses_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `teams_courses_ibfk_2` FOREIGN KEY (`coures_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams_courses`
--

LOCK TABLES `teams_courses` WRITE;
/*!40000 ALTER TABLE `teams_courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `teams_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `access_token` varchar(255) NOT NULL,
  `refresh_token` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES (23,23,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE0IiwiaWF0IjoxNzI3MzQxNTA0LCJleHAiOjE3MjczNDUxMDR9.Tg2Bkq-C-CAVr8BYNHL4307DA6L79dla9TPQjz4i4Jo','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE0IiwiaWF0IjoxNzI3MzQxNDM5LCJleHAiOjE3MjczNDUwMzl9.EhOHpJnSTxEJ26l-JcokMPLhLnTRvWDWlsIZ-y6Oomk','2024-09-26 16:01:37','2024-09-26 16:05:04',1),(24,24,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE1IiwiaWF0IjoxNzI3MzQ5NzcyLCJleHAiOjE3MjczNTMzNzJ9.6589DI5K9f04BiFvRiJueeRMDPhVb1pcsX1FHPI2cfs','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE1IiwiaWF0IjoxNzI3MzQ5NzcyLCJleHAiOjE3MjczNTMzNzJ9.8htSrYH-ehtI0AVhTtq_3SoQbHG2Vo29o5AdPfQfe5w','2024-09-26 16:11:14','2024-09-26 18:22:53',1),(25,25,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE2IiwiaWF0IjoxNzI3MzQ3OTY5LCJleHAiOjE3MjczNTE1Njl9.ZuTNhrN97a7USZlnq7jWHBkuGIB7E9ZOsq5tAe-ZG7s','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE2IiwiaWF0IjoxNzI3MzQ3OTY5LCJleHAiOjE3MjczNTE1Njl9.K-qsNNayDfw2Uns7a0ZHnH9NWSMUU1lLpxrHGwOMndM','2024-09-26 16:34:51','2024-09-26 17:52:50',1),(26,26,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE3IiwiaWF0IjoxNzI3MzQ4NzEzLCJleHAiOjE3MjczNTIzMTN9.GLmFAW-CYOu5wze548jQyFOsgUroMgtNSpTwXaD-3eE','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE3IiwiaWF0IjoxNzI3MzQ4NzEzLCJleHAiOjE3MjczNTIzMTN9.AEV9T86NzXKGAC_hL7LTi2AFqJLOV8-YsR-tBR6In2A','2024-09-26 17:52:28','2024-09-26 18:05:13',1),(27,27,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE4IiwiaWF0IjoxNzI3NDI4Mzk2LCJleHAiOjE3Mjc0MzE5OTZ9.wAoSwA-0gySUgpO-zueIracuegTfzB4XssOuoa_mUHU','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjY5XzE4IiwiaWF0IjoxNzI3NDI4Mzk2LCJleHAiOjE3Mjc0MzE5OTZ9.TV9GsWFgiFwt5SAYxsLPnuCEInH8DMQo5jdyEUicElU','2024-09-26 18:17:34','2024-09-27 16:13:16',1),(28,28,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjc5XzEiLCJpYXQiOjE3Mjc0MjQ0NTEsImV4cCI6MTcyNzQyODA1MX0.y-Au_8-Tlraz4OaQXj5zKcNjPJ-T7DcqnaKlarSDEAE','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjc5XzEiLCJpYXQiOjE3Mjc0MjQ0NTEsImV4cCI6MTcyNzQyODA1MX0.dggfwNjsm7J-_klWbNb9QAEIuy5MVKDRRdCN-SFM0H0','2024-09-27 11:37:50','2024-09-27 15:07:32',1),(29,29,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjc5XzMiLCJpYXQiOjE3Mjc0NjIzNzAsImV4cCI6MTcyNzQ2NTk3MH0.Fg4oMhE103WuaZGdW0UfxDbxtkfs-g3CWAhresey5FA','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjc5XzMiLCJpYXQiOjE3Mjc0NjIzNzAsImV4cCI6MTcyNzQ2NTk3MH0.W7vFDzU6t0KEkliTTRRK-cID-047OWgdtJo8w8p2b6c','2024-09-28 00:34:46','2024-09-28 01:39:31',1),(30,30,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzEiLCJpYXQiOjE3Mjc0OTQ1MDEsImV4cCI6MTcyNzQ5ODEwMX0.QtEAajRUPtsNyS-9MSDypwYxM5dhejgfy2wuha6KfrE','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzEiLCJpYXQiOjE3Mjc0OTQ1MDEsImV4cCI6MTcyNzQ5ODEwMX0.zzt4J3Y42AZ_W9GFHqtTe8I4kLFS3ACDuVSe5hRWM3U','2024-09-28 09:27:41','2024-09-28 10:35:01',1),(31,31,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzIiLCJpYXQiOjE3Mjc0OTQyMDEsImV4cCI6MTcyNzQ5NzgwMX0.LDFsbKSrJksFz8j1j1ixF4yMTgyARK2JnsxZruFpfsE','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzIiLCJpYXQiOjE3Mjc0OTQyMDEsImV4cCI6MTcyNzQ5NzgwMX0.d0G1MOo5BaFMt1nXvRJC55EsoblSFWOx1SZXus_pcv4','2024-09-28 10:30:02','2024-09-28 10:30:02',1),(32,32,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzMiLCJpYXQiOjE3Mjc2MjEyODMsImV4cCI6MTcyNzYyNDg4M30.qedf8JTmijq6ET9QJmlRRM4p0it9Tck-b5updawFprA','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMjg5XzMiLCJpYXQiOjE3Mjc2MjEyODMsImV4cCI6MTcyNzYyNDg4M30.0TPelLUU4H5t-ltw4QGsOxXegLvNa9yn975B20MovVs','2024-09-28 10:35:45','2024-09-29 21:48:04',1),(33,33,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzEiLCJpYXQiOjE3Mjc3MDQ1NjAsImV4cCI6MTcyNzcwODE2MH0.DUHEbrQKAXq_rRrO3WXRGolx9RSJX2NDs8HLtVJlhao','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzEiLCJpYXQiOjE3Mjc3MDQ1NjAsImV4cCI6MTcyNzcwODE2MH0.SQI18mJ1u7Nlt_4qDV008NIL94UbOMrgKmrFplJCed0','2024-09-30 14:48:49','2024-09-30 20:56:01',1),(34,34,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW1hZG1pbiIsImlhdCI6MTcyNzg1NzQxNCwiZXhwIjoxNzI3ODYxMDE0fQ.xk-FULCbbScRSxOQALMXGKloAF_oks2oTpU_N-vZmfA','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW1hZG1pbiIsImlhdCI6MTcyNzg1NzQxNCwiZXhwIjoxNzI3ODYxMDE0fQ.miT5xAt-YZusQHP3unRH0rg6J-uDUNDszeZV163oPcE','2024-09-30 16:17:54','2024-10-02 15:23:34',1),(35,35,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzIiLCJpYXQiOjE3Mjc3NTAxODUsImV4cCI6MTcyNzc1Mzc4NX0.K_vIvHF4WIIc3gVq3I13hOsivs6qAzsQ9dHEGCgneKU','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzIiLCJpYXQiOjE3Mjc3NTAxODUsImV4cCI6MTcyNzc1Mzc4NX0.6GNFI2Yr0hWyXCzjyJDh8mGa8JB1XO3Txb2R7doWFfQ','2024-09-30 23:27:13','2024-10-01 09:36:26',1),(36,36,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzMiLCJpYXQiOjE3Mjc3MjI4MzgsImV4cCI6MTcyNzcyNjQzOH0.oVovfkt5oMpVbzRBo2AgRUPAmuXpYFV_OL818LWfMjQ','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzMiLCJpYXQiOjE3Mjc3MjI4MzgsImV4cCI6MTcyNzcyNjQzOH0.gyXQIaJ3QO2KyYy7wZilA76EqekcuIHgPYkKLuN-OQ0','2024-09-30 23:42:08','2024-10-01 02:00:39',1),(37,37,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzQiLCJpYXQiOjE3Mjc3NTc2NTEsImV4cCI6MTcyNzc2MTI1MX0.QNoFoYUpQPJo8V7rqod9llW4Ui9Brs6PSx8j6NFQ_pM','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzQiLCJpYXQiOjE3Mjc3NTc2NTEsImV4cCI6MTcyNzc2MTI1MX0.Az-JGo15HuXDIvlBJNp-bplEOTYAHpZj6YS23mdzjSE','2024-10-01 09:13:39','2024-10-01 11:40:51',1),(38,38,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzUiLCJpYXQiOjE3Mjc3NjA3MjAsImV4cCI6MTcyNzc2NDMyMH0.bmu0QAi0k-mqahF6lpPMORYL99-DL9ZX70CZHdvS0Wg','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzUiLCJpYXQiOjE3Mjc3NjA3MjAsImV4cCI6MTcyNzc2NDMyMH0.lwnfBhiBCMJK-3d5xgMiVCVHqRjGBDsaAuwJx8rl05s','2024-10-01 09:32:43','2024-10-01 12:32:01',1),(39,39,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzYiLCJpYXQiOjE3Mjc3NjE5ODcsImV4cCI6MTcyNzc2NTU4N30.5xBUKQKria4TQU-NJLx3u_6X3CMoFMQDeBmXsCspdLc','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzYiLCJpYXQiOjE3Mjc3NjE5ODcsImV4cCI6MTcyNzc2NTU4N30.sirxa2_cKPLVPHC-X22LSMfCRmmduA_4U9NwwWfHCWw','2024-10-01 09:35:20','2024-10-01 12:53:07',1),(40,40,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzciLCJpYXQiOjE3Mjc3Njg0ODIsImV4cCI6MTcyNzc3MjA4Mn0.xvEYdo4WLLHoGM1QdQ2ih1N0Wq7QSTSHydcDwbZ03YY','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzciLCJpYXQiOjE3Mjc3Njg0ODIsImV4cCI6MTcyNzc3MjA4Mn0.bWpshOvdUnSSAjH-ZHynnNQWKpKH498GaK4ARqL6g50','2024-10-01 14:41:23','2024-10-01 14:41:23',1),(41,41,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzgiLCJpYXQiOjE3Mjc3NzM1NjQsImV4cCI6MTcyNzc3NzE2NH0.lxAlnUSt_uNhYNswx1_Qwj-Y24iymJoeys3Tp9YsoWo','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzgiLCJpYXQiOjE3Mjc3NzM1NjQsImV4cCI6MTcyNzc3NzE2NH0.4P4pqnm1cbwAP7c6FjiGL6gpMaCfEEaRgT39zGDgFto','2024-10-01 15:26:49','2024-10-01 16:06:05',1),(42,42,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzEwIiwiaWF0IjoxNzI3ODA3NjY1LCJleHAiOjE3Mjc4MTEyNjV9.GkO6r6UxPkNh98VUID5hM6_VXeeguNv8chuB5gQ_rOA','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMzA5XzEwIiwiaWF0IjoxNzI3ODA3NjY1LCJleHAiOjE3Mjc4MTEyNjV9.Y7GRhvZrsOpku4aaQF1uszU_si7r38u4ulgTvTdtf6A','2024-10-02 00:23:36','2024-10-02 01:34:25',1),(43,43,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDEiLCJpYXQiOjE3Mjc4Mzc2NjYsImV4cCI6MTcyNzg0MTI2Nn0.sQ1-bvqRkyLxDsPhH6xYkaMH9EXj41NWnM7Xm94A8qs','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDEiLCJpYXQiOjE3Mjc4Mzc2NjYsImV4cCI6MTcyNzg0MTI2Nn0.OGbOfftWvBhO-JFiG9WmlyEiY3E5VYwd889C5g3orDI','2024-10-02 09:54:27','2024-10-02 09:54:27',1),(44,44,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDIiLCJpYXQiOjE3Mjc4Mzc3MjcsImV4cCI6MTcyNzg0MTMyN30.VLDnIAHLq0DBbJ6WfpBTdyMv5rfGAz_gwhEZMA5grUg','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDIiLCJpYXQiOjE3Mjc4Mzc3MjcsImV4cCI6MTcyNzg0MTMyN30.ad9pTIkkvXOO_Judyo1vVqVqa-d-9T9ZfUUBWs85PIk','2024-10-02 09:55:28','2024-10-02 09:55:28',1),(45,45,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDMiLCJpYXQiOjE3Mjc4NDQ5OTksImV4cCI6MTcyNzg0ODU5OX0.HeM2j_w4ByJcdo88qzs7q_cJWeSacrm8EzUFam0ivNI','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMDIxMDMiLCJpYXQiOjE3Mjc4NDQ3NjQsImV4cCI6MTcyNzg0ODM2NH0.Y-ZYxMVTE5W04li4GawOc1a1yxntQYazGPwQzCAd9eI','2024-10-02 11:52:46','2024-10-02 11:58:49',0);
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `card_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,35,17,53.00,'By credit','This payment is implemented by credit','done','2024-10-01 02:05:57','2024-10-01 02:05:57','string'),(2,39,17,56.00,'By credit','This payment is implemented by credit','done','2024-10-01 12:53:28','2024-10-01 12:53:28','string'),(3,41,19,58.00,'By credit','This payment is implemented by credit','done','2024-10-01 15:33:45','2024-10-01 15:33:45','4545345345'),(4,41,20,100.00,'By credit','Pay for registering course 20','done','2024-10-01 16:07:05','2024-10-01 16:07:05','4545345345'),(5,41,20,100.00,'By credit','Pay for registering course 20','done','2024-10-01 16:22:22','2024-10-01 16:22:22','4545345345'),(6,34,22,0.00,'By credit','Pay for course 22','done','2024-10-02 11:06:54','2024-10-02 11:06:54','9120352874');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` datetime NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `address` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `system_role` varchar(15) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (23,'Demo','Account','2000-09-08 00:00:00','user269_14-Deleted-Deleted','$2a$10$eSrjevD5X6Ahw/Nsr1iYFuzZQgkEaSB2j8VNdpOIrv6aC6VDdjjd2','demo.account@gmail.com','554654654','Trung Vuong','1142417177','2024-09-26 16:01:36','2024-10-02 10:57:57','user',0),(24,'Kunno','Account','2001-02-09 00:00:00','user269_15','$2a$10$vqnAfEWy/EvIazgG/o6fDely5mH9.SfXe04bEwIH.cnMNguzJ3HAe','user269@gmail.com','032326561','Viet Tri, Phu Tho','0039996311','2024-09-26 16:11:14','2024-10-02 10:09:12','sysadmin',1),(25,'Kunno','Account','2001-02-09 00:00:00','user269_16','$2a$10$gRdeiH8axXs5oY5CMfGwfu0osArotbcDxTDuHXEZZq82fZH05fUNq','user269@gmail.com','032326561','Viet Tri, Phu Tho','9649259130','2024-09-26 16:34:51','2024-10-02 10:09:12','user',1),(26,'Kunno','Account','2001-02-09 00:00:00','user269_17','$2a$10$QBCekeLKGn6nBBXfVMuPJeT8FuJOW5Da95qQN7q0jx9KrXBmQBp0O','user269@gmail.com','032326561','Viet Tri, Phu Tho','4701494347','2024-09-26 17:52:28','2024-10-02 10:09:12','user',1),(27,'Kunno','Account','2001-02-09 00:00:00','user269_18','$2a$10$6KqCX6LTUQBS04.OR6cta.lQ7xwGDbv08iWHqsEo2EfJhwBQzKc3i','user269@gmail.com','032326561','Viet Tri, Phu Tho','0166380986','2024-09-26 18:17:34','2024-10-02 10:09:12','user',1),(28,'Kunno','Account','2001-02-09 00:00:00','user279_1','$2a$10$jlFwlgIB/KnPcz3OzR4BRuUpwIgnp6Ccjlhg9QS1OqgEzdW0wFsPe','user2791@gmail.com','032326561','Viet Tri, Phu Tho','1665600351','2024-09-27 11:37:50','2024-10-02 10:09:12','user',1),(29,'Kunno','Account','2001-02-09 00:00:00','user279_3','$2a$10$lkUBg87cBnzOWAEIhP4.Su512LO5ZY6/raGhD0qCOtVl/Ygn.2jiu','user2793@gmail.com','032326561','Viet Tri, Phu Tho','7435242916','2024-09-28 00:34:46','2024-10-02 10:09:12','user',1),(30,'user289','Account','2001-02-09 00:00:00','user289_1','$2a$10$geANVilDYhz9ax.HWsMf2.w2O35iz.2kZZlthEedfV6zfwBC6xIZK','user2891@gmail.com','032326561','Viet Tri, Phu Tho','9347945464','2024-09-28 09:27:41','2024-10-02 10:09:12','user',1),(31,'user289','Account','2001-02-09 00:00:00','user289_2','$2a$10$Agej/Z7TgbPELUS636fEIOdkq0bbqlQZ7D2y0pbABf9kOwNVw2sW2','user2892@gmail.com','032326561','Viet Tri, Phu Tho','6280134273','2024-09-28 10:30:02','2024-10-02 10:09:12','user',1),(32,'user289','Account','2001-02-09 00:00:00','user289_3','$2a$10$.eJWUKQJGklDwhqtCdbQpOAMVvJG3lIb/tCtoRfH1Qfb8WaFfNVMa','user2893@gmail.com','032326561','Viet Tri, Phu Tho','1802849965','2024-09-28 10:35:45','2024-10-02 10:09:12','user',1),(33,'User3091','Account','2000-09-08 00:00:00','user309_1','$2a$10$UhP7effnJiJt36gfPPyfSetrz9m49c.JFmSRxUAg6wzPjyXPLHd6S','user3091@gmail.com','554654654','Trung Vuong','9568479889','2024-09-30 14:48:49','2024-10-02 10:09:12','user',1),(34,'admin','account','2001-02-09 00:00:00','systemadmin','$2a$10$6w4Nlz7051zzdnmcPbkEWeEsxOgyqHbuhBDAa1uQD33YsIf6y9xSK','admin@gmail.com','032326561','Viet Tri, Phu Tho','9120352874','2024-09-30 16:17:54','2024-10-02 10:09:12','admin',1),(35,'user3092','account','2001-02-09 00:00:00','user309_2','$2a$10$bEi6VuQI0EbWgxGjq2nnbe0Qno7H66Jz5F369f/staZXs.md1aN/u','user3092@gmail.com','032326561','Viet Tri, Phu Tho','5628481060','2024-09-30 23:27:13','2024-10-02 10:09:12','user',1),(36,'user3093','account','2001-02-09 00:00:00','user309_3','$2a$10$cZsdUgF4EiiuG6lBL8yy0efvOxVaxRV8hCdc01nRq8A2s1xGGA.aa','user3093@gmail.com','032326561','Viet Tri, Phu Tho','2544497482','2024-09-30 23:42:08','2024-10-02 10:09:12','user',1),(37,'user3094','account','2001-02-09 00:00:00','user309_4','$2a$10$oauVb9j5ifLdNAoy/9jUnuTZ6pyxbB2llHMbLMCzZEZKCNql7UENS','user3094@gmail.com','032326561','Viet Tri, Phu Tho','7141561200','2024-10-01 09:13:39','2024-10-02 10:09:12','user',1),(38,'user3095','account','2001-02-09 00:00:00','user309_5','$2a$10$7Vk7b3hBoWdXSEUG7ecpxuNGz4/fyPRGUAuZ7wdCM7VAcBI1ElFIK','user3095@gmail.com','032326561','Viet Tri, Phu Tho','2686130116','2024-10-01 09:32:43','2024-10-02 10:09:12','user',1),(39,'user3096','account','2001-02-09 00:00:00','user309_6','$2a$10$YitPkplzUvy9dMiBUNZnF.KZ5cWAjKb7IJj75c5cgzDBOvy2sPuWy','user3096@gmail.com','032326561','Viet Tri, Phu Tho','1058882368','2024-10-01 09:35:20','2024-10-02 10:09:12','user',1),(40,'user3097','account','2001-02-09 00:00:00','user309_7','$2a$10$0VlwDFMvRyWF0L9FmEjZg.XWkT3iXH0gr8sTUVHJnoxXPfsha3PWu','user3097@gmail.com','032326561','Viet Tri, Phu Tho','02465416512','2024-10-01 14:41:22','2024-10-01 14:41:22','user',1),(41,'user3098','account','2001-02-09 00:00:00','user309_8','$2a$10$TGDUKwMzq/dsUL0JhSxdF.ZbiTWuv5EiBP8F.Q1MmYFtR6OBTivHi','user3098@gmail.com','032326561','Viet Tri, Phu Tho','4545345345','2024-10-01 15:26:47','2024-10-01 15:26:47','user',1),(42,'user3010','account','2001-02-09 00:00:00','user309_10','$2a$10$/NlwkVbwqG1lCxMHhFvJJOwra/bS/KpBLxQ.cKpbiPukN.ZSHDhVy','user3010@gmail.com','032326561','Viet Tri, Phu Tho','245345','2024-10-02 00:23:34','2024-10-02 00:23:34','user',1),(43,'Hieu','Nguyen','2004-08-10 00:00:00','user02101','$2a$10$dS8dMg6uE/tG/e.JF/Avd..rwr9nIGiSH3WKzgSRpQOV8mMksrwWi','user02101@gmail.com','23452345','Viet Tri','5234523452345234','2024-10-02 09:54:26','2024-10-02 09:54:26','user',1),(44,'Trung','Nguyen','2005-08-10 00:00:00','user02102','$2a$10$e0fTRma4gI/3001Z8hnHcOa252a7Db.IJy609xJs1VEiBGlOB7BXm','kunno@gmail.com','54654654','Trung Vuong','56465456','2024-10-02 09:55:27','2024-10-02 10:56:06','user',1),(45,'Trung','Trung','2003-10-05 00:00:00','user02103','$2a$10$s2VUMJn35/dcJlzvDppD5uA8i1l.N.wFynAbjnrgP0QquDfiw6kd2','user02103@gmail.com','55354354','trung van','56356356356','2024-10-02 11:52:44','2024-10-02 12:06:10','user',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_courses`
--

DROP TABLE IF EXISTS `users_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_courses` (
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `users_courses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_courses_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_courses`
--

LOCK TABLES `users_courses` WRITE;
/*!40000 ALTER TABLE `users_courses` DISABLE KEYS */;
INSERT INTO `users_courses` VALUES (28,3),(28,4),(28,5),(28,6),(28,7),(29,7),(30,7),(30,8),(30,9),(31,9),(30,12),(35,13),(35,14),(35,15),(35,17),(39,17),(41,19),(34,20),(41,20),(42,21),(42,22),(34,24);
/*!40000 ALTER TABLE `users_courses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-02 16:05:42
