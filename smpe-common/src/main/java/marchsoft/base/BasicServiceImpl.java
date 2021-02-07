package marchsoft.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;

/**
 * description:自定义ServiceImpl继承ServiceImpl，便于扩展；之后所有的ServiceImpl继承本类
 *
 * @author RenShiWei
 * Date: 2020/12/4 10:59
 **/
public class BasicServiceImpl<M extends BasicMapper<T>, T> extends ServiceImpl<M, T> implements IBasicService<T> {

    /**
     * description:重写getOne方法，加上"LIMIT 1"，防止出现异常
     * 不用担心存在多个abstractWrapper.last("LIMIT 1")，默认已经处理，不会出现重复
     *
     * @param wrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/4 15:16
     */
    @Override
    public T getOne(Wrapper<T> wrapper) {
        AbstractWrapper<T, SFunction<T, ?>, ?> abstractWrapper = (AbstractWrapper<T, SFunction<T, ?>, ?>) wrapper;
        abstractWrapper.last("LIMIT 1");
        return this.getOne(abstractWrapper, true);
    }

    /**
     * description:批量新增，一条sql插入所有的sql语句<br>
     * 批量新增，直接传实体的集合对象，底层执行的是类似于如下的sql:<br>
     * insert into user(id, name, age) values (1, "a", 17), (2,"b", 18),(3,"c",19);
     *
     * @param batchList 新增数据的集合对象
     * @return 是否执行成功
     * @author RenShiWei
     * Date: 2020/12/5 11:46
     */
    @Override
    public boolean saveAllBatch(Collection<T> batchList) {
        int count = baseMapper.insertAllBatch(batchList);
        return count > 0;
    }

}

