package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

public class Machine extends BaseEntity {
    /**
     * 主机编码       db_column: host_code
     */
    @ApiModelProperty(value = "主机编码")
    private String hostCode;
    /**
     * 主机IP       db_column: host_ip
     */
    @ApiModelProperty(value = "主机IP")
    private String hostIp;
    /**
     * 主机端口       db_column: host_port
     */
    @ApiModelProperty(value = "主机端口", example = "9999")
    private Integer hostPort;
    /**
     * 主机备注       db_column: host_remark
     */
    @ApiModelProperty(value = "主机备注")
    private String hostRemark;
    /**
     * ssh账号       db_column: host_ssh_account
     */
    @ApiModelProperty(value = "ssh账号")
    private String hostSshAccount;
    /**
     * ssh密码       db_column: host_ssh_password
     */
    @ApiModelProperty(value = "ssh密码")
    private String hostSshPassword;
    /**
     * 机房编码       db_column: machine_room_id
     */
    @ApiModelProperty(value = "机房编码")
    private String machineRoomId;
    /**
     * 操作系统类型，如redhat,centos       db_column: os_release
     */
    @ApiModelProperty(value = "操作系统类型，如redhat,centos")
    private String osRelease;
    /**
     * 操作系统版本，如7，6       db_column: os_version
     */
    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;
    /**
     * 机器是否可用0；可用，1：不可用       db_column: available
     */
    @ApiModelProperty(value = "机器是否可用")
    private int available;
    /**
     * CPU内核       db_column: core_arch
     */
    @ApiModelProperty(value = "cpu内核")
    private String coreArch;

    /**
     * 所属机房
     */
    private String computerRoom;
    /**
     * 部署路径
     */
    @ApiModelProperty(value = "部署路径")
    private String deploymentPath;

    /**
     * 单元id  db_column: unit_id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;

    public String getHostCode() {
        return hostCode;
    }

    public void setHostCode(String hostCode) {
        this.hostCode = hostCode;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public String getHostRemark() {
        return hostRemark;
    }

    public void setHostRemark(String hostRemark) {
        this.hostRemark = hostRemark;
    }

    public String getHostSshAccount() {
        return hostSshAccount;
    }

    public void setHostSshAccount(String hostSshAccount) {
        this.hostSshAccount = hostSshAccount;
    }

    public String getHostSshPassword() {
        return hostSshPassword;
    }

    public void setHostSshPassword(String hostSshPassword) {
        this.hostSshPassword = hostSshPassword;
    }

    public String getMachineRoomId() {
        return machineRoomId;
    }

    public void setMachineRoomId(String machineRoomId) {
        this.machineRoomId = machineRoomId;
    }

    public String getOsRelease() {
        return osRelease;
    }

    public void setOsRelease(String osRelease) {
        this.osRelease = osRelease;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getCoreArch() {
        return coreArch;
    }

    public void setCoreArch(String coreArch) {
        this.coreArch = coreArch;
    }

    public String getComputerRoom() {
        return computerRoom;
    }

    public void setComputerRoom(String computerRoom) {
        this.computerRoom = computerRoom;
    }

    public String getDeploymentPath() {
        return deploymentPath;
    }

    public void setDeploymentPath(String deploymentPath) {
        this.deploymentPath = deploymentPath;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
