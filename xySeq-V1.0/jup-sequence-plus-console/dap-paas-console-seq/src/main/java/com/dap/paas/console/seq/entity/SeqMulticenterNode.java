package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @className SeqMulticenterNode
 * @description 序列多中心节点实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqMulticenterNode extends BaseEntity {

    /**
     * 序列服务集群id
     */
    @NotBlank(message = "集群id不能为空！")
    @Size(min = 1, max = 32, message = "集群id长度范围为1~32")
    private String seqClusterId;

    /**
     * 多种心id
     */
    private String multiClusterId;

    /**
     * 同步方式，0：冷备，1：热备
     */
    private Integer syncMethod;

    /**
     * 机房id
     */
    private String machineRoomId;

    /**
     * 城市id
     */
    private String cityId;

    /**
     * 状态  0：停用，1：启用
     */
    private String status;

    /**
     * 同步任务id
     */
    private String syncTaskId;

    /**
     *  当前数据源id----sqeClusterId
     */
    private String currentDb;

    /**
     *  url
     */
    private String dbUrl;

    /**
     *  driver
     */
    private String dbDriver;

    /**
     *  用户
     */
    private String dbUser;

    /**
     *  密码
     */
    private String dbPassword;

    /**
     *  当前数据源id----sqeClusterId
     */
    private String currentDbName;

}
