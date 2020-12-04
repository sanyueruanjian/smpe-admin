package marchsoft.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * description:自定义BaseMapper，重写其方法，便于扩展；之后的Mapper继承本接口
 *
 * @author RenShiWei
 * Date: 2020/12/4 10:54
 **/
public interface BasicMapper<T> extends BaseMapper<T> {

    /**
     * description:查询一条数据，默认添加"LIMIT 1"<p>
     * wrapper无需再进行进行设置：wrapper.last("LIMIT 1");(禁止添加)
     *
     * @param wrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/4 18:58
     */
    T selectFirst(@Param("ew") Wrapper<T> wrapper);

}
