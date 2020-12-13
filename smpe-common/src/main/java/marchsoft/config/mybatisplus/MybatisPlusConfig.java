package marchsoft.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import marchsoft.injector.MyLogicSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus配置类
 *
 * @author RenShiWei
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * description:配置分页相关
     *
     * @author RenShiWei
     * Date: 2020/7/13 16:46
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    /**
     * mybatis 自定义拦截器
     */
//    @Bean
//    public CachingInterceptor getInterceptor() {
//        return new CachingInterceptor();
//    }

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public MyLogicSqlInjector myLogicSqlInjector() {
        return new MyLogicSqlInjector();
    }

}
