package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;

import java.util.List;

public class Unitization extends BaseEntity {

    /**
     * 备份号
     */
    private String unitCode;
    /**
     * 单元名称
     */
    private String unitName;
    /**
     * 机房id
     */
    private String machineRoomId;
    /**
     * 备注
     */
    private String unitDesc;
    /**
     * 数据中心名称
     */
    private String machineRoomName;

    /**
     * 数据中心编码
     */
    private String machineRoomCode;

    /**
     * 关联机器列表
     */
    private List<Machine> machineList;

    /**
     *单元类型 1-逻辑单元 2-全局单元 3-全局单元只读副本
     */
    private String unitType;

    /**
     * 备份号
     */
    private String bakNo;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMachineRoomId() {
        return machineRoomId;
    }

    public void setMachineRoomId(String machineRoomId) {
        this.machineRoomId = machineRoomId;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getMachineRoomName() {
        return machineRoomName;
    }

    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    public String getMachineRoomCode() {
        return machineRoomCode;
    }

    public void setMachineRoomCode(String machineRoomCode) {
        this.machineRoomCode = machineRoomCode;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getBakNo() {
        return bakNo;
    }

    public void setBakNo(String bakNo) {
        this.bakNo = bakNo;
    }
}
