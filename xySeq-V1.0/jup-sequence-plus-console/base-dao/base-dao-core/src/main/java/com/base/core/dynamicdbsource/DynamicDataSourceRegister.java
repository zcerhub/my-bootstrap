package com.base.core.dynamicdbsource;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * project name ：dap-paas-console <br>
 * description：description。。。。。 <br>
 * copyright : © 2019-2023 <br>
 * corporation : 中电金信公司 <br>
 * ----------------------------------------------------------------------------- <br>
 * change history <br>
 * <table width="432" border="1">
 * <tr>
 * <td>version</td>
 * <td>time</td>
 * <td>author</td>
 * <td>change</td>
 * </tr>
 * <tr>
 * <td>1.0.0</td>
 * <td>2023/10/8 09:49</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/10/8 09:49
 * package com.base.core.config
 */
@Component
public class DynamicDataSourceRegister implements InitializingBean {

    /**
     * log
     */
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    /**
     * dynamicSource
     */
    private final DataSource dynamicSource;

    /**
     * currentSourceMap
     */
    private final Map<Object, DataSource> currentDataSourceMap = new HashMap<>(1 << 3);

    public DynamicDataSourceRegister(@Qualifier(value = "dynamicDataSource") DataSource dynamicSource) {
        this.dynamicSource = dynamicSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 缓存当前所有的数据源信息
        this.currentDataSourceMap.putAll(((DynamicRoutingDataSource) dynamicSource).getResolvedDataSources());
    }

    /**
     * 判断数据源是否已经注册过
     *
     * @param dataSourceInfo 数据源信息
     * @return 是否已经注册过
     */
    public boolean isDatasourceRegister(DataSourceInfo dataSourceInfo) {
        // 检测该数据源是否已经注册过、注册过的不再处理
        return this.currentDataSourceMap.containsKey(dataSourceInfo.generateDatasourceKey());
    }

    /**
     * 注册新数据源
     *
     * @param dataSourceInfo 新数据源信息
     */
    public synchronized void register(DataSourceInfo dataSourceInfo) {
        // 动态数据源
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dynamicSource;
        // 新数据源集合
        Map<Object, Object> newDataSources = new HashMap<>(dynamicRoutingDataSource.getResolvedDataSources());
        // 获取数据源的key
        String datasourceKey = dataSourceInfo.generateDatasourceKey();
        // 检测该数据源是否已经注册过、注册过的不再处理
        if (newDataSources.containsKey(datasourceKey)) {
            return;
        }
        // 构建新数据源
        DruidDataSource dataSource = this.defauleDruidDataSource();
        dataSource.setUsername(dataSourceInfo.getUsername());
        dataSource.setPassword(dataSourceInfo.getPassword());
        dataSource.setUrl(dataSourceInfo.getUrl());
        dataSource.setDriverClassName(dataSourceInfo.getDriverClassName());
        // 加入数据源集合
        newDataSources.put(datasourceKey, dataSource);
        // 重置新数据源map
        dynamicRoutingDataSource.setTargetDataSources(newDataSources);
        // 重新初始化
        dynamicRoutingDataSource.afterPropertiesSet();
        // 获取最新的数据源集合
        Map<Object, DataSource> resolvedDataSources = dynamicRoutingDataSource.getResolvedDataSources();
        // 打印新数据源信息
        for (Map.Entry<Object, DataSource> sourceEntry : resolvedDataSources.entrySet()) {
            log.info("datasource info, key: {}, value: {}", sourceEntry.getKey(), sourceEntry.getValue());
        }
        // 同时更新下缓存的数据源信息
        this.currentDataSourceMap.clear();
        this.currentDataSourceMap.putAll(resolvedDataSources);
    }

    private DruidDataSource defauleDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMinIdle(0);
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(1);
        return druidDataSource;
    }

}
