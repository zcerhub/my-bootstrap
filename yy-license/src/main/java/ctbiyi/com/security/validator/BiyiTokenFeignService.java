package ctbiyi.com.security.validator;

import ctbiyi.com.security.BiyiUserServiceManage;
import com.ctbiyi.token.BiyiUserAuthDetail;

import com.ctbiyi.token.service.BiyiUserDetailsExtendService;

import javax.annotation.Resource;

/**
 * 通过Feign获取数据
 */
public class BiyiTokenFeignService implements BiyiUserDetailsExtendService {
    @Resource
    BiyiUserServiceManage biyiUserServiceManage;

    @Override
    public BiyiUserAuthDetail getUserDetailById(BiyiUserAuthDetail userAuthDetailDto) {
        Long userId = userAuthDetailDto.getUserId();
        BiyiUserAuthDetail biyiUserAuthDetailDto = biyiUserServiceManage.getAuthoritiesById(userId,
                userAuthDetailDto.getUserName());
        setBiyiDetail(biyiUserAuthDetailDto, userAuthDetailDto);
        return biyiUserAuthDetailDto;
    }

    @Override
    public BiyiUserAuthDetail getUserDetailByRole(BiyiUserAuthDetail userAuthDetailDto) {
        BiyiUserAuthDetail biyiUserAuthDetailDto = biyiUserServiceManage.getAuthoritiesByRole(
                userAuthDetailDto.getRolesStr());
        setBiyiDetail(biyiUserAuthDetailDto, userAuthDetailDto);
        return biyiUserAuthDetailDto;
    }

    /**
     * 设置用户权限详细系信息
     *
     * @param biyiUserAuthDetailDto 查询得到的用户权限信息
     * @param userAuthDetailDto     组装后的用户权限信息
     */
    private void setBiyiDetail(BiyiUserAuthDetail biyiUserAuthDetailDto, BiyiUserAuthDetail userAuthDetailDto) {
        userAuthDetailDto.setGrantedAuthorities(biyiUserAuthDetailDto.getGrantedAuthorities());
        userAuthDetailDto.setRoles(biyiUserAuthDetailDto.getRoles());
        userAuthDetailDto.setAnthStr(biyiUserAuthDetailDto.getAnthStr());
        userAuthDetailDto.setRolesStr(biyiUserAuthDetailDto.getRolesStr());
    }
}
