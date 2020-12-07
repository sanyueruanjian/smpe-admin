package marchsoft.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import marchsoft.injector.method.InsertAllBatch;

import java.util.List;

/**
 * description:装配BaseMapper自定义的方法
 *
 * @author RenShiWei
 * Date: 2020/12/4 17:59
 */
public class MyLogicSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留MP自带方法
     * 可以super.getMethodList() 再add
     *
     * @return /
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertAllBatch());
        return methodList;
    }

}
