package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqUseCondition;
import com.dap.paas.console.seq.service.SeqUseConditionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className SeqUseConditionController
 * @description 序列规则模板管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列使用状况")
@RestController
@RequestMapping("/v1/seq/condition")
public class SeqUseConditionController {

    @Autowired
    private SeqUseConditionService seqUseConditionService;

    /**
     * 分页查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("分页查询")
    @PostMapping("/queryPage")
    public ResultResponse<Page<SeqUseCondition>> queryPage(@RequestBody PageInDto<SeqUseCondition> param) {
        return seqUseConditionService.queryPage(param);
    }
}
