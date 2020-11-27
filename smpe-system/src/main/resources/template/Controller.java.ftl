package ${package.Controller};

import lombok.RequiredArgsConstructor;
<#--标注controller，判断是否为rset风格-->
<#if restControllerStyle>
    import org.springframework.web.bind.annotation.RestController;
<#else>
    import org.springframework.stereotype.Controller;
</#if>
<#--如果继承有公共controller，导入-->
<#if superControllerClassPackage??>
    import ${superControllerClassPackage};
</#if>
<#--请求类型注解-->
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
<#--swagger相关注解-->
<#if swagger2>
    import io.swagger.annotations.Api;
    import io.swagger.annotations.ApiImplicitParam;
    import io.swagger.annotations.ApiImplicitParams;
    import io.swagger.annotations.ApiOperation;
</#if>
<#--日志-->
<#if entityLombokModel>
    import lombok.extern.slf4j.Slf4j;
</#if>
<#--事务-->
import org.springframework.transaction.annotation.Transactional;
<#--实体类和service类-->
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
<#--自定义的导包——！！！需要根据实际情况修改-->
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;

import java.util.List;

/**
* <p>
    * ${table.comment} 前端控制器
    * </p>
* @author ${author}
* @since ${date}
*/
@RequiredArgsConstructor
<#if restControllerStyle>
    @RestController
<#else>
    @Controller
</#if>
<#--日志注解，采用Lombok的快捷日志-->
<#if entityLombokModel>
    @Slf4j
</#if>
<#--swagger注解-->
<#if swagger2>
    @Api(tags = "${table.comment}模块")
</#if>
@RequestMapping("/api/${table.entityPath}")
<#if superControllerClassPackage??>
    public class ${table.controllerName} extends ${superControllerClass} {
<#else>
    public class ${table.controllerName} {
</#if>

private final ${table.serviceName} ${table.entityPath}Service;

}
