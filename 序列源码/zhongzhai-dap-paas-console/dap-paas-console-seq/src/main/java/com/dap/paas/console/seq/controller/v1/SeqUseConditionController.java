package com.dap.paas.console.seq.controller.v1;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.seq.entity.SeqUseCondition;
import com.dap.paas.console.seq.service.SeqUseConditionService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

    private final SeqUseConditionService seqUseConditionService;

    public SeqUseConditionController(SeqUseConditionService seqUseConditionService) {
        this.seqUseConditionService = seqUseConditionService;
    }

    /**
     * 分页查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("分页查询")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqUseCondition> param) {
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> param.getRequestObject().setTenantId(tenantId));
        SeqUseCondition condition = param.getRequestObject();
        condition.setSeqName(condition.getSequenceName());
        Page seqUseCondition = seqUseConditionService.queryPage(param.getPageNo(), param.getPageSize(), condition);
        return ResultUtils.success(ResultEnum.SUCCESS, seqUseCondition);
    }
}
