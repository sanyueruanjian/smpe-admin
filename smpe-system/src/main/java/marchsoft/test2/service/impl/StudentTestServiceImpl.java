package marchsoft.test2.service.impl;

import lombok.RequiredArgsConstructor;
import marchsoft.test2.entity.StudentTest;
import marchsoft.test2.mapper.StudentTestMapper;
import marchsoft.test2.service.IStudentTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* <p>
* 学生表（主要用于测试） 服务实现类
* </p>
*
* @author RenShiWei
* @since 2020-12-05
*/
@Service
@RequiredArgsConstructor
public class StudentTestServiceImpl extends ServiceImpl<StudentTestMapper, StudentTest> implements IStudentTestService {

    private final StudentTestMapper studentTestMapper;

}

