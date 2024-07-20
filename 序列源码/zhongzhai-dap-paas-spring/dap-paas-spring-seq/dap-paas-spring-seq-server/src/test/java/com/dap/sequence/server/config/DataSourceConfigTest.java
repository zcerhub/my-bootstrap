package com.dap.sequence.server.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;

/**
 * @className DataSourceConfigTest
 * @description TODO
 * @author renle
 * @date 2024/02/06 09:03:08 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class DataSourceConfigTest {

    @InjectMocks
    private DataSourceConfig dataSourceConfig;

    @Test
    public void dynamicDataSource() {
        DynamicDataSource dynamicDataSource = dataSourceConfig.dynamicDataSource();
        Assert.assertNotNull(dynamicDataSource);
    }

    @Test
    public void sqlSessionFactory() throws Exception {
        DataSource dynamicDataSource = Mockito.mock(DataSource.class);
        SqlSessionFactory sessionFactory = dataSourceConfig.sqlSessionFactory(dynamicDataSource);
        Assert.assertNotNull(sessionFactory);
    }
}