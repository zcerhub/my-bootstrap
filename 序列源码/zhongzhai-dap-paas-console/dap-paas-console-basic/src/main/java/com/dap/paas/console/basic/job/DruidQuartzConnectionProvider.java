package com.dap.paas.console.basic.job;

import com.alibaba.druid.pool.DruidDataSource;
import com.dap.paas.console.basic.utils.SpringContext;
import org.quartz.utils.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * quartz druid连接池
 *
 * @author Arlo
 * @date 2021/10/12
 **/
public class DruidQuartzConnectionProvider implements ConnectionProvider {

    private DruidDataSource dataSource;

    public DruidQuartzConnectionProvider() {
        this.dataSource = SpringContext.getBeanNonnull(DruidDataSource.class);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() throws SQLException {
        dataSource.close();
    }

    @Override
    public void initialize() throws SQLException {
        // no need
    }
}
