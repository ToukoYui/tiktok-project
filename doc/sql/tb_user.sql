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

 Date: 19/08/2023 17:06:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_username`(`username`) USING BTREE,
  INDEX `index_pwd`(`password`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'touko', '123456', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819012227.jpg');
INSERT INTO `tb_user` VALUES (2, 'yukino', '123456', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819164627.jpg');
INSERT INTO `tb_user` VALUES (3, 'zhangsan', '123456', NULL);
INSERT INTO `tb_user` VALUES (4, 'lisi', '123456', NULL);
INSERT INTO `tb_user` VALUES (5, 'wangwu', '66666', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819164627.jpg');

SET FOREIGN_KEY_CHECKS = 1;
