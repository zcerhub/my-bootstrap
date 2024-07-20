package com.dap.paas.console.basic.service;

public interface EncryptService {
    String encrypt(String plaintext) throws Exception;
    String decrypt(String ciphertext) throws Exception;
}
