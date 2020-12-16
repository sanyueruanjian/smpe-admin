package marchsoft.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/12/15 11:30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IpCityInfoTest {

    @Test
    public void testIp() {
        System.out.println(StringUtils.getCityInfo("220.248.12.158"));
        System.out.println(StringUtils.getCityInfo("192.168.43.67"));
    }


}

