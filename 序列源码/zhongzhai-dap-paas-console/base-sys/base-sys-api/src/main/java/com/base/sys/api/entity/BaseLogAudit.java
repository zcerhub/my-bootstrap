package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;
import org.apache.ibatis.type.IntegerTypeHandler;

import java.util.List;

/**
 * 日志审计表(BaseLogAudit)表实体类
 *
 * @author makejava
 * @since 2021-01-14 09:40:14
 */
public class BaseLogAudit extends BaseEntity {

    //操作员id
    private String operatorUserId;
    //操作员
    private String operatorUserName;
    //菜单
    private String menuName;
    //按钮
    private String buttonName;
    //1 ：成功，0：失败
    private String operateResult;

    //标识是否将本 操作审计 已经进行同步 1 标识同步成功 0 标识同步失败
    private Integer asyncStatus;
    //用户ip
    private String operateIp;
    //新增/修改/删除/导入/导出
    private String  operatorType;
    //请求方式
    private String requestMethod;

    //请求参数
    private String operParam;
    //方法名
    private String methonName;
    //描述
    private String desc;

    private String params;
    private String tenantCode;

    private String userName;

    private List<String> operateTime;

    //请求接口的url
    private String operUrl;//根据操作类型设置请求地址

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public List<String> getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(List<String> operateTime) {
        this.operateTime = operateTime;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getOperatorUserName() {
        return operatorUserName;
    }

    public void setOperatorUserName(String operatorUserName) {
        this.operatorUserName = operatorUserName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getMethonName() {
        return methonName;
    }

    public void setMethonName(String methonName) {
        this.methonName = methonName;
    }

    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOperParam() {
        return operParam;
    }

    public void setOperParam(String operParam) {
        this.operParam = operParam;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public Integer getAsyncStatus() {
        return asyncStatus;
    }

    public void setAsyncStatus(Integer asyncStatus) {
        this.asyncStatus = asyncStatus;
    }
}
