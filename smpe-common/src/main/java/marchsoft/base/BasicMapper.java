package marchsoft.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description:自定义BaseMapper，重写其方法，便于扩展；之后的Mapper继承本接口
 *
 * @author RenShiWei
 * Date: 2020/12/4 10:54
 **/
public interface BasicMapper<T> extends BaseMapper<T> {

    /**
     * description:批量新增，一条sql插入所有的sql语句
     *
     * @param batchList 新增数据的集合对象
     * @return 新增的执行条数（正常情况下 >= 1）
     * @author RenShiWei
     * Date: 2020/12/5 11:46
     */
    int insertAllBatch(@Param("list") List<T> batchList);

}
