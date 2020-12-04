package marchsoft.test.service.impl;

import lombok.RequiredArgsConstructor;
import marchsoft.base.BasicServiceImpl;
import marchsoft.test.entity.StudentTest;
import marchsoft.test.mapper.StudentTestMapper;
import marchsoft.test.service.IStudentTestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表（主要用于测试） 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-04
 */
@Service
@RequiredArgsConstructor
public class StudentTestServiceImpl extends BasicServiceImpl<StudentTestMapper, StudentTest> implements IStudentTestService {

    private final StudentTestMapper studentTestMapper;

}

