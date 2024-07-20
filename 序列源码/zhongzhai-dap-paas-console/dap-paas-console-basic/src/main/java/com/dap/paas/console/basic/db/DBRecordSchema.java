package com.dap.paas.console.basic.db;

/**
 * 数据库初始化策略接口
 *
 */
public interface DBRecordSchema {

    /**
     * 数据库完全初始化
     */
    void initAll();

    /**
     * 初始化数据库（不存在则创建/存在则不处理）
     *
     * @param database 数据库名
     * @return
     */
    default boolean initDatabase(String database) {
        return true;
    }

    /**
     * 初始化数据库表
     */
    void initTables();
}
