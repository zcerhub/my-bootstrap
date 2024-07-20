package com.dap.sequence.server.service;


import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.dap.sequence.server.entity.SeqRecycleInfo;

import java.util.List;
import java.util.Map;

/**
 * @author: liu
 * @date: 2022/8/30 20:21
 * @description:
 */
public interface SeqRecycleInfoService extends BaseService<SeqRecycleInfo,String> {

    /**
     * 保存数据
     *
     * @param list list
     * @return Integer
     */
    Integer insertBatch(List<SeqRecycleInfo> list);

    /**
     * 获取序列
     *
     * @param map map
     * @return List
     */
    List<SeqRecycleInfo> getSeqForUpdate(Map<String, Object> map);
}
