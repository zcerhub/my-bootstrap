package com.dap.paas.console.seq.service;

import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceNode;

import java.util.List;
import java.util.Map;

/**
 * @className SeqServiceNodeService
 * @description 序列服务节点接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqServiceNodeService extends BaseService<SeqServiceNode, String> {

    /**
     * 删除服务节点
     *
     * @param id id
     * @return Result
     */
    Result delete(String id);

    /**
     * 更新服务节点
     *
     * @param node node
     * @return Result
     */
    Result update(SeqServiceNode node);

    /**
     * 保存服务节点
     *
     * @param node node
     * @return Result
     */
    Result insert(SeqServiceNode node);

    /**
     * 查询节点列表
     *
     * @param node node
     * @return List
     */
    List<SeqServiceNodeVo> queryNodes(SeqServiceNodeVo node);

    /**
     * 查询监控节点
     *
     * @param id id
     * @return List
     */
    List<SeqServiceNode> serviceMonitorNumber(Map id);

    /**
     * 保存集群节点
     *
     * @param nodeList nodeList
     * @param clusterId clusterId
     */
    void saveClusterNode(List<SeqServiceNode> nodeList, String clusterId);

    /**
     * 节点启动
     *
     * @param node node
     * @return Result
     */
    Result start(SeqServiceNode node);

    /**
     * 节点停止
     *
     * @param node node
     * @return Result
     */
    Result stop(SeqServiceNode node);
}
