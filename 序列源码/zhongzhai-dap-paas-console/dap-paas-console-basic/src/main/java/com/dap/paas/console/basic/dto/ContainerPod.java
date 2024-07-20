package com.dap.paas.console.basic.dto;

import com.dap.paas.console.basic.entity.ContainerImage;
import com.dap.paas.console.basic.entity.ContainerImagePullPolicy;
import lombok.Data;

import java.util.Map;

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
 * <td>2023/7/18 21:02</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/7/18 21:02
 * package com.dap.paas.console.cache
 */
@Data
public class ContainerPod {

    /**
     * image
     */
    private ContainerImage image;

    /**
     * 镜像拉取策略
     */
    private ContainerImagePullPolicy containerImagePullPolicy;

    /**
     * namespace
     */
    private String namespace;

    /**
     * podName
     */
    private String podName;

    /**
     * 申请cpu、最大cpu
     */
    private Integer requestCpu;

    private Integer limitCpu;

    /**
     * 申请内存、最大内存
     */
    private Integer requestMemory;

    private Integer limitMemory;

    /**
     * storageClass
     */
    private String storageClass;

    /**
     * volumeSize
     */
    private Integer volumeSize;

    /**
     * configMap
     */
    private Map<String, String> configMap;

}
