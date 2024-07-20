package com.dap.paas.console.basic.controller.v1;

import cn.hutool.json.JSONObject;
import com.base.api.dto.Page;
import com.base.api.entity.PageRequest;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.constant.ContainerizationConstant;
import com.dap.paas.console.basic.entity.BaseSysKubernetesInfo;
import com.dap.paas.console.basic.service.BaseSysKubernetesInfoService;
import com.dap.paas.console.basic.utils.SocketUtils;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.concurrent.*;

/**
 * (BaseSysKubernetesInfo)表控制层
 *
 * @author dzf
 * @since 2023-05-18 11:04:04
 */

/**
 * 应用管理
 */
@Api(tags = "基础信息-容器信息")
@RestController
@RequestMapping("/v1/basic/baseSysKubernetesInfo")
public class BaseContainerInfoController {

    private static final Logger log = LoggerFactory.getLogger(BaseContainerInfoController.class);

    private ExecutorService executorService = new ThreadPoolExecutor(50, 50, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(500),
            new ThreadFactoryBuilder().setNameFormat("agent-state-pool-thread-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());
    protected static final int TIMEOUT = 180;

    @Autowired
    private BaseSysKubernetesInfoService kubernetesInfoService;

    /**
     * 分页查询所有数据
     *
     * @param pageRequest 分页对象
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @PostMapping("/selectPage")
    @ResponseBody
    public Result selectAll(@RequestBody PageRequest<BaseSysKubernetesInfo> pageRequest) {
        try {
            Page page = this.kubernetesInfoService.queryPage(pageRequest.getPageNo(), pageRequest.getPageSize(), pageRequest.getRequestObject());
            List<BaseSysKubernetesInfo> baseSysKubernetesInfos = page.getData();
            List<Future<Boolean>> resultFutureList = new ArrayList<>(baseSysKubernetesInfos.size());
            for (BaseSysKubernetesInfo node : baseSysKubernetesInfos) {
                resultFutureList.add(executorService.submit(() -> {
                    if (SocketUtils.isRunning(node.getAgentIp(), node.getAgentPort())) {
                        node.setAgentState(ContainerizationConstant.AGENT_STATE_HEALTH);
                    } else {
                        node.setAgentState(ContainerizationConstant.AGENT_STATE_UNHEALTH);
                    }
                    return true;
                }));
            }
            for (Future<Boolean> resultFuture : resultFutureList) {
                try {
                    if (resultFuture.get(TIMEOUT, TimeUnit.SECONDS)) {
                    }
                } catch (Exception e) {
                    log.error("agent state check error ", e);
                }
            }
            return ResultUtils.success(200, page);
        } catch (ServiceException e) {
            log.error("error", e);
            return ResultUtils.error(500, "查询异常");
        }
    }


    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */
    @ApiOperation("通过主键查询")
    @PostMapping("getprotolInfo")
    @ResponseBody
    public Result selectOne(String id) {
        try {
            BaseSysKubernetesInfo baseSysKubernetesInfo = kubernetesInfoService.getObjectById(id);
            if (SocketUtils.isRunning(baseSysKubernetesInfo.getAgentIp(), baseSysKubernetesInfo.getAgentPort())) {
                baseSysKubernetesInfo.setAgentState(ContainerizationConstant.AGENT_STATE_HEALTH);
            } else {
                baseSysKubernetesInfo.setAgentState(ContainerizationConstant.AGENT_STATE_UNHEALTH);
            }
            return ResultUtils.success(200, baseSysKubernetesInfo);
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(500, "查询异常");
        }
    }

    /**
     * 新增数据
     *
     * @param baseSysKubernetesInfo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增")
    @PostMapping("/insert")
    @ResponseBody
    public Result insert(@RequestBody BaseSysKubernetesInfo baseSysKubernetesInfo) {
        baseSysKubernetesInfo.setDeleted("0");
        try {
            checkAgentInfo(baseSysKubernetesInfo);
            Integer integer = this.kubernetesInfoService.saveObject(baseSysKubernetesInfo);
            if (integer > 0) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            } else {
                return ResultUtils.error(ResultEnum.FAILED.getMsg());
            }
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg());
        }
    }

    /**
     * 修改数据
     * ft /实体对象
     *
     * @return 修改结果
     */
    @ApiOperation("修改")
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysKubernetesInfo baseSysKubernetesInfo) {
        try {
            checkAgentInfo(baseSysKubernetesInfo);
            Integer integer = this.kubernetesInfoService.updateObject(baseSysKubernetesInfo);
            if (integer > 0) {
                return ResultUtils.success(200, "更新成功");
            } else {
                return ResultUtils.error(400, "更新失败");
            }
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg());
        }
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            this.kubernetesInfoService.delObjectById(id);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(400, "删除异常");
        }
    }

    /**
     * 查询k8s命名空间
     *
     * @param agentId 主键
     * @return 查询结果
     */
    @ApiOperation("查询k8s命名空间")
    @GetMapping("/getK8sNamespaces")
    public Result getK8sNamespaces(String agentId) {
        try {
            JSONObject namespaces = kubernetesInfoService.getNamespaces(agentId);
            return ResultUtils.success(namespaces);
        } catch (HttpClientErrorException e) {
            log.error("error", e);
            return ResultUtils.error(e.getStatusCode().value(), ResultEnum.FAILED.getMsg());
        } catch (ResourceAccessException e2) {
            log.error("error", e2);
            return ResultUtils.error("访问该集群绑定代理资源异常,请检查代理服务是否健康!");
        }
    }

    /**
     * 查询镜像拉取秘钥
     *
     * @param agentId   主键
     * @param namespace 命名空间
     * @return 查询结果
     */
    @ApiOperation("查询镜像拉取秘钥")
    @GetMapping("/getImagePullSecrets/{agentId}/{namespace}")
    public Result getImagePullSecrets(@PathVariable("agentId") String agentId, @PathVariable("namespace") String namespace) {
        try {
            JSONObject namespaces = kubernetesInfoService.getImagePullSecrets(agentId, namespace);
            return ResultUtils.success(namespaces);
        } catch (HttpClientErrorException e) {
            log.error("error", e);
            return ResultUtils.error(e.getStatusCode().value(), ResultEnum.FAILED.getMsg());
        } catch (ResourceAccessException e2) {
            log.error("error", e2);
            return ResultUtils.error("访问该集群绑定代理资源异常,请检查代理服务是否健康!");
        }
    }


    /**
     * 查看StorageClass
     *
     * @param agentId 主键
     * @return 查询结果
     */
    @ApiOperation("查看StorageClass")
    @GetMapping("/getK8sStorageClass")
    public Result getK8sStorageClass(String agentId) {
        try {
            JSONObject storageclasses = this.kubernetesInfoService.getStorageclasses(agentId);
            return ResultUtils.success(storageclasses);
        } catch (HttpClientErrorException e) {
            log.error("error", e);
            return ResultUtils.error(e.getStatusCode().value(), ResultEnum.FAILED.getMsg());
        } catch (ResourceAccessException e2) {
            log.error("error", e2);
            return ResultUtils.error("访问该集群绑定代理资源异常,请检查代理服务是否健康!");
        }
    }

    /**
     * 查看容器事件日志
     *
     * @param agentId   主键
     * @param namespace 命名空间
     * @param podName   pod 名称
     * @return 查询结果
     */
    @ApiOperation("查看容器事件日志")
    @GetMapping("/queryContainerEventLog/{agentId}/{namespace}/{podName}")
    public Result queryContainerEventLog(@PathVariable("agentId") String agentId, @PathVariable("namespace") String namespace, @PathVariable("podName") String podName) {
        try {
            JSONObject storageclasses = this.kubernetesInfoService.queryContainerEventLog(agentId, namespace, podName);
            return ResultUtils.success(storageclasses);
        } catch (HttpClientErrorException e) {
            log.error("error", e);
            return ResultUtils.error(e.getStatusCode().value(), ResultEnum.FAILED.getMsg());
        } catch (ResourceAccessException e2) {
            log.error("error", e2);
            return ResultUtils.error("访问该集群绑定代理资源异常,请检查代理服务是否健康!");
        }
    }

    /**
     * 查看pod启动日志
     *
     * @param agentId   主键
     * @param namespace 命名空间
     * @param podName   pod 名称
     * @return 查询结果
     */
    @ApiOperation("查看pod启动日志")
    @GetMapping("/queryPodStartLog/{agentId}/{namespace}/{podName}")
    public Result queryPodStartLog(@PathVariable("agentId") String agentId, @PathVariable("namespace") String namespace, @PathVariable("podName") String podName) {
        try {
            String startLog = this.kubernetesInfoService.queryPodStartLog(agentId, namespace, podName).replace("\n", "\r\n");
            return ResultUtils.success(startLog);
        } catch (HttpClientErrorException e) {
            log.error("error", e);
            return ResultUtils.error(e.getStatusCode().value(), ResultEnum.FAILED.getMsg());
        } catch (ResourceAccessException e2) {
            log.error("error", e2);
            return ResultUtils.error("访问该集群绑定代理资源异常,请检查代理服务是否健康!");
        }
    }


    /**
     * 检测agent状态
     *
     * @param agentId 主键
     * @return 查询结果
     */
    @ApiOperation("检测agent状态")
    @GetMapping("/checkAgentState")
    public Result checkAgentState(String agentId) {
        try {
            this.kubernetesInfoService.checkAgentState(agentId);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(400, "checkAgentState失败");
        }
    }

    /**
     * @return 查询结果
     */
    @ApiOperation("查询所有")
    @GetMapping("/queryAllContainerInfo")
    public Result queryAllContainerInfo(String agentType) {
        try {
            Map var = new HashMap<String, String>();
            var.put("type", agentType);
            List<BaseSysKubernetesInfo> baseSysKubernetesInfos = kubernetesInfoService.queryList(var);
            List<Future<Boolean>> resultFutureList = new ArrayList<>(baseSysKubernetesInfos.size());
            for (BaseSysKubernetesInfo node : baseSysKubernetesInfos) {
                resultFutureList.add(executorService.submit(() -> {
                    if (SocketUtils.isRunning(node.getAgentIp(), node.getAgentPort())) {
                        node.setAgentState(ContainerizationConstant.AGENT_STATE_HEALTH);
                    } else {
                        node.setAgentState(ContainerizationConstant.AGENT_STATE_UNHEALTH);
                    }
                    return true;
                }));
            }
            for (Future<Boolean> resultFuture : resultFutureList) {
                try {
                    if (resultFuture.get(TIMEOUT, TimeUnit.SECONDS)) {
                    }
                } catch (Exception e) {
                    log.error("agent state check error ", e);
                }
            }
            return ResultUtils.success(baseSysKubernetesInfos);
        } catch (Exception e) {
            log.error("error", e);
            return ResultUtils.error(400, "查看StorageClass失败");
        }
    }

    private void checkAgentInfo(BaseSysKubernetesInfo baseSysKubernetesInfo) {
        if (ObjectUtils.isEmpty(baseSysKubernetesInfo)) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "null param");
        }
        if (ObjectUtils.isEmpty(baseSysKubernetesInfo.getClusterName())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "代理服务名称不能为空！");
        }
        if (ObjectUtils.isEmpty(baseSysKubernetesInfo.getAgentIp())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "代理服务ip地址不能为空!");
        }
        if (ObjectUtils.isEmpty(baseSysKubernetesInfo.getAgentPort())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "代理服务端口不能为空!");
        }
        if (ObjectUtils.isEmpty(baseSysKubernetesInfo.getType())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "代理服务类型不能为空!");
        }

        List<BaseSysKubernetesInfo> baseSysKubernetesInfos = kubernetesInfoService.queryList(new HashMap());
        if (!CollectionUtils.isEmpty(baseSysKubernetesInfos)) {
            baseSysKubernetesInfos.stream().forEach(e -> {
                if (!Objects.equals(e.getId(), baseSysKubernetesInfo.getId()) && Objects.equals(e.getClusterName(), baseSysKubernetesInfo.getClusterName())) {
                    throw new DapWebServerException("代理名称[" + baseSysKubernetesInfo.getClusterName() + "已被占用！");
                }
            });
        }
    }

}

