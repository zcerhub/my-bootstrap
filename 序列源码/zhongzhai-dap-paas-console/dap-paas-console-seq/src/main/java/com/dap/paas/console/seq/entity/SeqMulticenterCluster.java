package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @className SeqMulticenterCluster
 * @description 序列多中心集群实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqMulticenterCluster extends BaseEntity {

    /**
     * 名称
     */
    @NotBlank(message = "集群名称不能为空！")
    @Size(min = 1, max = 32, message = "集群名称长度范围为1~32")
    private String name;

    /**
     * 描述
     */
    @Size(max = 255, message = "集群描述信息长度范围为0~255")
    private String description;

}
