package marchsoft.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.dto.DeptQueryCriteria;
import marchsoft.modules.system.service.IDeptService;
import marchsoft.utils.QueryHelp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * description:mybatis通用查询测试
 *
 * @author RenShiWei
 * Date: 2020/11/19 9:31
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonQueryTest {

    @Autowired
    private IDeptService deptService;

    /**
     * description: 测试使用通用查询，查询dept信息
     * result: QueryHelp可以正常使用，但是里边有很多警告，需要进行优化  2020/11/19 @RenShiWei
     *
     * @author RenShiWei
     * Date: 2020/11/19 9:39
     */
    @Test
    public void testQueryDeptByCommon() {
        DeptQueryCriteria deptCriteria = new DeptQueryCriteria();
        deptCriteria.setPidIsNull(true);
        //通过传入参数，快速构建Wrapper
        QueryWrapper<Dept> queryWrapper = QueryHelp.getQueryWrapper(deptCriteria, Dept.class);
        List<Dept> deptList = deptService.list(queryWrapper);
        System.out.println(deptList);
    }


}

