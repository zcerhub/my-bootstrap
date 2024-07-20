package com.dap.sequence.server.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * DynamicDataSource
 *
 * @author scalor
 * @date 2022/2/24
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static DynamicDataSource instance;
    private static final byte[] LOCK = new byte[0];
    private static final Map<Object, Object> DATA_SOURCE_MAP = new HashMap<>();

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        DATA_SOURCE_MAP.putAll(targetDataSources);

        // 必须添加该句，否则新添加数据源无法识别到
        super.afterPropertiesSet();
    }

    public Map<Object, Object> getDataSourceMap() {
        return DATA_SOURCE_MAP;
    }

    public static synchronized DynamicDataSource getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new DynamicDataSource();
                }
            }
        }
        return instance;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDBType();
    }
}
