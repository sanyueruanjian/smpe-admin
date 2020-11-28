package marchsoft.modules.system.entity.dto;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * description:用户查询条件类
 *
 * @author RenShiWei
 * Date: 2020/11/24 15:04
 */
@Data
public class UserQueryCriteria implements Serializable {

    private Long id;

    /** 权限（部门）集合 */
    private Set<Long> deptIds = new HashSet<>();

    /** 模糊查询参数（用户名、邮箱、昵称） */
    private String blurry;

    private Boolean enabled;

    /** 预查询部门的id */
    private Long deptId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * description:重写deptIds的getter方法，保证至少有deptId这个参数
     *
     * @author RenShiWei
     * Date: 2020/11/25 17:01
     */
    public Set<Long> getDeptIds() {
        if (ObjectUtil.isNotNull(getDeptId())) {
            deptIds.add(getDeptId());
        }
        return deptIds;
    }
}
