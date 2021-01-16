package marchsoft.modules.quartz.service;

import marchsoft.modules.quartz.entity.QuartzJob;

/**
 * @author lixiangxiang
 * @description /
 * @date 2021/1/14 16:25
 */
public interface QuartzJobService {

    /**
     * fetch
     * description: 创建定时任务
     *
     * @author: lixiangxiang
     * @param resources /
     * @return void
     * @date 2021/1/14 16:54
     */
    void create(QuartzJob resources);

    /**
     * fetch
     * description: 执行子任务
     *
     * @author: lixiangxiang
     * @param tasks /
     * @return void
     * @date 2021/1/15 15:55
     */
    void executionSubJob(String[] tasks) throws InterruptedException;

    void updateIsPause(QuartzJob quartzJob);
}
