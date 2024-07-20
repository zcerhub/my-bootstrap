package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.dao.ContainerImageDao;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.ContainerImage;
import org.springframework.stereotype.Repository;

@Repository
public class ContainerImageDaoImpl extends AbstractBaseDaoImpl<ContainerImage,String> implements ContainerImageDao {
}
