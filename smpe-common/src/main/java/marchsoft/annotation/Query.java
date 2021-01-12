package marchsoft.annotation;
import java.lang.annotation.*;

/**
 * 重新实现 @One、@Many注解
 * 关联查询注解，用于mapper层查询方法上
 * 仅需关联查询一个属性时，可以直接使用@Query
 * 如果需要使用多个@Query，请先使用@Queries套在外层（类似 @Results）
 * 参数、作用基本和原@One保持一致，既可以用于一对一也可以一对多
 *
 * @author Wangmingcan
 * Date: 2021/01/12 09:35
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Queries.class)
public @interface Query {

    /** 被关联的列，一般为id，请保证和实体类中属性名称一致，驼峰下划线都可以
     * 如关联的列为dept_id，填deptId和dept_id都可以*/
    String column() default "";

    /** 关联的属性 ，和实体类中需要封装的属性名称保持一致*/
    String property() default "";

    /** 执行的查询方法，建议填写mapper层的全限定方法名 ，方法返回值必须和property类型一致*/
    String select() default "";

}
