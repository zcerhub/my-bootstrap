package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.org.service.BaseSysUserService;
import com.dap.paas.console.basic.entity.ClientOperate;
import com.dap.paas.console.basic.entity.DeployClient;
import com.dap.paas.console.basic.entity.ShellLog;
import com.dap.paas.console.basic.service.ClientOperateService;
import com.dap.paas.console.basic.service.DeployService;
import com.dap.paas.console.basic.service.ShellLogService;
import com.dap.paas.console.basic.vo.ClientOperateVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 实例操作信息管理
 *
 */
@Api(tags = "基础信息-实例操作信息管理")
@RestController
@RequestMapping("/v1/basic/operate")
public class ClientOperateController {

    private static final Logger log= LoggerFactory.getLogger(ClientOperateController.class);
    @Autowired
    private ClientOperateService clientOperateService;
    @Autowired
    private DeployService deployClientService;
    @Autowired
    private ShellLogService shellLogService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private BaseSysUserService baseSysUserService;


    //查询最近一条操作
    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String clientId) {
        try {
            ClientOperate clientOperate = clientOperateService.getObjectById(clientId);
            if(clientOperate == null) {
                return ResultUtils.error(201, "无操作记录");
            }
            DeployClient deployClient = deployClientService.getObjectById(clientId);
            ShellLog shellLog = shellLogService.getObjectById(clientOperate.getLogId());
            ClientOperateVo operateVo = new ClientOperateVo();
            operateVo.setId(clientOperate.getId());
            operateVo.setClientName(deployClient.getName());
            operateVo.setType(clientOperate.getType());
            operateVo.setLogStatus(shellLog == null ? "" : shellLog.getStatus());
            operateVo.setLogContent(shellLog == null ? "" : shellLog.getContent());
            operateVo.setOperateUser(baseSysUserService.getObjectById(clientOperate.getCreateUserId()).getName());
            operateVo.setOperateTime(clientOperate.getCreateDate());
            return ResultUtils.success(ResultEnum.SUCCESS,operateVo);
        } catch (ServiceException e) {
            log.error("查询操作记录失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }

    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<ClientOperate> param) {
        try {
            Page operates = clientOperateService.queryPage(param.getPageNo(),param.getPageSize(), param.getRequestObject());
            if(operates != null && operates.getData().size() > 0) {
                List<ClientOperateVo> data = new ArrayList();
                for(Object obj : operates.getData()) {
                    ClientOperate operate = (ClientOperate) obj;
                    DeployClient deployClient = deployClientService.getObjectById(operate.getClientId());
                    ShellLog shellLog = shellLogService.getObjectById(operate.getLogId());
                    ClientOperateVo operateVo = new ClientOperateVo();
                    operateVo.setClientName(deployClient == null ? "" : deployClient.getName());
                    operateVo.setType(operate.getType());
                    operateVo.setLogStatus(shellLog == null ? "成功" : (shellLog.getStatus().equals("0") ? "成功" : "失败"));
                    operateVo.setLogContent(shellLog == null ? "" : shellLog.getContent());
                    operateVo.setOperateUser(baseSysUserService.getObjectById(operate.getCreateUserId()).getName());
                    operateVo.setOperateTime(operate.getCreateDate());
                    data.add(operateVo);
                }
                operates.setData(data);
                return ResultUtils.success(ResultEnum.SUCCESS, operates);
            }
            return ResultUtils.error(201, "无操作记录");

        } catch (ServiceException e) {
            log.error("查询分页操作记录失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            ClientOperate clientOperate = clientOperateService.getObjectById(id);
            if(clientOperate == null) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
            shellLogService.delObjectById(clientOperate.getLogId());
            clientOperateService.delObjectById(id);
            transactionManager.commit(status);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            transactionManager.rollback(status);
            log.error("删除操作记录失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            for(String id : idList) {
                ClientOperate clientOperate = clientOperateService.getObjectById(id);
                if(clientOperate != null) {
                    shellLogService.delObjectById(clientOperate.getLogId());
                }
            }
            clientOperateService.delObjectByIds(idList);
            transactionManager.commit(status);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            transactionManager.rollback(status);
            log.error("删除操作记录失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
