package com.base.sys.log.dao;

import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseLogAudit;

import java.util.List;

/**
 * @Author: scalor
 * @Date: 2021/1/15:17:42
 * @Descricption:
 */
public interface BaseLogAuditDao extends BaseDao<BaseLogAudit, String> {
    List<BaseLogAudit> queryListAsyncNotAsyncStatus(BaseLogAudit baseLogAudit);
}
