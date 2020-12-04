package marchsoft.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
     * description:查询一条数据，默认添加"LIMIT 1"<p>
     * wrapper无需再进行进行设置：wrapper.last("LIMIT 1");
     *
     * @param wrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/4 18:58
     */
    @Override
    public T selectFirst(Wrapper<T> wrapper) {
        return this.getOne(wrapper);
    }

}

