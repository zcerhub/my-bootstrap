# 2024-01-15 增加防暴力破解密码逻辑
ALTER TABLE `base_sys_user`
	CHANGE COLUMN `status` `status` VARCHAR(1) NULL DEFAULT NULL COMMENT '0：停用，1：启用' AFTER `entry_date`,
	ADD COLUMN `is_lock` VARCHAR(1) NULL DEFAULT NULL COMMENT '是否锁定' AFTER `status`,
	ADD COLUMN `error_count` INT NULL DEFAULT NULL COMMENT '错误次数' AFTER `is_lock`,
	ADD COLUMN `last_error_date` DATETIME NULL DEFAULT NULL COMMENT '最后一次失败时间' AFTER `error_count`;