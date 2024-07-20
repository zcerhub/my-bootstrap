package com.base.sys.auth.token.util;

import com.base.sys.auth.constant.JwtConstant;
import com.base.sys.auth.constant.TokenConstant;
import com.base.sys.auth.token.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Jwt 工具类
 *
 * @author xiayuan
 */
public class JwtUtil {
    /**
     * 创建令牌
     *
     * @param user      user
     * @param audience  audience
     * @param issuer    issuer
     * @param tokenType tokenType
     * @return jwt
     */
    public static TokenInfo createJWT(Map<String, Object> user, String audience, String issuer, String tokenType) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(JwtUtil.getBase64Security());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signingKey);

        //设置JWT参数
        user.forEach(builder::claim);
        //添加Token过期时间
        long expireMillis;
        if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
            expireMillis = 24 * 3600 * 1000L;
        }  else {
            expireMillis = getExpire();
        }
        long expMillis = nowMillis + expireMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        //组装Token信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire((int) (expireMillis / 1000L));
        return tokenInfo;
    }

    /**
     * 获取过期时间(次日凌晨2点)
     *
     * @return expire
     */
    public static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    /**
     * 签名加密
     */
    public static String getBase64Security() {
        return Base64.getEncoder().encodeToString(JwtConstant.DEFAULT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken token串
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(getBase64Security())).build()
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取请求传递的token串
     *
     * @param auth token
     * @return String
     */
    public static String getToken(String auth) {
        if (isBearer(auth)) {
            return auth.substring(7);
        }
        return auth;
    }

    /**
     * 判断token类型为bearer
     *
     * @param auth token
     * @return String
     */
    public static Boolean isBearer(String auth) {
        if ((auth != null) && (auth.length() > 7)) {
            String headStr = auth.substring(0, 6);
            return headStr.compareTo(TokenConstant.BEARER) == 0;
        }
        return false;
    }
}
