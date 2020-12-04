package marchsoft.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description:公共实体类
 *
 * @author RenShiWei
 * Date: 2020/11/28 19:37
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BasicModel<T extends Model<?>> extends Model<T> implements Serializable {

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /**
     * description:重写父类的selectOne方法，添加"LIMIT 1"的限制，默认查第一条；
     * 防止selectList扫描全表，提高效率
     *
     * @param queryWrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/4 15:44
     */
    @Override
    public T selectOne(Wrapper<T> queryWrapper) {
        AbstractWrapper<T, SFunction<T, ?>, ?> abstractWrapper = (AbstractWrapper<T, SFunction<T, ?>, ?>) queryWrapper;
        abstractWrapper.last("LIMIT 1");
        return super.selectOne(abstractWrapper);
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
    public T selectFirst(Wrapper<T> wrapper) {
        return this.selectOne(wrapper);
    }

}

