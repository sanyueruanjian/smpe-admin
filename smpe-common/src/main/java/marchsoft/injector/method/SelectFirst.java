package marchsoft.injector.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import marchsoft.enums.BaseMapperMethodEnum;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * description:继承com.baomidou.mybatisplus.core.injector.methods.SelectOne，重写其方法
 *
 * @author RenShiWei
 * Date: 2020/12/4 16:53
 **/
public class SelectFirst extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        BaseMapperMethodEnum baseMapperMethodEnum = BaseMapperMethodEnum.SELECT_FIRST;
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration,
                String.format(baseMapperMethodEnum.getSql(), this.sqlFirst(),
                        this.sqlSelectColumns(tableInfo, true),
                        tableInfo.getTableName(), this.sqlWhereEntityWrapper(true, tableInfo), this.sqlComment()),
                modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, baseMapperMethodEnum.getMethod(), sqlSource,
                tableInfo);
    }

}

