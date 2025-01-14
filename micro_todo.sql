/*
 Navicat Premium Data Transfer

 Source Server         : micro_todo
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : micro_todo

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 14/01/2025 12:37:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_task`;
CREATE TABLE `tb_task`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务描述',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `parent_id` int NULL DEFAULT NULL COMMENT '父任务ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务类型',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '代办' COMMENT '任务状态',
  `priority` int NOT NULL DEFAULT 5 COMMENT '任务优先级',
  `sequence` int NOT NULL COMMENT '任务顺序',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  `reminder_time` datetime NULL DEFAULT NULL COMMENT '任务提醒时间',
  `completion_time` datetime NULL DEFAULT NULL COMMENT '任务完成时间',
  `previous_sequence` int NULL DEFAULT NULL COMMENT '任务完成前的序号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `tb_task_fk_1` FOREIGN KEY (`parent_id`) REFERENCES `tb_task` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 604 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_task
-- ----------------------------
INSERT INTO `tb_task` VALUES (571, '添加任务操作的撤销/重做功能', '2025-01-07 18:32:22', NULL, NULL, NULL, '代办', 5, 1000000, 0, 974, '2025-01-14 12:12:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (572, '项目实现绑定微信登录', '2025-01-07 18:32:36', NULL, NULL, NULL, '代办', 5, 1300000, 0, 941, '2025-01-09 00:33:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (573, 'Ai判定优先级', '2025-01-07 18:32:41', NULL, NULL, NULL, '代办', 5, 1400000, 0, 949, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (574, '扩展todolist时间提醒形式', '2025-01-07 18:33:12', NULL, NULL, NULL, '代办', 5, 1100000, 0, 978, '2025-01-14 11:50:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (577, '邮箱提醒', '2025-01-07 18:33:37', NULL, 574, NULL, '代办', 5, 100000, 0, 252, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (580, '了解微信登录开发文档', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 100000, 0, 221, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (581, '注册微信开放平台账号', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 160000, 0, 243, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (582, '创建应用并获取AppID和AppSecret', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 140000, 0, 245, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (583, '集成微信登录SDK到项目中', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 130000, 0, 238, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (584, '编写后端代码实现微信登录功能', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 150000, 0, 246, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (585, '测试微信登录功能', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 110000, 0, 255, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (586, '上线项目并绑定微信登录', '2025-01-07 18:34:16', NULL, 572, NULL, '代办', 5, 120000, 0, 262, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (587, '学习撤销/重做功能的实现原理', '2025-01-07 18:34:26', NULL, 571, NULL, '代办', 5, 100000, 0, 238, '2025-01-14 10:17:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (588, '设计任务操作的撤销/重做功能框架', '2025-01-07 18:34:26', NULL, 571, NULL, '代办', 5, 130000, 0, 304, '2025-01-14 10:24:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (589, '实现任务操作的撤销功能', '2025-01-07 18:34:26', NULL, 571, NULL, '代办', 5, 140000, 0, 263, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (590, '实现任务操作的重做功能', '2025-01-07 18:34:26', NULL, 571, NULL, '代办', 5, 110000, 0, 273, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (591, '测试撤销/重做功能的准确性和稳定性', '2025-01-07 18:34:26', NULL, 571, NULL, '代办', 5, 120000, 0, 284, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (597, '短信提醒', '2025-01-07 18:36:23', NULL, 574, NULL, '代办', 5, 110000, 0, 351, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (598, '多选编辑', '2025-01-07 18:37:11', NULL, NULL, NULL, '代办', 5, 1500000, 0, 925, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (599, '时间管理', '2025-01-07 18:37:39', NULL, NULL, NULL, '代办', 5, 1600000, 0, 1037, '2025-01-08 22:09:00', NULL, NULL);
INSERT INTO `tb_task` VALUES (600, '预估耗时', '2025-01-07 18:37:45', NULL, 599, NULL, '代办', 5, 100000, 0, 221, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (601, '优先级自动建议', '2025-01-07 18:38:10', NULL, NULL, NULL, '代办', 5, 1700000, 0, 978, NULL, NULL, NULL);
INSERT INTO `tb_task` VALUES (602, '工作量分析', '2025-01-07 18:38:23', NULL, NULL, NULL, '代办', 5, 1200000, 0, 875, '2025-01-09 09:00:00', NULL, NULL);

-- ----------------------------
-- Table structure for tb_task_copy1
-- ----------------------------
DROP TABLE IF EXISTS `tb_task_copy1`;
CREATE TABLE `tb_task_copy1`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务描述',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `parent_id` int NULL DEFAULT NULL COMMENT '父任务ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务类型',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '代办' COMMENT '任务状态',
  `priority` int NOT NULL DEFAULT 5 COMMENT '任务优先级',
  `sequence` int NOT NULL COMMENT '任务顺序',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  `reminder_time` datetime NULL DEFAULT NULL COMMENT '任务提醒时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `tb_task_copy1_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `tb_task` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 541 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_task_copy1
-- ----------------------------
INSERT INTO `tb_task_copy1` VALUES (467, '项目代码修改（增加项目功能描述页面）', '2024-06-05 19:24:48', NULL, NULL, NULL, '代办', 5, 25, 1, 1, '2025-01-07 14:56:00');
INSERT INTO `tb_task_copy1` VALUES (494, '时间预测', '2025-01-06 10:56:30', NULL, NULL, NULL, '代办', 5, 32, 0, 1, '2025-01-06 20:00:00');
INSERT INTO `tb_task_copy1` VALUES (501, '发邮箱时间提醒', '2025-01-06 11:31:43', NULL, NULL, NULL, '代办', 5, 31, 0, 0, '2025-01-07 23:29:00');
INSERT INTO `tb_task_copy1` VALUES (519, '睡大觉', '2025-01-06 17:00:57', NULL, NULL, NULL, '代办', 5, 35, 0, 12, '2025-01-16 16:15:59');
INSERT INTO `tb_task_copy1` VALUES (524, '项目实现绑定微信登录', '2025-01-07 19:09:32', NULL, NULL, NULL, '代办', 5, 37, 0, 0, NULL);
INSERT INTO `tb_task_copy1` VALUES (537, 'Ai判定优先级', '2025-01-07 22:59:05', NULL, NULL, NULL, '代办', 5, 45, 0, 0, NULL);
INSERT INTO `tb_task_copy1` VALUES (539, '任务测试1', '2025-01-07 16:08:56', NULL, NULL, NULL, '代办', 5, 1000, 0, 0, NULL);
INSERT INTO `tb_task_copy1` VALUES (540, '测试2', '2025-01-07 16:11:28', NULL, NULL, NULL, '代办', 5, 1000, 0, 0, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示名称',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL，可以是本地上传或GitHub头像URL',
  `github_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'GitHub用户ID',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'GitHub邮箱',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'GitHub访问令牌',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uname`(`uname` ASC) USING BTREE,
  UNIQUE INDEX `idx_github_id`(`github_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'suki1', '12345', 'suki', '0763866b-dab0-45b1-9f9a-64430ca602a3.jpg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, 'admin', 'admin', 'dumb fox', '90a6de0d-da88-40d5-9194-8679b7764eba.jpg', NULL, '', NULL);
INSERT INTO `user` VALUES (3, 'john_doe', 'password123', 'john_doe', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, 'jane_smith', 'qwerty', 'jane_smith', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, 'bob', 'bobpassword', 'bob', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, 'charlie', 'charlie123', 'charlie', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, 'dave', 'davepassword', 'dave', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, 'frank', 'frankpassword', 'frank', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (11, 'zhangsan1', '12345', 'zhangsan1', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (12, 'suki12', '123456', 'suki12', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (13, 'suki', '12345', 'suki', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (14, 'zzkdsb', 'zzkdsb', 'zzkdsb', 'https://api.dicebear.com/7.x/avataaars/svg', NULL, NULL, NULL);
INSERT INTO `user` VALUES (18, 'testuser', 'admin', 'Test User', 'https://github.com/identicons/test.png', '12345', 'test@example.com', NULL);
INSERT INTO `user` VALUES (28, 'blessonism', '583af3f2-cf61-4ccd-9714-cf80949572d9', 'Edom', 'https://avatars.githubusercontent.com/u/87158944?v=4', '87158944', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
