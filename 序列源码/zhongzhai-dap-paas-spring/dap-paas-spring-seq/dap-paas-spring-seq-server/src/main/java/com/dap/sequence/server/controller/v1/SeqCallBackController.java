package com.dap.sequence.server.controller.v1;

import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqDesignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * @className SeqCallBackController
 * @description 序列回调
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@RestController
@RequestMapping("/v1/seq/callback")
public class SeqCallBackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqCallBackController.class);

    @Resource
    private SeqDesignService seqDesignService;

    /**
     * 保存回收的序列
     *
     * @param request request
     * @param httpServletRequest httpServletRequest
     * @return Result
     */
    @PostMapping("/shutdownCallbackSeq")
    public ResultResponse shutdownCallbackSeq(HttpEntity<SeqCallbackDto> request, HttpServletRequest httpServletRequest) {
        LOGGER.info("客户端序列开始回收");
        SeqCallbackDto seqCallbackDto = request.getBody();
        return Optional.ofNullable(seqCallbackDto).map(dto -> {
                    SeqDesignPo seqDesignPo = new SeqDesignPo();
                    seqDesignPo.setSequenceCode(seqCallbackDto.getSequenceCode());
                    LOGGER.info("客户端回收序列为：{}", seqCallbackDto.getSequenceCode());
                    seqDesignPo.setTenantId(seqCallbackDto.getTenantId());
                    return seqDesignService.queryList(seqDesignPo);
                })
                .filter(seqDesignPos -> !seqDesignPos.isEmpty())
                .map(seqDesignPos -> SeqHolder.getMap().get(SeqKeyUtils.getSeqCacheKey(seqCallbackDto.getSequenceCode(), seqCallbackDto.getDay(), seqCallbackDto.getTenantId())))
                .map(blockingQueue -> {
                    AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(SeqKeyUtils.getSeqEngineKey(seqCallbackDto.getSequenceCode(), seqCallbackDto.getTenantId()));
                    SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
                    seqProducer.loadSeq(seqCallbackDto, seqCallbackDto.getCallbackNumQueue());
                    return ResultResponse.success("回收成功");
                })
                .orElseGet(() -> ResultResponse.error(ExceptionEnum.SEQ_RECOVERY_NOT_FOUND));
    }
}
