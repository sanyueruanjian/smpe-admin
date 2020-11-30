package marchsoft.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具
 *
 * @author RenShiWei
 * @date 2020-11-17
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * description:List集合分页
     *
     * @param current 当前页
     * @param size    当前页条数
     * @return 分页后的list
     * @author RenShiWei
     * Date: 2020/11/22 11:01
     */
    public static <K> List<K> toPageList(int current, int size, List<K> list) {
        int fromIndex = current * size;
        int toIndex = current * size + size;
        if (fromIndex > list.size()) {
            return new ArrayList<>();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * description:List集合转为IPage分页（不用查数据库的分页，或者查全部数据的物理分页)
     *
     * @param pageVO 自定义分页入参实体
     * @param list   待分页集合数据
     * @return 分页后的IPage
     * @author RenShiWei
     * Date: 2020/11/22 11:02
     */
    public static <K> IPage<K> toPage(PageVO pageVO, List<K> list) {
        int current = pageVO.getCurrent();
        int size = pageVO.getSize();
        int fromIndex = current * size;
        int toIndex = current * size + size;

        IPage<K> iPage = new Page<>(current, size);
        //设置总条数
        iPage.setTotal(list.size());
        iPage.setCurrent(pageVO.getCurrent());
        iPage.setSize(pageVO.getSize());
        //计算页数
        int pages = list.size() / size;
        pages = list.size() % size == 0 ? pages : pages + 1;
        iPage.setPages(pages);

        if (fromIndex > list.size()) {
            iPage.setRecords(new ArrayList<K>());
        } else if (toIndex >= list.size()) {
            iPage.setRecords(list.subList(fromIndex, list.size()));
        } else {
            iPage.setRecords(list.subList(fromIndex, toIndex));
        }
        return iPage;
    }

    /**
     * description:根据pageVO构建分页查询IPage
     *
     * @param pageVO 分页查询条件
     * @return IPage查询条件
     * @author RenShiWei
     * Date: 2020/11/22 17:19
     */
    public static <K> IPage<K> buildPage(PageVO pageVO) {
        Page<K> page = new Page<>(pageVO.getCurrent(), pageVO.getSize());
        page.addOrder(pageVO.getOrderList());
        return page;
    }

    /**
     * description:根据分页条件构建分页查询IPage
     *
     * @param current       当前页
     * @param size          当前页条数
     * @param orderItemList 排序规则集合
     * @return IPage查询条件
     * @author RenShiWei
     * Date: 2020/11/22 17:19
     */
    public static <K> IPage<K> buildPage(int current, int size, List<OrderItem> orderItemList) {
        Page<K> page = new Page<>(current, size);
        page.addOrder(orderItemList);
        return page;
    }

    /**
     * description:根据分页条件构建分页查询IPage
     *
     * @param current 当前页
     * @param size    当前页条数
     * @param orders  排序规则集合（json数组字符串）
     * @return IPage查询条件
     * @author RenShiWei
     * Date: 2020/11/22 17:19
     */
    public static <K> IPage<K> buildPage(int current, int size, String orders) {
        Page<K> page = new Page<>(current, size);
        page.addOrder(getOrderList(orders));
        return page;
    }

    /**
     * description:将orders（json数组字符串）转为List<OrderItem>（排序规则集合）
     *
     * @param orders 排序规则集合（json数组字符串）
     * @return /
     * @author RenShiWei
     * Date: 2020/11/22 16:43
     */
    public static List<OrderItem> getOrderList(String orders) {
        List<OrderItem> orderItemList = new ArrayList<>();
        if (StrUtil.isBlank(orders)) {
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
     * description:MapStruct转换时，转换生成IPage，保持IPage中的属性不变
     *
     * @param sourcePage 源IPage
     * @param targetList MapStruct转换后的list
     * @return 转换后的IPage
     * @author RenShiWei
     * Date: 2020/11/24 18:27
     */
    public static <K> IPage<K> toMapStructPage(IPage<?> sourcePage, List<K> targetList) {
        IPage<K> targetPage = new Page<>();
        //属性拷贝
        BeanUtil.copyProperties(sourcePage, targetPage);
        //因为数据类型不一致，重新set分页的数据
        targetPage.setRecords(targetList);
        return targetPage;
    }

}
