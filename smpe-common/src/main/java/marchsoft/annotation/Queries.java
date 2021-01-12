package marchsoft.annotation;

import java.lang.annotation.*;

/**
 * 多个关联查询注解，用于mapper层查询方法上
 * 使用方法类比 @Results ，里面套多个 @Query
 *  Queries({
 *        @ Query(),@Query(),...
 *  })
 *
 * @author Wangmingcan
 * Date: 2021/01/12 09:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Queries {

    Query[] value() default {};

}
