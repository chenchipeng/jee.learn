package com.jee.learn.interfaces.controller.api;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.support.web.FileUploadUtil;
import com.jee.learn.interfaces.support.web.base.AbstractFileuploadController;
import com.jee.learn.interfaces.support.web.dto.DParam;
import com.jee.learn.interfaces.support.web.dto.FileUploadDto;
import com.jee.learn.interfaces.support.web.dto.RequestParams;
import com.jee.learn.interfaces.support.web.dto.ResponseDto;

/**
 * 文件上传 测试 controller
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月13日 下午5:44:30 ccp 新建
 */
@RestController
public class FileController extends AbstractFileuploadController {

    @Override
    protected String getRelativeDir() {
        return "/example";
    }

    @CrossOrigin
    @PostMapping(path = "/example/file/upload")
    @Override
    public CompletableFuture<ResponseDto<FileUploadDto>> execute(RequestParams<DParam> params) {
        return super.execute(params);
    }

    @Override
    protected String fileCheck(File file) {
        if (!FileUploadUtil.checkFileType(file, FileUploadUtil.JPEG)) {
            return "文件类型校验不通过";
        }
        if(!FileUploadUtil.checkFileSize(file, 1024, 3)) {
            return "文件大小校验不通过";
        }
        return null;
    }

}
