package com.dap.paas.console.basic.entity.beijing;

import com.dap.paas.console.basic.entity.Machine;
import lombok.Data;

/**
 * @author: liu
 * @date: 2022/9/6 15:31
 * @description:
 */
public class DcServerInfo extends Machine {
    private String typeCode ;
    private String info ;
    private int port;
    private String creDate;
    private String updDate;
    private String name;
    private String endTime;
    private String startTime;
    private String deployName;
    private String deployPasswd;
    private String deployRoot;
    private String serversStatus;
    private String serversType;
    private String serversDisk;
    private String serversMem;
    private String serversCpu;
    private String serversDomain;
    private String serversSystem;
    private String serversSftppassword;
    private String serversSftpname;
    private String serversNetwork;

    public String getDeployName() {
        return deployName;
    }

    public void setDeployName(String deployName) {
        this.deployName = deployName;
    }

    public String getDeployPasswd() {
        return deployPasswd;
    }

    public void setDeployPasswd(String deployPasswd) {
        this.deployPasswd = deployPasswd;
    }

    public String getDeployRoot() {
        return deployRoot;
    }

    public void setDeployRoot(String deployRoot) {
        this.deployRoot = deployRoot;
    }

    public String getServersStatus() {
        return serversStatus;
    }

    public void setServersStatus(String serversStatus) {
        this.serversStatus = serversStatus;
    }

    public String getServersType() {
        return serversType;
    }

    public void setServersType(String serversType) {
        this.serversType = serversType;
    }

    public String getServersDisk() {
        return serversDisk;
    }

    public void setServersDisk(String serversDisk) {
        this.serversDisk = serversDisk;
    }

    public String getServersMem() {
        return serversMem;
    }

    public void setServersMem(String serversMem) {
        this.serversMem = serversMem;
    }

    public String getServersCpu() {
        return serversCpu;
    }

    public void setServersCpu(String serversCpu) {
        this.serversCpu = serversCpu;
    }

    public String getServersDomain() {
        return serversDomain;
    }

    public void setServersDomain(String serversDomain) {
        this.serversDomain = serversDomain;
    }

    public String getServersSystem() {
        return serversSystem;
    }

    public void setServersSystem(String serversSystem) {
        this.serversSystem = serversSystem;
    }

    public String getServersSftppassword() {
        return serversSftppassword;
    }

    public void setServersSftppassword(String serversSftppassword) {
        this.serversSftppassword = serversSftppassword;
    }

    public String getServersSftpname() {
        return serversSftpname;
    }

    public void setServersSftpname(String serversSftpname) {
        this.serversSftpname = serversSftpname;
    }

    public String getServersNetwork() {
        return serversNetwork;
    }

    public void setServersNetwork(String serversNetwork) {
        this.serversNetwork = serversNetwork;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
