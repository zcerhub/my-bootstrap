package com.base.sys.auth.dataAuth;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 添加自定义sql拦截器
 */
@Component
public class SqlInterceptorPluginListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(SqlInterceptorPluginListener.class);

    @Autowired
    private DataAuthInterceptor dataAuthInterceptor;

    @Autowired
    private SupplementSqlInterceptor sqlInterceptor;

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      this.addMyInterceptor();
    }

    private void addMyInterceptor() {
        log.debug("添加自定义Mybatis SQL拦截器.");
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(dataAuthInterceptor);
            sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        }
    }

}
