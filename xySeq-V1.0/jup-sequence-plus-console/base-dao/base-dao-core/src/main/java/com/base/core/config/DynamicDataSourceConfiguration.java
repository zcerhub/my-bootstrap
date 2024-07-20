package com.base.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.base.core.dynamicdbsource.DynamicRoutingDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

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
 * <td>2023/10/8 09:57</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/10/8 09:57
 * package com.base.core.config
 */
@Configuration
public class DynamicDataSourceConfiguration {

    /**
     * @return 数据源
     */
    @Bean(name = "consoleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource consoleDataSource() {
        return new DruidDataSource();
    }

    /**
     * @return 数据源
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier(value = "consoleDataSource") DataSource consoleDataSource) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        //配置多数据源
        Map<Object, Object> dbMap = new HashMap<>(1 << 2);
        dbMap.put("consoleDataSource", consoleDataSource);
        dynamicRoutingDataSource.setTargetDataSources(dbMap);
        // 设置默认数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(consoleDataSource);
        return dynamicRoutingDataSource;
    }

    /**
     * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
     * 配置文件和mybatis-boot的配置文件同步
     *
     * @return consoleSqlSessionFactoryBean
     * @throws Exception 异常
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory consoleSqlSessionFactoryBean(@Qualifier(value = "dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        // mybatis-plus配置
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 数据源
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        // MapperLocations
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
        // mybaties配置
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        //
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 事务管理
     *
     * @param dynamicDataSource 数据源
     * @return 事务管理器
     */
    @Bean
    protected PlatformTransactionManager platformTransactionManager(@Qualifier(value = "dynamicDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
