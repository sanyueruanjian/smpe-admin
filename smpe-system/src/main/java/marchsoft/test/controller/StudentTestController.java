package marchsoft.test.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.test.service.IStudentTestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 学生表（主要用于测试） 前端控制器
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-04
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "学生表（主要用于测试）模块")
@RequestMapping("/api/studentTest")
public class StudentTestController {
    private final IStudentTestService studentTestService;

}
