package com.dap.sequence.api.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 序列传输对象 用于客户端获取服务端序列集合并缓存本地
 * @Author: liuz
 */
public class SeqTransferDto implements Serializable {

    private static final long serialVersionUID = -4630779005875445958L;

    /**
     * 序号集合
     */
    private List<Object> list;

    /**
     * 序号编码
     */
    private String sequenceCode;

    /**
     * 自选参数
     */
    private String seqVal;

    /**
     * 编码
     */
    private String code;

    /**
     * msg
     */
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public String getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(String sequenceCode) {
        this.sequenceCode = sequenceCode;
    }

    public String getSeqVal() {
        return seqVal;
    }

    public void setSeqVal(String seqVal) {
        this.seqVal = seqVal;
    }
}
