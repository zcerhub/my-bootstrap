package com.dap.sequence.client.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.DateUtils;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.service.SeqCacheCleaningService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @description:
 * @author: zhangce
 * @create: 6/4/2024 12:20 PM
 **/
@Service
public class SeqClientCacheCleaningByDayService implements SeqCacheCleaningService {

    @Value("${seq.cacheCleaning.cacheDayEnd:30}")
    private int cacheDayEnd;


    public void cleanHistorySeqCache(SeqParamsDto seqParamsDto) {
        if ( ObjectUtil.isNull(seqParamsDto)) {
            return ;
        }

        SeqObj seqObj = SequenceQueueFactory.getSeqObj(seqParamsDto.getSeqCode());

        if (ObjectUtil.isNull(seqObj)) {
            return;
        }

        LocalDate seqLocalDate = DateUtils.getLocalDateBy(seqParamsDto.getDay(), seqObj.getCallbackMode());
        if (ObjectUtil.isNull(seqLocalDate)) {
            return ;
        }

        String seqCode = seqParamsDto.getSeqCode();
        for (int cacheDay = SequenceConstant.SEQ_CACHE_CLEANING_CACHE_DAY_START; cacheDay <= cacheDayEnd; cacheDay++) {
            LocalDate tmpLocalDate = seqLocalDate.minusDays(cacheDay);
            String tmpLocalDateStr = tmpLocalDate.format(DateUtils.DAY_CALLBACK_DATETIME_FORMATTER);
            String cacheKey = SeqKeyUtils.getSeqClientCacheKey(seqCode, tmpLocalDateStr);
            if (SequenceQueueFactory.SEQUENCE_QUEUE_MAP.containsKey(cacheKey)) {
                SequenceQueueFactory.SEQUENCE_QUEUE_MAP.remove(cacheKey);
            }
        }


    }

}
