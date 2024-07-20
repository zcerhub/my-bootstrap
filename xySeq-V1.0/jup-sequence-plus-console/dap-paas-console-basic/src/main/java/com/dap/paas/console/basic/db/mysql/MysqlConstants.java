package com.dap.paas.console.basic.db.mysql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mysql初始化
 *
 */
public interface MysqlConstants {

    String SHOW_DATABASES = "SHOW DATABASES";

    String SHOW_TABLES = "SHOW TABLES";

    String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    /** 创建数据库 */
    String CREATE_DB_SQL = "CREATE DATABASE IF NOT EXISTS %s";

    /** 建表语句 */
    List<String> TBLS = Arrays.asList("basic_application", "basic_city", "basic_clientoperate", "basic_deployclient",
            "basic_machine", "basic_machineroom", "basic_organization", "basic_shelllog");
    String CREATE_TABLE_BASIC_APPLICATION = "CREATE TABLE IF NOT EXISTS `basic_application` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `application_name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '地区名称',\n" +
            "  `application_code` varchar(64) NOT NULL,\n" +
            "  `organization_id` bigint(20) DEFAULT NULL,\n" +
            "  `charge_user` varchar(64) DEFAULT NULL,\n" +
            "  `description` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_CITY = "CREATE TABLE IF NOT EXISTS `basic_city` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `city_name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '地区名称',\n" +
            "  `city_code` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '描述',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_CLIENTOPERATE = "CREATE TABLE IF NOT EXISTS `basic_clientoperate` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `client_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '客户端id',\n" +
            "  `type` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '操作类型',\n" +
            "  `log_id` varchar(64) DEFAULT NULL COMMENT '关联日志id',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_DEPLOYCLIENT = "CREATE TABLE IF NOT EXISTS `basic_deployclient` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '地区名称',\n" +
            "  `host_address` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '描述',\n" +
            "  `host_port` varchar(10) DEFAULT NULL,\n" +
            "  `remark` varchar(255) DEFAULT NULL,\n" +
            "  `pack_version` varchar(64) DEFAULT NULL,\n" +
            "  `state` tinyint(1) DEFAULT NULL,\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_MACHINE = "CREATE TABLE IF NOT EXISTS `basic_machine` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `host_code` varchar(64) NOT NULL COMMENT '主机编码',\n" +
            "  `host_ip` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '机器ip',\n" +
            "  `host_port` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '机器端口',\n" +
            "  `host_ssh_account` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT 'ssh账号',\n" +
            "  `host_ssh_password` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'ssh密码',\n" +
            "  `machine_room_id` varchar(32) NOT NULL COMMENT '所在机房id',\n" +
            "  `os_release` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作系统',\n" +
            "  `os_version` varchar(20) DEFAULT NULL,\n" +
            "  `host_remark` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',\n" +
            "  `available` tinyint(1) DEFAULT '1',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_MACHINEROOM = "CREATE TABLE IF NOT EXISTS `basic_machineroom` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `machine_room_code` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '机房名称',\n" +
            "  `machine_room_name` varchar(64) NOT NULL COMMENT '所在地区id',\n" +
            "  `organization_id` varchar(32) DEFAULT NULL,\n" +
            "  `city_id` varchar(32) NOT NULL,\n" +
            "  `description` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_ORGANIZATION = "CREATE TABLE IF NOT EXISTS `basic_organization` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `organization_code` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '地区名称',\n" +
            "  `organization_name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '描述',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    String CREATE_TABLE_BASIC_SHELLLOG = "CREATE TABLE IF NOT EXISTS `basic_shelllog` (\n" +
            "  `id` varchar(32) NOT NULL COMMENT 'id',\n" +
            "  `status` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '状态',\n" +
            "  `content` text CHARACTER SET utf8 NOT NULL COMMENT '描述',\n" +
            "  `deleted` tinyint(1) DEFAULT '0',\n" +
            "  `create_user_id` varchar(64) DEFAULT NULL COMMENT '新增人',\n" +
            "  `create_date` datetime DEFAULT NULL COMMENT '新增时间',\n" +
            "  `update_date` datetime DEFAULT NULL,\n" +
            "  `update_user_id` varchar(64) DEFAULT NULL,\n" +
            "  `tenant_id` varchar(64) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    /** 组装 */
    Map<String, String> KEYS = new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;
        {
            put("CREATE_TABLE_BASIC_APPLICATION", CREATE_TABLE_BASIC_APPLICATION);
            put("CREATE_TABLE_BASIC_CITY", CREATE_TABLE_BASIC_CITY);
            put("CREATE_TABLE_BASIC_CLIENTOPERATE", CREATE_TABLE_BASIC_CLIENTOPERATE);
            put("CREATE_TABLE_BASIC_DEPLOYCLIENT", CREATE_TABLE_BASIC_DEPLOYCLIENT);
            put("CREATE_TABLE_BASIC_MACHINE", CREATE_TABLE_BASIC_MACHINE);
            put("CREATE_TABLE_BASIC_MACHINEROOM", CREATE_TABLE_BASIC_MACHINEROOM);
            put("CREATE_TABLE_BASIC_ORGANIZATION", CREATE_TABLE_BASIC_ORGANIZATION);
            put("CREATE_TABLE_BASIC_SHELLLOG", CREATE_TABLE_BASIC_SHELLLOG);
        }

    };
}
