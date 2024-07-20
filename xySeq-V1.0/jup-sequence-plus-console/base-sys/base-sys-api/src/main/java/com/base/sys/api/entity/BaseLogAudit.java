package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;

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
    //0 ：成功，1：失败
    private String operateResult;
    //用户ip
    private String operateIp;
    //方法名
    private String methonName;
    //描述
    private String desc;

    private String params;

    private List<String> operateTime;

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
}
