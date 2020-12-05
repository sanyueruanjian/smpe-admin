package marchsoft.test2.mapper;

import marchsoft.test2.entity.StudentTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
* <p>
* 学生表（主要用于测试） Mapper 接口
* </p>
*
* @author RenShiWei
* @since 2020-12-05
*/
@Component
public interface StudentTestMapper extends BaseMapper<StudentTest> {

}
