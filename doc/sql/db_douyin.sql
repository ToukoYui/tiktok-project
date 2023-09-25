/*
 Navicat Premium Data Transfer

 Source Server         : mysq2
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : db_douyin

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 18/09/2023 01:04:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `video_id` bigint(0) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE,
  INDEX `index_video_id`(`video_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 98 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (24, 4, 1, '牛逼6666哈哈哈123', '08-26');
INSERT INTO `tb_comment` VALUES (25, 4, 1, '6666', '08-26');
INSERT INTO `tb_comment` VALUES (26, 4, 1, '第一次见这种操作', '08-26');
INSERT INTO `tb_comment` VALUES (27, 4, 1, '第一次见这种操作', '08-26');
INSERT INTO `tb_comment` VALUES (28, 4, 1, '可以的啊', '08-26');
INSERT INTO `tb_comment` VALUES (29, 4, 1, '可以的啊', '08-26');
INSERT INTO `tb_comment` VALUES (30, 5, 1, '这是什么操作啊666', '08-26');
INSERT INTO `tb_comment` VALUES (31, 5, 1, '这是什么操作啊666', '08-26');
INSERT INTO `tb_comment` VALUES (32, 5, 1, '长见识了哈哈哈哈哈', '08-26');
INSERT INTO `tb_comment` VALUES (33, 5, 1, '长见识了哈哈哈哈哈', '08-26');
INSERT INTO `tb_comment` VALUES (34, 5, 2, '我去哲哲。。。', '08-26');
INSERT INTO `tb_comment` VALUES (35, 5, 2, '我去哲哲。。。', '08-26');
INSERT INTO `tb_comment` VALUES (36, 5, 2, '难崩啊', '08-26');
INSERT INTO `tb_comment` VALUES (37, 5, 2, '难崩啊', '08-26');
INSERT INTO `tb_comment` VALUES (38, 5, 2, '难崩啊11111', '08-26');
INSERT INTO `tb_comment` VALUES (39, 4, 1, '1212', '08-26');
INSERT INTO `tb_comment` VALUES (40, 4, 2, '111', '08-26');
INSERT INTO `tb_comment` VALUES (41, 4, 3, '123', '08-26');
INSERT INTO `tb_comment` VALUES (42, 4, 1, '11111', '08-26');
INSERT INTO `tb_comment` VALUES (43, 4, 2, '123', '08-26');
INSERT INTO `tb_comment` VALUES (44, 4, 1, '123456', '08-26');
INSERT INTO `tb_comment` VALUES (45, 4, 2, '发评论', '08-26');
INSERT INTO `tb_comment` VALUES (46, 4, 1, '111', '08-27');
INSERT INTO `tb_comment` VALUES (47, 4, 1, 'lisi comment', '08-27');
INSERT INTO `tb_comment` VALUES (48, 4, 2, 'lisi comment', '08-27');
INSERT INTO `tb_comment` VALUES (49, 4, 1, '123', '08-27');
INSERT INTO `tb_comment` VALUES (50, 4, 1, 'lisi comment', '08-27');
INSERT INTO `tb_comment` VALUES (53, 4, 3, '测试评论数量', '08-28');
INSERT INTO `tb_comment` VALUES (54, 4, 3, '测试评论数量2', '08-28');
INSERT INTO `tb_comment` VALUES (55, 1, 3, '。。。', '08-29');
INSERT INTO `tb_comment` VALUES (56, 1, 3, '。。。。', '08-29');
INSERT INTO `tb_comment` VALUES (57, 1, 3, '好好好', '08-30');
INSERT INTO `tb_comment` VALUES (58, 1, 3, '哈哈哈哈', '08-30');
INSERT INTO `tb_comment` VALUES (60, 1, 5, 'test', '08-30');
INSERT INTO `tb_comment` VALUES (61, 1, 5, 'ttt', '08-30');
INSERT INTO `tb_comment` VALUES (62, 1, 5, 'aaa', '08-30');
INSERT INTO `tb_comment` VALUES (66, 1, 2, '好好好', '08-30');
INSERT INTO `tb_comment` VALUES (67, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (68, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (69, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (70, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (71, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (72, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (73, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (74, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (75, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (76, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (77, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (78, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (79, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (80, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (81, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (82, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (83, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (84, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (85, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (86, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (87, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (88, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (89, 6, 5, '？', '08-30');
INSERT INTO `tb_comment` VALUES (90, 6, 3, '？', '08-30');
INSERT INTO `tb_comment` VALUES (91, 1, 3, 'ggg', '08-31');
INSERT INTO `tb_comment` VALUES (92, 1, 3, 'hhhh', '08-31');
INSERT INTO `tb_comment` VALUES (94, 1, 6, 'test', '09-03');
INSERT INTO `tb_comment` VALUES (95, 1, 3, 'snow', '09-03');
INSERT INTO `tb_comment` VALUES (96, 1, 6, '好好好', '09-05');
INSERT INTO `tb_comment` VALUES (97, 4, 6, '11111', '09-10');

-- ----------------------------
-- Table structure for tb_follows
-- ----------------------------
DROP TABLE IF EXISTS `tb_follows`;
CREATE TABLE `tb_follows`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL COMMENT '关注者id',
  `follow_id` bigint(0) NOT NULL COMMENT '被关注者id',
  `is_follow` int(0) NOT NULL COMMENT '默认为0，关注后为1',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_follow_id`(`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_follows
-- ----------------------------
INSERT INTO `tb_follows` VALUES (1, 1, 2, 1);
INSERT INTO `tb_follows` VALUES (2, 5, 1, 1);
INSERT INTO `tb_follows` VALUES (3, 5, 3, 1);
INSERT INTO `tb_follows` VALUES (4, 5, 2, 1);
INSERT INTO `tb_follows` VALUES (5, 5, 4, 0);
INSERT INTO `tb_follows` VALUES (6, 5, 6, 0);
INSERT INTO `tb_follows` VALUES (7, 1, 5, 1);
INSERT INTO `tb_follows` VALUES (8, 4, 1, 1);
INSERT INTO `tb_follows` VALUES (9, 4, 5, 1);
INSERT INTO `tb_follows` VALUES (10, 4, 6, 1);
INSERT INTO `tb_follows` VALUES (12, 4, 2, 0);
INSERT INTO `tb_follows` VALUES (13, 3, 2, 1);
INSERT INTO `tb_follows` VALUES (14, 1, 6, 1);
INSERT INTO `tb_follows` VALUES (15, 6, 1, 1);

-- ----------------------------
-- Table structure for tb_likes
-- ----------------------------
DROP TABLE IF EXISTS `tb_likes`;
CREATE TABLE `tb_likes`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `video_id` bigint(0) NOT NULL,
  `user_id` bigint(0) NOT NULL,
  `is_favorite` int(0) NOT NULL COMMENT '默认为0，点赞后为1',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_video_id`(`video_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_likes
-- ----------------------------
INSERT INTO `tb_likes` VALUES (16, 1, 6, 0);
INSERT INTO `tb_likes` VALUES (17, 2, 6, 0);
INSERT INTO `tb_likes` VALUES (18, 2, 6, 1);
INSERT INTO `tb_likes` VALUES (19, 3, 6, 1);
INSERT INTO `tb_likes` VALUES (20, 5, 6, 0);
INSERT INTO `tb_likes` VALUES (21, 6, 6, 1);
INSERT INTO `tb_likes` VALUES (22, 5, 6, 1);
INSERT INTO `tb_likes` VALUES (23, 3, 1, 1);
INSERT INTO `tb_likes` VALUES (24, 1, 1, 0);
INSERT INTO `tb_likes` VALUES (25, 6, 1, 1);
INSERT INTO `tb_likes` VALUES (26, 1, 4, 1);
INSERT INTO `tb_likes` VALUES (27, 3, 4, 1);
INSERT INTO `tb_likes` VALUES (28, 6, 4, 1);
INSERT INTO `tb_likes` VALUES (29, 2, 4, 1);
INSERT INTO `tb_likes` VALUES (30, 5, 4, 1);
INSERT INTO `tb_likes` VALUES (31, 2, 1, 1);
INSERT INTO `tb_likes` VALUES (32, 5, 1, 1);

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL COMMENT '发送者id',
  `to_user_id` bigint(0) NOT NULL COMMENT '接收者id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送内容',
  `create_time` datetime(0) NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_to_user_id`(`to_user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_message
-- ----------------------------
INSERT INTO `tb_message` VALUES (1, 1, 2, 'hello', '2023-09-14 10:31:00');
INSERT INTO `tb_message` VALUES (2, 1, 2, '你好呀', '2023-09-14 10:31:23');
INSERT INTO `tb_message` VALUES (3, 1, 2, '你好呀111', '2023-09-14 10:32:20');
INSERT INTO `tb_message` VALUES (4, 1, 6, '猫猫头', '2023-09-15 01:00:23');
INSERT INTO `tb_message` VALUES (5, 1, 5, '你好', '2023-09-15 01:00:51');
INSERT INTO `tb_message` VALUES (6, 1, 6, '你好', '2023-09-15 01:03:15');
INSERT INTO `tb_message` VALUES (8, 6, 1, 'hello', '2023-09-15 22:19:17');
INSERT INTO `tb_message` VALUES (10, 1, 6, 'abc', '2023-09-15 23:26:29');
INSERT INTO `tb_message` VALUES (11, 1, 6, 'bbb', '2023-09-15 23:28:32');
INSERT INTO `tb_message` VALUES (12, 1, 6, '啊啊啊', '2023-09-15 23:31:31');
INSERT INTO `tb_message` VALUES (13, 1, 5, 'hello', '2023-09-15 23:38:36');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'touko', '123456', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819012227.jpg');
INSERT INTO `tb_user` VALUES (2, 'yukino', '123456', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819164627.jpg');
INSERT INTO `tb_user` VALUES (3, 'zhangsan', '123456', NULL);
INSERT INTO `tb_user` VALUES (4, 'lisi', '123456', NULL);
INSERT INTO `tb_user` VALUES (5, 'wangwu', '66666', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230819164627.jpg');
INSERT INTO `tb_user` VALUES (6, 'anan', 'psx0211', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/QQ%E5%9B%BE%E7%89%8720230905005836.jpg');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_video
-- ----------------------------
INSERT INTO `tb_video` VALUES (1, 1, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/08/18/87d0747d368b4c069fe089a8bd1eb706%E5%94%B1%E6%AD%8C.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/18/87d0747d368b4c069fe089a8bd1eb706%E5%94%B1%E6%AD%8C.jpg', '唱歌测试视频', '2023-08-19 15:05:54');
INSERT INTO `tb_video` VALUES (2, 2, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/2/18/92415cd0a4ba2542a27eff77e6b20161.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/2/18/92415cd0a4ba2542a27eff77e6b20161.jpg', '柯基', '2023-08-19 13:56:07');
INSERT INTO `tb_video` VALUES (3, 5, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/2/18/d3c797204c1acaa06a377d86c8f25b3b.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/2/18/d3c797204c1acaa06a377d86c8f25b3b.jpg', '下雪', '2023-08-19 16:56:39');
INSERT INTO `tb_video` VALUES (5, 1, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/08/30/a38f7c1031224d13a3387a01c9c7933dkungfu.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/30/a38f7c1031224d13a3387a01c9c7933dkungfu.jpg', '功夫', '2023-08-30 11:37:24');
INSERT INTO `tb_video` VALUES (6, 6, 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/video/2023/08/31/09c3ec2860db4a408374cc423aa9a7f7VID_20230830_201907.mp4', 'https://douyin-1313537069.cos.ap-guangzhou.myqcloud.com/picture/video/2023/08/31/09c3ec2860db4a408374cc423aa9a7f7VID_20230830_201907.jpg', 'my home', '2023-08-31 15:19:08');

SET FOREIGN_KEY_CHECKS = 1;
