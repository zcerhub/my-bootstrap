package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.OrganizationDao;
import com.dap.paas.console.basic.entity.Organization;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationDaoImpl extends AbstractBaseDaoImpl<Organization,String> implements OrganizationDao {
}
