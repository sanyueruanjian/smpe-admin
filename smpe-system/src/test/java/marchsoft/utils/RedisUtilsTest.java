package marchsoft.utils;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Description:
 * <p>
 *
 * @author liuxingxing
 * @date 2020/11/17 22:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilsTest {

    @Autowired
    RedisUtils redisUtils;

    @Test
    public void isRedisUtils() {
        System.out.println(redisUtils);
        redisUtils.set("string", "哈哈哈");
        redisUtils.set("int",123);
        Student student = new Student();
        student.setAge(12);
        student.setName("张三");
        redisUtils.set("stu1", student);
        Student st =(Student)redisUtils.get("stu1");
        System.out.println(redisUtils.get("string"));
        System.out.println(redisUtils.get("int"));
        System.out.println(st);
    }

    @Test
    public void saveOneStu() {
        Student student = new Student();
        student.setAge(12);
        student.setName("张三");
        Student student2 = new Student();
        student2.setAge(13);
        student2.setName("李四");
        student.setFriend(student2);
        redisUtils.set("stu2", student);
    }

    @Test
    public void saveOneStuByFastJson() {
        Student student = new Student();
        student.setAge(12);
        student.setName("张三");
        Student student2 = new Student();
        student2.setAge(13);
        student2.setName("李四");
        student.setFriend(student2);
//        student2.setFriend(student);
        redisUtils.set("stu2",student);
    }


    @Test
    public void getOneStu() {
        Student stu = (Student) redisUtils.get("stu2");
        stu.setAge(123);
        System.out.println(stu);
        System.out.println(stu.getFriend());
    }

    @Test
    public void getOneStuByFastJson() {
    }


}

/**
 * Description:
 * <p>  redis的java bean转化测试用例
 *
 * @author liuxingxing
 * @date 2020/11/16 20:41
 */
@Data
class Student {

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 好朋友
     */
    private Student friend;

    /**
     * 同班同学
     */
//    private List<Student> stus;


}
