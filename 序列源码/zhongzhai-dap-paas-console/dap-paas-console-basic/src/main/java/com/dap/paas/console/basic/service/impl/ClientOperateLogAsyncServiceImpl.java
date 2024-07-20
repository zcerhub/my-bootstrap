package com.dap.paas.console.basic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.StatEnum;
import com.base.sys.async.service.impl.BaseLogAuditAsyncServiceImpl;
import com.base.sys.log.dto.OperateLog;
import com.base.sys.log.enums.OperateLogStatus;
import com.base.sys.utils.CommonUtils;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.dao.ClientOperateDao;
import com.dap.paas.console.basic.entity.ClientOperate;
import com.dap.paas.console.basic.service.OperateLogAsyncService;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author qqqab
 */
@Service
public class ClientOperateLogAsyncServiceImpl extends AbstractBaseServiceImpl<ClientOperate, String> implements OperateLogAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOperateLogAsyncServiceImpl.class);
    @Value("${dmc.dmcUrl}")
    private String auditUrl;
    @Autowired
    ClientOperateDao clientOperateDao;

    @Override

    public void asynFailedLogAudits(String componentType) {
        ClientOperate logAuditWhere = new ClientOperate();
        logAuditWhere.setComponentType(componentType);
        logAuditWhere.setAsyncStatus(OperateLogStatus.SEND_SUC.getCode());
        //将状态为非0的数据进行同步
        List<ClientOperate> logAuditList = clientOperateDao.queryListAsyncNotAsyncStatus(logAuditWhere);
        for (ClientOperate logAudit : logAuditList) {
            asyncLogAudit(logAudit,componentType,false);
        }
    }

    @Override
    public void asyncLogAudit(ClientOperate logAudit, String componentType, boolean isRetry) {
        try {
            RestTemplate restTemplate = CommonUtils.restTemplate(30000, 30000);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("token",UserUtil.getAuthorization());
            RequestParam requestParam = RequestParam.builder().
                    operId(SnowflakeIdWorker.getID()).
                    componentCode(componentType).
                    title("").
                    operName("").
                    tenantCode(UserUtil.getUser().getTenantId()).
                    operatorType("").
                    operIp(InetAddress.getLocalHost().getHostAddress()).
                    operUrl("").
                    status(String.valueOf(ResultEnum.SUCCESS.getCode())).
                    operTime(LocalDateTime.now().toString()).
                    requestMethod("").
                    method(this.getClass().getName()).
                    operParam("").
                    resCode( String.valueOf(ResultEnum.SUCCESS.getCode())).
                    jsonResult( ResultEnum.SUCCESS.getMsg() ).build();
            HttpEntity<String> httpEntity = new HttpEntity<>(JSONObject.toJSONString(requestParam),httpHeaders);
            ResponseEntity<Map> mapResponseEntity;
            mapResponseEntity = restTemplate.postForEntity(auditUrl, httpEntity, Map.class);
            if (HttpStatus.OK.equals(mapResponseEntity.getStatusCode())) {
                LOGGER.info("SendAuditLog success,result:"+mapResponseEntity.getStatusCode());
                logAudit.setAsyncStatus(OperateLogStatus.SEND_SUC.getCode());
                if(isRetry){
                    logAudit.setAsyncStatus(StatEnum.SUCCESS.getState());
                    clientOperateDao.updateObject(logAudit);
                }
                return;
            } else {
                LOGGER.info("SendAuditLog failed,result:"+mapResponseEntity.getStatusCode());
                logAudit.setAsyncStatus(StatEnum.FAILD.getState());
                clientOperateDao.updateObject(logAudit);
            }
        } catch (Exception e) {
            LOGGER.error("SendAuditLog exception,result:"+e.getMessage());
            logAudit.setAsyncStatus(StatEnum.FAILD.getState());
            clientOperateDao.updateObject(logAudit);
        }
    }


    @Builder
    static class RequestParam{
        //操作ID
        private String operId;
        //组件名称
        private String componentCode;
        //操作模块
        private String title;
        //操作人员
        private String operName;
        //所属租户
        private String tenantCode;
        //操作类型  新增/修改/删除/导入导出
        private String operatorType;
        //操作终端ip地址
        private String operIp;
        //请求接口的url
        private String operUrl;//根据操作类型设置请求地址
        //操作状态
        private String status;
        //操作日期
        private String operTime;
        //请求方式
        private String requestMethod;
        //请求方法
        private String method;
        //请求参数
        private String operParam;
        //响应码
        private String resCode;
        //响应信息
        private String jsonResult;
        //其他参数
        private String otherMsg;

        public String getOperId() {
            return operId;
        }

        public void setOperId(String operId) {
            this.operId = operId;
        }

        public String getComponentCode() {
            return componentCode;
        }

        public void setComponentCode(String componentCode) {
            this.componentCode = componentCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOperName() {
            return operName;
        }

        public void setOperName(String operName) {
            this.operName = operName;
        }

        public String getTenantCode() {
            return tenantCode;
        }

        public void setTenantCode(String tenantCode) {
            this.tenantCode = tenantCode;
        }

        public String getOperIp() {
            return operIp;
        }

        public void setOperIp(String operIp) {
            this.operIp = operIp;
        }

        public String getOperUrl() {
            return operUrl;
        }

        public void setOperUrl(String operUrl) {
            this.operUrl = operUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOperTime() {
            return operTime;
        }

        public void setOperTime(String operTime) {
            this.operTime = operTime;
        }

        public String getRequestMethod() {
            return requestMethod;
        }

        public void setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getOperParam() {
            return operParam;
        }

        public void setOperParam(String operParam) {
            this.operParam = operParam;
        }

        public String getResCode() {
            return resCode;
        }

        public void setResCode(String resCode) {
            this.resCode = resCode;
        }

        public String getJsonResult() {
            return jsonResult;
        }

        public void setJsonResult(String jsonResult) {
            this.jsonResult = jsonResult;
        }

        public String getOtherMsg() {
            return otherMsg;
        }

        public void setOtherMsg(String otherMsg) {
            this.otherMsg = otherMsg;
        }
    }
}
