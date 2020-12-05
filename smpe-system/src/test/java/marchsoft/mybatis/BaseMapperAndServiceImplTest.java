package marchsoft.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.mapper.JobMapper;
import marchsoft.modules.system.service.IJobService;
import marchsoft.test.entity.StudentTest;
import marchsoft.test.mapper.StudentTestMapper;
import marchsoft.test.service.IStudentTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * description:测试自定义mybatisPlus的方法
 *
 * @author RenShiWei
 * Date: 2020/12/4 12:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseMapperAndServiceImplTest {

    @Autowired
    private IJobService jobService;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private StudentTestMapper studentTestMapper;
    @Autowired
    private IStudentTestService studentTestService;


    /**
     * description:测试重新service的getOne方法，查出两条数据是否还会报错<p>
     * 结果：成功，不会报错
     *
     * @author RenShiWei
     * Date: 2020/12/4 15:26
     */
    @Test
    public void testJobServiceGetOne() {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getJobSort, 5);
        Job job = jobService.getOne(wrapper);
        System.out.println(job);
    }

    @Test
    public void testJobMapperSelectOne() {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getJobSort, 5).last("LIMIT 1").last("LIMIT 1");
        Job job = jobMapper.selectOne(wrapper);
        System.out.println(job);
    }

    @Test
    public void testBaseModelSelectOne() {
        LambdaQueryWrapper<StudentTest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentTest::getAge, 22).last("LIMIT 1").last("LIMIT 1");
        StudentTest studentTest = new StudentTest();
        System.out.println(studentTest.selectOne(wrapper));
    }

    @Test
    public void testStudentTestMapperSelectOne() {
        LambdaQueryWrapper<StudentTest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentTest::getAge, 22).last("LIMIT 1");
        System.out.println("getSqlSegment：" + wrapper.getSqlSegment());
        System.out.println("getSqlSet：" + wrapper.getSqlSet());
        System.out.println("getTargetSql：" + wrapper.getTargetSql());
        System.out.println("getCustomSqlSegment：" + wrapper.getCustomSqlSegment());
        System.out.println("getExpression：" + wrapper.getExpression());
        System.out.println("getSqlComment：" + wrapper.getSqlComment());
        System.out.println("getSqlFirst：" + wrapper.getSqlFirst());
        System.out.println("getParamNameValuePairs：" + wrapper.getParamNameValuePairs());
//        System.out.println(studentTestMapper.selectFirst(wrapper));
    }


}

