package marchsoft.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.annotation.Queries;
import marchsoft.annotation.Query;
import marchsoft.base.BasicMapper;
import marchsoft.entity.SysLog;
import marchsoft.entity.bo.SysLogBO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 日志 Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-14
 */
@Component
public interface SysLogMapper extends BasicMapper<SysLog> {

    /**
     * description:根据条件构造返回查询的所有匹配的日志信息集合
     *
     * @param queryWrapper /
     * @return /
     * @author ZhangYuKun
     * Date: 2021/1/15 16:31
     */
    @Select("SELECT id,user_id,description,log_type,method,params,request_time,request_ip," +
            "address,browser,exception_detail,create_by,update_by,create_time,update_time " +
            "FROM sys_log u ${ew.customSqlSegment}")
    @Queries({
            @Query(column = "user_id", property = "username",
                    select = "marchsoft.modules.system.mapper.UserMapper.findUserNameById")
    })
    List<SysLogBO> queryLogDetailsList(@Param(Constants.WRAPPER) LambdaQueryWrapper<SysLog> queryWrapper);


    /**
     * description:根据条件构造返回查询的所有匹配的日志信息集合
     *
     * @param queryWrapper /
     * @param page         /
     * @return /
     * @author ZhangYuKun
     * Date: 2021/1/15 16:52
     */
    @Select("SELECT id,user_id,description,log_type,method,params,request_time,request_ip," +
            "address,browser,create_by,update_by,create_time,update_time " +
            "FROM sys_log u ${ew.customSqlSegment}")
    @Queries({
            @Query(column = "user_id", property = "username",
                    select = "marchsoft.modules.system.mapper.UserMapper.findUserNameById")
    })
    IPage<SysLogBO> queryLogDetailsListPage(@Param(Constants.WRAPPER) LambdaQueryWrapper<SysLog> queryWrapper,
                                            IPage<SysLog> page);

    /**
     * description:根据用户名查用户id
     * 单表不使用mp，为了走mybatis二级缓存
     *
     * @param id /
     * @return /
     * @author ZhangYuKun
     * Date: 2021/1/15 18:47
     */
    @Select("SELECT id,user_id,description,log_type,method,params,request_time,request_ip," +
            "address,browser,exception_detail,create_by,update_by,create_time,update_time " +
            "FROM sys_log u WHERE u.id = #{id}")
    SysLogBO queryErrDetail(Long id);

    /**
     * description:根据日志类型删除日志
     *
     * @param logType /
     * @author ZhangYuKun
     * Date: 2021/1/15 22:23
     */
    @Delete("DELETE FROM sys_log WHERE log_type = #{logType}")
    void deleteByLogType(Integer logType);
}
