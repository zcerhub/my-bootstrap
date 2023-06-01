package ctbiyi.com.security;

import com.ctbiyi.auth.domain.User;
import com.ctbiyi.auth.repository.PermissionMapper;
import com.ctbiyi.auth.repository.RoleBaseMapper;
import com.ctbiyi.auth.service.UserInfoService;
import com.ctbiyi.token.domain.BiyiUserRoleDetail;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 用户权限实现类
 */
@Service
public class BiyiUserDetailsService implements UserDetailsService {

    private final UserInfoService userInfoService;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleBaseMapper roleBaseMapper;

    public BiyiUserDetailsService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        if (ObjectUtils.isEmpty(username)) {
            return null;
        }

        // String userKey = redisKeyUtil.getBizKey(RedisKeyConstants.USER_AUTH_KEY, username);
        User cscpUser = userInfoService.selectByUsername(username);
        if (Objects.isNull(cscpUser)) {
            return null;
        }

        List<String> roleIds = Lists.newArrayList();
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();

        /*  grantedAuthorities.add(new SimpleGrantedAuthority("test"));*/

        permissionMapper.selectPermCodeByUserIdType(cscpUser.getId(), null).forEach(menu -> {
            if (!StringUtils.isEmpty(menu.getPermCode())) {
                grantedAuthorities.add(new SimpleGrantedAuthority(menu.getPermCode()));
            }
        });
        roleBaseMapper.findByUserId(cscpUser.getId()).forEach(role -> {
            roleIds.add(role.getId().toString());
        });

        BiyiUserRoleDetail cscpUserAuthDetails = new BiyiUserRoleDetail(username, cscpUser.getPassword(),
                grantedAuthorities);
        cscpUserAuthDetails.setId(cscpUser.getId());
        cscpUserAuthDetails.setRoles(roleIds);

        StringJoiner joiner = new StringJoiner(",");
        grantedAuthorities.forEach(grantedAuthority -> joiner.add(grantedAuthority.getAuthority()));
        return cscpUserAuthDetails;
    }

}
