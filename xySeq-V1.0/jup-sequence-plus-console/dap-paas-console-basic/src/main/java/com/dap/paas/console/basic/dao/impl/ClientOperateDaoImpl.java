package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.ClientOperateDao;
import com.dap.paas.console.basic.entity.ClientOperate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientOperateDaoImpl extends AbstractBaseDaoImpl<ClientOperate,String> implements ClientOperateDao {
}
