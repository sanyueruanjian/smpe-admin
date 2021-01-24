/*
 Navicat Premium Data Transfer

 Source Server         : 我的服务器数据库
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 39.106.101.156:3306
 Source Schema         : smpe

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 24/01/2021 20:21:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级部门（顶级部门为0，默认为0）',
  `sub_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `dept_sort` int UNSIGNED NOT NULL DEFAULT 999 COMMENT '排序',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用（默认）、0禁用',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态：1启用（默认）、0禁用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE COMMENT '普通索引——pid查询部门',
  INDEX `idx_enabled`(`enabled`) USING BTREE COMMENT '普通索引——enabled查询部门'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (2, 7, 0, '研发部', 3, b'1', 1, 1, '2020-12-13 11:41:04', '2020-12-13 11:41:06', b'0');
INSERT INTO `sys_dept` VALUES (5, 7, 0, '运维部', 4, b'1', 1, 1, '2020-12-13 11:41:36', '2020-12-13 11:41:38', b'0');
INSERT INTO `sys_dept` VALUES (6, 8, 0, '测试部', 6, b'1', 1, 1, '2020-12-13 11:42:17', '2020-12-13 11:42:19', b'0');
INSERT INTO `sys_dept` VALUES (7, 0, 2, '华南分部', 1, b'1', 1, 1, '2020-12-13 11:42:58', '2020-12-13 11:43:01', b'0');
INSERT INTO `sys_dept` VALUES (8, 0, 1, '华北分部', 7, b'1', 1, 1, '2020-12-13 11:43:40', '2020-12-13 11:43:43', b'0');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用（默认）、0禁用',
  `job_sort` int UNSIGNED NOT NULL DEFAULT 999 COMMENT '排序',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_name`(`name`) USING BTREE COMMENT '岗位名唯一',
  INDEX `idx_enabled`(`enabled`) USING BTREE COMMENT '普通索引——enabled查询岗位'
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '人事', b'1', 3, 1, 1, '2020-12-13 11:39:24', '2020-12-13 11:39:28', b'0');
INSERT INTO `sys_job` VALUES (2, '产品', b'1', 4, 1, 1, '2020-12-13 11:39:49', '2020-12-13 11:39:52', b'0');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '操作用户id',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `log_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '日志类型',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '方法名',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求ip',
  `request_time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '请求耗时（毫秒值）',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '地址',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器',
  `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细异常',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人id',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE COMMENT '普通索引——根据创建时间查询日志',
  INDEX `idx_log_type`(`log_type`) USING BTREE COMMENT '普通索引——根据日志类型查询日志'
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级菜单ID',
  `sub_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '子菜单数目',
  `type` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '菜单类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '菜单标题',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '组件',
  `menu_sort` int UNSIGNED NOT NULL DEFAULT 999 COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '链接地址',
  `i_frame` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否外链',
  `cache` bit(1) NOT NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者',
  `update_by` bigint NOT NULL DEFAULT 0 COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE COMMENT '普通索引——pid查询菜单'
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 7, 0, '系统管理', '', '', 1, 'system', 'system', b'0', b'0', b'0', '', 1, 1, '2018-12-18 15:11:29', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 1, 1, '2018-12-18 15:14:44', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 1, 1, '2018-12-18 15:16:07', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 1, 1, '2018-12-18 15:17:28', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (6, 0, 4, 0, '系统监控', '', '', 10, 'monitor', 'monitor', b'0', b'0', b'0', '', 1, 1, '2018-12-18 15:17:48', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/index', 11, 'log', 'logs', b'0', b'0', b'0', '', 1, 1, '2018-12-18 15:18:26', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 999, 'timing', 'timing', b'0', b'0', b'0', 'timing:list', 1, 1, '2019-01-07 20:34:40', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 12, 'error', 'errorLog', b'0', b'0', b'0', '', 1, 1, '2019-01-13 13:49:03', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 1, 1, '2019-03-25 09:46:00', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 1, 1, '2019-03-29 13:51:18', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 1, 1, '2019-04-10 11:49:04', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', '', 1, 1, '2019-10-26 22:08:43', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (44, 2, 0, 2, '用户新增', '', '', 2, '', '', b'0', b'0', b'0', 'user:add', 1, 1, '2019-10-29 10:59:46', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (45, 2, 0, 2, '用户编辑', '', '', 3, '', '', b'0', b'0', b'0', 'user:edit', 1, 1, '2019-10-29 11:00:08', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (46, 2, 0, 2, '用户删除', '', '', 4, '', '', b'0', b'0', b'0', 'user:del', 1, 1, '2019-10-29 11:00:23', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (48, 3, 0, 2, '角色创建', '', '', 2, '', '', b'0', b'0', b'0', 'roles:add', 1, 1, '2019-10-29 12:45:34', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (49, 3, 0, 2, '角色修改', '', '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 1, 1, '2019-10-29 12:46:16', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (50, 3, 0, 2, '角色删除', '', '', 4, '', '', b'0', b'0', b'0', 'roles:del', 1, 1, '2019-10-29 12:46:51', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (52, 5, 0, 2, '菜单新增', '', '', 2, '', '', b'0', b'0', b'0', 'menu:add', 1, 1, '2019-10-29 12:55:07', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (53, 5, 0, 2, '菜单编辑', '', '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 1, 1, '2019-10-29 12:55:40', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (54, 5, 0, 2, '菜单删除', '', '', 4, '', '', b'0', b'0', b'0', 'menu:del', 1, 1, '2019-10-29 12:56:00', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (56, 35, 0, 2, '部门新增', '', '', 2, '', '', b'0', b'0', b'0', 'dept:add', 1, 1, '2019-10-29 12:57:09', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (57, 35, 0, 2, '部门编辑', '', '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 1, 1, '2019-10-29 12:57:27', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (58, 35, 0, 2, '部门删除', '', '', 4, '', '', b'0', b'0', b'0', 'dept:del', 1, 1, '2019-10-29 12:57:41', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (60, 37, 0, 2, '岗位新增', '', '', 2, '', '', b'0', b'0', b'0', 'job:add', 1, 1, '2019-10-29 12:58:27', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (61, 37, 0, 2, '岗位编辑', '', '', 3, '', '', b'0', b'0', b'0', 'job:edit', 1, 1, '2019-10-29 12:58:45', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (62, 37, 0, 2, '岗位删除', '', '', 4, '', '', b'0', b'0', b'0', 'job:del', 1, 1, '2019-10-29 12:59:04', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (64, 39, 0, 2, '字典新增', '', '', 2, '', '', b'0', b'0', b'0', 'dict:add', 1, 1, '2019-10-29 13:00:17', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (65, 39, 0, 2, '字典编辑', '', '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 1, 1, '2019-10-29 13:00:42', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (66, 39, 0, 2, '字典删除', '', '', 4, '', '', b'0', b'0', b'0', 'dict:del', 1, 1, '2019-10-29 13:00:59', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (73, 28, 0, 2, '任务新增', '', '', 2, '', '', b'0', b'0', b'0', 'timing:add', 1, 1, '2019-10-29 13:07:28', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (74, 28, 0, 2, '任务编辑', '', '', 3, '', '', b'0', b'0', b'0', 'timing:edit', 1, 1, '2019-10-29 13:07:41', '2020-12-13 16:25:07', b'0');
INSERT INTO `sys_menu` VALUES (75, 28, 0, 2, '任务删除', '', '', 4, '', '', b'0', b'0', b'0', 'timing:del', 1, 1, '2019-10-29 13:07:54', '2020-12-13 16:25:07', b'0');

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'cron 表达式',
  `is_pause` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态：0暂停、1启用',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `person_in_charge` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '负责人',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '报警邮箱',
  `sub_task` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '子任务ID',
  `pause_after_failure` bit(1) NOT NULL DEFAULT b'0' COMMENT '任务失败后是否暂停,0是暂停，1是不暂停',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除：0启用（默认）、1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `inx_is_pause`(`is_pause`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO `sys_quartz_job` VALUES (2, 'testTask', '*/5 * * * * ?', b'1', 'test', 'run', '我想要休息一下', '测试有参数run', '李四', '', '', b'1', 1, 1, '2021-01-22 10:48:51', '2021-01-22 10:48:51', b'0');
