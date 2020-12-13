-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: smpe
-- ------------------------------------------------------
-- Server version	8.0.12

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
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '上级部门（顶级部门为0，默认为0）',
  `sub_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `dept_sort` int(11) unsigned NOT NULL DEFAULT '999' COMMENT '排序',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用（默认）、0禁用',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `update_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态：1启用（默认）、0禁用',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `inx_pid` (`pid`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (2,7,0,'研发部',3,_binary '',1,1,'2020-12-13 11:41:04','2020-12-13 11:41:06',_binary '\0'),(5,7,0,'运维部',4,_binary '',1,1,'2020-12-13 11:41:36','2020-12-13 11:41:38',_binary '\0'),(6,8,0,'测试部',6,_binary '',1,1,'2020-12-13 11:42:17','2020-12-13 11:42:19',_binary '\0'),(7,0,2,'华南分部',1,_binary '',1,1,'2020-12-13 11:42:58','2020-12-13 11:43:01',_binary '\0'),(8,0,1,'华北分部',7,_binary '',1,1,'2020-12-13 11:43:40','2020-12-13 11:43:43',_binary '\0');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用（默认）、0禁用',
  `job_sort` int(11) unsigned NOT NULL DEFAULT '999' COMMENT '排序',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='岗位';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'人事',_binary '',3,1,1,'2020-12-13 11:39:24','2020-12-13 11:39:28',_binary '\0'),(2,'产品',_binary '',4,1,1,'2020-12-13 11:39:49','2020-12-13 11:39:52',_binary '\0');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '上级菜单ID',
  `sub_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '子菜单数目',
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '菜单类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '菜单标题',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '组件',
  `menu_sort` int(11) unsigned NOT NULL DEFAULT '999' COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '链接地址',
  `i_frame` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否外链',
  `cache` bit(1) NOT NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `update_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `inx_pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='系统菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,7,0,'系统管理','','',1,'system','system',_binary '\0',_binary '\0',_binary '\0','',1,1,'2018-12-18 15:11:29','2020-12-13 16:25:07',_binary '\0'),(2,1,3,1,'用户管理','User','system/user/index',2,'peoples','user',_binary '\0',_binary '\0',_binary '\0','user:list',1,1,'2018-12-18 15:14:44','2020-12-13 16:25:07',_binary '\0'),(3,1,3,1,'角色管理','Role','system/role/index',3,'role','role',_binary '\0',_binary '\0',_binary '\0','roles:list',1,1,'2018-12-18 15:16:07','2020-12-13 16:25:07',_binary '\0'),(5,1,3,1,'菜单管理','Menu','system/menu/index',5,'menu','menu',_binary '\0',_binary '\0',_binary '\0','menu:list',1,1,'2018-12-18 15:17:28','2020-12-13 16:25:07',_binary '\0'),(6,0,4,0,'系统监控','','',10,'monitor','monitor',_binary '\0',_binary '\0',_binary '\0','',1,1,'2018-12-18 15:17:48','2020-12-13 16:25:07',_binary '\0'),(7,6,0,1,'操作日志','Log','monitor/log/index',11,'log','logs',_binary '\0',_binary '\0',_binary '\0','',1,1,'2018-12-18 15:18:26','2020-12-13 16:25:07',_binary '\0'),(28,1,3,1,'任务调度','Timing','system/timing/index',999,'timing','timing',_binary '\0',_binary '\0',_binary '\0','timing:list',1,1,'2019-01-07 20:34:40','2020-12-13 16:25:07',_binary '\0'),(32,6,0,1,'异常日志','ErrorLog','monitor/log/errorLog',12,'error','errorLog',_binary '\0',_binary '\0',_binary '\0','',1,1,'2019-01-13 13:49:03','2020-12-13 16:25:07',_binary '\0'),(35,1,3,1,'部门管理','Dept','system/dept/index',6,'dept','dept',_binary '\0',_binary '\0',_binary '\0','dept:list',1,1,'2019-03-25 09:46:00','2020-12-13 16:25:07',_binary '\0'),(37,1,3,1,'岗位管理','Job','system/job/index',7,'Steve-Jobs','job',_binary '\0',_binary '\0',_binary '\0','job:list',1,1,'2019-03-29 13:51:18','2020-12-13 16:25:07',_binary '\0'),(39,1,3,1,'字典管理','Dict','system/dict/index',8,'dictionary','dict',_binary '\0',_binary '\0',_binary '\0','dict:list',1,1,'2019-04-10 11:49:04','2020-12-13 16:25:07',_binary '\0'),(41,6,0,1,'在线用户','OnlineUser','monitor/online/index',10,'Steve-Jobs','online',_binary '\0',_binary '\0',_binary '\0','',1,1,'2019-10-26 22:08:43','2020-12-13 16:25:07',_binary '\0'),(44,2,0,2,'用户新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','user:add',1,1,'2019-10-29 10:59:46','2020-12-13 16:25:07',_binary '\0'),(45,2,0,2,'用户编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','user:edit',1,1,'2019-10-29 11:00:08','2020-12-13 16:25:07',_binary '\0'),(46,2,0,2,'用户删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','user:del',1,1,'2019-10-29 11:00:23','2020-12-13 16:25:07',_binary '\0'),(48,3,0,2,'角色创建','','',2,'','',_binary '\0',_binary '\0',_binary '\0','roles:add',1,1,'2019-10-29 12:45:34','2020-12-13 16:25:07',_binary '\0'),(49,3,0,2,'角色修改','','',3,'','',_binary '\0',_binary '\0',_binary '\0','roles:edit',1,1,'2019-10-29 12:46:16','2020-12-13 16:25:07',_binary '\0'),(50,3,0,2,'角色删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','roles:del',1,1,'2019-10-29 12:46:51','2020-12-13 16:25:07',_binary '\0'),(52,5,0,2,'菜单新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','menu:add',1,1,'2019-10-29 12:55:07','2020-12-13 16:25:07',_binary '\0'),(53,5,0,2,'菜单编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','menu:edit',1,1,'2019-10-29 12:55:40','2020-12-13 16:25:07',_binary '\0'),(54,5,0,2,'菜单删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','menu:del',1,1,'2019-10-29 12:56:00','2020-12-13 16:25:07',_binary '\0'),(56,35,0,2,'部门新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','dept:add',1,1,'2019-10-29 12:57:09','2020-12-13 16:25:07',_binary '\0'),(57,35,0,2,'部门编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','dept:edit',1,1,'2019-10-29 12:57:27','2020-12-13 16:25:07',_binary '\0'),(58,35,0,2,'部门删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','dept:del',1,1,'2019-10-29 12:57:41','2020-12-13 16:25:07',_binary '\0'),(60,37,0,2,'岗位新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','job:add',1,1,'2019-10-29 12:58:27','2020-12-13 16:25:07',_binary '\0'),(61,37,0,2,'岗位编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','job:edit',1,1,'2019-10-29 12:58:45','2020-12-13 16:25:07',_binary '\0'),(62,37,0,2,'岗位删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','job:del',1,1,'2019-10-29 12:59:04','2020-12-13 16:25:07',_binary '\0'),(64,39,0,2,'字典新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','dict:add',1,1,'2019-10-29 13:00:17','2020-12-13 16:25:07',_binary '\0'),(65,39,0,2,'字典编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','dict:edit',1,1,'2019-10-29 13:00:42','2020-12-13 16:25:07',_binary '\0'),(66,39,0,2,'字典删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','dict:del',1,1,'2019-10-29 13:00:59','2020-12-13 16:25:07',_binary '\0'),(73,28,0,2,'任务新增','','',2,'','',_binary '\0',_binary '\0',_binary '\0','timing:add',1,1,'2019-10-29 13:07:28','2020-12-13 16:25:07',_binary '\0'),(74,28,0,2,'任务编辑','','',3,'','',_binary '\0',_binary '\0',_binary '\0','timing:edit',1,1,'2019-10-29 13:07:41','2020-12-13 16:25:07',_binary '\0'),(75,28,0,2,'任务删除','','',4,'','',_binary '\0',_binary '\0',_binary '\0','timing:del',1,1,'2019-10-29 13:07:54','2020-12-13 16:25:07',_binary '\0');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `level` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '角色级别（越小越大）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据权限',
  `is_protection` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否受保护（内置角色，1为内置角色，默认值为0）',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `role_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员',1,'-','全部',_binary '',1,1,'2020-12-13 11:36:45','2020-12-13 11:36:47',_binary '\0'),(2,'普通用户',2,'-','自定义',_binary '',1,1,'2020-12-13 11:37:14','2020-12-13 11:37:18',_binary '\0');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_roles_depts`
