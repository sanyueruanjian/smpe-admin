package marchsoft.modules.upload.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.upload.service.FileService;
import marchsoft.response.Result;
import marchsoft.utils.FileUtils;
import marchsoft.utils.SecurityUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * class description：关于文件的Controller类
 *
 * @author RenShiWei
 * Date: 2020/2/5
 **/
@Slf4j
@Api(tags = "文件：文件管理")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @ApiOperation(value = "文件上传（任意类型文件）", notes = " \n author：RenShiWei 2020/11/28")
    @ApiImplicitParam(name = "file", value = "文件")
    @PostMapping(value = "/uploadFile")
    public Result<String> uploadFile(@RequestPart MultipartFile file) {
        return Result.success(fileService.upload(file));
    }

    @ApiOperation("文件上传（图片）")
    @PostMapping(value = "/uploadImage")
    public Result<String> uploadImage(@RequestPart MultipartFile file) {
        if (!FileUtils.isImage(file)) {
            log.error(StrUtil.format("【图片上传失败-文件类型不符合】操作人id：{}", SecurityUtils.getCurrentUserId()));
            throw new BadRequestException(ResultEnum.FILE_TYPE_IMAGE_FAIL);
        }
        return Result.success(fileService.upload(file));
    }

    @ApiOperation(value = "文件获取=>ALL", notes = " \n author：RenShiWei 2020/11/28")
    @ApiImplicitParam(name = "fileName", value = "文件名", paramType = "path")
    @GetMapping(value = "/download/{fileName:.+}", produces = "application/octet-stream;charset=UTF-8")
    @AnonymousAccess
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request,
                                                 HttpServletResponse response) {
        //加载文件资源
        Resource resource = fileService.loadFileAsResource(fileName);

        //设置response参数
        String contentType = null;
        try {
            assert resource != null;
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn(StrUtil.format("【Could not determine file type】操作人id：{}", SecurityUtils.getCurrentUserId()));
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        // todo @RenShiWei 2020/11/28 description:返回类型 ResponseEntity -> Result; 修改设置Header属性的策略，下载文件会失败

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}

