package com.base.core.dynamicdbsource;

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
 * <td>2023/10/8 10:00</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/10/8 10:00
 * package com.base.core.config
 */
public class DataSourceContextHolder {

    /**
     *
     */
    private static final ThreadLocal<String> DB_CONTEXT = new ThreadLocal<>();

    public static void setDbSource(String dbSource) {
        DB_CONTEXT.set(dbSource);
    }

    public static String getDbSource() {
        return DB_CONTEXT.get();
    }

    public static void clearDbSource() {
        DB_CONTEXT.remove();
    }

}
