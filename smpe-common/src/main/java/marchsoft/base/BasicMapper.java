package marchsoft.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * description:自定义BaseMapper，重写其方法，便于扩展；之后的Mapper继承本接口
 *
 * @author RenShiWei
 * Date: 2020/12/4 10:54
 **/
public interface BasicMapper<T> extends BaseMapper<T> {

}
