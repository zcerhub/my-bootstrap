package com.dap.paas.console.seq.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqCurNumDao;
import com.dap.paas.console.seq.entity.SeqCurNum;
import org.springframework.stereotype.Repository;

/**
 * @className SeqCurNumDaoImpl
 * @description 序列生产计数DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqCurNumDaoImpl extends AbstractBaseDaoImpl<SeqCurNum, String> implements SeqCurNumDao {
}
