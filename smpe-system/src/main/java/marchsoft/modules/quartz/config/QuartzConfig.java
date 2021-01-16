package marchsoft.modules.quartz.config;

import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author lixiangxiang
 * @date 2021/1/14 8:58
 * @description 定时任务配置
 */
@Configuration
public class QuartzConfig {

    /**
     * 解决job中注入 Spring Bean 为null的问题
     */
    @Component("quartzJobFactory")
    public static class QuartzJobFactory extends AdaptableJobFactory {

        private final AutowireCapableBeanFactory capableBeanFactory;


        public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
            this.capableBeanFactory = capableBeanFactory;
        }

        /**
         * description: 创建job实例
         *
         * @author: lixiangxiang
         * @param bundle /
         * @return java.lang.Object
         * @date 2021/1/14 11:42
         */
        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

            //调用父类的方法
            Object jobInstance = super.createJobInstance(bundle);
            //进行注入
            capableBeanFactory.autowireBean(jobInstance);
            return jobInstance;
        }
        
    }

    /**
     * description: 注入scheduler到spring
     *
     * @author: lixiangxiang
     * @param quartzJobFactory /
     * @return org.quartz.Scheduler
     * @date 2021/1/14 11:29
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(QuartzJobFactory quartzJobFactory) throws Exception {
        SchedulerFactoryBean factoryBean=new SchedulerFactoryBean();
        factoryBean.setJobFactory(quartzJobFactory);
        factoryBean.afterPropertiesSet();
        Scheduler scheduler=factoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }

}
