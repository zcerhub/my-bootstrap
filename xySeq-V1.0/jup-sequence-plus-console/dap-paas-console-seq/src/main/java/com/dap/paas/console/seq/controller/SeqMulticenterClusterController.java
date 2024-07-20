package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.seq.dto.SeqMulticenterClusterVo;
import com.dap.paas.console.seq.entity.SeqMulticenterCluster;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;
import com.dap.paas.console.seq.service.SeqMulticenterClusterService;
import com.dap.paas.console.seq.service.SeqMulticenterNodeService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
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

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @className SeqMulticenterClusterController
 * @description 多中心集群管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "多中心集群管理")
@RestController
@RequestMapping("/seq/multicenter/cluster")
@Validated
public class SeqMulticenterClusterController {

    private final static Logger LOG = LoggerFactory.getLogger(SeqMulticenterClusterController.class);

    @Autowired
    private SeqMulticenterClusterService seqClusterService;

    @Autowired
    private SeqMulticenterNodeService seqNodeService;

    /**
     * 集群新增
     *
     * @param seqMulticenterCluster 对象
     * @return 成功/失败
     */
    @ApiOperation(value = "多中心集群新增")
    @PostMapping("/insert")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "服务集群新增", componentType = OperateTypeAspect.SEQ)
    public Result insert(@RequestBody @Validated SeqMulticenterCluster seqMulticenterCluster) {
        LOG.info("集群新增信息 = {}", seqMulticenterCluster);
        Integer integer = seqClusterService.saveObject(seqMulticenterCluster);
        return ResultUtils.success(ResultEnum.SUCCESS, integer);
    }

    /**
     * 根据集群ID查询当前对象
     *
     * @param id 集群ID
     * @return 集群对象
     */
    @ApiOperation(value = "多中心集群ID查询")
    @GetMapping("/query/{id}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "集群详情查询", componentType = OperateTypeAspect.SEQ)
    public Result query(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id非法！") String id) {
        SeqMulticenterCluster seqMulticenterCluster = seqClusterService.getObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS, seqMulticenterCluster);

    }

    /**
     * 根据集群对象属性查询
     *
     * @param param 集群对象
     * @return 带有分页的集群查询结果
     */
    @ApiOperation(value = "多中心集群对象分页")
    @PostMapping("/queryPage")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "多中心集群对象分页", componentType = OperateTypeAspect.SEQ)
    public Result queryPage(@RequestBody PageInDto<SeqMulticenterClusterVo> param) {
        SeqMulticenterClusterVo requestObject = param.getRequestObject();
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(requestObject::setTenantId);
        Page seqMulticenterCluster = seqClusterService.queryPage(param.getPageNo(), param.getPageSize(), requestObject);
        return ResultUtils.success(ResultEnum.SUCCESS, seqMulticenterCluster);

    }

    /**
     * 多中心集群修改
     *
     * @param seqMulticenterCluster 多中心集群对象
     * @return 成功/失败
     */
    @ApiOperation(value = "多中心集群修改")
    @PutMapping("/update")
    @AuditLog(operateType = OperateTypeAspect.PUT, modelName = "多中心集群修改", componentType = OperateTypeAspect.SEQ)
    public Result update(@RequestBody @Validated SeqMulticenterCluster seqMulticenterCluster) {
        LOG.info("多中心集群修改信息 = {}", seqMulticenterCluster);
        Integer integer = seqClusterService.updateObject(seqMulticenterCluster);
        return ResultUtils.success(ResultEnum.SUCCESS, integer);

    }

    /**
     * 多中心集群删除-单选
     *
     * @param id 多中心集群ID
     * @return 成功/失败
     */
    @ApiOperation(value = "多中心集群删除-单选")
    @DeleteMapping("/delete/{id}")
    @AuditLog(operateType = OperateTypeAspect.DELETE, modelName = "多中心集群删除-单选", componentType = OperateTypeAspect.SEQ)
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id非法！") String id) {
        LOG.info("多中心集群删除id = {}", id);
        SeqMulticenterNode node = new SeqMulticenterNode();
        node.setMultiClusterId(id);
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(node::setTenantId);
        List<SeqMulticenterNode> registerInstanceVos = seqNodeService.queryNodes(node);
        if (!registerInstanceVos.isEmpty()) {
            return ResultUtils.error(ResultEnum.INSTANCE_EXIST);
        } else {
            Integer integer = seqClusterService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, integer);
        }
    }
}
