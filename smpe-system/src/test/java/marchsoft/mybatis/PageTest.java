package marchsoft.mybatis;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import marchsoft.base.PageVO;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * description:测试分页处理
 *
 * @author RenShiWei
 * Date: 2020/11/22 16:44
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PageTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testPageVO() {
        String orders = "[\n" +
                "  {\n" +
                "    'column': 'create_time',\n" +
                "    'asc': false\n" +
                "  },\n" +
                "  {\n" +
                "    'column': 'name',\n" +
                "    'asc': true\n" +
                "  }\n" +
                "]";
        List<OrderItem> orderItemList = JSONArray.parseArray(orders, OrderItem.class);
        System.out.println(orderItemList);

    }

    /**
     * description:根据PageVO提供方法快速构建mp分页查询条件IPage
     *
     * @author RenShiWei
     * Date: 2020/11/22 17:33
     */
    @Test
    public void testPageUser() {
        String orders = "[\n" +
                "  {\n" +
                "    'column': 'create_time',\n" +
                "    'asc': false\n" +
                "  },\n" +
                "  {\n" +
                "    'column': 'username',\n" +
                "    'asc': true\n" +
                "  }\n" +
                "]";
        PageVO pageVO = new PageVO();
        pageVO.setCurrent(1);
        pageVO.setSize(1);
        pageVO.setOrders(orders);
        IPage<User> userIPage = pageVO.buildPage();
        System.out.println(userService.page(userIPage));
    }


}

