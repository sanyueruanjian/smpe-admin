package marchsoft.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.bean.PageVO;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.dto.UserDTO;
import marchsoft.modules.system.entity.dto.UserQueryCriteria;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * description:用户相关业务测试
 *
 * @author RenShiWei
 * Date: 2020/11/21 11:03
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * description:测试根据用户名查询用户相关信息的业务
     *
     * @author RenShiWei
     * Date: 2020/11/21 11:05
     */
    @Test
    public void testUserFindByName() {
        Long test = userService.findUserIdByName("test");
        System.out.println(test);
    }

    @Test
    public void testUserFindAll() {
        PageVO pageVO = new PageVO();
        IPage<User> userAll = userMapper.findUserAll(pageVO.buildPage());
        System.out.println(userAll.getRecords().toString());
    }


    /**
     * description: 测试构建查询条件，查询用户的详细信息
     *
     * @author RenShiWei
     * Date: 2020/11/24 16:41
     */
    @Test
    public void queryUserDetailList() {
        UserQueryCriteria userQueryCriteria = new UserQueryCriteria();
        userQueryCriteria.setBlurry("t");

        List<UserDTO> userDTOS = userService.queryUserDetailsList(userQueryCriteria);
        System.out.println(userDTOS);
    }


}

