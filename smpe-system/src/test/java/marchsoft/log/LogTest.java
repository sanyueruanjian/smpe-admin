package marchsoft.log;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * description:日志测试
 *
 * @author RenShiWei
 * Date: 2020/12/24 17:26
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class LogTest {

    /**
     * description:测试优雅的使用log输出，使用
     *
     * @author RenShiWei
     * Date: 2020/12/24 17:31
     */
    @Test
    public void testLogOut() {
        int userId = 1;
        String username = "test";
        log.info(StrUtil.format("【测试log】用户id：{}", Integer.toString(userId)));
        log.error(StrUtil.format("【新增用户失败】用户名已存在。操作人id：{}，新增用户用户名：{}", userId, username));
    }

}

