package marchsoft.enums;

import lombok.Getter;

/**
 * description:自定义MybatisPlus的BaseMapper方法枚举
 *
 * @author RenShiWei
 * Date: 2020/12/4 18:19
 */
@Getter
public enum BaseMapperMethodEnum {

    /** 查询数据的第一条 */
    INSERT_ALL_BATCH("insertAllBatch", "批量新增，一条sql插入所有的内容", "<script>INSERT INTO %s %s VALUES %s\n</script>"),

    ;
    /** 方法名 */
    private final String method;

    /** 方法描述 */
    private final String desc;

    /** 方法对应的sql语句 */
    private final String sql;

    BaseMapperMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

}
