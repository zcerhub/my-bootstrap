package com.dap.sequence.server.service;

import com.base.core.service.BaseService;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecoveryRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * @className SeqRecoveryRecordService
 * @description 回收业务接口
 * @author renle
 * @date 2024/02/01 10:52:23 
 * @version: V23.06
 */
public interface SeqRecoveryRecordService extends BaseService<SeqRecoveryRecord, String> {

    /**
     * 获取可使用的已生成序列
     *
     * @param seqParamsDto seqParamsDto
     * @param seqObj seqObj
     * @return LinkedList
     */
    List<SeqRecoveryRecord> selectRecoveryForUpdate(SeqParamsDto seqParamsDto, SeqObj seqObj);

    /**
     * 保存回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @param seqObj seqObj
     * @param seqList seqList
     * @return int
     */
    int saveRecoveryRecord(SeqParamsDto seqParamsDto, SeqObj seqObj, List<Object> seqList);

    /**
     * 更新回收状态
     *
     * @param seqRecoveryRecord seqOptionalRecord
     */
    void updateRecoveryStatus(SeqRecoveryRecord seqRecoveryRecord);

    /**
     * 更新回收信息
     *
     * @param seqParamsDto seqParamsDto
     */
    void updateRecovery(SeqParamsDto seqParamsDto);
}
