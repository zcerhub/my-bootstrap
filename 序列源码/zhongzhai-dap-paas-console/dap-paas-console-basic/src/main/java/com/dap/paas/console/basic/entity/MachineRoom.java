package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

public class MachineRoom extends BaseEntity {
    /**
     * 机房编码       db_column: machine_room_code
     */
    @ApiModelProperty(value = "机房编码")
    private String machineRoomCode;
    /**
     * 机房名称       db_column: machine_room_name
     */
    @ApiModelProperty(value = "机房名称")
    private String machineRoomName;
    /**
     * 组织编码       db_column: organization_id
     */
    @ApiModelProperty(value = "组织编码")
    private String organizationId;
    /**
     * 城市编码       db_column: city_id
     */
    @ApiModelProperty(value = "城市编码")
    private String cityId;
    /**
     * 描述       db_column: description
     */
    @ApiModelProperty(value = "描述")
    private String description;

    public String getMachineRoomCode() {
        return machineRoomCode;
    }

    public void setMachineRoomCode(String machineRoomCode) {
        this.machineRoomCode = machineRoomCode;
    }

    public String getMachineRoomName() {
        return machineRoomName;
    }

    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
