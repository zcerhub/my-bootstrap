package com.dap.sequence.client.service;

import com.dap.sequence.api.dto.SeqParamsDto;

public interface SeqCacheCleaningService {

    void cleanHistorySeqCache(SeqParamsDto seqParamsDto);

}
