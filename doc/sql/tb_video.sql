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

 Date: 19/08/2023 17:06:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_video
-- ----------------------------
DROP TABLE IF EXISTS `tb_video`;
CREATE TABLE `tb_video`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
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
INSERT INTO `tb_video` VALUES (1, 1, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/08/18/87d0747d368b4c069fe089a8bd1eb706%E5%94%B1%E6%AD%8C.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/87d0747d368b4c069fe089a8bd1eb706%E5%94%B1%E6%AD%8C.jpg', '唱歌测试视频', '2023-08-19 15:05:54');
INSERT INTO `tb_video` VALUES (2, 1, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/2/18/92415cd0a4ba2542a27eff77e6b20161.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/2/18/92415cd0a4ba2542a27eff77e6b20161.jpg', '柯基', '2023-08-19 13:56:07');
INSERT INTO `tb_video` VALUES (3, 5, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/2/18/d3c797204c1acaa06a377d86c8f25b3b.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/2/18/d3c797204c1acaa06a377d86c8f25b3b.jpg', '下雪', '2023-08-19 16:56:39');

SET FOREIGN_KEY_CHECKS = 1;
