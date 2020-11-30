package marchsoft.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.modules.system.entity.dto.JobDTO;
import marchsoft.modules.system.service.IJobService;
import marchsoft.modules.system.service.mapstruct.JobMapStruct;
import marchsoft.test.entity.QueryLocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/23 10:35
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Api(tags = "测试")
public class TestController {

    @Autowired
    private IJobService jobService;

    @Autowired
    private JobMapStruct jobMapStruct;

    @ApiOperation("测试后端直接接受时间戳")
    @GetMapping("/timestamp")
    @AnonymousAccess
    public ResponseEntity<Object> testLocalDateTime(@RequestParam LocalDateTime h) throws Exception {
        System.out.println(h);
        return new ResponseEntity<>(h, HttpStatus.OK);
    }

    @ApiOperation("测试后端接收JavaBean的时间戳")
    @GetMapping("/timestamp2")
    @AnonymousAccess
    public ResponseEntity<Object> testLocalDateTime2(QueryLocalDateTime queryLocalDateTime) throws Exception {
        System.out.println(queryLocalDateTime);
        return new ResponseEntity<>(queryLocalDateTime, HttpStatus.OK);
    }

    @ApiOperation("测试后端返回JavaBean的LocalDatetime否为时间戳")
    @GetMapping("/timeJobLocalDateTime")
    @AnonymousAccess
    public ResponseEntity<Object> testReturnLocalDatetime() throws Exception {
        return new ResponseEntity<>(jobService.list(), HttpStatus.OK);
    }

    @ApiOperation("测试后端使用MapStruct返回JavaBeanDTO的LocalDatetime否为时间戳")
    @GetMapping("/timeJobLocalDateTime2")
    @AnonymousAccess
    public ResponseEntity<Object> testReturnLocalDatetime2() throws Exception {
        List<JobDTO> jobDTOList = jobMapStruct.toDto(jobService.list());
        return new ResponseEntity<>(jobDTOList, HttpStatus.OK);
    }

}

