package com.dap.paas.console.basic.vo;

/**
 * 分布式管控数据中心
 * @author XIAYUANA
 */
public class DataCenterVo {

    String logicDataCenterCode;
    String logicDataCenterName;
    String dataCenterCode;
    String dataCenterName;
    String tenantCode;

    public String getLogicDataCenterCode() {
        return logicDataCenterCode;
    }

    public void setLogicDataCenterCode(String logicDataCenterCode) {
        this.logicDataCenterCode = logicDataCenterCode;
    }

    public String getLogicDataCenterName() {
        return logicDataCenterName;
    }

    public void setLogicDataCenterName(String logicDataCenterName) {
        this.logicDataCenterName = logicDataCenterName;
    }

    public String getDataCenterCode() {
        return dataCenterCode;
    }

    public void setDataCenterCode(String dataCenterCode) {
        this.dataCenterCode = dataCenterCode;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getDataCenterName() {
        return dataCenterName;
    }

    public void setDataCenterName(String dataCenterName) {
        this.dataCenterName = dataCenterName;
    }
}
