package com.dap.paas.console.basic.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.base.api.entity.BaseEntity;
import com.dap.paas.console.basic.enums.ContainerImageType;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * project name ：dap-paas-console <br>
 * description：description。。。。。 <br>
 * copyright : © 2019-2023 <br>
 * corporation : 中电金信公司 <br>
 * ----------------------------------------------------------------------------- <br>
 * change history <br>
 * <table width="432" border="1">
 * <tr>
 * <td>version</td>
 * <td>time</td>
 * <td>author</td>
 * <td>change</td>
 * </tr>
 * <tr>
 * <td>1.0.0</td>
 * <td>2023/7/18 21:03</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/7/18 21:03
 * package com.dap.paas.console.cache
 */
public class ContainerImage extends BaseEntity {

    /**
     * 镜像类型
     */
    @JsonEnumDefaultValue
    private ContainerImageType imageType;

    /**
     * 镜像名
     */
    private String imageName;

    /**
     * 镜像地址
     */
    private String imageAddress;

    /**
     * 镜像版本
     */
    private String imageVersion;

    /**
     * 镜像拉取密钥
     */
    private String imagePullSecret;

    /**
     * 镜像描述
     */
    private String remark;

    /**
     * 镜像内部组件版本
     */
    private String componentVersion;


    public ContainerImageType getImageType() {
        return imageType;
    }

    public void setImageType(ContainerImageType imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public String getImagePullSecret() {
        return imagePullSecret;
    }

    public void setImagePullSecret(String imagePullSecret) {
        this.imagePullSecret = imagePullSecret;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }
}
