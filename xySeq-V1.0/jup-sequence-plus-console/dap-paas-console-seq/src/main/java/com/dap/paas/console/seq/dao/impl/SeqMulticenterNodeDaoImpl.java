package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqMulticenterNodeDao;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className SeqMulticenterNodeDaoImpl
 * @description 多中心节点DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Slf4j
@Repository
public class SeqMulticenterNodeDaoImpl extends AbstractBaseDaoImpl<SeqMulticenterNode, String> implements SeqMulticenterNodeDao {

    @Override
    public List<SeqMulticenterNode> queryNodes(SeqMulticenterNode node) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace().concat(".queryNodes"), node);
        } catch (Exception e) {
            log.error("queryNodes执行异常",e);
            throw new DaoException("queryNodes执行异常:" + e.getMessage());
        }
    }
}
