package com.dap.sequence.server.dao;


import com.base.core.dao.BaseDao;
import com.dap.sequence.server.entity.SeqOptionalRecord;

import java.util.List;

/**
 * (SeqOptionalRecord)表数据库访问层
 */
public interface SeqOptionalRecordDao extends BaseDao<SeqOptionalRecord, String> {

    /**
     * 查询补位参数数据，或者状态是已回收的数据
     * @param po po
     * @param createNum createNum
     * @return SeqOptionalRecord
     */
    List<SeqOptionalRecord> getRecoveryOptional(SeqOptionalRecord po, int createNum);

    /**
     * 批量保存记录
     *
     * @param seqOptionalRecords seqOptionalRecords
     * @return int
     */
    Integer saveBatchOptional(List<SeqOptionalRecord> seqOptionalRecords);

    /**d
     * 更新记录状态
     *
     * @param optionalRecord optionalRecord
     * @return int
     */
    Integer updateRecordStatus(SeqOptionalRecord optionalRecord);

    /**
     * 查询自选值
     *
     * @param optionalRecord optionalRecord
     * @return List
     */
    List<SeqOptionalRecord> getRecordByValue(SeqOptionalRecord optionalRecord);

    /**
     * 更新过滤状态
     *
     * @param seqOptionalRecords seqOptionalRecords
     */
    void updateFilterStatus(List<SeqOptionalRecord> seqOptionalRecords);
}

