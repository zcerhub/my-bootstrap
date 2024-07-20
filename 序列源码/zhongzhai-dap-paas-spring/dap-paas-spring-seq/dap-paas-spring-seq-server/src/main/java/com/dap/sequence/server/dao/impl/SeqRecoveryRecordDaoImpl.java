package com.dap.sequence.server.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqRecoveryRecordDao;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecoveryRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className SeqRecoveryRecordDaoImpl
 * @description TODO
 * @author renle
 * @date 2024/02/01 11:21:35 
 * @version: V23.06
 */
@Repository
public class SeqRecoveryRecordDaoImpl extends AbstractBaseDaoImpl<SeqRecoveryRecord, String> implements SeqRecoveryRecordDao {

    @Override
    public List<SeqRecoveryRecord> selectRecoveryForUpdate(SeqRecoveryRecord seqRecoveryRecord) {
        return this.getSqlSession().selectList(getSqlNamespace() + "." + "selectRecoveryForUpdate", seqRecoveryRecord);
    }

    @Override
    public int saveBatchRecovery(List<SeqRecoveryRecord> seqRecoveryRecords) {
        return this.getSqlSession().insert(getSqlNamespace() + "." + "saveBatchRecovery", seqRecoveryRecords);
    }

    @Override
    public void updateRecoveryStatus(SeqRecoveryRecord seqRecoveryRecord) {
        this.getSqlSession().update(getSqlNamespace() + "." + "updateRecoveryStatus", seqRecoveryRecord);
    }

    @Override
    public List<SeqRecoveryRecord> queryRecoveryRecords(SeqRecoveryRecord seqRecoveryRecord) {
        return this.getSqlSession().selectList(getSqlNamespace() + "." + "queryRecoveryRecords", seqRecoveryRecord);
    }
}
