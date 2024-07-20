package com.dap.paas.console.common.constants;

import com.dap.paas.console.common.util.DapUtil;

public class DeployConst {
    public static final String PACKAGE_FOLDER = DapUtil.getProjectPath() + "/package/";

    public static final String UPLOAD_DIR = "/app/dap-clients/package/";

    public static final String WORKSPACE = "/app/dap-clients/workspace/";

    public static final String CONF_DIR = WORKSPACE + "conf/";

    public static final String STORAGE = "store/";

    public static final String KRAFT_MODE = "1";

    public static final String ZOOKEEPER_MODE = "0";
}