--

DROP TABLE IF EXISTS `sys_roles_depts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_roles_depts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色id',
  `dept_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_mid_dept_id` (`dept_id`) USING BTREE /*!80000 INVISIBLE */,
  KEY `idx_mid_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色部门关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_roles_depts`
--

LOCK TABLES `sys_roles_depts` WRITE;
/*!40000 ALTER TABLE `sys_roles_depts` DISABLE KEYS */;
INSERT INTO `sys_roles_depts` VALUES (1,2,7);
/*!40000 ALTER TABLE `sys_roles_depts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_roles_menus`
--

DROP TABLE IF EXISTS `sys_roles_menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_roles_menus` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id\n',
  `menu_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_mid_role_id` (`role_id`) USING BTREE /*!80000 INVISIBLE */,
  KEY `idx_mid_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色菜单关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_roles_menus`
--

LOCK TABLES `sys_roles_menus` WRITE;
/*!40000 ALTER TABLE `sys_roles_menus` DISABLE KEYS */;
INSERT INTO `sys_roles_menus` VALUES (1,1,1),(2,1,2),(3,2,1),(4,2,2),(5,3,1),(6,3,2),(7,5,1),(8,5,2),(9,6,1),(10,7,1),(26,28,1),(28,32,1),(31,35,1),(34,37,1),(36,39,1),(37,41,1);
/*!40000 ALTER TABLE `sys_roles_menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '部门id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `gender` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别（0为男默认，1为女）',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `avatar_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像路径',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `is_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bigint(20) NOT NULL DEFAULT '1' COMMENT '状态：1启用（默认）、0禁用',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新者id',
  `pwd_reset_time` datetime DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK5rwmryny6jthaaxkogownknqp` (`dept_id`) USING BTREE,
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,2,'admin','管理员',_binary '\0','18888888888','201507802@qq.com','','$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa',_binary '',1,1,1,'2020-12-13 11:27:20','2020-12-13 11:26:52','2020-12-13 11:26:57',_binary '\0'),(2,5,'test','测试',_binary '','15689899898','231@qq.com','','$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa',_binary '\0',1,1,1,NULL,'2020-12-13 11:34:53','2020-12-13 11:34:59',_binary '\0');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_users_jobs`
--

DROP TABLE IF EXISTS `sys_users_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_users_jobs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `job_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '岗位ID',
  PRIMARY KEY (`id`),
  KEY `idx_mid_job_id` (`job_id`) /*!80000 INVISIBLE */,
  KEY `idx_mid_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_jobs`
--

LOCK TABLES `sys_users_jobs` WRITE;
/*!40000 ALTER TABLE `sys_users_jobs` DISABLE KEYS */;
INSERT INTO `sys_users_jobs` VALUES (1,1,1),(2,1,1),(3,2,2);
/*!40000 ALTER TABLE `sys_users_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_users_roles`
--

DROP TABLE IF EXISTS `sys_users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_users_roles` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id\n',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_mid_role_id` (`role_id`) USING BTREE /*!80000 INVISIBLE */,
  KEY `idx_mid_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='用户角色关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_roles`
--

LOCK TABLES `sys_users_roles` WRITE;
/*!40000 ALTER TABLE `sys_users_roles` DISABLE KEYS */;
INSERT INTO `sys_users_roles` VALUES (1,1,1),(2,2,1);
/*!40000 ALTER TABLE `sys_users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-13 19:11:40
