package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.FilePackageDao;
import com.dap.paas.console.basic.entity.FilePackage;
import org.springframework.stereotype.Repository;

@Repository
public class FilePackageDaoImpl extends AbstractBaseDaoImpl<FilePackage,String> implements FilePackageDao {
}
