package com.dap.sequence.server.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecoveryRecord;

import java.util.List;

/**
 * @className SeqRecoveryRecordDao
 * @description TODO
 * @author renle
 * @date 2024/02/01 11:19:36 
 * @version: V23.06
 */
public interface SeqRecoveryRecordDao extends BaseDao<SeqRecoveryRecord, String> {

    /**
     * 获取可使用的已生成序列
     *
     * @param seqRecoveryRecord seqRecoveryRecord
     * @return List
     */
    List<SeqRecoveryRecord> selectRecoveryForUpdate(SeqRecoveryRecord seqRecoveryRecord);

    /**
     * 保存回收序列
     *
     * @param seqRecoveryRecords seqParamsDto
     * @return int
     */
    int saveBatchRecovery(List<SeqRecoveryRecord> seqRecoveryRecords);

    /**
     * 更新回收状态
     *
     * @param seqRecoveryRecord seqRecoveryRecord
     */
    void updateRecoveryStatus(SeqRecoveryRecord seqRecoveryRecord);

    /**
     * 查询回收序号
     *
     * @param seqRecoveryRecord seqRecoveryRecord
     * @return List
     */
    List<SeqRecoveryRecord> queryRecoveryRecords(SeqRecoveryRecord seqRecoveryRecord);
}