INSERT INTO `sys_quartz_job` VALUES (4, 'testTask', '*/5 * * * * ?', b'1', 'test2', 'run', '', '测试子任务', '张三', '', '2', b'1', 1, 1, '2021-01-22 10:48:51', '2021-01-22 10:48:51', b'0');
INSERT INTO `sys_quartz_job` VALUES (5, 'testTask', '*/5 * * * * ?', b'1', 'rest', 'run', '', '测试无参数run', '李四', '', '', b'1', 1, 1, '2021-01-22 10:48:51', '2021-01-22 10:48:51', b'0');
INSERT INTO `sys_quartz_job` VALUES (9, 'testTask', '*/5 * * * * ?', b'1', 'test', 'job', '', '测试job任务', '张三', '', '', b'1', 1, 1, '2021-01-22 10:48:51', '2021-01-22 10:48:51', b'0');

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_log`;
CREATE TABLE `sys_quartz_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'bean对象名称',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'cron表达式',
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常详情',
  `is_success` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态（是否成功）1成功，0失败(默认)',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '执行方法',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '方法参数',
  `time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '执行时间(ms)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除：0启用（默认）、1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 141 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务日志' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_quartz_log
-- ----------------------------
INSERT INTO `sys_quartz_log` VALUES (1, 'testTask', '*/5 * * * * ?', 'java.lang.NumberFormatException: For input string: \"\"\r\n	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\r\n	at java.base/java.lang.Long.parseLong(Long.java:702)\r\n	at java.base/java.lang.Long.parseLong(Long.java:817)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl.executionSubJob(QuartzJobServiceImpl.java:67)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$FastClassBySpringCGLIB$$c8d365fd.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:746)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$EnhancerBySpringCGLIB$$747379eb.executionSubJob(<generated>)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:74)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', '', 886, '2021-01-22 10:49:10', '2021-01-22 10:49:10', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (2, 'testTask', '*/5 * * * * ?', 'java.lang.NumberFormatException: For input string: \"\"\r\n	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\r\n	at java.base/java.lang.Long.parseLong(Long.java:702)\r\n	at java.base/java.lang.Long.parseLong(Long.java:817)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl.executionSubJob(QuartzJobServiceImpl.java:67)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$FastClassBySpringCGLIB$$c8d365fd.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:746)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$EnhancerBySpringCGLIB$$4c66ffb1.executionSubJob(<generated>)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:76)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', '', 315, '2021-01-22 10:52:53', '2021-01-22 10:52:53', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (3, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 10:56:15', '2021-01-22 10:56:15', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (4, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 1, '2021-01-22 11:19:23', '2021-01-22 11:19:23', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (5, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:21:55', '2021-01-22 11:21:55', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (6, '11', '*/5 * * * * ?', NULL, b'0', '2', '1', '', 0, '2021-01-22 11:22:02', '2021-01-22 11:22:02', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (7, '11', '*/5 * * * * ?', NULL, b'0', '2', '1', '', 0, '2021-01-22 11:22:26', '2021-01-22 11:22:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (8, '11', '*/5 * * * * ?', NULL, b'0', '2', '1', '', 0, '2021-01-22 11:23:53', '2021-01-22 11:23:53', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (9, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:28:01', '2021-01-22 11:28:01', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (10, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 11:28:06', '2021-01-22 11:28:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (11, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 11:28:20', '2021-01-22 11:28:20', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (12, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:28:25', '2021-01-22 11:28:25', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (13, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 11:28:31', '2021-01-22 11:28:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (14, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:28:39', '2021-01-22 11:28:39', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (15, '11', '*/5 * * * * ?', NULL, b'0', '2', '1', '', 0, '2021-01-22 11:30:50', '2021-01-22 11:30:50', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (16, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:31:54', '2021-01-22 11:31:54', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (17, '11', '*/5 * * * * ?', NULL, b'0', '2', '1', '', 0, '2021-01-22 11:32:12', '2021-01-22 11:32:12', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (18, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:43:14', '2021-01-22 11:43:14', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (19, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 2, '2021-01-22 11:44:11', '2021-01-22 11:44:11', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (20, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:15', '2021-01-22 11:44:15', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (21, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:20', '2021-01-22 11:44:20', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (22, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 11:44:25', '2021-01-22 11:44:25', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (23, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:30', '2021-01-22 11:44:30', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (24, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:35', '2021-01-22 11:44:35', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (25, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:40', '2021-01-22 11:44:40', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (26, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:44:45', '2021-01-22 11:44:45', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (27, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 11:45:30', '2021-01-22 11:45:30', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (28, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 2, '2021-01-22 14:05:22', '2021-01-22 14:05:22', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (29, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:06:07', '2021-01-22 14:06:07', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (30, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 1, '2021-01-22 14:07:52', '2021-01-22 14:07:52', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (31, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:08:43', '2021-01-22 14:08:43', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (32, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 0, '2021-01-22 14:09:08', '2021-01-22 14:09:08', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (33, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 2, '2021-01-22 14:14:55', '2021-01-22 14:14:55', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (34, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:28)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:15:33', '2021-01-22 14:15:33', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (35, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 2, '2021-01-22 14:20:06', '2021-01-22 14:20:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (36, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 未登录或当前登录状态过期\r\n	at marchsoft.utils.SecurityUtils.getCurrentUser(SecurityUtils.java:34)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:41)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 1, '2021-01-22 14:20:11', '2021-01-22 14:20:11', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (37, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:22:07', '2021-01-22 14:22:07', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (38, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:24:37', '2021-01-22 14:24:37', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (39, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:25:26', '2021-01-22 14:25:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (40, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 16591, '2021-01-22 14:26:02', '2021-01-22 14:26:02', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (41, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 2705, '2021-01-22 14:35:31', '2021-01-22 14:35:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (42, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 2, '2021-01-22 14:35:50', '2021-01-22 14:35:50', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (43, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 2, '2021-01-22 14:35:50', '2021-01-22 14:35:50', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (44, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 16710, '2021-01-22 14:36:35', '2021-01-22 14:36:35', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (45, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 14:37:48', '2021-01-22 14:37:48', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (46, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 15954, '2021-01-22 14:41:29', '2021-01-22 14:41:30', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (47, '11', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 该bean对象或者方法不存在\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 25369, '2021-01-22 14:43:34', '2021-01-22 14:43:34', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (48, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 14:47:39', '2021-01-22 14:47:39', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (49, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 0, '2021-01-22 14:53:16', '2021-01-22 14:53:16', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (50, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 1, '2021-01-22 14:57:23', '2021-01-22 14:57:23', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (51, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 1, '2021-01-22 14:58:52', '2021-01-22 14:58:52', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (52, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 14:59:49', '2021-01-22 14:59:49', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (53, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 14:59:58', '2021-01-22 14:59:58', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (54, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:00:00', '2021-01-22 15:00:00', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (55, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:00:06', '2021-01-22 15:00:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (56, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 15:00:15', '2021-01-22 15:00:15', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (57, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:00:16', '2021-01-22 15:00:16', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (58, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:00:20', '2021-01-22 15:00:20', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (59, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 15:03:17', '2021-01-22 15:03:17', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (60, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:03:21', '2021-01-22 15:03:21', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (61, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 15:03:21', '2021-01-22 15:03:21', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (62, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 15:03:26', '2021-01-22 15:03:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (63, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 15:03:26', '2021-01-22 15:03:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (64, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 15:03:31', '2021-01-22 15:03:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (65, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 15:03:31', '2021-01-22 15:03:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (66, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 0, '2021-01-22 15:03:36', '2021-01-22 15:03:36', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (67, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 3, '2021-01-22 15:12:23', '2021-01-22 15:12:23', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (68, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 15:17:42', '2021-01-22 15:17:42', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (69, 'testTask', '*/5 * * * * ?', 'marchsoft.exception.BadRequestException: 未登录或当前登录状态过期\r\n	at marchsoft.utils.SecurityUtils.getCurrentUser(SecurityUtils.java:34)\r\n	at marchsoft.modules.quartz.utils.QuartzManage.runJobNow(QuartzManage.java:198)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl.execution(QuartzJobServiceImpl.java:148)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl.executionSubJob(QuartzJobServiceImpl.java:71)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$FastClassBySpringCGLIB$$c8d365fd.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:746)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)\r\n	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at marchsoft.modules.quartz.service.impl.QuartzJobServiceImpl$$EnhancerBySpringCGLIB$$7bda4b27.executionSubJob(<generated>)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:78)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test2', 'run', '', 629, '2021-01-22 15:17:43', '2021-01-22 15:17:43', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (70, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 15:19:11', '2021-01-22 15:19:11', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (71, '11', '*/5 * * * * ?', 'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'11\' available\r\n	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:772)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1212)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:294)\r\n	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)\r\n	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1083)\r\n	at marchsoft.utils.SpringContextHolder.getBean(SpringContextHolder.java:31)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:27)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', '2', '1', '', 3, '2021-01-22 15:23:09', '2021-01-22 15:23:09', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (72, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', 'name', 3, '2021-01-22 19:53:15', '2021-01-22 19:53:15', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (73, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run()\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:35)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test2', 'run', '', 0, '2021-01-22 19:53:27', '2021-01-22 19:53:27', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (74, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 4, '2021-01-22 20:00:27', '2021-01-22 20:00:27', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (75, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 148, '2021-01-22 20:30:16', '2021-01-22 20:30:16', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (76, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 3, '2021-01-22 20:30:21', '2021-01-22 20:30:21', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (77, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 301, '2021-01-22 20:30:22', '2021-01-22 20:30:22', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (78, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 148, '2021-01-22 20:30:26', '2021-01-22 20:30:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (79, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:26', '2021-01-22 20:30:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (80, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:31', '2021-01-22 20:30:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (81, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 391, '2021-01-22 20:30:32', '2021-01-22 20:30:32', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (82, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:36', '2021-01-22 20:30:36', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (83, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 409, '2021-01-22 20:30:36', '2021-01-22 20:30:36', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (84, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 144, '2021-01-22 20:30:41', '2021-01-22 20:30:41', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (85, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:41', '2021-01-22 20:30:41', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (86, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:46', '2021-01-22 20:30:46', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (87, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 159, '2021-01-22 20:30:46', '2021-01-22 20:30:46', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (88, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:30:51', '2021-01-22 20:30:51', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (89, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 143, '2021-01-22 20:30:51', '2021-01-22 20:30:51', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (90, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 165, '2021-01-22 20:30:56', '2021-01-22 20:30:56', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (91, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:30:56', '2021-01-22 20:30:56', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (92, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:31:01', '2021-01-22 20:31:01', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (93, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 271, '2021-01-22 20:31:01', '2021-01-22 20:31:01', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (94, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:31:06', '2021-01-22 20:31:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (95, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 154, '2021-01-22 20:31:06', '2021-01-22 20:31:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (96, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:11', '2021-01-22 20:31:11', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (97, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 408, '2021-01-22 20:31:12', '2021-01-22 20:31:12', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (98, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 120, '2021-01-22 20:31:16', '2021-01-22 20:31:16', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (99, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:17', '2021-01-22 20:31:17', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (100, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:21', '2021-01-22 20:31:21', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (101, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 202, '2021-01-22 20:31:22', '2021-01-22 20:31:22', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (102, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:26', '2021-01-22 20:31:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (103, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 430, '2021-01-22 20:31:26', '2021-01-22 20:31:26', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (104, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 188, '2021-01-22 20:31:31', '2021-01-22 20:31:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (105, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:31', '2021-01-22 20:31:31', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (106, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:31:36', '2021-01-22 20:31:36', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (107, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 162, '2021-01-22 20:31:37', '2021-01-22 20:31:37', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (108, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 0, '2021-01-22 20:31:41', '2021-01-22 20:31:41', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (109, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 62, '2021-01-22 20:31:52', '2021-01-22 20:31:52', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (110, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 0, '2021-01-22 20:32:12', '2021-01-22 20:32:12', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (111, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 150, '2021-01-22 20:49:53', '2021-01-22 20:49:53', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (112, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 3, '2021-01-22 20:49:58', '2021-01-22 20:49:58', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (113, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:33)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:57)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'name', 157, '2021-01-22 20:50:34', '2021-01-22 20:50:34', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (114, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:50:40', '2021-01-22 20:50:40', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (115, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:52:08', '2021-01-22 20:52:08', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (116, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:52:17', '2021-01-22 20:52:17', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (117, 'testTask', '*/5 * * * * ?', NULL, b'1', 'rest', 'run', '', 1, '2021-01-22 20:52:18', '2021-01-22 20:52:18', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (118, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 1, '2021-01-22 21:14:47', '2021-01-22 21:14:47', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (119, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:14:51', '2021-01-22 21:14:51', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (120, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 21:14:52', '2021-01-22 21:14:52', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (121, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:14:56', '2021-01-22 21:14:56', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (122, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 2, '2021-01-22 21:14:56', '2021-01-22 21:14:56', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (123, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 21:15:01', '2021-01-22 21:15:01', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (124, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:15:01', '2021-01-22 21:15:01', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (125, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:15:06', '2021-01-22 21:15:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (126, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 1, '2021-01-22 21:15:06', '2021-01-22 21:15:06', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (127, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test2', 'run', '', 0, '2021-01-22 21:15:11', '2021-01-22 21:15:11', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (128, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:17:00', '2021-01-22 21:17:00', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (129, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '', 0, '2021-01-22 21:17:05', '2021-01-22 21:17:05', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (130, 'testTask', '*/5 * * * * ?', 'java.lang.ClassNotFoundException: Integer\r\n	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)\r\n	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)\r\n	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)\r\n	at java.base/java.lang.Class.forName0(Native Method)\r\n	at java.base/java.lang.Class.forName(Class.java:315)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:36)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'Integer,String', 3, '2021-01-23 08:47:59', '2021-01-23 08:47:59', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (131, 'testTask', '*/5 * * * * ?', 'java.lang.ClassNotFoundException: Integer\r\n	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)\r\n	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)\r\n	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)\r\n	at java.base/java.lang.Class.forName0(Native Method)\r\n	at java.base/java.lang.Class.forName(Class.java:315)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:37)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'Integer,String', 3, '2021-01-23 08:51:53', '2021-01-23 08:51:53', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (132, 'testTask', '*/5 * * * * ?', 'java.lang.ClassNotFoundException: Integer\r\n	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)\r\n	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)\r\n	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)\r\n	at java.base/java.lang.Class.forName0(Native Method)\r\n	at java.base/java.lang.Class.forName(Class.java:315)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:37)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'Integer,String', 0, '2021-01-23 08:53:51', '2021-01-23 08:53:51', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (133, 'testTask', '*/5 * * * * ?', 'java.lang.ClassNotFoundException: Integer\r\n	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)\r\n	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)\r\n	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)\r\n	at java.base/java.lang.Class.forName0(Native Method)\r\n	at java.base/java.lang.Class.forName(Class.java:315)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:37)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'Integer,String', 1, '2021-01-23 08:53:58', '2021-01-23 08:53:58', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (134, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', 'Integer,String', 3, '2021-01-23 09:18:51', '2021-01-23 09:18:51', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (135, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', 'Integer,String', 3, '2021-01-23 09:19:33', '2021-01-23 09:19:33', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (136, 'testTask', '*/5 * * * * ?', NULL, b'1', 'test', 'run', '我想要休息一下', 3, '2021-01-23 09:25:58', '2021-01-23 09:25:58', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (137, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run1(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run1', '我想要休息一下', 1, '2021-01-23 10:30:49', '2021-01-23 10:30:49', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (138, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run1(java.lang.String)\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:40)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run1', '我想要休息一下', 0, '2021-01-23 18:56:43', '2021-01-23 18:56:43', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (139, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run()\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:42)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', '', 0, '2021-01-23 18:57:32', '2021-01-23 18:57:32', 0, 0, b'0');
INSERT INTO `sys_quartz_log` VALUES (140, 'testTask', '*/5 * * * * ?', 'java.lang.NoSuchMethodException: marchsoft.modules.quartz.task.TestTask.run()\r\n	at java.base/java.lang.Class.getDeclaredMethod(Class.java:2475)\r\n	at marchsoft.modules.quartz.utils.QuartzRunnable.<init>(QuartzRunnable.java:42)\r\n	at marchsoft.modules.quartz.utils.ExecutionJob.executeInternal(ExecutionJob.java:56)\r\n	at org.springframework.scheduling.quartz.QuartzJobBean.execute(QuartzJobBean.java:75)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', b'0', 'test', 'run', '', 0, '2021-01-23 18:57:36', '2021-01-23 18:57:36', 0, 0, b'0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `level` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色级别（越小越大）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '数据权限',
  `is_protection` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否受保护（内置角色，1为内置角色，默认值为0）',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_name`(`name`) USING BTREE COMMENT '角色名唯一',
  INDEX `idx_role_name`(`name`) USING BTREE COMMENT '普通索引——角色名查询角色信息'
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '-', '全部', b'1', 1, 1, '2020-12-13 11:36:45', '2020-12-13 11:36:47', b'0');
INSERT INTO `sys_role` VALUES (2, '普通用户', 2, '-', '自定义', b'1', 1, 1, '2020-12-13 11:37:14', '2020-12-13 11:37:18', b'0');

