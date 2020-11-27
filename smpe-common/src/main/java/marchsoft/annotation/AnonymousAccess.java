package marchsoft.annotation;

import java.lang.annotation.*;

/**
 * 用于标记匿名访问方法
 *
 * @author jiaoqianjin
 * Date: 2020/11/16 14:35
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {

}
