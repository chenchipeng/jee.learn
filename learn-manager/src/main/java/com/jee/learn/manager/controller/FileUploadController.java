package com.jee.learn.manager.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jee.learn.manager.dto.FileUploadDto;
import com.jee.learn.manager.dto.RequestDto;
import com.jee.learn.manager.dto.ResponseDto;
import com.jee.learn.manager.service.FileUploadService;
import com.jee.learn.manager.util.WebConstants;
import com.jee.learn.manager.util.io.FileTypeUtil;
import com.jee.learn.manager.util.io.FileUtil;

/**
 * 文件上传 controller <br/>
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月9日 下午1:19:56 ccp 新建
 */
public abstract class FileUploadController extends BaseController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 在子类指定文件相对目录
     * 
     * @return
     */
    protected abstract String getRelativeDir();

    /** 文件上传 */
    @ResponseBody
    @RequiresPermissions("user")
    @PostMapping(path = "${system.authc-path}/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<FileUploadDto>> fileUpload(HttpServletRequest request, MultipartFile file) {
        logger.debug("进入普通文件上传接口 fileUpload");

        if (file == null) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE));
        }
        // 保存文件
        FileUploadDto fileUploadDto;
        try {
            fileUploadDto = fileUploadService.writeToFile(file, getRelativeDir());
        } catch (IOException e) {
            logUtil.saveLog(request, null, e, "文件上传出错");
            logger.info("", e);
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.BUSINESS_ERROR_CODE, WebConstants.BUSINESS_ERROR_MESSAGE));
        }
        // 类型校验
        if (!fileTypeCheck(fileUploadDto.getDiskPath())) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.INVALID_FILE_TYPE_CODE, WebConstants.INVALID_FILE_TYPE_MESSAGE));
        }
        // 清除校验不通过的文件
        if (!extendCheck(Paths.get(fileUploadDto.getDiskPath()))) {
            try {
                FileUtil.deleteFile(Paths.get(fileUploadDto.getDiskPath()));
                logger.debug("文件校验不通过, 清除文件 {}", fileUploadDto.getDiskPath());
            } catch (IOException e) {
                logger.info("", e);
            }
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.FILE_CHECK_ERROR_CODE, WebConstants.FILE_CHECK_ERROR_MESSAGE));
        }

        ResponseDto<FileUploadDto> responseDto = new ResponseDto<>(WebConstants.SUCCESS_CODE);
        responseDto.setD(fileUploadDto);
        return CompletableFuture.completedFuture(responseDto);
    }

    /**
     * 指定文件类型, 多个类型以','分隔, 任意类型返回一个{@link StringUtils#EMPTY}
     * 
     * @return
     */
    protected String getFileType() {
        return StringUtils.EMPTY;
    }

    /**
     * 文件类型检查
     * 
     * @param path
     * @return true 表示验证通过
     */
    protected boolean fileTypeCheck(String path) {
        String types = getFileType();
        if (StringUtils.isBlank(types)) {
            return true;
        }

        boolean isPass = false;
        String fileType = StringUtils.EMPTY;
        Path p = Paths.get(path);
        // 获取文件类型
        try {
            fileType = FileTypeUtil.getFileType(p);
        } catch (IOException e) {
            logger.info("", e);
            return false;
        }
        if (StringUtils.isBlank(fileType)) {
            isPass = true;
        }
        // 校验
        String[] ary = types.split(",");
        for (String type : ary) {
            if (fileType.equals(type)) {
                isPass = true;
                break;
            }
        }
        // 清除文件
        if (!isPass) {
            try {
                FileUtil.deleteFile(p);
                logger.debug("文件类型检查不通过, 清除文件 {}", path);
            } catch (IOException e) {
                logger.info("", e);
            }
        }

        return isPass;
    }

    /**
     * 扩展文件检查
     * 
     * @param path
     * @return
     */
    protected boolean extendCheck(Path path) {
        return true;
    }

    /** 文件删除 */
    @Async
    @ResponseBody
    @RequiresPermissions("user")
    @PostMapping(path = "${system.authc-path}/fileDelete", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<FileUploadDto>> fileDelete(HttpServletRequest request,
            @RequestBody RequestDto<FileUploadDto> params) {
        logger.debug("进入文件删除接口 fileDelete");
        // 参数校验
        if (params == null || params.getD() == null || StringUtils.isBlank(params.getD().getPath())) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE));
        }
        // 通过文件访问路径获取文件绝对路径
        params.getD().setDiskPath(fileUploadService.vistPathToDiskPath(params.getD().getPath()));

        // 删除文件
        try {
            fileUploadService.deleteFile(params.getD().getDiskPath());
        } catch (IOException e) {
            logUtil.saveLog(request, null, e, "文件删除出错");
            logger.info("", e);
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.BUSINESS_ERROR_CODE, WebConstants.BUSINESS_ERROR_MESSAGE));
        }

        return CompletableFuture.completedFuture(new ResponseDto<>(WebConstants.SUCCESS_CODE));
    }

}
