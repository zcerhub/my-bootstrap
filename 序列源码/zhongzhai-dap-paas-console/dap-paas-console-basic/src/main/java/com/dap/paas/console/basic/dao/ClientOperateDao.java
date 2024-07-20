package com.dap.paas.console.basic.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.ClientOperate;

import java.util.List;

public interface ClientOperateDao extends BaseDao<ClientOperate, String> {
    List<ClientOperate> queryListAsyncNotAsyncStatus(ClientOperate clientOperate);
}
