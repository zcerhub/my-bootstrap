package com.dap.sequence.server.service;


import com.base.core.service.BaseService;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqOptionalRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * (SeqOptionalRecord)表服务接口
 *
 * @author makejava
 * @since 2023-09-29 09:31:50
 */
public interface SeqOptionalRecordService extends BaseService<SeqOptionalRecord, String> {

    /**
     * 获取可使用的已生成序列
     *
     * @param seqParamsDto seqParamsDto
     * @param seqObj seqObj
     * @return LinkedList
     */
    LinkedList<Object> queryRecoveryOptional(SeqParamsDto seqParamsDto, SeqObj seqObj);

    /**
     * 选中序号
     *
     * @param seqParamsDto seqParamsDto
     */
    void selectedOptionalRecord(SeqParamsDto seqParamsDto);

    /**
     * 取消自选
     *
     * @param seqParamsDto seqParamsDto
     */
    void cancelOptionalRecord(SeqParamsDto seqParamsDto);

    /**
     * 通过自选位值区间获取已使用额序号
     *
     * @param seqCode seqCode
     * @param start start
     * @param end end
     * @return List
     */
    List<SeqOptionalRecord> getRecordByValue(String seqCode, long start, long end);

    /**
     * 更新过滤状态
     *
     * @param optionalList optionalList
     */
    void updateFilterStatus(List<SeqOptionalRecord> optionalList);
}
