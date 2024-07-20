package com.dap.sequence.client.service;

import com.dap.sequence.api.dto.SeqTransferDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 此接口平安行内抽取到了apaas parent api包里
 */
public interface SequenceGeneratorClient {

    /**
     * 根据序列编号获取全局唯一ID序
     *
     * @param seqCode 序列Code编码
     * @param params 占位符规则参数
     * @return 序号
     */
    String getStringSequence(String seqCode, String... params);

    /**
     * 根据序列编号获取全局唯一ID序
     *
     * @param seqCode 序列Code编码
     * @param sign 数字规则从头开始标志
     * @param replaceSymbols 占位符规则参数
     * @return 序号
     */
    String getStringSequence(String seqCode, String sign, String... replaceSymbols);

    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode 序列Code编码
     * @param params  占位符规则参数
     * @return 序号
     */
    List<String> getIncreaseSequence(String seqCode, Integer requestNumber, String... params);

    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode 序列Code编码
     * @param sign    数字规则从头开始标志
     * @param params  占位符规则参数
     * @return 序号
     */
    List<String> getIncreaseSequence(String seqCode, String sign, Integer requestNumber, String... params);

    /**
     * 获取普通非自选序列
     *
     * @param seqCode 序列Code编码
     * @return 序号
     */
    String getRecoverySequence(String seqCode);

    /**
     * 回收普通非自选序列
     *
     * @param seqCode 序列Code编码
     * @param seqNo 回收序号
     * @return 序号
     */
    boolean recoverySequence(String seqCode, String seqNo);

    /**
     * 获取每日切换序列
     *
     * @param seqCode 序列Code编码
     * @param localDate 序列生产时间
     * @param params 占位符规则参数
     * @return 序号
     */
    String getCycleSequence(String seqCode, LocalDate localDate, String... params);

    /**
     * 自选序号获取
     *
     * @param seqCode 序列Code编码
     * @param seqVal 自选号段参数
     * @return 序列段
     */
    SeqTransferDto getOptionalSequence(String seqCode, String seqVal);

    /**
     * 自选序号获取并指定获取数量
     *
     * @param seqCode 序列Code编码
     * @param seqVal 自选号段参数
     * @param requestNumber 获取数量
     * @return 序列段
     */
    SeqTransferDto getOptionalSequence(String seqCode, String seqVal, Integer requestNumber);

    /**
     * 选中序号
     *
     * @param seqCode seqCode
     * @param seqNo seqNo
     * @return SeqTransferDto
     */
    SeqTransferDto selectedOptionalSequence(String seqCode, String seqNo);

    /**
     * 取消自选
     *
     * @param seqCode seqCode
     * @param seqNo seqNo
     * @return SeqTransferDto
     */
    SeqTransferDto cancelOptionalSequence(String seqCode, String seqNo);

    /**
     * 获取雪花序列
     *
     * @return 序列号
     */
    String getSnowFlake();
}