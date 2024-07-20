package com.dap.paas.console.seq.entity;


import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @className SeqServiceNode
 * @description 序列服务节点实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceNode extends BaseEntity {

    /**
     * 实例节点ip      db_column: ip
     */
    @NotBlank(message = "节点IP不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    @Pattern(message = "节点IP非法", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class}, regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$")
    private String hostIp;

    /**
     * 实例节点端口       db_column: port
     */
    @NotNull(message = "端口号不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    @Range(min = 0, max = 65535, message = "端口号范围为0~65535", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    private Integer port;

    /**
     * 实例状态 0：停止，1：运行中 ，2：异常      db_column: status
     */
    private String status;

    /**
     * 单元化       db_column: unit_id
     */
    private String unitId;

    /**
     * 单元类型       db_column: unit_type
     */
    private String unitType;

    /**
     * machineId       db_column: machine_id 
     */
    @NotBlank(message = "机器id不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    @Size(min = 1, max = 255, message = "机器id长度范围为1~255", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    private String machineId;

    /**
     * 部署路径       db_column: deploy_path 
     */
    @NotBlank(message = "部署路径不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    @Size(min = 1, max = 2000, message = "部署路径长度范围为1~2000", groups = {ValidGroup.Valid.ClusterEnvCheck.class, ValidGroup.Valid.NodeEnvCheck.class})
    @Pattern(regexp = "^\\/$|^((\\/([a-zA-Z0-9_-]+))+)$", message = "部署路径非法！")
    private String deployPath;

    /**
     * 服务集群id       db_column: clusterId
     */
    @NotBlank(message = "集群id不能为空！", groups = {ValidGroup.Valid.NodeEnvCheck.class})
    @Size(min = 1, max = 32, message = "集群id长度范围为1~255", groups = {ValidGroup.Valid.NodeEnvCheck.class})
    private String clusterId;


    /**
     * 配置文件路径       db_column: config_path
     */
    private String configPath;

    /**
     * log文件路径
     */
    private String logfilePath;
}

