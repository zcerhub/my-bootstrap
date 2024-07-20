package com.dap.paas.console.basic.controller.v1;

import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管控端探测接口
 **/
@Api(tags = "detectApi")
@RestController
@RequestMapping("/v1/console/detect")
public class ServerDetectController {

    @ApiOperation(value = "管控端探测")
    @GetMapping("/do-detect")
    public Result serverDetect() {
        return ResultUtils.success("hello ,welcome to console portal !!!");
    }

}
