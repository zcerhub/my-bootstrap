package com.dap.sequence.client.dao.impl;


import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.client.dao.SeqRecycleInfoDao;
import com.dap.sequence.client.entity.SeqRecycleInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: liu
 * @date: 2022/8/30 20:19
 * @description:
 */
@Repository
public class SeqRecycleInfoDaoImpl extends AbstractBaseDaoImpl<SeqRecycleInfo, String> implements SeqRecycleInfoDao {
    @Override
    public Integer insertBatch(List<SeqRecycleInfo> seqRecycleInfos) {
        return this.getSqlSession().insert(getSqlNamespace() + ".insertBatch", seqRecycleInfos);
    }

    @Override
    public List<SeqRecycleInfo> getSeqForUpdate(Map map) {
        return this.getSqlSession().selectList(getSqlNamespace() + ".getSeqForUpdate", map);
    }

    @Override
    public List<SeqRecycleInfo> selectRecycleCode(Map map) {
        return this.getSqlSession().selectList(getSqlNamespace() + ".selectRecycleCode", map);
    }
}