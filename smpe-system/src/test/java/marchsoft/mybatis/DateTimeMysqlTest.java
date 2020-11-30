package marchsoft.mybatis;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import marchsoft.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/22 18:07
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DateTimeMysqlTest {

    @Test
    public void testUpdateTime() {

        System.out.println(System.currentTimeMillis());
        System.out.println("java8时间：" + LocalDateTime.now());
        //获取毫秒数
        long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("时间戳：" + milliSecond);
        System.out.println(LocalDateTimeUtil.of(milliSecond, ZoneOffset.of("+8")));
        long milli = LocalDateTimeUtil.toEpochMilli(LocalDateTimeUtil.of(milliSecond, ZoneOffset.of("+8")));
        System.out.println(milli);
    }

    /**
     * 测试hutool的LocalDateTimeUtil的使用
     */
    @Test
    public void testLocalDateTimeUtil() {
        System.out.println(LocalDateTime.now());
        String format = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER);
        System.out.println(format);
    }

    @Test
    public void testStrUtil() {
        System.out.println(StringUtils.strip("[1,2,3]", "[", "]"));
    }

}

