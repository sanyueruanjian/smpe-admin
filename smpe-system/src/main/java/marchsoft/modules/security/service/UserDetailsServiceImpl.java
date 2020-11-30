package marchsoft.modules.security.service;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.security.config.bean.LoginProperties;
import marchsoft.modules.security.service.dto.JwtUserDto;
import marchsoft.modules.system.entity.bo.UserBO;
import marchsoft.modules.system.entity.dto.UserDTO;
import marchsoft.modules.system.service.IDataService;
import marchsoft.modules.system.service.IRoleService;
import marchsoft.modules.system.service.IUserService;
import marchsoft.modules.system.service.mapstruct.UserMapStruct;
import marchsoft.utils.RedisUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Wangmingcan
 * @date 2020-08-17
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    private final IRoleService roleService;

    private final IDataService dataService;

    private final LoginProperties loginProperties;

    private final UserMapStruct userMapStruct;

    private final RedisUtils redisUtils;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * description:
     * modify @RenShiWei 2020/11/21 description:修改为根据id生成token，改变身份认证策略，根据id查询
     *
     * @param username 查询参数 实质为user_id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/21 21:24
     */
    @Override
    public JwtUserDto loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtUserDto jwtUserDto = null;

        if (loginProperties.isCacheEnable() && redisUtils.hasKey(username)) {
            jwtUserDto = (JwtUserDto) SerializationUtils.deserialize((byte[]) redisUtils.get(username));
            searchDb = false;
        }
        if (searchDb) {
            UserDTO userDto;
            UserBO user = userService.findUserDetailById(Long.parseLong(username));
            userDto = userMapStruct.toDto(user);
            if (user.getIsAdmin()) {
                userDto.setIsAdmin(true);
            }
            if (ObjectUtil.isEmpty(userDto)) {
                throw new BadRequestException(ResultEnum.USER_NOT_EXIST);
            } else {
                if (! userDto.getEnabled()) {
                    throw new BadRequestException(ResultEnum.COUNT_NOT_ENABLE);
                }
                jwtUserDto = new JwtUserDto(
                        userDto,
                        dataService.getDataScopeWithDeptIds(userDto),
                        roleService.mapToGrantedAuthorities(userDto)
                );
                redisUtils.set(username, SerializationUtils.serialize(jwtUserDto));
            }
        }
        return jwtUserDto;
    }
}
