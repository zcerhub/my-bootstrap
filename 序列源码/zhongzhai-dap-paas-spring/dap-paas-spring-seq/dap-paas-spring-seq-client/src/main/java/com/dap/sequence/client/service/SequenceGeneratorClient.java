package com.dap.sequence.client.service;

import com.dap.sequence.api.dto.SeqTransferDto;

import java.util.Date;


public interface SequenceGeneratorClient {

    /**
     * 根据序列编号获取全局唯一ID序和日切序号，使用自然日
     *
     * @param seqCode 序列Code编码
     * @param params 占位符规则参数
     * @return 序号
     */
    String getStringSequence(String seqCode, String... params);

    /**
     * 根据序列编号获取全局唯一ID序和日切序号，使用工作日
     *
     * @param seqCode 序列Code编码
     * @param params 占位符规则参数
     * @return 序号
     */
    String getStringSequence(String seqCode, Date date,String... params);

    /**
     * 根据序列编号获取全局唯一ID序，具有日期占位符替换功能
     *
     * @param seqCode 序列Code编码
     * @param date  待格式化日期
     * @param params 占位符规则参数
     * @return 序号
     */
    String getStringSequenceWithDatePlaceholder(String seqCode,Date date, String... params);

    /**
     * 根据序列编号获取全局唯一ID序
     *
     * @param seqCode 序列Code编码
     * @param sign 数字规则从头开始标志
     * @param params 占位符规则参数
     * @return 序号
     */
    String getVariableSequence(String seqCode, String sign, String... params);

    /**
     * 根据序列编号获取全局唯一ID序，具有日期占位符替换功能
     *
     * @param seqCode 序列Code编码
     * @param date 日期
     * @param sign 数字规则从头开始标志
     * @param params 占位符规则参数
     * @return 序号
     */
    String getVariableSequence(String seqCode,Date date, String sign, String... params);



    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode 序列Code编码
     * @param params 占位符规则参数
     * @return 序号
     */
    String getIncreaseSequence(String seqCode, String... params);

    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode 序列Code编码
     * @param sign 数字规则从头开始标志
     * @param params 占位符规则参数
     * @return 序号
     */
    String getIncreaseSequenceBySign(String seqCode, String sign, String... params);

    /**
     * 根据序列编号获取可回收I序号
     *
     * @param seqCode 序列Code编码
     * @return 序号
     */
    String getRecoverySequence(String seqCode);

    /**
     * 根据序列编号获取带有日期占位符替换功能的可回收的I序号
     *
     * @param seqCode 序列Code编码
     * @return 序号
     */
    String getRecoverySequenceWithDatePlaceholder(String seqCode,Date date);

    /**
     * 根据序列编号和序号回收序号
     *
     * @param seqCode 序列Code编码
     * @param seqNo 回收序号
     * @return 序号
     */
    Boolean recoverySequence(String seqCode, String seqNo);

    /**
     * 获取每日切换序列
     *
     * @param seqCode 序列Code编码
     * @param date 序列生产时间
     * @param params 占位符规则参数
     * @return 序号
     */
    String getDaySwitchSequence(String seqCode, Date date, String... params);

    /**
     * 获取带有日期占位符替换功能的日切换序列
     *
     * @param seqCode 序列Code编码
     * @param date 序列生产时间
     * @param enableDatePlaceholder 是否开启日期占位符替换功能
     * @param params 占位符规则参数
     * @return 序号
     */
    String getDaySwitchSequenceWithDatePlaceholder(String seqCode,Date date, boolean enableDatePlaceholder, String... params);

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