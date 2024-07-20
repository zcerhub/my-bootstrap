package com.dap.paas.console.seq.util.jsch;

import java.io.Serializable;

/**
 * @className ShellResult
 * @description shell脚本运行结果
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class ShellResult implements Serializable {

    private int exitStatus;

    private String shellOutPut;

    public int getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    public String getShellOutPut() {
        return shellOutPut;
    }

    public void setShellOutPut(String shellOutPut) {
        this.shellOutPut = shellOutPut;
    }

    @Override
    public String toString() {
        return "ShellResult{" +
                "exitStatus=" + exitStatus +
                ", shellOutPut='" + shellOutPut + '\'' +
                '}';
    }
}