-- ----------------------------
-- Table structure for sys_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_depts`;
CREATE TABLE `sys_roles_depts`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色id',
  `dept_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mid_dept_id`(`dept_id`) USING BTREE COMMENT '普通索引——根据dept_id查询',
  INDEX `idx_mid_role_id`(`role_id`) USING BTREE COMMENT '普通索引——根据role_id查询'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色部门关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_roles_depts
-- ----------------------------
INSERT INTO `sys_roles_depts` VALUES (1, 2, 7);

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_menus`;
CREATE TABLE `sys_roles_menus`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id\n',
  `menu_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '菜单ID',
  `role_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mid_role_id`(`role_id`) USING BTREE COMMENT '普通索引——根据role_id查询',
  INDEX `idx_mid_menu_id`(`menu_id`) USING BTREE COMMENT '普通索引——根据menu_id查询'
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
INSERT INTO `sys_roles_menus` VALUES (1, 1, 1);
INSERT INTO `sys_roles_menus` VALUES (2, 1, 2);
INSERT INTO `sys_roles_menus` VALUES (3, 2, 1);
INSERT INTO `sys_roles_menus` VALUES (4, 2, 2);
INSERT INTO `sys_roles_menus` VALUES (5, 3, 1);
INSERT INTO `sys_roles_menus` VALUES (6, 3, 2);
INSERT INTO `sys_roles_menus` VALUES (7, 5, 1);
INSERT INTO `sys_roles_menus` VALUES (8, 5, 2);
INSERT INTO `sys_roles_menus` VALUES (9, 6, 1);
INSERT INTO `sys_roles_menus` VALUES (10, 7, 1);
INSERT INTO `sys_roles_menus` VALUES (26, 28, 1);
INSERT INTO `sys_roles_menus` VALUES (28, 32, 1);
INSERT INTO `sys_roles_menus` VALUES (31, 35, 1);
INSERT INTO `sys_roles_menus` VALUES (34, 37, 1);
INSERT INTO `sys_roles_menus` VALUES (36, 39, 1);
INSERT INTO `sys_roles_menus` VALUES (37, 41, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `gender` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别（0为男默认，1为女）',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `avatar_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像路径',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `is_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用（默认）、0禁用',
  `create_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_by` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新者id',
  `pwd_reset_time` datetime(0) NULL DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '软删除（默认值为0，1为删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE COMMENT '普通索引——根据dept_id查询用户',
  INDEX `idx_enabled`(`enabled`) USING BTREE COMMENT '普通索引——根据enabled查询'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 2, 'admin', '管理员', b'0', '18888888888', '201507802@qq.com', '', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1', 1, 1, '2020-12-13 11:27:20', '2020-12-13 11:26:52', '2020-12-13 11:26:57', b'0');
