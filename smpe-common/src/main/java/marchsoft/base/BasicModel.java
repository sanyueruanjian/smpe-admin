package marchsoft.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.enums.BaseMapperMethodEnum;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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
    private Long createBy;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否已删除")
    @TableField(value = "is_deleted")
    private Boolean deleted;

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
     * description:批量新增，一条sql插入所有的sql语句<br>
     * 批量新增，直接传实体的集合对象，底层执行的是类似于如下的sql:<br>
     * insert into user(id, name, age) values (1, "a", 17), (2,"b", 18),(3,"c",19);
     * 使用Collection<T>其中一个对象执行即可批量插入
     *
     * @param batchList 新增数据的集合对象
     * @return 是否执行成功
     * @author RenShiWei
     * Date: 2020/12/5 11:46
     */
    public boolean insertAllBatch(Collection<T> batchList) {
        SqlSession sqlSession = this.sqlSession();
        boolean var2;
        try {
            var2 = SqlHelper.retBool(sqlSession.insert(this.sqlStatement(BaseMapperMethodEnum.INSERT_ALL_BATCH.getMethod()), batchList));
        } finally {
            this.closeSqlSession(sqlSession);
        }
        return var2;
    }

}

