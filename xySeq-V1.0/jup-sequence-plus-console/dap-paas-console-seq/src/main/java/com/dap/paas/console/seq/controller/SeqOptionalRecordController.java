package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;
import com.dap.paas.console.seq.service.SeqOptionalRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

/**
 * @className SeqOptionalRecordController
 * @description 序列自选记录
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列自选记录")
@RestController
@RequestMapping("/seq/optional/record")
@Validated
public class SeqOptionalRecordController {

    private static final Logger LOG = LoggerFactory.getLogger(SeqOptionalRecordController.class);

    /**
     * 服务对象
     */
    @Resource
    private SeqOptionalRecordService seqOptionalRecordService;

    /**
     * 序列自选记录列表
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列自选记录列表")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqOptionalRecord> param) {
        Page seqOptionalRecordList = seqOptionalRecordService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, seqOptionalRecordList);
    }

    /**
     * 序列自选记录新增
     *
     * @param seqOptionalRecord seqOptionalRecord
     * @return Result
     */
    @ApiOperation(value = "序列自选记录新增")
    @PostMapping("/insert")
    public ResultResponse insert(@RequestBody @Validated SeqOptionalRecord seqOptionalRecord) {
        LOG.info("序列自选记录新增信息 = {}", seqOptionalRecord);
        return seqOptionalRecordService.insert(seqOptionalRecord);
    }

    /**
     * 序列自选记录更新
     *
     * @param seqOptionalRecord seqOptionalRecord
     * @return Result
     */
    @ApiOperation(value = "序列自选记录更新")
    @PostMapping("/update")
    public Result updateOrder(@RequestBody @Valid SeqOptionalRecord seqOptionalRecord) {
        LOG.info("序列自选记录更新信息 = {}", seqOptionalRecord);
        Integer record = seqOptionalRecordService.updateObject(seqOptionalRecord);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列自选记录删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation("序列自选记录删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列自选id非法！") String id) {
        LOG.info("序列自选记录删除id = {}", id);
        seqOptionalRecordService.delObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }
}

