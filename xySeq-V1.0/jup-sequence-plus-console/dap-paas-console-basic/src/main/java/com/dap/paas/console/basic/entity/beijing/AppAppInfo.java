package com.dap.paas.console.basic.entity.beijing;

import com.dap.paas.console.basic.entity.Application;
import lombok.Data;

/**
 * @author: liu
 * @date: 2022/9/6 14:19
 * @description:
 */

public class AppAppInfo extends Application {
    private String remark;
    private String appType ;
    private String appLanguage ;
    private String appFrame ;
    private String image;
    private String usable ;
    private String showStoreFlag;
    private String sysName;
    private String creDate;
    private String updDate;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public String getAppFrame() {
        return appFrame;
    }

    public void setAppFrame(String appFrame) {
        this.appFrame = appFrame;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getShowStoreFlag() {
        return showStoreFlag;
    }

    public void setShowStoreFlag(String showStoreFlag) {
        this.showStoreFlag = showStoreFlag;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getCreDate() {
        return creDate;
    }

    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
