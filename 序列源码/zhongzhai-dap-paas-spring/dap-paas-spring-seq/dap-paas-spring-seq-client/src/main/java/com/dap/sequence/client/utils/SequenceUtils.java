package com.dap.sequence.client.utils;

import cn.hutool.core.util.ObjectUtil;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.SequenceQueueFactory;

/**
 * @description:
 * @author: zhangce
 * @create: 6/4/2024 12:21 PM
 **/
public class SequenceUtils {

    public static SeqObj getSeqObj(SeqParamsDto seqParamsDto) {
        if (ObjectUtil.isNull(seqParamsDto)) {
            return null;
        }
        return SequenceQueueFactory.SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
    }

    public static String getSeqClientCacheKey(String seqCode, String day) {
        return seqCode+ SequenceConstant.DAY_SWITCH_SPLIT+day;
    }

}
