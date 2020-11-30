package marchsoft.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/11/25 10:08
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheTest {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 功能描述：测试server层和mapper层查询是否走mapper二级缓存
     *
     * @author Jiaoqianjin
     * Date: 2020/11/25 15:55
     */
    @Test
    public void cacheTest() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", 1L);
        System.out.println(userService.getOne(queryWrapper));
        System.out.println("------------------------");
        System.out.println(userMapper.selectById(1L));
        System.out.println("-----------------------");
        System.out.println(userMapper.findUserDetailById(1L));
    }

    @Test
    public void testPage() {
        IPage<User> userIPage = new Page<>();
        userIPage.setCurrent(1);
        userIPage.setSize(- 1);
        userService.page(userIPage);
    }
}
