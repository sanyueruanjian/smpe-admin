package marchsoft.service;

import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.service.IDeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description:
 * <p>
 * Author liuxingxing
 *
 * @date 2020.11.27 08:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeptServiceTest {

    @Autowired
    private IDeptService deptService;

    @Test
    public void getDeptChildrenTest() {
        Long deptId = 22L;
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        List<Dept> byPid = this.deptService.findByPid(deptId);
        deptIds.addAll(deptService.getDeptChildren(byPid));
        System.out.println(deptIds);

    }

    @Test
    public void findByRoleIdTest() {
        Long roleId = 12L;
        Set<Dept> byRoleId = deptService.findByRoleId(roleId);
        for (Dept dept : byRoleId) {
            System.out.println(dept);
        }

    }

}
