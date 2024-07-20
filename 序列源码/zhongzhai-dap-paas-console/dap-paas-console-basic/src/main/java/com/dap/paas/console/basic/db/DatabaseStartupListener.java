package com.dap.paas.console.basic.db;

import com.dap.paas.console.basic.db.mysql.MySqlRecordSchema;
import com.dap.paas.console.basic.db.mysql.MysqlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class DatabaseStartupListener implements ApplicationContextAware {

    @Autowired
    private MySqlRecordSchema mySqlRecordSchema;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       // mySqlRecordSchema.initAll();
    }
}
