package com.dap.paas.console.seq.util.jsch;

import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @className LinuxUserInfo
 * @description linux用户信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class LinuxUserInfo implements UserInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxUserInfo.class);

    String password;

    public LinuxUserInfo(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptYesNo(String str) {
        return true;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    @Override
    public boolean promptPassword(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        LOGGER.info(message);
    }
}
