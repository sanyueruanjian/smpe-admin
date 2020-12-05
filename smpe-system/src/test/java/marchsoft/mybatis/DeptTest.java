package marchsoft.mybatis;

import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.service.IDeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * description:
 *
 * @Author liuxingxing
 * @date 2020.12.05 15:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeptTest {

    @Autowired
    public IDeptService deptService;

    @Test
    public void update() {
        Dept dept = new Dept();
        deptService.updateDept(dept);
    }
}
