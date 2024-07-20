package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.MultiFieldAssociation;
import com.dap.paas.console.seq.check.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @className SeqServiceCluster
 * @description 序列服务集群实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
@MultiFieldAssociation.List(
        value = {
                @MultiFieldAssociation(field = "dbUrl", when = "#dbType.equals('1')", must = "#dbUrl != null && #dbUrl.trim().length() > 0 && #dbUrl.trim().length() < 255", message = "数据库URL长度范围为1~255"),
                @MultiFieldAssociation(field = "dbDriver", when = "#dbType.equals('1')", must = "#dbDriver != null && #dbDriver.trim().length() > 0 && #dbDriver.trim().length() < 255", message = "数据库驱动长度范围为1~255"),
                @MultiFieldAssociation(field = "dbUser", when = "#dbType.equals('1')", must = "#dbUser != null && #dbUser.trim().length() > 0 && #dbUser.trim().length() < 255", message = "数据库用户长度范围为1~255"),
                @MultiFieldAssociation(field = "dbPassword", when = "#dbType.equals('1')", must = "#dbPassword != null && #dbPassword.trim().length() > 0 && #dbPassword.trim().length() < 255", message = "数据库密码长度范围为1~255")
        }
)
public class SeqServiceCluster extends BaseEntity {

    /**
     * 注册中心集群名称       db_column: group_name
     */
    @NotBlank(message = "集群名称不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 255, message = "集群名称长度范围为1~255", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String name;

    /**
     * 集群状态 0 停止 1 运行 2 故障      db_column: status
     */
    private String status;

    /**
     * 机房id       db_column: room_id
     */
    @NotBlank(message = "集群id不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 32, message = "集群id长度范围为1~32", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String roomId;

    /**
     * 城市       db_column: cityId
     */
    @NotBlank(message = "城市id不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 32, message = "城市id长度范围为1~32", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String cityId;

    /**
     *单元类型  2-全局单元 3-全局单元只读副本
     */
    private String unitType;

    /**
     * 单元名称
     */
    private String unitId;

    /**
     *  0 本地数据源，1 外部数据源
     */
    @NotBlank(message = "数据库类型不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @LegalValue(values = {"0", "1"}, message = "数据库类型不合法", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String dbType;

    /**
     *  url
     */
    //@NotBlank(message = "数据库URL不能为空！", groups = {ValidGroup.Valid.Update.class})
    //@Size(min = 1, max = 255, message = "数据库URL长度范围为1~255", groups = {ValidGroup.Valid.Update.class})
    private String dbUrl;

    /**
     *  driver
     */
    //@NotBlank(message = "数据库驱动不能为空！", groups = {ValidGroup.Valid.Update.class})
    //@Size(min = 1, max = 255, message = "数据库驱动长度范围为1~255", groups = {ValidGroup.Valid.Update.class})
    private String dbDriver;

    /**
     *  用户
     */
    //@NotBlank(message = "数据库用户不能为空！", groups = {ValidGroup.Valid.Update.class})
    //@Size(min = 1, max = 255, message = "数据库用户长度范围为1~255", groups = {ValidGroup.Valid.Update.class})
    private String dbUser;

    /**
     *  密码
     */
    //@NotBlank(message = "数据库密码不能为空！", groups = {ValidGroup.Valid.Update.class})
    //@Size(min = 1, max = 255, message = "数据库密码长度范围为1~255", groups = {ValidGroup.Valid.Update.class})
    private String dbPassword;
}
