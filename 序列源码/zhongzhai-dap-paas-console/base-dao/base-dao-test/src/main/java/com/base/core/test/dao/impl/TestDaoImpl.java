package com.base.core.test.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.core.test.dao.TestDao;
import com.base.core.test.entity.Test;
import org.springframework.stereotype.Repository;

@Repository
public class TestDaoImpl extends AbstractBaseDaoImpl<Test,String> implements TestDao  {


}
