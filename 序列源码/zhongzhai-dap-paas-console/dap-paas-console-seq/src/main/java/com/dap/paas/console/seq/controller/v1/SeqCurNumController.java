package com.dap.paas.console.seq.controller.v1;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.seq.entity.SeqCurNum;
import com.dap.paas.console.seq.service.SeqCurNumService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className SeqCurNumController
 * @description 序列服务端生成情况
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列服务端生成情况")
@RestController
@Validated
@RequestMapping("/v1/seq/useInfo")
public class SeqCurNumController {

    @Autowired
    private SeqCurNumService seqCurNumService;

    /**
     * 序列使用情况列表
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列使用情况列表")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqCurNum> param) {
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> param.getRequestObject().setTenantId(tenantId));
        Page SeqDesignList = seqCurNumService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, SeqDesignList);
    }

    /**
     * 序列使用情况编辑
     *
     * @param seqUseCondition seqUseCondition
     * @return Result
     */
    @ApiOperation(value = "序列使用情况编辑")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated SeqCurNum seqUseCondition) {
        seqCurNumService.updateObject(seqUseCondition);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }
}
