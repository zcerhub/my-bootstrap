package com.dap.sequence.server.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;


/**
 * @className SeqServiceCluster
 * @description 序列服务集群实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceCluster extends BaseEntity {

    /**
     * id
     */
    private String id;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群状态 0 停止 1 运行 2 故障
     */
    private String status;

    /**
     * 组织
     */
    private String orgId;

    /**
     * 城市
     */
    private String cityId;

    /**
     * 机房id
     */
    private String roomId;


    /**
     * 单元名称
     */
    private String unitId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     *  新增用户
     */
    private String createUserId;

    /**
     * 更新用户
     */
    private String updateUserId;


    /**
     *  数据库类型
     */
    private String dbType;

    /**
     *  数据库url
     */
    private String dbUrl;

    /**
     *  数据库驱动
     */
    private String dbDriver;

    /**
     *  数据库用户
     */
    private String dbUser;

    /**
     *  数据库密码
     */
    private String dbPassword;

    /**
     *  单元类型
     */
    private String unitType;
}
