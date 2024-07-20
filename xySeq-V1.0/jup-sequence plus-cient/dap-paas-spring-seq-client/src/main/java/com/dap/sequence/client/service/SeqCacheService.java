package com.dap.sequence.client.service;

import java.util.Map;

public interface SeqCacheService {

    /**
     * 将管控端推送过来的map进行解析，缓存数据增加、数据入库、调用生产序列进行序列生产
     */
    String mapToCacheAndDb (Map<String, Object> map);

    void loadRules();

    void updateSeqHolder();
}
