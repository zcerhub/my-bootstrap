/*
 Navicat Premium Data Transfer

 Source Server         : 10.64.13.31
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 10.64.13.31:6308
 Source Schema         : seq_demo

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 11/07/2024 15:28:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for seq_cur_num_day
-- ----------------------------
DROP TABLE IF EXISTS `seq_cur_num_day`;
CREATE TABLE `seq_cur_num_day`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `instance_rule_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `in_day` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部署发布数量',
  `seq_lock` int NOT NULL COMMENT '乐观锁标识',
  `cur_val` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前号段',
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `instance_rule_day`(`instance_rule_id` ASC, `in_day` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '序列当前值记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_design
-- ----------------------------
DROP TABLE IF EXISTS `seq_design`;
CREATE TABLE `seq_design`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `seq_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列名称',
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列编号',
  `seq_application_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用id',
  `seq_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列描述',
  `seq_application_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `seq_number` int NULL DEFAULT NULL COMMENT '生成数量',
  `seq_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态  1未完成 2完成',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户',
  `sequence_prod_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列生成类型',
  `seq_application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `callback_mode` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否回拨序列： 0 全局序列 1：按日回拨 （到下一日时序号中数字从头开始）  2：按月回拨 3：按年回拨',
  `request_number` int NULL DEFAULT NULL COMMENT '请求数量',
  `client_cache_threshold` int NULL DEFAULT NULL COMMENT '客户端缓存补仓阈值',
  `server_cache_threshold` int NULL DEFAULT NULL COMMENT '服务端缓存补仓阈值',
  `client_recovery_switch` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端回收开关：0-不回收 1-回收',
  `server_recovery_switch` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务端回收开关：0-不回收 1-回收',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '序列设计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_instance_rule
-- ----------------------------
DROP TABLE IF EXISTS `seq_instance_rule`;
CREATE TABLE `seq_instance_rule`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一id',
  `seq_design_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联id（序列设计表id）',
  `instance_rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `instance_rule_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则类型',
  `instance_rule_info` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则信息',
  `sort_no` int NULL DEFAULT NULL COMMENT '排序号',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户',
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_optional_pad_value
-- ----------------------------
DROP TABLE IF EXISTS `seq_optional_pad_value`;
CREATE TABLE `seq_optional_pad_value`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seq_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列编号',
  `paddind_value` bigint NULL DEFAULT NULL COMMENT '补位值',
  `seq_value` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户输入参数（如生日、888）',
  `create_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户',
  `optional_value` bigint NULL DEFAULT NULL COMMENT '补位+自选值',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `seq_code_seq_value`(`seq_code` ASC, `seq_value` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_optional_record
-- ----------------------------
DROP TABLE IF EXISTS `seq_optional_record`;
CREATE TABLE `seq_optional_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列编号',
  `paddind_value` bigint NULL DEFAULT NULL COMMENT '补位值',
  `optional_value` bigint NULL DEFAULT NULL COMMENT '补位+自选值',
  `seq_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户输入参数（如生日、888）',
  `seq_lock` int NULL DEFAULT 0 COMMENT '乐观锁字段',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新用户',
  `instance_rule_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列规则实例ID',
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户',
  `optional_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '自选状态1.已使用   2.已回收',
  `serial_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `seq_code_serial_number`(`seq_code` ASC, `serial_number` ASC) USING BTREE,
  UNIQUE INDEX `seq_code_optional_value`(`seq_code` ASC, `optional_value` ASC) USING BTREE,
  INDEX `seq_code_seq_value`(`seq_code` ASC, `seq_value` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自选序列记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_recovery_record
-- ----------------------------
DROP TABLE IF EXISTS `seq_recovery_record`;
CREATE TABLE `seq_recovery_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列编号',
  `recovery_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '回收状态0:未使用 1:已使用',
  `serial_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序号',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户',
  `seq_lock` int NULL DEFAULT 0 COMMENT '乐观锁字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `seq_code_serial_number`(`seq_code` ASC, `serial_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回收序号记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for seq_recycle_info
-- ----------------------------
DROP TABLE IF EXISTS `seq_recycle_info`;
CREATE TABLE `seq_recycle_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一id',
  `recycle_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回收编号',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ip',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户id',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增用户',
  `update_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户',
  `create_date` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `seq_design_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列id',
  `rq_day` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列时间',
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '停机序列回收记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
