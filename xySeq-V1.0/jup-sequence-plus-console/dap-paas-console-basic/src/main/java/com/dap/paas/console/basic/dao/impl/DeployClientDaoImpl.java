package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.DeployClientDao;
import com.dap.paas.console.basic.entity.DeployClient;
import org.springframework.stereotype.Repository;

@Repository
public class DeployClientDaoImpl extends AbstractBaseDaoImpl<DeployClient,String> implements DeployClientDao {
}
