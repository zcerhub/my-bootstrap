package com.dap.paas.console.seq.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.service.SeqMulticenterNodeService;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * @className SeqMulticenterNodeController
 * @description 多中心节点管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "多中心节点管理")
@RestController
@RequestMapping("/seq/multicenter/node")
@Validated
public class SeqMulticenterNodeController {

    private static final Logger LOG = LoggerFactory.getLogger(SeqMulticenterNodeController.class);

    @Autowired
    private SeqMulticenterNodeService seqNodeService;

    @Autowired
    private SeqServiceClusterService seqClusterService;


    /**
     * 集群->多中心实例新增
     * @param node 对象
     * @return 成功/失败
     */
    @ApiOperation(value = "集群->多中心实例新增")
    @PostMapping("/insert")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "多中心实例新增", componentType = OperateTypeAspect.SEQ)
    public Result insert(@RequestBody @Valid SeqMulticenterNode node) {
        LOG.info("集群->多中心实例新增信息 = {}", node);
        SeqServiceCluster cluster = seqClusterService.getObjectById(node.getSeqClusterId());
        node.setStatus("0");
        node.setDbUrl(cluster.getDbUrl());
        node.setDbDriver(cluster.getDbDriver());
        node.setDbUser(cluster.getDbUser());
        node.setDbPassword(cluster.getDbPassword());
        node.setCurrentDb(node.getSeqClusterId());
        return ResultUtils.success(seqNodeService.saveObject(node));

    }

    /**
     * 多中心实例查询
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "多中心实例ID查询")
    @GetMapping("/query/{id}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "多中心实例详情", componentType = OperateTypeAspect.SEQ)
    public Result query(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "多中心实例id非法！") String id) {
        SeqMulticenterNode seqMulticenterNode = seqNodeService.getObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS, seqMulticenterNode);
    }

    /**
     * 多中心实例分页
     *
     * @param param param
     * @return Result
     */
    @ApiOperation(value = "多中心实例分页")
    @PostMapping("/queryPage")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "多中心实例分页", componentType = OperateTypeAspect.SEQ)
    public Result queryPage(@RequestBody PageInDto<SeqMulticenterNode> param) {
        Page configEvnCluster = seqNodeService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, configEvnCluster);
    }

    /**
     * 多中心实例修改
     *
     * @param seqMulticenterNode seqMulticenterNode
     * @return Result
     */
    @ApiOperation(value = "多中心实例修改")
    @PutMapping("/update")
    @AuditLog(operateType = OperateTypeAspect.PUT, modelName = "多中心实例修改", componentType = OperateTypeAspect.SEQ)
    public Result update(@RequestBody @Valid SeqMulticenterNode seqMulticenterNode) {
        LOG.info("多中心实例修改信息 = {}", seqMulticenterNode);
        Integer integer = seqNodeService.updateObject(seqMulticenterNode);
        return ResultUtils.success(ResultEnum.SUCCESS, integer);
    }

    /**
     * 切换数据源
     *
     * @param json json
     * @return Result
     */
    @ApiOperation(value = "切换数据源")
    @PutMapping("/switchCurrentDb")
    @AuditLog(operateType = OperateTypeAspect.PUT, modelName = "切换数据源", componentType = OperateTypeAspect.SEQ)
    public Result switchCurrentDb(@RequestBody JSONObject json) {
        LOG.info("切换数据源信息 = {}", json);
        Integer integer = seqNodeService.switchCurrentDb(json);
        return ResultUtils.success(ResultEnum.SUCCESS, integer);
    }

    /**
     * 多中心实例删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "多中心实例删除-单选")
    @DeleteMapping("/delete/{id}")
    @AuditLog(operateType = OperateTypeAspect.DELETE, modelName = "多中心实例删除", componentType = OperateTypeAspect.SEQ)
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "多中心实例id非法！") String id) {
        LOG.info("多中心实例删除id = {}", id);
        Integer integer = seqNodeService.delObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS, integer);
    }

    /**
     * 多中心实例
     *
     * @param multiClusterId multiClusterId
     * @return Result
     */
    @ApiOperation(value = "多中心实例 List")
    @GetMapping("/queryList/{multiClusterId}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "多中心实例列表", componentType = OperateTypeAspect.SEQ)
    public Result queryList(@PathVariable("multiClusterId") @Pattern(regexp = "^\\d{1,32}$", message = "多中心实例id非法！") String multiClusterId) {
        SeqMulticenterNode node = new SeqMulticenterNode();
        node.setMultiClusterId(multiClusterId);
        List<SeqMulticenterNode> registerInstances = seqNodeService.queryNodes(node);
        SeqServiceCluster seqServiceCluster = new SeqServiceCluster();
        List<SeqServiceClusterVo> multiCenters = new ArrayList<>();
        for (SeqMulticenterNode seqMulticenterNode : registerInstances) {
            String seqClusterId = seqMulticenterNode.getSeqClusterId();
            seqServiceCluster.setId(seqClusterId);
            SeqServiceClusterVo clusterInfo = seqClusterService.getClusterInfo(seqServiceCluster);
            clusterInfo.setCurrentDb(seqMulticenterNode.getCurrentDb());
            clusterInfo.setId(seqMulticenterNode.getId());
            clusterInfo.setDbDriver(null);
            clusterInfo.setDbPassword(null);
            clusterInfo.setDbUrl(null);
            clusterInfo.setDbUser(null);
            clusterInfo.setSeqClusterId(seqClusterId);
            SeqServiceCluster objectById = seqClusterService.getObjectById(seqMulticenterNode.getCurrentDb());
            clusterInfo.setCurrentDbName(objectById.getName());
            clusterInfo.setSyncMethod(seqMulticenterNode.getSyncMethod());
            multiCenters.add(clusterInfo);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, multiCenters);
    }
}
