package marchsoft.service;

import marchsoft.base.PageVO;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobQueryCriteria;
import marchsoft.modules.system.service.IJobService;
import marchsoft.modules.system.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Description
 * ClassName JobServieceTest
 * Author liuxingxing
 *
 * @date 2020.11.24 15:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobServieceTest {

    @Autowired
    private IJobService jobService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void findByUserId() {
        System.out.println(jobService.findByUserId(0L));
    }

    @Test
    public void findById() {
        System.out.println(jobService.findById(1L));
    }

    @Test
    public void update() {
        Job job = new Job();
        job.setId(8L);
        job.setName("");
        jobService.update(job);
    }

    @Test
    public void queryAll() {
        JobQueryCriteria jobQueryCriteria = new JobQueryCriteria();
        jobQueryCriteria.setName(null);
        jobQueryCriteria.setEnabled(null);
        jobQueryCriteria.setStratTime(null);
        jobQueryCriteria.setEndTime(null);
        System.out.println(jobService.queryAll(jobQueryCriteria));
    }

    @Test
    public void queryAllPage() {
        JobQueryCriteria jobQueryCriteria = new JobQueryCriteria();
        jobQueryCriteria.setEnabled(null);
        jobQueryCriteria.setStratTime(null);
        jobQueryCriteria.setEndTime(null);
        jobQueryCriteria.setName(null);
        PageVO pageVO = new PageVO();
        pageVO.setCurrent(null);
        pageVO.setSize(null);
        pageVO.setOrders(null);
        System.out.println(jobService.queryAll(jobQueryCriteria, pageVO));
    }


    public void download() {
        try {
            jobService.download(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() {
        Job job = new Job();
        job.setName("人事专员");
        jobService.create(job);
    }


    @Test
    public void updatePW() {
//        User user = new User();
//        user.setId(1L);
//        String encode = passwordEncoder.encode("123456");
//        user.setPassword(encode);
//        String s = "$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa";

        String s = "$2a$10$kkHhhc/VwbO7WwRUqQzCr./1RL/Qb/N38tjCvkXpUA/A3EB/cNPsG";
        boolean matches = passwordEncoder.matches("123456", s);
//        userService.updateById(user);
        System.out.println(matches);
    }

    ;


    public void verification() {
        jobService.verification(null);
    }

    ;


    public void delete() {
        jobService.delete(null);
    }

    ;


}
