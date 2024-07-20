package com.dap.paas.console.basic.controller.v1;

import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.service.impl.HealthEndpointService;
import com.dap.paas.console.basic.entity.DeployClient;
import com.dap.paas.console.basic.service.DeployService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部署服务管理
 * （同一实例再相同机器只能部署一套服务，之后修改）
 */
@Api(tags = "基础信息-部署服务管理")
@RequestMapping("/v1/basic/client")
@RestController
public class DeployClientController {
    private static final Logger log= LoggerFactory.getLogger(DeployClientController.class);

    @Resource(name = "deployServices")
    private List<DeployService> deployServices;
    @Autowired
    private HealthEndpointService healthEndpointService;


    @PostMapping("/queryList")
    public Result queryList(@RequestBody DeployClient client){
        try {
            List<DeployClient> clientList = deployServices.get(0).queryList(client);
            return ResultUtils.success(ResultEnum.SUCCESS,clientList);
        } catch (ServiceException e) {
            log.error("查询部署服务失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PostMapping("/deploy")
    public Result deploy(@RequestBody DeployClient client){
        List<DeployService> supportService = getSupportDeployService(client);
        if(supportService.size() == 0) {
            log.error("新增部署服务失败","无法获取部署实例");
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        return supportService.get(0).doDeploy(client);
    }


    @PutMapping("/start")
    public Result start(@RequestBody String id){
        DeployClient client = deployServices.get(0).getObjectById(id);

        if(client == null) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        List<DeployService> supportService = getSupportDeployService(client);
        if(supportService.size() == 0) {
            log.error("启动服务失败","无法获取部署实例");
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return supportService.get(0).doStart(client);
    }

    @PutMapping("/stop")
    public Result stop(@RequestBody String id){
        DeployClient client = deployServices.get(0).getObjectById(id);

        if(client == null) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        List<DeployService> supportService = getSupportDeployService(client);
        if(supportService.size() == 0) {
            log.error("停止服务失败","无法获取部署实例");
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        return supportService.get(0).doStop(client);
    }


    @DeleteMapping("/remove")
    public Result remove(@RequestBody String id){
        DeployClient client = deployServices.get(0).getObjectById(id);

        if(client == null) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        List<DeployService> supportService = getSupportDeployService(client);
        if(supportService.size() == 0) {
            log.error("删除服务失败","无法获取部署实例");
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "找不到对应实例");
        }
        return supportService.get(0).doRemove(client);
    }


    @GetMapping("/health")
    public Result checkClient(String host, int port){
        try {
            Map response = healthEndpointService.request(host, port,"");
            return ResultUtils.success(ResultEnum.SUCCESS,response);
        } catch(Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    private List<DeployService> getSupportDeployService(DeployClient client) {
        return deployServices.stream()
                .filter(deployService -> deployService.support(client))
                .collect(Collectors.toList());
    }

}
