package marchsoft.utils;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.Query;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * description:mybatis查询辅助工具（暂时禁用）
 *
 * @author RenShiWei
 * Date: 2020/11/19 9:20
 */
@Deprecated
@Slf4j
public class QueryHelp {

    //    public static ThreadLocal<QueryWrapper> queryWrapperThreadLocal = new ThreadLocal<>();

    protected static Map<String, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>(16);
    protected static Map<String, SmpeField> COLUMN_CACHE = new ConcurrentHashMap<>(16);

    /**
     * description:Mybatis Plus QueryWrapper查询构建
     *
     * @param criteria 参数
     * @param clazz    实体类
     * @return QueryWrapper
     * @author RenShiWei
     * Date: 2020/11/20 16:14
     */
    public static <K> QueryWrapper<K> getQueryWrapper(Object criteria, Class<K> clazz) {
        //创建wrapper
        QueryWrapper<K> queryWrapper = Wrappers.query();
        //获得所有参数的属性名
        final List<Field> allFields = getAllFields(criteria.getClass());
        //构建wrapper的各种条件
        allFields.forEach(field -> buildQuery(criteria, field, clazz, queryWrapper));
        return queryWrapper;
    }

    /**
     * description: QueryWrapper条件构建
     *
     * @param criteria     参数
     * @param field        属性
     * @param clazz        实体类
     * @param queryWrapper /
     * @author RenShiWei
     * Date: 2020/11/20 19:02
     */
    private static <K> void buildQuery(Object criteria, Field field, Class<K> clazz, QueryWrapper<K> queryWrapper) {
        //获得标记@Query的注解
        final Query query = field.getAnnotation(Query.class);
        Object value = null;
        //获取数据库表信息
        final TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        //设置反射是否开启对私有属性的访问
        boolean accessible = field.isAccessible();
        String attributeName = null;
        try {
            // 设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            value = field.get(criteria);
            if (Objects.isNull(value)) {
                return;
            }
            attributeName = getTableColumnFromField(tableInfo, clazz.getName(), field.getName());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            return;
        } finally {
            //结束，关闭对私有属性的访问权限
            field.setAccessible(accessible);
        }
        if (Objects.nonNull(query)) {
            String propName = query.propName();
            String blurry = query.blurry();
            attributeName = isBlank(propName) ? attributeName : propName;
            if (StringUtils.isNotBlank(blurry)) {
                String[] blurrys = blurry.split(",");
                for (String item : blurrys) {
                    queryWrapper.like(item, value);
                }
            }

            List<Object> between = null;
            switch (query.type()) {
                case EQUAL:
                    break;
                case GREATER_THAN:
                    queryWrapper.ge(attributeName, value);
                    break;
                case LESS_THAN:
                    queryWrapper.le(attributeName, value);
                    break;
                case LESS_THAN_NQ:
                    queryWrapper.lt(attributeName, value);
                    break;
                case INNER_LIKE:
                    queryWrapper.like(attributeName, value);
                    break;
                case LEFT_LIKE:
                    queryWrapper.likeLeft(attributeName, value);
                    break;
                case RIGHT_LIKE:
                    queryWrapper.likeRight(attributeName, value);
                    break;
                case IN:
                    between = new ArrayList<>((List<Object>) value);
                    queryWrapper.in(attributeName, between.toArray());
                    break;
                case NOT_EQUAL:
                    queryWrapper.ne(attributeName, value);
                    break;
                case NOT_NULL:
                    queryWrapper.isNotNull(attributeName);
                    break;
                case IS_NULL:
                    queryWrapper.isNull(attributeName);
                    break;
                case BETWEEN:
                    between = new ArrayList<>((List<Object>) value);
                    queryWrapper.in(attributeName, between.toArray());
                    break;
                default:
                    break;
            }
        } else {
            queryWrapper.eq(field.getName(), value);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            //判断是否为空白字符
            if (! Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 依据Mybatis Plus 获取 Database 真实Column 字段
     *
     * @param tableInfo 数据库表信息
     * @param field     实体类属性名称
     * @return 数据库字段名
     */
    public static String getTableColumnFromField(TableInfo tableInfo, String className, String field) {
        String columnName = null;
        final String key = className + "_" + field;
        if (FIELD_CACHE.containsKey(key)) {
            final SmpeField smpeField = COLUMN_CACHE.get(field);
            if (smpeField.isStatus()) {
                columnName = smpeField.getColumn();
            }
        } else {
            AtomicReference<String> tableColumnName = null;
            for (TableFieldInfo item : tableInfo.getFieldList()) {
                if (item.getField().getName().equals(field)) {
                    tableColumnName = new AtomicReference<>();
                    tableColumnName.set(item.getColumn());
                    break;
                }
            }
            if (Objects.nonNull(tableColumnName)) {
                columnName = tableColumnName.get();
                COLUMN_CACHE.put(key, new SmpeField(columnName, true));
            } else {
                COLUMN_CACHE.put(key, new SmpeField(columnName, false));
            }
        }
        return columnName;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        //需要过滤无关字段（暂时无用）
        List<Field> fields = null;
        if (clazz != null) {
            if (FIELD_CACHE.containsKey(clazz.getName())) {
                fields = FIELD_CACHE.get(clazz.getName());
            } else {
                fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
                final List<Field> allFields = getAllFields(clazz.getSuperclass());
                if (CollUtil.isNotEmpty(allFields)) {
                    fields.addAll(allFields);
                }
                FIELD_CACHE.put(clazz.getName(), fields);
            }
        }
        return fields;
    }
}

@Deprecated
@Data
@AllArgsConstructor
class SmpeField {

    private String column;
    private boolean status;

}
