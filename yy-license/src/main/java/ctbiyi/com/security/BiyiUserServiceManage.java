package ctbiyi.com.security;

import com.ctbiyi.token.BiyiUserAuthDetail;
import feign.Param;
import feign.RequestLine;

/**
 * 用户权限信息feign调用
 */
public interface BiyiUserServiceManage {

    /**
     * 根据用户id获取权限
     *
     * @param userId   用户id
     * @param userName 用户名
     * @return 用户权限
     */
    @RequestLine("GET /inner/authoritiesById?method=id&userId={userId}&userName={userName}")
    BiyiUserAuthDetail getAuthoritiesById(@Param("userId") Long userId, @Param("userName") String userName);

    /**
     * 根据用户角色获取权限
     *
     * @param roles 角色
     * @return 用户权限
     */
    @RequestLine("GET /inner/authoritiesByRole?method=roles&roles={roles}")
    BiyiUserAuthDetail getAuthoritiesByRole(@Param("roles") String roles);

}
