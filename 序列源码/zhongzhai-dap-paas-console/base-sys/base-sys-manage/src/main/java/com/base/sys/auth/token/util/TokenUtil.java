package com.base.sys.auth.token.util;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.auth.constant.TokenConstant;
import com.base.sys.auth.token.TokenInfo;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证工具类
 *
 * @author xiayuan
 */
public class TokenUtil {

	/**
	 * 创建认证token
	 *
	 * @param userInfo 用户信息
	 * @return token
	 */
	public static Kv createAuthInfo(AuthenticationUserDto userInfo) {
		Kv authInfo = Kv.create();
		//设置jwt参数
		Map<String, Object> param = new HashMap<>(16);
		param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
		param.put(TokenConstant.TENANT_ID, userInfo.getTenantId());
		param.put(TokenConstant.USER_ID, userInfo.getUserId());
		param.put(TokenConstant.ACCOUNT,  userInfo.getAccount());
		param.put(TokenConstant.AUTHORIZATION,  userInfo.getAuthorization());

		//拼装accessToken
		try {
			TokenInfo accessToken = JwtUtil.createJWT(param, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
			//返回accessToken
			return authInfo.set(TokenConstant.TENANT_ID, userInfo.getTenantId())
				.set(TokenConstant.USER_ID, userInfo.getUserId())
				.set(TokenConstant.ACCOUNT, userInfo.getAccount())
				.set(TokenConstant.ACCESS_TOKEN, accessToken.getToken())
				.set(TokenConstant.TOKEN_TYPE, TokenConstant.BEARER)
				.set(TokenConstant.EXPIRES_IN, accessToken.getExpire())
				.set(TokenConstant.LICENSE, TokenConstant.LICENSE_NAME)
			    .set(TokenConstant.AUTHORIZATION, userInfo.getAuthorization());
		} catch (Exception ex) {
			return authInfo.set("error_code", HttpServletResponse.SC_UNAUTHORIZED).set("error_description", ex.getMessage());
		}
	}
}
