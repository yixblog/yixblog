-- MySQL dump 10.14  Distrib 5.5.32-MariaDB, for Win64 (x86)
--
-- Host: localhost    Database: yixblog
-- ------------------------------------------------------
-- Server version	5.5.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blog_accounts`
--

DROP TABLE IF EXISTS `blog_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_accounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(30) DEFAULT NULL,
  `pwd` varchar(32) DEFAULT NULL,
  `nick` varchar(30) NOT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `qq` varchar(11) DEFAULT NULL,
  `weibo` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `banflag` tinyint(1) DEFAULT '0',
  `regtime` bigint(20) NOT NULL,
  `lastlogin` bigint(20) DEFAULT NULL,
  `sex` varchar(1) NOT NULL DEFAULT '',
  `temp_email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nick_UNIQUE` (`nick`),
  UNIQUE KEY `uid_UNIQUE` (`uid`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `qq_UNIQUE` (`qq`),
  UNIQUE KEY `weibo_UNIQUE` (`weibo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_accounts`
--

LOCK TABLES `blog_accounts` WRITE;
/*!40000 ALTER TABLE `blog_accounts` DISABLE KEYS */;
INSERT INTO `blog_accounts` VALUES (1,'testuser1','f3066ae2889d8a3b679806111ea5a0e0','tester1',NULL,NULL,NULL,NULL,0,1377620025491,1377620116215,'男','davepotter@163.com');
/*!40000 ALTER TABLE `blog_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_admin_character`
--

DROP TABLE IF EXISTS `blog_admin_character`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_admin_character` (
  `admin` int(10) unsigned NOT NULL,
  `character` int(10) unsigned NOT NULL,
  PRIMARY KEY (`admin`,`character`),
  KEY `FK_ADMIN_CHARACTER_ADMIN_idx` (`admin`),
  KEY `FK_ADMIN_CHARACTER_CHARACTER_idx` (`character`),
  CONSTRAINT `FK_ADMIN_CHARACTER_ADMIN` FOREIGN KEY (`admin`) REFERENCES `blog_admins` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ADMIN_CHARACTER_CHARACTER` FOREIGN KEY (`character`) REFERENCES `blog_characters` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_admin_character`
--

LOCK TABLES `blog_admin_character` WRITE;
/*!40000 ALTER TABLE `blog_admin_character` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_admin_character` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `blog_admin_permission_view`
--

DROP TABLE IF EXISTS `blog_admin_permission_view`;
/*!50001 DROP VIEW IF EXISTS `blog_admin_permission_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `blog_admin_permission_view` (
  `id` tinyint NOT NULL,
  `uid` tinyint NOT NULL,
  `pwd` tinyint NOT NULL,
  `email` tinyint NOT NULL,
  `regtime` tinyint NOT NULL,
  `lastlogin` tinyint NOT NULL,
  `temp_email` tinyint NOT NULL,
  `system_config` tinyint NOT NULL,
  `user_manage` tinyint NOT NULL,
  `admin_manage` tinyint NOT NULL,
  `article_manage` tinyint NOT NULL,
  `comment_manage` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `blog_admins`
--

DROP TABLE IF EXISTS `blog_admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_admins` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(30) NOT NULL,
  `pwd` varchar(32) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `regtime` bigint(20) NOT NULL,
  `lastlogin` bigint(20) NOT NULL,
  `temp_email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid_UNIQUE` (`uid`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_admins`
--

LOCK TABLES `blog_admins` WRITE;
/*!40000 ALTER TABLE `blog_admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_articles`
--

DROP TABLE IF EXISTS `blog_articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_articles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `content` text NOT NULL,
  `addtime` bigint(20) NOT NULL,
  `edittime` bigint(20) DEFAULT NULL,
  `author` int(10) unsigned NOT NULL,
  `topflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `IK_ARTICLE_TITLE` (`title`),
  KEY `FK_ARTICLE_AUTHOR_idx` (`author`),
  KEY `IK_ARTICLE_ADDTIME` (`addtime`),
  CONSTRAINT `FK_ARTICLE_AUTHOR` FOREIGN KEY (`author`) REFERENCES `blog_accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_articles`
--

LOCK TABLES `blog_articles` WRITE;
/*!40000 ALTER TABLE `blog_articles` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_articles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_characters`
--

DROP TABLE IF EXISTS `blog_characters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_characters` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `system_config` tinyint(1) DEFAULT '0',
  `user_manage` tinyint(1) DEFAULT '0',
  `admin_manage` tinyint(1) DEFAULT '0',
  `article_manage` tinyint(1) DEFAULT '0',
  `comment_manage` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_characters`
--

LOCK TABLES `blog_characters` WRITE;
/*!40000 ALTER TABLE `blog_characters` DISABLE KEYS */;
INSERT INTO `blog_characters` VALUES (1,'超级管理员',1,1,1,1,1);
/*!40000 ALTER TABLE `blog_characters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_comments`
--

DROP TABLE IF EXISTS `blog_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_comments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  `author` int(10) unsigned NOT NULL,
  `article` int(10) unsigned NOT NULL,
  `addtime` bigint(20) NOT NULL,
  `floor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COMMENTS_AUTHOR_idx` (`author`),
  KEY `FK_COMMENTS_ARTICLE_idx` (`article`),
  CONSTRAINT `FK_COMMENTS_ARTICLE` FOREIGN KEY (`article`) REFERENCES `blog_articles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_COMMENTS_AUTHOR` FOREIGN KEY (`author`) REFERENCES `blog_accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_comments`
--

LOCK TABLES `blog_comments` WRITE;
/*!40000 ALTER TABLE `blog_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_notice`
--

DROP TABLE IF EXISTS `blog_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_notice` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `content` text,
  `addtime` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_notice`
--

LOCK TABLES `blog_notice` WRITE;
/*!40000 ALTER TABLE `blog_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_tags`
--

DROP TABLE IF EXISTS `blog_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blog_tags` (
  `article` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag` varchar(45) NOT NULL,
  PRIMARY KEY (`article`,`tag`),
  KEY `IK_TAGS` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_tags`
--

LOCK TABLES `blog_tags` WRITE;
/*!40000 ALTER TABLE `blog_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `blog_admin_permission_view`
--

/*!50001 DROP TABLE IF EXISTS `blog_admin_permission_view`*/;
/*!50001 DROP VIEW IF EXISTS `blog_admin_permission_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `blog_admin_permission_view` AS select `a`.`id` AS `id`,`a`.`uid` AS `uid`,`a`.`pwd` AS `pwd`,`a`.`email` AS `email`,`a`.`regtime` AS `regtime`,`a`.`lastlogin` AS `lastlogin`,`a`.`temp_email` AS `temp_email`,max(`c`.`system_config`) AS `system_config`,max(`c`.`user_manage`) AS `user_manage`,max(`c`.`admin_manage`) AS `admin_manage`,max(`c`.`article_manage`) AS `article_manage`,max(`c`.`comment_manage`) AS `comment_manage` from ((`blog_admins` `a` left join `blog_admin_character` `ac` on((`ac`.`admin` = `a`.`id`))) left join `blog_characters` `c` on((`c`.`id` = `ac`.`character`))) group by `a`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-28 19:50:29
