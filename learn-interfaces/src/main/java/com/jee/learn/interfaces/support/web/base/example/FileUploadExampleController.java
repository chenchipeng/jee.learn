package com.jee.learn.interfaces.support.web.base.example;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.support.web.FileUploadUtil;
import com.jee.learn.interfaces.support.web.WebConstants;
import com.jee.learn.interfaces.support.web.base.AbstractFileuploadController;
import com.jee.learn.interfaces.support.web.dto.DParam;
import com.jee.learn.interfaces.support.web.dto.FileUploadDto;
import com.jee.learn.interfaces.support.web.dto.RequestParams;
import com.jee.learn.interfaces.support.web.dto.ResponseDto;

/**
 * example.file.singleFileUpload 文件上传测试
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月13日 下午5:44:30 ccp 新建
 */
@RestController
public class FileUploadExampleController extends AbstractFileuploadController {

    @Override
    protected String getUserTag(RequestParams<DParam> params) {
        return "/usertag";
    }

    @Override
    protected String getRelativeDir() {
        return "/example";
    }

    @Override
    protected String fileCheck(File file) {
        if (!FileUploadUtil.checkFileType(file, FileUploadUtil.JPEG)) {
            return "文件类型校验不通过";
        }
        if (!FileUploadUtil.checkFileSize(file, 1024000, 3)) {
            return "文件大小校验不通过";
        }
        return null;
    }

    @CrossOrigin
    @PostMapping(path = "/rest/example.file.singleFileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Override
    public CompletableFuture<ResponseDto<FileUploadDto>> execute(RequestParams<DParam> params) {
        return super.execute(params);
    }

    @Override
    protected ResponseDto<FileUploadDto> checkRequestParams(RequestParams<DParam> params) {
        ResponseDto<FileUploadDto> r = super.checkRequestParams(params);
        if (r.getC().equals(WebConstants.SUCCESS_CODE)) {
            if (!FILE_UPLOAD_NONCE_STRING.equals(params.getH().getT())) {
                return new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE);
            }
        }
        return r;
    }

}
