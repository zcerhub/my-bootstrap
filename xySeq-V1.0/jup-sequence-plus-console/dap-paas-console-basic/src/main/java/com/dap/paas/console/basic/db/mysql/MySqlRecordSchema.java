package com.dap.paas.console.basic.db.mysql;

import com.dap.paas.console.basic.db.DBRecordSchema;
import com.dap.paas.console.basic.db.DatabaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动时自动初始化数据库、表
 *
 */
@Component
@Slf4j
public class MySqlRecordSchema implements DBRecordSchema {

    @Autowired
    private DatabaseTemplate databaseTemplate;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Override
    public void initAll() {
//        try{
//            if (dataSourceProperties.getInitializationMode().toString().equals("NEVER")) {
//                return ;
//            }
//            Class.forName(MysqlConstants.MYSQL_DRIVER);
//            String url = dataSourceProperties.getUrl();
//            String db = url.split("//")[1].split("/")[1].split("\\?")[0];
//            if (initDatabase(db)) {
//                initTables();
//            }
//        } catch (ClassNotFoundException e) {
//            log.info(e.getMessage());
//        }
    }

    @Override
    public void initTables() {
        databaseTemplate.doExecute(connection -> {
            ResultSet rs = null;
            Statement stmt = null;
            List<String> tbls = new ArrayList<>();
            try {
                rs = connection.createStatement().executeQuery(MysqlConstants.SHOW_TABLES);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        tbls.add(rs.getString(i));
                    }
                }

                for (String tbl : MysqlConstants.TBLS) {
                    if (tbls.contains(tbl)) {
                        log.info("The [" + tbl + "] table already exists. Do not need to create it.");
                    } else {
                        String key = "CREATE_TABLE_" + tbl.toUpperCase();
                        stmt = connection.createStatement();
                        if (MysqlConstants.KEYS.containsKey(key)) {
                            stmt.addBatch(MysqlConstants.KEYS.get(key));
                        }
                        if (MysqlConstants.KEYS.containsKey(key + "_INSERT")) {
                            stmt.addBatch(MysqlConstants.KEYS.get(key + "_INSERT"));
                        }
                        int[] code = stmt.executeBatch();
                        if (code.length > 0) {
                            log.info("Create [" + tbl + "] has successed.");
                        } else {
                            log.error("Create [" + tbl + "] has failed.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }

                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean initDatabase(String database) {
        return databaseTemplate.doExecute(connection -> {
            ResultSet rs = null;
            List<String> dbs = new ArrayList<>();
            try {
                rs = connection.createStatement().executeQuery(MysqlConstants.SHOW_DATABASES);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        dbs.add(rs.getString(i));
                    }
                }
                if (dbs.contains(database)) {
                    log.info("The [" + database + "] database already exists. Do not need to create it.");
                } else {
                    boolean status = connection.createStatement().execute(String.format(MysqlConstants.CREATE_DB_SQL, database));
                    if (!status) {
                        log.info("SQL statement affect the 0 records. Create [" + database + "] has successed.");
                    } else {
                        log.error("Create [" + database + "] has failed.");
                    }
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }
}
