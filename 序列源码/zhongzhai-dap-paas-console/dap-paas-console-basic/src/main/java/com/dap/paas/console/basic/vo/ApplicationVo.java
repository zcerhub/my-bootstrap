package com.dap.paas.console.basic.vo;

import com.dap.paas.console.basic.entity.Application;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ApplicationVo extends Application {
    public ApplicationVo(Application application){
        BeanUtils.copyProperties(application,this);
    }
    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织")
    private String organizationName;
}
