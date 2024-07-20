package com.dap.paas.console.basic.service.impl;

import com.dap.paas.console.basic.service.EncryptService;
import com.dap.paas.console.common.encrypt.encode.Base64Util;
import org.springframework.stereotype.Service;

/**
 * 默认Base64转码
 *
 * @author XIAYUANA
 */
@Service
public class BasicEncryptService implements EncryptService {
    @Override
    public String encrypt(String plaintext) throws Exception {
        return Base64Util.encrypt(plaintext);
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {
        return Base64Util.decrypt(ciphertext);
    }
}