INSERT INTO `sys_user` VALUES (2, 5, 'test', '测试', b'1', '15689899898', '231@qq.com', '', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 1, 1, NULL, '2020-12-13 11:34:53', '2020-12-13 11:34:59', b'0');

-- ----------------------------
-- Table structure for sys_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_jobs`;
CREATE TABLE `sys_users_jobs`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `job_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '岗位ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mid_job_id`(`job_id`) USING BTREE COMMENT '普通索引——根据job_id查询',
  INDEX `idx_mid_user_id`(`user_id`) USING BTREE COMMENT '普通索引——根据user_id查询'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_users_jobs
-- ----------------------------
INSERT INTO `sys_users_jobs` VALUES (1, 1, 1);
INSERT INTO `sys_users_jobs` VALUES (2, 1, 1);
INSERT INTO `sys_users_jobs` VALUES (3, 2, 2);

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_roles`;
CREATE TABLE `sys_users_roles`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id\n',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `role_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mid_role_id`(`role_id`) USING BTREE COMMENT '普通索引——根据role_id查询',
  INDEX `idx_mid_user_id`(`user_id`) USING BTREE COMMENT '普通索引——根据user_id查询用户'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO `sys_users_roles` VALUES (1, 1, 1);
INSERT INTO `sys_users_roles` VALUES (2, 2, 1);

SET FOREIGN_KEY_CHECKS = 1;
