package com.example.yylicenseweb;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Component
public class JwtTokenUtil {

    /**
     * 加密token.
     */
    public String getToken() throws JsonProcessingException {
        // 1、创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                // 秘钥位置
                new ClassPathResource("jwt.jks"),
                // 秘钥库密码
                "123456".toCharArray()
        );

        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("small-tools", "123456".toCharArray());

        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        //这个是放到负载payLoad 里面,魔法值可以使用常量类进行封装.
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("licenseValid", "false");
//        tokenMap.put("licenseValid", "true");
        Jwt jwt = JwtHelper.encode(new ObjectMapper().writeValueAsString(tokenMap), new RsaSigner(rsaPrivateKey));
        String jwtEncoded = jwt.getEncoded();
        System.out.println("jwtEncoded:" + jwtEncoded);
        String claims = jwt.getClaims();
        System.out.println("claims:" + claims);
        String token = jwt.getEncoded();
        return token;
    }

    public static void main(String[] args) throws JsonProcessingException {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        System.out.println(jwtTokenUtil.getToken());

//        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        System.out.println(jwtTokenUtil.parseToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJuYW1lIjoienEifQ.U-eS3Be6yWIxdGges3HuquxUZlbsBtLi_HRlGAbIhvo8zyGqYIr2lYm0tcVOfgn3ydRwh17DrrEL20TL4VkCvKDHsGIWGFvUt9lLj8jGb09jZ_nHZcY5U4zXM7ktGHX12GF72ci4Du4xIEvN4jWHl3zrAcQUUVBVc5nHixawMV38SJWO1Bm6we-2j4B5UBlY6HWEBAG3Lpwd9cPUKRryCm4EOPFGS5pJh1GdqFdEy3YhcDMW_T5vdGvhRkHtveRYrEMpGzP_e3RB9x-dFnjEVt6GAPwQfnaqoJXv_YvpP13TpOqw24v0llx_qvtMWFR5P_ZcZvYxxdQ1Hc74ezzL4g"));
    }


    public static String parseToken(String token)  {
        String publickey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgXNjNijKNxlt+PZmL8n5\n" +
                "cq1/JkWdhVyF4uJPGRyZVGV0YJ3N7S3T3kwvgJR7BdAnb7hbSzC0KjSJE6br2ALf\n" +
                "Rj6YRTZ6pfZ9ZoXR4/aGrBeWuloUXxjLopgiZhHuUt1KnOoHJaThYFKFf/Z+AfWo\n" +
                "EPNAFpB7RbCpvJdHXe2EpGbZZyft7lvllnv5GKEujXJYqMbUwioLdvmM+wX2ehM2\n" +
                "QfKxLB63LGaoLuKJss5nzNxU7L71aLCbzV5D3mTCUUAkn5h8pmuHb07hzFzQtq9P\n" +
                "oGmaF/Un7IoDUgnRPkzRuzF4gDv4uYIduHBZOQDmuoEhqg2BxYlLISvQheb/dnvy\n" +
                "JQIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取jwt原始内容
        String claims = jwt.getClaims();

        return claims;
    }



}
