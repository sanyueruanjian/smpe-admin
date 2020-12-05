package marchsoft.injector.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import marchsoft.enums.BaseMapperMethodEnum;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * description:对自定义BaseMapper中的insertAllBatch方法进行sql装配<p>
 * insertAllBatch——批量新增，一条sql插入所有的内容
 *
 * @author RenShiWei
 * Date: 2020/12/5 11:49
 **/
public class InsertAllBatch extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        BaseMapperMethodEnum baseMapperMethodEnum = BaseMapperMethodEnum.INSERT_ALL_BATCH;
        final String fieldSql = prepareFieldSql(tableInfo);
        final String valueSql = prepareValuesSqlForMysqlBatch(tableInfo);
        final String sqlResult = String.format(baseMapperMethodEnum.getSql(), tableInfo.getTableName(), fieldSql,
                valueSql);
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sqlResult, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, baseMapperMethodEnum.getMethod(), sqlSource,
                new NoKeyGenerator(), null, null);
    }

    /**
     * description:生成新增语句VALUES之前的数据库字段
     *
     * @param tableInfo /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/5 15:14
     */
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        //拼接主键列
        fieldSql.append(tableInfo.getKeyColumn()).append(",");
        //拼接其他字段列
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        //去除最后一个","
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        //前后添加"()"
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }

    /**
     * description:生成拼接VALUES后的多个值
     *
     * @param tableInfo /
     * @return /
     * @author RenShiWei
     * Date: 2020/12/5 15:18
     */
    private String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        //构建foreach语句
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" " +
                "close=\")\">");
        valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }

}

