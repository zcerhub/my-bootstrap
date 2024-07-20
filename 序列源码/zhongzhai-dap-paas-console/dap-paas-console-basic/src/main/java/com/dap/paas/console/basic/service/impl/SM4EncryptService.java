package com.dap.paas.console.basic.service.impl;

import com.dap.paas.console.common.encrypt.des.Des;
import com.dap.paas.console.common.encrypt.sm4.SM4Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * SM4加密算法
 *
 * @author XIAYUANA
 */
@Service
public class SM4EncryptService extends BasicEncryptService {
    private final static String secretKey ="ugDzswUgvxcW9167";

    private static Logger logger = LoggerFactory.getLogger(SM4EncryptService.class);
    @Override
    public String encrypt(String plaintext) throws Exception {
        try {
            return SM4Utils.encryptDataECB(secretKey, Des.encrypt(plaintext));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        try {
            return Des.decrypt(SM4Utils.decryptDataECB(secretKey, ciphertext));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
