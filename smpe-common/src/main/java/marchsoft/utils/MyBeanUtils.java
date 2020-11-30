package marchsoft.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * bean工具
 * 可使用HuTool的BeanUtil进行属性拷贝
 *
 * @author RenShiWei
 */
@Deprecated
public class MyBeanUtils {

    /**
     * description:得到不为空的属性名
     *
     * @param source 源对象
     * @return 不为空的属性名数组
     * @author RenShiWei
     * Date: 2020/7/23 11:41
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * description: 判断参数是否为0
     *
     * @param object 参数
     * @return 是否为0
     * @author RenShiWei
     * Date: 2020/7/23 11:38
     */
    private static boolean isZero(Object object) {
        Class<?> clazz = object.getClass();

        boolean isDigital = clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Short.class;

        if (! isDigital) {
            return false;
        }
        long l = Long.parseLong(object.toString());
        return l == 0;
    }

    /**
     * description:拷贝部位null的属性值
     *
     * @param source 源对象
     * @param target 拷贝后的对象
     * @author RenShiWei
     * Date: 2020/7/23 11:39
     */
    public static void copyNotNullProperties(Object source, Object target) {
        String[] ignoreProperties = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static <T, S> T convert(final S s, Class<T> clz) {
        return s == null ? null : BeanUtil.copyProperties(s, clz);
    }

    public static <T, S> List<T> convertList(List<S> s, Class<T> clz) {
        return s == null ? null : s.stream().map(vs -> BeanUtil.copyProperties(vs, clz)).collect(Collectors.toList());
    }

    public static <T, S> Set<T> convertSet(Set<S> s, Class<T> clz) {
        return s == null ? null : s.stream().map(vs -> BeanUtil.copyProperties(vs, clz)).collect(Collectors.toSet());
    }

}
