package marchsoft.modules.system.service;

import marchsoft.modules.system.entity.dto.UserDTO;

import java.util.List;

/**
 * 数据权限服务类
 *
 * @author Wangmingcan
 * @date 2020-08-17
 */
public interface IDataService {

    /**
     * description 获取数据权限
     *
     * @param user userDto
     * @return 数据权限（部门id）
     * @author Wangmingcan
     * @date 2020-08-23 16:16
     */
    List<Long> getDataScopeWithDeptIds(UserDTO user);


}
