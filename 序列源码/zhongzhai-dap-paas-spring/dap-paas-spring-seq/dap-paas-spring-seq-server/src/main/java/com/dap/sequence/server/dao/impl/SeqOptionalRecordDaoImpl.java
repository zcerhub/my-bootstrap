package com.dap.sequence.server.dao.impl;


import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.server.dao.SeqOptionalRecordDao;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className SeqOptionalRecordDaoImpl
 * @description 序列回调
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Repository
public class SeqOptionalRecordDaoImpl extends AbstractBaseDaoImpl<SeqOptionalRecord, String> implements SeqOptionalRecordDao {

    @Override
    public List<SeqOptionalRecord> queryList(String statement, SeqOptionalRecord po, int offset, int limit) {
        RowBounds rowBounds = new RowBounds(offset, limit);
        return this.getSqlSession().selectList(getSqlNamespace() + "." + statement, po, rowBounds);
    }

    @Override
    public List<SeqOptionalRecord> getRecoveryOptional(SeqOptionalRecord po, int createNum) {
        RowBounds rowBounds = new RowBounds(0, createNum);
        return this.getSqlSession().selectList(getSqlNamespace() + "." + "getRecoveryOptional", po, rowBounds);
    }

    @Override
    public Integer saveBatchOptional(List<SeqOptionalRecord> seqOptionalRecords) {
        return this.getSqlSession().insert(getSqlNamespace() + "." + "saveBatchOptional", seqOptionalRecords);
    }

    @Override
    public Integer updateRecordStatus(SeqOptionalRecord optionalRecord) {
        return this.getSqlSession().update(getSqlNamespace() + "." + "updateRecordStatus", optionalRecord);
    }

    @Override
    public List<SeqOptionalRecord> getRecordByValue(SeqOptionalRecord optionalRecord) {
        return this.getSqlSession().selectList(getSqlNamespace() + "." + "getRecordByValue", optionalRecord);
    }

    @Override
    public void updateFilterStatus(List<SeqOptionalRecord> seqOptionalRecords) {
        this.getSqlSession().update(getSqlNamespace() + "." + "updateFilterStatus", seqOptionalRecords);
    }
}
