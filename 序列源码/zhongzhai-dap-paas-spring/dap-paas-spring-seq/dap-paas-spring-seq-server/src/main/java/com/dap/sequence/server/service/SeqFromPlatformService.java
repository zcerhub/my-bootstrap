package com.dap.sequence.server.service;

import com.dap.sequence.server.entity.SeqDesignPo;

import java.util.List;

/**
 * @className SeqServerFromPlatfromService
 * @description TODO
 * @author renle
 * @date 2023/12/13 17:04:13 
 * @version: V23.06
 */
public interface SeqFromPlatformService {

    /**
     * 获取全量序列
     *
     * @return List
     */
    List<SeqDesignPo> getAllSeqDesign();
}
