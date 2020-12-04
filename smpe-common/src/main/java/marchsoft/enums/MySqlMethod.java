package marchsoft.enums;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/12/4 18:19
 */
public enum MySqlMethod {

    SELECT_FIRST("selectFirst", "查询数据的第一条", "<script>%s SELECT %s FROM %s %s %s LIMIT 1\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    private MySqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return this.method;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSql() {
        return this.sql;
    }
}
