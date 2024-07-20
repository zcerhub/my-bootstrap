package com.base.sys.api.common;


/**
 * 系统常量
 */
public class SysConstant {

    private SysConstant() { throw new IllegalStateException("Utility class"); }
    /**
     * 数据权限Sql语句
     */
    public static final String  DATA_AUTH_SQL_STATEMENT="dataAuthSqlStatement";

    /**
     * 请求路径映射
     */
    public static final String  ESB_PATH_MAPPING="esb_path_Mapping";


    /**
     * 用户信息
     */
    public static final String  USER_INFO = "user_info";


    /**
     * 所有urls
     */
    public static final String  USER_INFO_ALL_URLS = "user_info_all_urls";


    /**
     * 所拥有的url
     */
    public static final String  USER_INFO_ROLE_OWN_URL= "user_info_role_own_urls";


    /**
     * 拥有的菜单
     */
    public static final String  USER_INFO_OWN_MENUS= "user_info_role_own_menus";


    /**
     * 拥有的操作点对象
     */
    public static final String  USER_INFO_OWN_OPERATES= "user_info_own_operates";



    /**
     * 拥有的数据权限
     */
    public static final String  USER_INFO_OWN_DATA_RULE= "user_info_own_data_rule";


    /**
     * 角色对应的应用
     */
    public static final String  ROLE_APP= "role_app";


    /**
     * 角色--应用--对应的菜单
     */
    public static final String  ROLE_APP_MENU= "role_app_menu";


    /**
     * 角色--应用--对应的操作点
     */
    public static final String  ROLE_APP_OPERATE= "role_app_operate";


    /**
     * 所有应用
     */
    public static final String  All_APP= "all_app";


    public static final String  All_APP_MENU= "all_app_menu";

    public static final String  All_APP_OPERATE= "all_app_operate";


    public static final String  All_APP_URLS= "all_app_urls";

    public static final String  All_APP_OPERATE_SIGN= "all_app_operate_sign";
    /**
     * 用户信息
     */
    public static final String  USER_ID = "user_id";
    /**
     * 斜杠
     */
    public static final String  SYS_SLASH = "/";
    /**
     * 空格
     */
    public static final String  SYS_BLACK = " ";
    /**
     * 空格
     */
    public static final String  SYS_SHELL_LINE = "\'\\n\'";
}
