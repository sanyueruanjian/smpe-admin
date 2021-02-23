package marchsoft.modules.notice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.annotation.Queries;
import marchsoft.annotation.Query;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.base.BasicMapper;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeSendDTO;
import marchsoft.modules.system.entity.bo.UserBO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

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
public interface NoticeSendMapper extends BasicMapper<NoticeSend> {

    @Select("SELECT " +
            " id,notice_id,target_id,user_id,type,is_read,create_by,update_by,create_time,update_time" +
            " FROM notice_send ${ew.customSqlSegment}")
    @Results({
            @Result(column = "notice_id", property = "noticeId"),
            @Result(column = "target_id", property = "targetId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "is_read", property = "read")
    })
    @Queries({
            @Query(column = "notice_id", property = "notice", select = "marchsoft.modules.notice.mapper.NoticeMapper.selectById"),
            @Query(column = "target_id", property = "noticeTarget", select = "marchsoft.modules.notice.mapper.NoticeTargetMapper.selectById")
    })
    IPage<NoticeSendBO> queryPage(IPage<NoticeSend> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<NoticeSend> queryWrapper);

    @Select("<script>" +
            "SELECT count(1) FROM notice_send ns WHERE ns.is_deleted=0 AND ns.notice_id" +
            " " +
            "IN " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByNoticeIds(@Param("ids") Set<Long> ids);

    @Select("<script>" +
            "SELECT count(1) FROM notice_send ns WHERE ns.is_deleted=0 AND ns.notice_target_id" +
            " " +
            "IN " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByNoticeTargetIds(@Param("ids") Set<Long> ids);

}
