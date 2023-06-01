package ctbiyi.com.security.validator;

import ctbiyi.com.security.BiyiUserDetailsService;
import com.ctbiyi.token.BiyiUserAuthDetail;
import com.ctbiyi.token.domain.BiyiUserRoleDetail;
import com.ctbiyi.token.service.BiyiUserDetailsExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库中获取用户权限信息
 */
@Service
public class BiyiTokenDBService implements BiyiUserDetailsExtendService {

    @Autowired
    BiyiUserDetailsService biyiUserDetailsService;

    @Override
    public BiyiUserAuthDetail getUserDetailById(BiyiUserAuthDetail userAuthDetailDto) {
        //获取cscpUserAuthDetails
        BiyiUserRoleDetail cscpUserAuthDetails = (BiyiUserRoleDetail) biyiUserDetailsService.loadUserByUsername(
                userAuthDetailDto.getUserName());

        userAuthDetailDto.setUserId(cscpUserAuthDetails.getId());
        userAuthDetailDto.setUserName(cscpUserAuthDetails.getUsername());

        //封装权限
        List<String> grantedAuthorities = new ArrayList<>();
        userAuthDetailDto.setGrantedAuthorities(grantedAuthorities);
        cscpUserAuthDetails.getAuthorities()
                .forEach(grantedAuthority -> grantedAuthorities.add(grantedAuthority.getAuthority()));
        return userAuthDetailDto;
    }

    @Override
    public BiyiUserAuthDetail getUserDetailByRole(BiyiUserAuthDetail userAuthDetailDto) {
        return null;
    }
}
