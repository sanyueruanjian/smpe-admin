package marchsoft.security;

import marchsoft.utils.RedisUtils;
import marchsoft.utils.SecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/21 11:23
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtUserTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void testJwtUserByRedis() {
        System.out.println(SecurityUtils.getCurrentUser());//UserDetails
        System.out.println(SecurityUtils.getCurrentUserId());//id
        System.out.println(SecurityUtils.getCurrentUserDataScope());//权限部门id集合，全部时为[]空集合
        System.out.println(SecurityUtils.getCurrentUsername());//id
        System.out.println(SecurityUtils.getDataScopeType());//全部
    }


}

