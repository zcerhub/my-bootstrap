package com.base.sys.async.service;

import com.base.core.service.BaseService;
import com.base.sys.api.entity.BaseLogAudit;

/**
 * @author qqqab
 */
public interface BaseLogAuditAsyncService extends BaseService<BaseLogAudit, String> {
    void asyncLogAudit(BaseLogAudit logAudit, String componentType);
    void asyncLogAudit(BaseLogAudit logAudit,String componentType,boolean isRetry);
    void asynFailedLogAudits(String componentType);
}
