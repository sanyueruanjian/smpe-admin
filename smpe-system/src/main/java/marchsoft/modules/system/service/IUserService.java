package marchsoft.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.base.IBasicService;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.UserBO;
import marchsoft.modules.system.entity.dto.UserDTO;
import marchsoft.modules.system.entity.dto.UserInsertOrUpdateDTO;
import marchsoft.modules.system.entity.dto.UserPersonalInfoDTO;
import marchsoft.modules.system.entity.dto.UserQueryCriteria;
import marchsoft.modules.system.entity.vo.UserPassVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
public interface IUserService extends IBasicService<User> {

    /**
     * description:根据用户名查用户id
     * 单表不使用mp，为了走mybatis二级缓存
     *
     * @param username 用户名
     * @return 用户id
     * @author RenShiWei
     * Date: 2020/11/25 16:19
     */
    Long findUserIdByName(String username);

    /**
     * description:根据用户id查询详细信息（用户信息，部门信息，角色信息，权限信息等）
     *
     * @param id 用户id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/21 21:27
     */
    UserBO findUserDetailById(Long id);

    /**
     * description: 根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）
     *
     * @param criteria 条件
     * @return 用户集合
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    List<UserDTO> queryUserDetailsList(UserQueryCriteria criteria);


    /**
     * description: 根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）(可分页)
     *
     * @param criteria 条件
     * @param page     /
     * @return 用户集合
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    IPage<UserDTO> queryUserDetailsList(UserQueryCriteria criteria, IPage<User> page);


    /**
     * description: 导出数据
     *
     * @param userDTOList 待导出的数据
     * @param response    /
     * @throws IOException IO异常
     * @author Wangmingcan
     * @date 2020-08-27 09:25
     */
    void download(List<UserDTO> userDTOList, HttpServletResponse response) throws IOException;

    /**
     * description:新增用户(维护岗位、角色表)
     *
     * @param userInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/24 20:52
     */
    void insertUserWithDetail(UserInsertOrUpdateDTO userInsertOrUpdateDTO);

    /**
     * description:修改用户(维护岗位、角色表)
     *
     * @param userInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/24 21:35
     */
    void updateUserWithDetail(UserInsertOrUpdateDTO userInsertOrUpdateDTO);

    /**
     * description:修改头像
     *
     * @param file 头像文件
     * @return 图片路径
     * @author RenShiWei
     * Date: 2020/11/25 15:35
     */
    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * @author Wangmingcan
     * @date 2021-02-03 11:09
     * @param userPersonalInfoDTO 个人信息参数列表
     * @return void
     * @description 修改用户：个人中心
     */
    void updateUserPersonalInfo(UserPersonalInfoDTO userPersonalInfoDTO);

    /**
     * @author Wangmingcan
     * @date 2021-02-03 11:14
     * @param ids 用户id集合
     * @return void
     * @description  删除用户
     */
    void delete(Set<Long> ids);

    /**
     * @author Wangmingcan
     * @date 2021-02-03 11:18
     * @param passVo 密码
     * @return void
     * @description 修改密码
     */
    void updatePass(UserPassVo passVo);

}
