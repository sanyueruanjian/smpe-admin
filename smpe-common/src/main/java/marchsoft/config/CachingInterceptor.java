package marchsoft.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 缓存控制拦截器
 *
 * @author lee
 * @since 2016年3月17日
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class }) })
public class CachingInterceptor implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(CachingInterceptor.class);
    /**
     * 表名关联的命名空间<br/>
     * 一个table下关联的namespace
     */
    private final static Map<String, Map<String, String>> tableLinks = new HashMap<>();

    /**
     * 记录已经解析过的SQL，确保一条SQL只被解析一次，提高效率
     */
    private final static Map<String, String> dealedMap = new HashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        BoundSql boundSql = ms.getBoundSql(args[1]);
        if (!ms.getConfiguration().isCacheEnabled() || ms.getCache() == null) {
            return invocation.proceed();
        }
        if (dealedMap.containsKey(ms.getId()) && dealedMap.get(ms.getId()).equals(boundSql.getSql())) {
            return invocation.proceed();
        }
        dealedMap.put(ms.getId(), boundSql.getSql());

        final String operate = invocation.getMethod().getName();
        final List<String> tableNames = getTableList(boundSql.getSql());
        final String namespace = ms.getCache().getId();
        logger.debug("当前操作SQL中包含的namespace:" + namespace);
        if ("query".equals(operate)) {
            deal(namespace, tableNames);
        } else {
            Configuration configuration = ms.getConfiguration();
            clearCache(tableNames, configuration.getMappedStatements());
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 一个namespace包含多个SQL<br/>
     * 一条SQL语句包含多个table
     *
     * @param namespace  namespace
     * @param tableNames tableNames
     */
    private void deal(final String namespace, final List<String> tableNames) {
        if (tableNames == null || tableNames.size() == 0) {
            return;
        }
        for (String tableName : tableNames) {
            Map<String, String> namespaces = tableLinks.get(tableName);
            if (namespaces == null) {
                namespaces = new HashMap<>();
                namespaces.put(namespace, namespace);
                tableLinks.put(tableName, namespaces);
            } else if (!namespaces.containsKey(namespace)) {
                namespaces.put(namespace, namespace);
            }
        }
    }

    /**
     * 清除缓存
     *
     * @param mappedStatments mappedStatments
     */
    @SuppressWarnings("rawtypes")
    private void clearCache(List<String> tableNames, Collection mappedStatments) {
        if (tableNames == null || tableNames.isEmpty()) {
            return;
        }
        for (String tableName : tableNames) {
            Map<String, String> namespaces = tableLinks.get(tableName);
            if (namespaces == null) {
                continue;
            }
            for (String namespaceNeedClearKey : namespaces.keySet()) {
                for (Object o : mappedStatments) {
                    if (o instanceof MappedStatement) {
                        MappedStatement sta = ((MappedStatement) o);
                        final String namespace = sta.getCache().getId();
                        if (namespaceNeedClearKey.equals(namespace)) {
                            logger.debug("命名空间[{}]的缓存被清除", namespace);
                            sta.getCache().clear();
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 解析SQL中包含的表名
     *
     * @param sql sql
     * @return /
     */
    public List<String> getTableList(final String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = null;
            if (statement instanceof Select) {
                Select selectStatement = (Select) statement;
                tableList = tablesNamesFinder.getTableList(selectStatement);
            } else if (statement instanceof Update) {
                Update updateStatement = (Update) statement;
                tableList = tablesNamesFinder.getTableList(updateStatement);
            } else if (statement instanceof Insert) {
                Insert insertStatement = (Insert) statement;
                tableList = tablesNamesFinder.getTableList(insertStatement);
            } else if (statement instanceof Delete) {
                Delete deleteStatement = (Delete) statement;
                tableList = tablesNamesFinder.getTableList(deleteStatement);
            }
            logger.debug("SQL:{}中包含的表名：" + tableList, sql);
            return tableList;
        } catch (JSQLParserException e) {
            logger.error("解析sql异常:" + sql, e);
        }
        return null;
    }
}
