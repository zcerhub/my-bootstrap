package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.seq.entity.SeqRecycleInfo;
import com.dap.paas.console.seq.service.SeqRecycleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className SeqRecycleInfoController
 * @description 序列回收查询
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列回收查询")
@RestController
@RequestMapping("/seq/recycle/info")
public class SeqRecycleInfoController {

    @Autowired
    private SeqRecycleInfoService seqRecycleInfoService;

    /**
     * 序列回收查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation(value = "回收详情")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqRecycleInfo> param) {
        Page page = seqRecycleInfoService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, page);
    }
}
