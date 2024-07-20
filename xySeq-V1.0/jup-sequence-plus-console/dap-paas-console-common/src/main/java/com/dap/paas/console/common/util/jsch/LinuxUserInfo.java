package com.dap.paas.console.common.util.jsch;

import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zpj
 * @date: 2021/11/27
 * @version: 1.0
 * @descriptions: TODO
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
