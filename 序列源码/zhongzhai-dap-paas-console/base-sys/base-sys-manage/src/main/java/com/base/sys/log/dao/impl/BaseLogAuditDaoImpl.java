package com.base.sys.log.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.log.dao.BaseLogAuditDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: scalor
 * @Date: 2021/1/15:17:42
 * @Descricption:
 */
@Repository
public class BaseLogAuditDaoImpl extends AbstractBaseDaoImpl<BaseLogAudit,String> implements BaseLogAuditDao {
    @Override
    public List<BaseLogAudit> queryListAsyncNotAsyncStatus(BaseLogAudit baseLogAudit) {
        return this.getSqlSession().selectList(getSqlNamespace().concat(".queryListAsyncNotAsyncStatus"),baseLogAudit);
    }
}
