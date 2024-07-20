package com.dap.sequence.client.service;

import com.dap.sequence.api.dto.SeqParamsDto;

/**
 * 清理缓存接口：用于清理内存中历史序列缓存，防止内存占用过大导致溢出
 *
 * @author yzp
 */
public interface SeqCacheCleaningService {

    void cleanHistorySeqCache(SeqParamsDto seqParamsDto);

}
