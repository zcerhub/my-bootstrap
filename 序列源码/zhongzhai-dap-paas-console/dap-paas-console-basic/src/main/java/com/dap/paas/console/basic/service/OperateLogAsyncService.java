package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.base.sys.api.entity.BaseLogAudit;
import com.dap.paas.console.basic.entity.ClientOperate;

/**
 * @author qqqab
 */
public interface OperateLogAsyncService extends BaseService<ClientOperate, String> {
    void asynFailedLogAudits(String componentType);
    void asyncLogAudit(ClientOperate clientOperate, String componentType, boolean isRetry);

}
