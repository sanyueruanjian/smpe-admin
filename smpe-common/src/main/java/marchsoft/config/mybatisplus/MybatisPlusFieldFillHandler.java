package marchsoft.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import marchsoft.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * description:mybatis plus 自定义自动填充策略
 *
 * @author RenShiWei
 * Date: 2020/7/13 16:13
 **/
@Component
@Slf4j
public class MybatisPlusFieldFillHandler implements MetaObjectHandler {

    /**
     * description:新增数据，填充创建时间和更新时间
     *
     * @author RenShiWei
     * Date: 2020/7/13 16:14
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Fill field createAT and updateAT by insert...");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtils.getCurrentUsername());
        this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getCurrentUsername());
    }

    /**
     * description:更新数据，填充更新时间
     *
     * @author RenShiWei
     * Date: 2020/7/13 16:14
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Fill field updateAT by update...");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateBy", String.class, SecurityUtils.getCurrentUsername());
    }
}
