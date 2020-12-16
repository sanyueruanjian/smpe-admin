package marchsoft.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.service.ISysLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 日志 前端控制器
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-14
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "日志模块")
@RequestMapping("/api/logs")
public class SysLogController {
    private final ISysLogService logService;

}
