package marchsoft.test2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.test2.service.ISysUserService;
import marchsoft.test2.entity.SysUser;


/**
* <p>
* 系统用户 前端控制器
* </p>
* @author RenShiWei
* @since 2020-12-05
*/
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "系统用户模块")
@RequestMapping("/api/sysUser")
public class SysUserController {
    private final ISysUserService sysUserService;

}
