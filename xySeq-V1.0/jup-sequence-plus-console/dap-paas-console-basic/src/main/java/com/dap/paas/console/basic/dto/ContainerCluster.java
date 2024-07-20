package com.dap.paas.console.basic.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
 * <td>2023/7/19 11:01</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/7/19 11:01
 * package com.dap.paas.console.cache
 */
@NoArgsConstructor
@Data
public class ContainerCluster<M extends BaseMetadataDTO, S extends BaseSpecDTO> {

    /**
     * clusterId，对应数据库表的集群id
     */
    private String clusterId;

    /**
     * imageId，镜像id
     */
    private String imageId;

    /**
     * apiVersion
     */
    private String apiVersion;

    /**
     * kind
     */
    private String kind;

    /**
     * metadata
     */
    private M metadata;

    /**
     * spec
     */
    private S spec;

}
