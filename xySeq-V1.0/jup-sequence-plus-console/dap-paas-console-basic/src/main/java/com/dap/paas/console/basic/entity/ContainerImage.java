package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.basic.enums.ContainerImageType;
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
@Data
@EqualsAndHashCode(callSuper = true)
public class ContainerImage extends BaseEntity {

    /**
     * 镜像类型
     */
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

}
