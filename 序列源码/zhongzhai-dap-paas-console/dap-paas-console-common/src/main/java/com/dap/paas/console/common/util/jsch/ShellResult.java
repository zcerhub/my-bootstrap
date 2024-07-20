package com.dap.paas.console.common.util.jsch;

import java.io.Serializable;

/**
 * @author: zpj
 * @date: 2021/11/27
 * @version: 1.0
 * @descriptions: TODO
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
