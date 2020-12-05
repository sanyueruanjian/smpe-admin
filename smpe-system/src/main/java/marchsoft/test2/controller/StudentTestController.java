package marchsoft.test2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.test2.service.IStudentTestService;
import marchsoft.test2.entity.StudentTest;


/**
* <p>
* 学生表（主要用于测试） 前端控制器
* </p>
* @author RenShiWei
* @since 2020-12-05
*/
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "学生表（主要用于测试）模块")
@RequestMapping("/api/studentTest")
public class StudentTestController {
    private final IStudentTestService studentTestService;

}
