package marchsoft.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * description:继承MybatisPlus的IService，方便进行自定义和扩展
 *
 * @author RenShiWei
 * Date: 2020/12/4 18:56
 **/
public interface IBasicService<T> extends IService<T> {

    /**
     * description:批量新增，一条sql插入所有的sql语句<br>
     * 批量新增，直接传实体的集合对象，底层执行的是类似于如下的sql:<br>
     * insert into user(id, name, age) values (1, "a", 17), (2,"b", 18),(3,"c",19);
     *
     * @param batchList 新增数据的集合对象
     * @return 是否执行成功
     * @author RenShiWei
     * Date: 2020/12/5 11:46
     */
    boolean saveAllBatch(Collection<T> batchList);

}

