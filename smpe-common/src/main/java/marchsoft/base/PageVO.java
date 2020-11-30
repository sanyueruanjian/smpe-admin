package marchsoft.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import marchsoft.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：分页参数
 *
 * @author jiaoqianjin
 * Date: 2020/11/22 9:33
 **/
@Data
@ApiModel(value = "分页", description = "orders传参实例：[{'column': 'create_time','asc': false},{'column': 'name','asc': " +
        "true}]")
public class PageVO {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer current;

    /**
     * 当前页显示数据的条数
     */
    @ApiModelProperty(value = "当前页显示数据的条数")
    private Integer size;

    /**
     * 获取排序信息，排序的字段和正反序
     */
    @ApiModelProperty(value = "排序方式。(默认【创建时间倒序】:[{'column': 'create_time','asc': false}])。",
            notes = "例子：[{'column': 'create_time','asc': false},{'column':'name','asc': true}]"
    )
    private String orders;

    /**
     * 当前页默认值为1
     */
    public Integer getCurrent() {
        return current = (current == null || current <= 0) ? 1 : current;
    }

    /**
     * 每页大小默认为10
     */
    public Integer getSize() {
        return size = (size == null || size == 0) ? 10 : size;
    }

    /**
     * description:将orders（json数组字符串）转为List
     *
     * @return /
     * @author RenShiWei
     * Date: 2020/11/22 16:43
     */
    public List<OrderItem> getOrderList() {
        List<OrderItem> orderItemList = new ArrayList<>();
        if (StrUtil.isBlank(getOrders())) {
            orderItemList.add(OrderItem.desc("create_time"));
        } else {
            try {
                orderItemList = JSONArray.parseArray(orders, OrderItem.class);
            } catch (Exception e) {
                throw new BadRequestException("分页排序参数orders不合法，请传正确的参数格式——['column':'','asc':'true/false']");
            }
        }
        return orderItemList;
    }

    /**
     * description:根据pageVO构建分页查询IPage
     *
     * @return IPage查询条件
     * @author RenShiWei
     * Date: 2020/11/22 17:19
     */
    public <K> IPage<K> buildPage() {
        Page<K> page = new Page<>(getCurrent(), getSize());
        page.addOrder(getOrderList());
        return page;
    }


}
