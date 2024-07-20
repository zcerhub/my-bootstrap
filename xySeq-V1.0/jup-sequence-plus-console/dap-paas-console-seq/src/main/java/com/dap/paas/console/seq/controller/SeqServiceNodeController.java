package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.utils.SshDeployServerUtil;
import com.dap.paas.console.basic.vo.ReadFileVo;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.dap.paas.console.common.util.NetUtils;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dto.SeqMachineNodeDTO;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.enums.NodeRunState;
import com.dap.paas.console.seq.plugin.SeqMachineOperation;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
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
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @className SeqServiceNodeController
 * @description 序列服务实例管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列服务监控")
@RestController
@RequestMapping("/seq/serviceNode")
@Validated
public class SeqServiceNodeController {

    private static final Logger LOG = LoggerFactory.getLogger(SeqServiceNodeController.class);

    @Autowired
    private MachineService machineService;

    @Autowired
    private SeqServiceNodeService seqNodeService;

    @Autowired
    private SeqServiceClusterService seqClusterService;

    @Autowired
    private SeqMachineOperation seqMachineOperation;

    /**
     * 集群->序列实例新增
     *
     * @param seqServiceNode 对象
     * @return 成功/失败
     */
    @ApiOperation(value = "集群->序列实例新增")
    @PostMapping("/insert")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列实例新增", componentType = OperateTypeAspect.SEQ)
    public Result insert(@RequestBody @Validated SeqServiceNode seqServiceNode) {
        return seqNodeService.insert(seqServiceNode);
    }

