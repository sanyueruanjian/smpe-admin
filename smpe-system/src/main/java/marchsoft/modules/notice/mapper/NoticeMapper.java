package marchsoft.modules.notice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.annotation.Queries;
import marchsoft.annotation.Query;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.base.BasicMapper;
import marchsoft.modules.notice.entity.bo.NoticeBO;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
* <p>
*  Mapper 接口
* </p>
*
* @author wangmingcan
* @since 2021-02-19
*/
@Mapper
public interface NoticeMapper extends BasicMapper<Notice> {

    @Select("SELECT " +
            " id,template_id,target_id,user_id,type,is_read,create_by,update_by,create_time,update_time" +
            " FROM notice ${ew.customSqlSegment}")
    @Results({
            @Result(column = "template_id", property = "templateId"),
            @Result(column = "target_id", property = "targetId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "is_read", property = "read")
    })
    @Queries({
            @Query(column = "template_id", property = "noticeTemplate", select = "marchsoft.modules.notice.mapper.NoticeTemplateMapper.selectById"),
            @Query(column = "target_id", property = "noticeTarget", select = "marchsoft.modules.notice.mapper.NoticeTargetMapper.selectById")
    })
    IPage<NoticeBO> queryPage(IPage<Notice> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<Notice> queryWrapper);

    @Select("<script>" +
            "SELECT count(1) FROM notice n WHERE n.is_deleted=0 AND n.template_id" +
            " " +
            "IN " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByNoticeIds(@Param("ids") Set<Long> ids);

    @Select("<script>" +
            "SELECT count(1) FROM notice n WHERE n.is_deleted=0 AND n.target_id" +
            " " +
            "IN " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByNoticeTargetIds(@Param("ids") Set<Long> ids);

}
