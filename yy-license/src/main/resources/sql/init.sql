/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.152.5
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 192.168.152.5:3306
 Source Schema         : test_mp

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 24/08/2022 10:58:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biyi_user
-- ----------------------------
DROP TABLE IF EXISTS `biyi_user`;
CREATE TABLE `biyi_user`  (
                              `id` int(0) NOT NULL,
                              `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                              `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                              `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                              `create_time` datetime(0) NULL DEFAULT NULL,
                              `update_time` datetime(0) NULL DEFAULT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;