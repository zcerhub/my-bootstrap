package com.dap.paas.console.seq.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @className SeqServiceStatusDTO
 * @description 服务状态DTO
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceStatusDTO implements Serializable {

    /**
     * 服务总数
     */
    private String serviceTotal;

    /**
     * 正常服务总数
     */
    private String normalServiceTotal;

    /**
     * 异常服务总数
     */
    private String exceptionServiceTotal;
}
