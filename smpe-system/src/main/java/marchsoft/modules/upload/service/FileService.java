package marchsoft.modules.upload.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * class description：关于文件的业务处理
 *
 * @author RenShiWei
 * Date: 2020/2/5
 **/
public interface FileService {


    /**
     * 上传文件，并返回文件上传地址
     *
     * @param multipartFile 前台传来的文件
     * @return 文件上传地址
     */
    String upload(MultipartFile multipartFile);

    /**
     * description:加载文件资源
     *
     * @param fileName 文件名
     * @return 文件资源
     * @author RenShiWei
     * Date: 2020/7/21 9:48
     */
    Resource loadFileAsResource(String fileName);


}

