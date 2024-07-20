package com.dap.paas.console.seq.service;

import com.alibaba.fastjson.JSONObject;
import com.base.core.service.BaseService;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;

import java.util.List;

/**
 * @className SeqMulticenterNodeService
 * @description 序列多中心节点接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqMulticenterNodeService extends BaseService<SeqMulticenterNode, String> {

    /**
     * 多中心节点查询
     *
     * @param node node
     * @return List
     */
    List<SeqMulticenterNode> queryNodes(SeqMulticenterNode node);

    /**
     * 多中心数据库切换
     *
     * @param json json
     * @return Integer
     */
    Integer switchCurrentDb(JSONObject json);
}
