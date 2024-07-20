package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.log.service.BaseLogAuditService;
import com.dap.paas.console.seq.constant.SeqConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className SeqAuditInfoController
 * @description 操作日志
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列操作日志")
@RestController
@RequestMapping("/seq/logAudit")
public class SeqAuditInfoController {

    @Autowired
    private BaseLogAuditService seqAuditInfoService;

    /**
     * 序列审计接口
     *
     * @param param param
     * @return Result
     */
    @ApiOperation(value = "序列审计查询")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<BaseLogAudit> param) {
        BaseLogAudit requestObject = param.getRequestObject();
        requestObject.setComponentType(SeqConstant.SEQ_SERVER_SIX);
        Page seqAuditInfo = seqAuditInfoService.queryPage(param.getPageNo(), param.getPageSize(), requestObject);
        return ResultUtils.success(ResultEnum.SUCCESS, seqAuditInfo);
    }
}
