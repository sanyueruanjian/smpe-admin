package marchsoft.modules.quartz.entity.dto;


import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author lixiangxiang
 * @date 2021/1/14 8:54
 */
@Data
public class JobQueryDto {

    private String jobName;

    private Boolean isSuccess;

    private List<Timestamp> createTime;

}
