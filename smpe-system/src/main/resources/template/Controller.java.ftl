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
<#--swagger相关注解-->
<#if swagger2>
import io.swagger.annotations.Api;
</#if>
<#--日志-->
<#if entityLombokModel>
import lombok.extern.slf4j.Slf4j;
</#if>

<#--实体类和service类-->
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};


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
