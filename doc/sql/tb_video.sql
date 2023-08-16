/*
 Navicat Premium Data Transfer

 Source Server         : mysqlFromMyCloud
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : db_douyin

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 16/08/2023 16:51:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_video
-- ----------------------------
DROP TABLE IF EXISTS `tb_video`;
CREATE TABLE `tb_video`  (
  `id` bigint(0) NOT NULL,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `play_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE,
  INDEX `index_create_time`(`created_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_video
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
