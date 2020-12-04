package marchsoft.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * description:继承MybatisPlus的IService，方便进行自定义和扩展
 *
 * @author RenShiWei
 * Date: 2020/12/4 18:56
 **/
public interface IBasicService<T> extends IService<T> {

    /**
     * description:查询一条数据，默认添加"LIMIT 1"<p>
     * wrapper无需再进行进行设置：wrapper.last("LIMIT 1");
     *
     * @param wrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/4 18:58
     */
    T selectFirst(Wrapper<T> wrapper);


}

