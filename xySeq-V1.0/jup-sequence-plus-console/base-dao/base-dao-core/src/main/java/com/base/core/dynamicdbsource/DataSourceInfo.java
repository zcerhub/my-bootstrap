package com.base.core.dynamicdbsource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * <td>2023/10/9 14:00</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/10/9 14:00
 * package com.base.core.dynamicdbsource
 */
public class DataSourceInfo {

    /**
     * JDBC_URL_PATTERN
     * jdbc url 正则解析表达式
     */
    private static final Pattern MYSQL_JDBC_URL_PATTERN = Pattern.compile("jdbc:(?<type>[a-z]+)://(?<host>[a-zA-Z0-9-/.]+):(?<port>[0-9]+)/(?<database>[a-zA-Z0-9_]+)?");

    /**
     * 包含在jdbcUrl中的信息
     * 数据库类型、地址、端口、数据库schema
     */
    private String type;
    private String host;
    private Integer port;
    private String database;

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    /**
     * 数据源key，这里使用如下格式拼接：
     * host:port|database|username
     */
    private String datasourceKey;

    /**
     * 生成数据源的key
     *
     * @return 数据源key
     */
    public String generateDatasourceKey() {
        if (this.datasourceKey != null && !this.datasourceKey.isEmpty()) {
            return this.datasourceKey;
        }
        // 将jdbcUrl中的部分参数解析出来
        this.extractDbConnectInfo();
        // 计算数据源key
        this.datasourceKey = String.format("%s:%d|%s|%s", this.host, this.port, this.database, this.username);
        //
        return this.datasourceKey;
    }

    /**
     * 将jdbcUrl中的部分参数解析出来，如ip、端口等
     */
    public void extractDbConnectInfo() {
        if (this.url == null || this.url.isEmpty()) {
            return;
        }
        // 解析
        Matcher dateMatcher;
        if (this.url.startsWith("jdbc:mysql:")) {
            this.type = "mysql";
            this.driverClassName = "com.mysql.cj.jdbc.Driver";
            dateMatcher = MYSQL_JDBC_URL_PATTERN.matcher(this.url);
        } else if (this.url.startsWith("jdbc:qianbase:")) {
            this.type = "qianbase";
            this.driverClassName = "org.qianbase.Driver";
            dateMatcher = MYSQL_JDBC_URL_PATTERN.matcher(this.url);
        } else if (this.url.startsWith("jdbc:postgresql:")) {
            this.type = "postgresql";
            this.driverClassName = "org.postgresql.Driver";
            dateMatcher = MYSQL_JDBC_URL_PATTERN.matcher(this.url);
        } else {
            throw new IllegalArgumentException("unknown jdbcUrl type: " + this.url);
        }
        // 格式错误
        if (!dateMatcher.find()) {
            throw new IllegalArgumentException("unknown jdbcUrl type: " + this.url);
        }
        this.host = dateMatcher.group("host");
        this.port = Integer.valueOf(dateMatcher.group("port"));
        this.database = dateMatcher.group("database");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

}
