package com.dap.paas.console.common.constants;

import java.util.regex.Pattern;

/**
 * @author: zpj
 * @date: 2021/10/18
 * @version: 1.0
 * @descriptions: TODO
 */
public class BootConstant {

    /**
     * 常用字符常量
     */
    public static final String SYMBOL_COLON = ":";
    public static final String LINE_SEPARATOR = "\n";
    public static final String SPACE_SEPARATOR = " ";
    /**
     * 当前操作系统的换行符
     */
    public static final String SYS_LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String OS_NAME = "os.name";
    public static final String OS_WINDOWS = "windows";
    public static final String OS_LINUX = "linux";

    /**
     * 执行状态，返回状态
     * 0 ; 成功
     * 1 : 失败
     */
    public static final String RES_CODE_SUCC = "0";
    public static final String RES_CODE_ERROR = "1";

    /**
     * shell 命令标识符
     */
    public static final String SHELL_TAG = "sh ";

    /**
     * 服务器相关参数
     */
    public static final int SSH_PORT = 22;
    public static final String USER_ROOT = "root";

    /**
     * 远程机器相关
     */
    public static final String REMOTE_DEPLOY_DIR = "/home/dap-deploy";
    public static final String REMOTE_SHELL_TAG = "source /etc/profile ; sh ";
    public static final String REMOTE_READ_LOG = "sed -n 'start,stop p' ";
    public static final String SHELL_FILE_LINE_COUNT = "awk 'END{print NR}' ";
    public static final String LOG_LINE_START = "start";
    public static final String LOG_LINE_STOP = "stop";
    /**
     * nfs
     */
    public static final String NFS_DIR = "nfsDir";
    public static final String NFS_CLIENT_IP = "nfsClientIP";
    public static final String NFS_CONFIG = "'nfsDir nfsClientIP(insecure,rw,sync,no_root_squash,all_squash)'";

    public static final Integer CONS_ZERO = 0;
    public static final Integer CONS_ONE = 0;
    public static final Integer CONS_TWO = 2;
    public static final Integer CONS_NEGATIVE_ONE = -1;
    public static final Integer CONS_BYTE_UNIT = 1024;
    public static final Integer CONS_SSH_PORT = 22;
    public static final long SLEEP_TIME = 1000L;

    /**
     * shell执行结果
     */
    public static final String SHELL_STATUS = "shellStatus";
    public static final String SHELL_OUT_PUT = "shellOut";
    public static final String SHELL_STATUS_ZERO = "0";
    public static final String SHELL_STATUS_NEGATIVE_ONE = "-1";

    public static Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]*");
    public static String FILE_SEPARATOR = "/";

    public static final String CPU_ARCHITECTURE_ARM = "ARM";
    public static final String CPU_ARCHITECTURE_X86 = "X86";

}