    /**
     * 序列实例ID查询
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列实例ID查询")
    @GetMapping("/query/{id}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "序列实例详情", componentType = OperateTypeAspect.SEQ)
    public Result query(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "实例id不合法！") String id) {
        SeqServiceNode seqServiceNode = seqNodeService.getObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS, seqServiceNode);
    }

    /**
     * 序列实例分页
     *
     * @param param param
     * @return Result
     */
    @ApiOperation(value = "序列实例分页")
    @PostMapping("/queryPage")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列实例分页", componentType = OperateTypeAspect.SEQ)
    public Result queryPage(@RequestBody PageInDto<SeqServiceNode> param) {
        Page configEvnCluster = seqNodeService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, configEvnCluster);
    }

    /**
     * 序列实例修改
     *
     * @param seqServiceNode seqServiceNode
     * @return Result
     */
    @ApiOperation(value = "序列实例修改")
    @PutMapping("/update")
    @AuditLog(operateType = OperateTypeAspect.PUT, modelName = "序列实例修改", componentType = OperateTypeAspect.SEQ)
    public Result update(@RequestBody @Validated SeqServiceNode seqServiceNode) {
        return seqNodeService.update(seqServiceNode);
    }

    /**
     * 序列实例删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列实例删除-单选")
    @DeleteMapping("/delete/{id}")
    @AuditLog(operateType = OperateTypeAspect.DELETE, modelName = "序列实例删除", componentType = OperateTypeAspect.SEQ)
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "实例id不合法！") String id) {
        return seqNodeService.delete(id);
    }

    /**
     * 序列实例删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列实例删除-确认删除")
    @DeleteMapping("/isDelete/{id}")
    @AuditLog(operateType = OperateTypeAspect.DELETE, modelName = "序列实例确认删除", componentType = OperateTypeAspect.SEQ)
    public Result isDelete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "实例id不合法！") String id) {
        return seqNodeService.delete(id);
    }

    /**
     * 序列实例
     *
     * @param clusterId clusterId
     * @return Result
     */
    @ApiOperation(value = "序列实例 List")
    @GetMapping("/queryList/{clusterId}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "序列实例列表", componentType = OperateTypeAspect.SEQ)
    public Result queryList(@PathVariable("clusterId") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String clusterId) {
        SeqServiceNodeVo node = new SeqServiceNodeVo();
        node.setClusterId(clusterId);
        List<SeqServiceNodeVo> registerInstances = seqNodeService.queryNodes(node);
        return ResultUtils.success(ResultEnum.SUCCESS, registerInstances);
    }

    /**
     * 集群id->机器列表
     *
     * @param machineRoomId  machineRoomId
     * @return Result
     */
    @ApiOperation(value = "集群id->机器列表")
    @GetMapping("/machineList/{machineRoomId}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "机器列表", componentType = OperateTypeAspect.SEQ)
    public Result machineList(@PathVariable("machineRoomId") @Pattern(regexp = "^\\d{1,32}$", message = "机器id不合法！") String machineRoomId) {
        Map<String, String> map = Collections.singletonMap("machineRoomId", machineRoomId);
        List<Machine> machines = machineService.queryList(map);
        return ResultUtils.success(ResultEnum.SUCCESS, machines);
    }

    /**
     * 新增单个服务前检查环境
     *
     * @param clusterNode clusterNode
     * @return Result
     */
    @ApiOperation(value = "新增单个服务前检查环境", httpMethod = "POST")
    @PostMapping(value = "checkEnv")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "新增序列实例前检查环境", componentType = OperateTypeAspect.SEQ)
    public Result checkEnv(@RequestBody @Validated(value = {ValidGroup.Valid.NodeEnvCheck.class}) SeqServiceNode clusterNode) {
        // 检测端口占用
        String ip = clusterNode.getHostIp();
        int port = clusterNode.getPort();
        // 检测端口号不能小于1000
        if (port < 1000) {
            return ResultUtils.error("端口号不能小于1000");
        }
        // 如果端口能通，则认为该端口被占用
        if (NetUtils.telnet(ip, port)) {
            return ResultUtils.error("主机：" + ip + "端口：" + port + " ，已占用请更换后重试");
        }
        return ResultUtils.success();
    }

    /**
     * 新增单个服务初始化
     *
     * @param clusterNode clusterNode
     * @return Result
     * @throws Exception Exception
     */
    @ApiOperation(value = "新增单个服务初始化", httpMethod = "POST")
    @PostMapping(value = "nodeInit")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "新增序列实例初始化", componentType = OperateTypeAspect.SEQ)
    public Result nodeInit(@RequestBody @Validated(value = {ValidGroup.Valid.NodeEnvCheck.class}) SeqServiceNode clusterNode) throws Exception {
        SeqServiceCluster cluster = seqClusterService.getObjectById(clusterNode.getClusterId());
        if (cluster == null) {
            return ResultUtils.error("集群不存在");
        }

        Machine machine = machineService.getObjectById(clusterNode.getMachineId());
        if (machine == null) {
            return ResultUtils.error("主机机器不存在");
        }
        clusterNode.setHostIp(machine.getHostIp());
        SeqMachineNodeDTO machineNodeDTO = new SeqMachineNodeDTO();
        machineNodeDTO.setMachine(machine);
        machineNodeDTO.getNodes().add(clusterNode);
        try {
            StopWatch stopWatch = new StopWatch();
            // 复制安装包到机器并修改配置
            stopWatch.start();
            boolean check = seqMachineOperation.pullImage(cluster, machineNodeDTO);
            stopWatch.stop();
            LOG.info("推送部署包到目标机器耗时：{}", stopWatch.getTotalTimeMillis());
            if (!check) {
                return ResultUtils.error("复制安装包到机器未通过");
            }
        } catch (DapWebServerException ex) {
            return ResultUtils.error(ex.getMessage());
        }

        // 集群初始化
        clusterNode.setStatus(NodeRunState.RUN.getKey() + "");
        // 持久化序列实例信息到数据库
        seqNodeService.insert(clusterNode);
        cluster.setStatus(SeqConstant.SEQ_SERVER_ONE);
        cluster.setUpdateDate(new Date());
        seqClusterService.updateObject(cluster);
        return ResultUtils.success();
    }

    /**
     * 服务启动
     *
     * @param node node
     * @return Result
     */
    @ApiOperation(value = "服务启动")
    @PostMapping("/start")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "服务启动", componentType = OperateTypeAspect.SEQ)
    public Result start(@RequestBody SeqServiceNode node) {
        if (StringUtils.isBlank(node.getId())) {
            return ResultUtils.error("节点id不能为空");
        }
        node = seqNodeService.getObjectById(node.getId());
        Result startResult = seqNodeService.start(node);
        if (startResult.getCode() == 200) {
            SeqServiceCluster cluster = new SeqServiceCluster();
            cluster.setId(node.getClusterId());
            cluster.setUpdateDate(new Date());
            cluster.setStatus(SeqConstant.SEQ_SERVER_ONE);
            seqClusterService.updateObject(cluster);
        }
        return startResult;

    }

    /**
     * 服务停止
     *
     * @param node node
     * @return Result
     */
    @ApiOperation(value = "服务停止")
    @PostMapping("/stop")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "服务停止", componentType = OperateTypeAspect.SEQ)
    public Result stop(@RequestBody SeqServiceNode node) {
        if (StringUtils.isBlank(node.getId())) {
            return ResultUtils.error("节点id不能为空");
        }
        node = seqNodeService.getObjectById(node.getId());
        Result stopResult = seqNodeService.stop(node);
        if (stopResult.getCode() == 200) {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("clusterId", node.getClusterId());
            List<SeqServiceNode> nodeList = seqNodeService.queryListByMap(queryMap);
            List<SeqServiceNode> stopedList = nodeList.stream().filter(x -> "0".equals(x.getStatus())).collect(Collectors.toList());
            if (nodeList.size() == stopedList.size()) {
                SeqServiceCluster cluster = new SeqServiceCluster();
                cluster.setId(node.getClusterId());
                cluster.setUpdateDate(new Date());
                cluster.setStatus(SeqConstant.SEQ_SERVER_ZERO);
                seqClusterService.updateObject(cluster);
            }
        }
        return stopResult;
    }

    /**
     * 启动日志
     *
     * @param clusterNodeId clusterNodeId
     * @return Result
     */
    @ApiOperation(value = "启动日志", httpMethod = "GET")
    @GetMapping("/log/{clusterNodeId}")
    public Result log(@PathVariable("clusterNodeId") @Pattern(regexp = "^\\d{1,32}$", message = "集群节点id不合法！") String clusterNodeId) {
        SeqServiceNode clusterNode = seqNodeService.getObjectById(clusterNodeId);
        Machine machine = machineService.getObjectById(clusterNode.getMachineId());
        String basePath = clusterNode.getLogfilePath();
        if (basePath == null || machine == null) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "seq server 服务器信息有误，无法查询");
        }
        try {
            return ResultUtils.success(ResultEnum.SUCCESS, SshDeployServerUtil.readFile(machine, basePath));
        } catch (IOException e) {
            LOG.error("查询日志失败：", e);
            ReadFileVo vo = new ReadFileVo();
            vo.setContent("查询日志失败：" + e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED.getCode(), e.getMessage(), vo);
        }
    }
}
