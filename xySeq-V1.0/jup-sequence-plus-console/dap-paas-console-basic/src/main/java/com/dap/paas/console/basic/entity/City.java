package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class City extends BaseEntity {
    /**
     * 城市编码       db_column: city_code
     */
    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    /**
     * 城市名称       db_column: city_name
     */
    @ApiModelProperty(value = "城市名称")
    private String cityName;

}
