package marchsoft.utils;

/**
 * description:关于缓存的Key集合
 *
 * @author RenShiWei
 * Date: 2020/11/30 13:10
 */
public interface CacheKey {

    /**
     * 内置 用户、岗位、应用、菜单、角色 相关key
     */
    String USER_MODIFY_TIME_KEY = "user:modify:time:key:";
    String APP_MODIFY_TIME_KEY = "app:modify:time:key:";
    String JOB_MODIFY_TIME_KEY = "job:modify:time:key:";
    String MENU_MODIFY_TIME_KEY = "menu:modify:time:key:";
    String ROLE_MODIFY_TIME_KEY = "role:modify:time:key:";
    String DEPT_MODIFY_TIME_KEY = "dept:modify:time:key:";

    /**
     * 用户
     */
    String USER_ID = "user::id:";
    /**
     * 数据
     */
    String DATA_USER = "data::user:";
    /**
     * 菜单
     */
    String MENU_ID = "menu::id:";
    String MENU_USER = "menu::user:";
    String MENU_ROLE = "menu::role:";
    /**
     * 角色授权
     */
    String ROLE_AUTH = "role::auth:";
    String ROLE_USER = "role::user:";
    /**
     * 角色信息
     */
    String ROLE_ID = "role::id:";
    /**
     * 部门信息
     */
    String DEPT_ID = "dept::id:";
    String DEPT_ROLE = "dept::role:";
    /**
     * 岗位信息
     */
    String JOB_ID = "job::id:";
    String JOB_USER = "job::user:";
}
