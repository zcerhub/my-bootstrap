package com.dap.paas.console.basic.config;

import com.dap.paas.console.basic.db.DatabaseTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 全局资源配置
 *
 */
@Configuration
public class GlobalResourceConfig {

    /**
     * 数据库模板
     *
     * @param dataSourceProperties 数据源配置
     * @return
     */
    @Bean
    public DatabaseTemplate databaseTemplate(DataSourceProperties dataSourceProperties) {
        return new DatabaseTemplate(dataSourceProperties);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
