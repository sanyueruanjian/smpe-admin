package marchsoft.test2.service.impl;

import lombok.RequiredArgsConstructor;
import marchsoft.test2.entity.SysUser;
import marchsoft.test2.mapper.SysUserMapper;
import marchsoft.test2.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* <p>
* 系统用户 服务实现类
* </p>
*
* @author RenShiWei
* @since 2020-12-05
*/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper sysUserMapper;

}

