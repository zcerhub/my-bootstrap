package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.ShellLogDao;
import com.dap.paas.console.basic.entity.ShellLog;
import org.springframework.stereotype.Repository;

@Repository
public class ShellLogDaoImpl extends AbstractBaseDaoImpl<ShellLog,String> implements ShellLogDao {
}
