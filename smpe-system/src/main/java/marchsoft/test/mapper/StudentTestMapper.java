package marchsoft.test.mapper;

import marchsoft.base.BasicMapper;
import marchsoft.test.entity.StudentTest;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 学生表（主要用于测试） Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-04
 */
@Component
public interface StudentTestMapper extends BasicMapper<StudentTest> {

}
