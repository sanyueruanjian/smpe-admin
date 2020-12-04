package marchsoft.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class BasicModel<T extends Model<?>> extends Model<T> implements Serializable {

    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
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

