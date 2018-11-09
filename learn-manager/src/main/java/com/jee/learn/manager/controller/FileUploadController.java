package com.jee.learn.manager.controller;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jee.learn.manager.dto.FileUploadDto;
import com.jee.learn.manager.dto.ResponseDto;
import com.jee.learn.manager.security.LogUtil;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.WebConstants;
import com.jee.learn.manager.util.idgen.IdGenerate;

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
    protected LogUtil logUtil;

    /**
     * 在子类指定文件相对目录
     * 
     * @return
     */
    protected abstract String getRelativeDir();

    /** 文件上传 */
    @Async
    @ResponseBody
    @PostMapping(path = "/test/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<FileUploadDto>> fileUpload(HttpServletRequest request, MultipartFile file) {
        ResponseDto<FileUploadDto> responseDto = new ResponseDto<>();
        responseDto.setC(WebConstants.SUCCESS_CODE);

        // 构建存储路径
        String dir = systemConfig.getFileUploadPath() + getRelativeDir();
        String fileName = IdGenerate.numid();
        String path = dir + Constants.SPRIT + fileName;

        // 写入本地文件
        File f = new File(path);
        try {
            file.transferTo(f);
        } catch (Exception e) {
            responseDto.setC(WebConstants.BUSINESS_ERROR_CODE);
            responseDto.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
            logUtil.saveLog(request, null, e, "文件上传出错");// TODO 日志没有保存
            logger.info("", e);
        }

        FileUploadDto fileUploadDto = new FileUploadDto(file.getName(), fileName, path);
        responseDto.setD(fileUploadDto);

        return CompletableFuture.completedFuture(responseDto);
    }

}
