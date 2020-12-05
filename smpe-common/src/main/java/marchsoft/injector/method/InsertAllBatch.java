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

    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        fieldSql.append(tableInfo.getKeyColumn()).append(",");
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }

    private String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" " +
                "close=\")\">");
        valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }

}

