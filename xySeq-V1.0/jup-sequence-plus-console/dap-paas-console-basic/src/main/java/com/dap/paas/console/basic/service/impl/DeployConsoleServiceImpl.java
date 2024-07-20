package com.dap.paas.console.basic.service.impl;

import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.constant.DataState;
import com.dap.paas.console.basic.entity.ClientOperate;
import com.dap.paas.console.basic.entity.DeployClient;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.ShellLog;
import com.dap.paas.console.basic.enums.ConsoleClient;
import com.dap.paas.console.basic.enums.OperateType;
import com.dap.paas.console.basic.service.ClientOperateService;
import com.dap.paas.console.basic.service.DeployService;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.service.ShellLogService;
import com.dap.paas.console.basic.utils.*;
import com.dap.paas.console.common.constants.DeployConst;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeployConsoleServiceImpl extends AbstractBaseServiceImpl<DeployClient,String> implements DeployService {
    private static final Logger log= LoggerFactory.getLogger(DeployConsoleServiceImpl.class);

    @Autowired
    private ClientOperateService clientOperateService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private ShellLogService shellLogService;
    @Autowired
    @Qualifier("coreThreadPool")
    private TaskExecutor taskExecutor;

    @Override
    public boolean support(DeployClient client) {
        return isEnumInclude(client.getName());
    }

    @Override
    public Result doDeploy(DeployClient client) {
        Map<String,String> params = new HashMap<>();
        params.put("hostIp", client.getHostAddress());
        Machine machine = machineService.getObjectByMap(params);
        if(machine == null) {
            log.error("新增部署服务失败","无法获取部署机器信息");
            return ResultUtils.error(ResultEnum.FAILED);
        }

        long logId = IdCenter.getInstance().getId();
        try {
            client.setState(DataState.EFFECTIVE);
            this.saveObject(client);//保存client

            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setClientId(client.getId());
            clientOperate.setType(OperateType.CLIENT_INSTALL.getDescription());
            clientOperate.setLogId(String.valueOf(logId));
            clientOperateService.saveObject(clientOperate);
        } catch (ServiceException e) {
            log.error("新增部署服务失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }

        //执行部署动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            EchoFunction echoFunction = new WebSocketEcho();
            try {
                ExecResult execResult = SshDeployServerUtil.deploy(String.valueOf(logId), echoFunction,
                        DeployConst.PACKAGE_FOLDER  + client.getName() + ".tar.gz", machine, DeployConst.WORKSPACE, DeployConst.WORKSPACE);
                log = execResult.getResult();
                status = execResult.getStatus();
                if(status.equals(ExecResult.SUCCESS)) {
                    execResult = SshDeployServerUtil.start(String.valueOf(logId), echoFunction, machine,
                            null, null, null, "/server.sh start");
                    log = log + execResult.getResult();
                    status = execResult.getStatus();
                }
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
            }
            finally {
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog();
            shellLog.setId(String.valueOf(logId));
            shellLog.setStatus(status);
            shellLog.setContent(log);
            shellLogService.saveObject(shellLog);
        });

        //返回logId
        return ResultUtils.success(ResultEnum.SUCCESS,logId);
    }

    @Override
    public Result doStart(DeployClient client) {

        Map<String,String> params = new HashMap<>();
        params.put("hostIp", client.getHostAddress());
        Machine machine = machineService.getObjectByMap(params);
        if(machine == null) {
            log.error("启动服务失败","无法获取部署机器信息");
            return ResultUtils.error(ResultEnum.FAILED);
        }

        long logId = IdCenter.getInstance().getId();
        try {
            client.setState(DataState.EFFECTIVE);
            this.updateObject(client);//保存client

            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setClientId(client.getId());
            clientOperate.setType(OperateType.CLIENT_START.getDescription());
            clientOperate.setLogId(String.valueOf(logId));
            clientOperateService.saveObject(clientOperate);
        } catch (ServiceException e) {
            log.error("启动服务失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }

        //执行卸载动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            EchoFunction echoFunction = new WebSocketEcho();
            try {
                ExecResult execResult = SshDeployServerUtil.start(String.valueOf(logId), echoFunction, machine,
                        null, null ,null, "./server.sh start");
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
            }
            finally {
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog();
            shellLog.setId(String.valueOf(logId));
            shellLog.setStatus(status);
            shellLog.setContent(log);
            shellLogService.saveObject(shellLog);
        });

        //返回logid,
        return ResultUtils.success(ResultEnum.SUCCESS,logId);
    }

    @Override
    public Result doStop(DeployClient client) {
        Map<String,String> params = new HashMap<>();
        params.put("hostIp", client.getHostAddress());
        Machine machine = machineService.getObjectByMap(params);
        if(machine == null) {
            log.error("停止服务失败","无法获取部署机器信息");
            return ResultUtils.error(ResultEnum.FAILED);
        }

        long logId = IdCenter.getInstance().getId();
        try {
            client.setState(DataState.INVALID);
            this.updateObject(client);//保存client

            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setClientId(client.getId());
            clientOperate.setType(OperateType.CLIENT_STOP.getDescription());
            clientOperate.setLogId(String.valueOf(logId));
            clientOperateService.saveObject(clientOperate);
        } catch (ServiceException e) {
            log.error("停止服务失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }

        //执行卸载动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            EchoFunction echoFunction = new WebSocketEcho();
            try {
                ExecResult execResult = SshDeployServerUtil.stop(String.valueOf(logId), echoFunction, machine, "./server.sh stop");
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
            }
            finally {
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog();
            shellLog.setId(String.valueOf(logId));
            shellLog.setStatus(status);
            shellLog.setContent(log);
            shellLogService.saveObject(shellLog);
        });

        //返回logid,
        return ResultUtils.success(ResultEnum.SUCCESS,logId);
    }

    @Override
    public Result doRemove(DeployClient client) {
        Map<String,String> params = new HashMap<>();
        params.put("hostIp", client.getHostAddress());
        Machine machine = machineService.getObjectByMap(params);
        if(machine == null) {
            log.error("停止服务失败","无法获取部署机器信息");
            return ResultUtils.error(ResultEnum.FAILED);
        }

        long logId = IdCenter.getInstance().getId();
        try {
            this.delObjectById(client.getId());

            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setClientId(client.getId());
            clientOperate.setType(OperateType.CLIENT_UNINSTALL.getDescription());
            clientOperate.setLogId(String.valueOf(logId));
            clientOperateService.saveObject(clientOperate);
        } catch (ServiceException e) {
            log.error("删除部署服务失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }


        //执行卸载动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            EchoFunction echoFunction = new WebSocketEcho();
            try {
                ExecResult execResult = SshDeployServerUtil.uninstall(String.valueOf(logId), echoFunction, machine, client.getName());
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
            }
            finally {
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog();
            shellLog.setId(String.valueOf(logId));
            shellLog.setStatus(status);
            shellLog.setContent(log);
            shellLogService.saveObject(shellLog);
        });

        //返回logid,
        return ResultUtils.success(ResultEnum.SUCCESS,logId);
    }

    private boolean isEnumInclude(String name){
        boolean include = false;
        for (ConsoleClient client: ConsoleClient.values()){
            if(client.getName().equals(name)){
                include = true;
                break;
            }
        }
        return include;
    }
}
