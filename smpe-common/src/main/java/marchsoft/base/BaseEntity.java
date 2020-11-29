package marchsoft.base;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * description:公共实体类
 *
 * @author RenShiWei
 * Date: 2020/11/28 19:37
 **/
@Getter
@Setter
@ToString
public class BaseEntity<T extends Model<?>> extends Model<T> implements Serializable {

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updatedBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

}

