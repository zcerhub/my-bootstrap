package com.dap.paas.console.basic.entity.beijing;

import com.dap.paas.console.basic.entity.MachineRoom;
import lombok.Data;

/**
 * @author: liu
 * @date: 2022/9/6 14:34
 * @description:
 */

public class DcDcInfo extends MachineRoom {
    //城市id
    private int areaId;
    //地区名称
    private String areaFullName;
    //详细地址
    private String areaDetailAddr;
    private int resNum;
    private String state ;
    private String creDate;
    private String updDate;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaFullName() {
        return areaFullName;
    }

    public void setAreaFullName(String areaFullName) {
        this.areaFullName = areaFullName;
    }

    public String getAreaDetailAddr() {
        return areaDetailAddr;
    }

    public void setAreaDetailAddr(String areaDetailAddr) {
        this.areaDetailAddr = areaDetailAddr;
    }

    public int getResNum() {
        return resNum;
    }

    public void setResNum(int resNum) {
        this.resNum = resNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
