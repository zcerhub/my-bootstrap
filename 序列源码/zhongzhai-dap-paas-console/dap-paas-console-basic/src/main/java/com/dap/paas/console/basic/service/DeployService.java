package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.basic.entity.DeployClient;


public interface DeployService extends BaseService<DeployClient, String> {
    boolean support(DeployClient client);

    Result doDeploy(DeployClient client);

    Result doStart(DeployClient client);

    Result doStop(DeployClient client);

    Result doRemove(DeployClient client);
}
