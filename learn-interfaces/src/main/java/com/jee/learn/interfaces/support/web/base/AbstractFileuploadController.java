package com.jee.learn.interfaces.support.web.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.jee.learn.interfaces.support.cache.CacheConstants;
import com.jee.learn.interfaces.support.web.FileUploadUtil;
import com.jee.learn.interfaces.support.web.WebConstants;
import com.jee.learn.interfaces.support.web.dto.DParam;
import com.jee.learn.interfaces.support.web.dto.FileUploadDto;
import com.jee.learn.interfaces.support.web.dto.HParam;
import com.jee.learn.interfaces.support.web.dto.RequestParams;
import com.jee.learn.interfaces.support.web.dto.ResponseDto;

/**
 * 文件上传接口处理模板方法类<br/>
 * 根据参数 a 选择不同的上传操作: 1=单文件上传
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月14日 上午9:53:21 ccp 新建
 */
public abstract class AbstractFileuploadController extends AbstractBaseController {

    @Value("${system.fileupload-basedir:/data/file}")
    protected String fileuploadBasedir;

    @Value("${system.file-content:http://127.0.0.1}")
    protected String fileContent;

    /**
     * 接口处理入口
     * 
     * @param params
     * @return
     */
    public CompletableFuture<ResponseDto<FileUploadDto>> execute(RequestParams<DParam> params) {
        return execute(params, null, null);
    }

    /**
     * 接口处理入口<br/>
     * 扩展获取客户端IP, 使用{@link HParam#getIp()}获取
     * 
     * @param params
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<ResponseDto<FileUploadDto>> execute(RequestParams<DParam> params,
            HttpServletRequest request, HttpServletResponse response) {
        logger.debug("文件上传请求参数：{}", params);

        // 参数校验
        ResponseDto<FileUploadDto> dto = checkRequestParams(params);
        if (!WebConstants.SUCCESS_CODE.equals(dto.getC())) {
            return CompletableFuture.completedFuture(dto);
        }
        // 校验用户
        dto = checkToken(params);
        if (!WebConstants.SUCCESS_CODE.equals(dto.getC())) {
            return CompletableFuture.completedFuture(dto);
        }
        // 检查redis缓存
        String redisKey = getRedisKey(params);
        if (StringUtils.isNotBlank(redisKey)) {
            dto = (ResponseDto<FileUploadDto>) redisService.getKeyValue(redisKey);
            if (dto != null) {
                return CompletableFuture.completedFuture(dto);
            }
        }
        // 获取客户端IP
        if (request != null && params != null && params.getH() != null) {
            params.getH().setIp(getIp(request));
        }
        // 业务处理
        dto = handler(params);
        // 写入redis
        if (StringUtils.isNotBlank(redisKey)) {
            redisService.setKeyValue(redisKey, dto, CacheConstants.SIXTY);
        }
        return CompletableFuture.completedFuture(dto);
    }

    /**
     * 第一步参数校验<br/>
     * 默认校验 H, D, T 是否为空<br/>
     * 追加校验 F, file 是否为空
     * 
     * @param params
     * @return
     */
    protected ResponseDto<FileUploadDto> checkRequestParams(RequestParams<DParam> params) {
        boolean b = params.getH() == null || params.getD() == null || StringUtils.isBlank(params.getH().getT());
        if (b || params.getF() == null || params.getF().getFile() == null) {
            return new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE);
        }
        return new ResponseDto<>(WebConstants.SUCCESS_CODE);
    }

    /**
     * 第二步校验token是否合法
     * 
     * @param params
     * @return
     */
    protected ResponseDto<FileUploadDto> checkToken(RequestParams<DParam> params) {
        if (StringUtils.isBlank(params.getH().getT())) {
            return new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE);
        }

        if (getCurrentUser(params) == null) {
            return new ResponseDto<>(WebConstants.RELOGIN_CODE, WebConstants.RELOGIN_MESSAGE);
        }
        return new ResponseDto<>(WebConstants.SUCCESS_CODE);
    }

    /**
     * 获取当前用户
     * 
     * @param params
     * @return
     */
    protected Object getCurrentUser(RequestParams<DParam> params) {
        return new Object();
    }

    /**
     * 第三步具体操作
     * 
     * @param params
     * @return
     */
    protected ResponseDto<FileUploadDto> handler(RequestParams<DParam> params) {
        ResponseDto<FileUploadDto> result = new ResponseDto<FileUploadDto>(WebConstants.SUCCESS_CODE);
        int key = params.getD().getA();
        switch (key) {
        case 1:
            // 单文件直接上传
            result = singleFileUpload(params);
            break;
        default:
            result.setC(WebConstants.PARAMETER_ERROR_CODE);
            result.setE(WebConstants.PARAMETER_ERROR_MESSAGE);
            break;
        }
        return result;
    }

    /**
     * 第四步获取redis缓存key，返回null表示不用缓存
     * 
     * @param params
     * @return
     */
    protected String getRedisKey(RequestParams<DParam> params) {
        return null;
    }

    /**
     * 上传文件存放的路径, 在子类中指定
     * 
     * @return 形如 "/parren/children"
     */
    protected abstract String getRelativeDir();

    /**
     * 文件校验, 在子类实现<br/>
     * 校验规则参考 {@link FileUploadUtil} 的文件校验部分
     * 
     * @param file
     * @return null is pass, empty("") is default msg
     */
    protected abstract String fileCheck(File file);

    /**
     * 单文件直接上传
     * 
     * @param params
     * @return
     */
    private ResponseDto<FileUploadDto> singleFileUpload(RequestParams<DParam> params) {
        ResponseDto<FileUploadDto> result = new ResponseDto<FileUploadDto>(WebConstants.SUCCESS_CODE);
        MultipartFile file = params.getF().getFile();
        String key = params.getF().getKey();

        if (file.isEmpty()) {
            result.setC(WebConstants.INVALID_FILE_CODE);
            result.setE(WebConstants.INVALID_FILE_MESSAGE);
            return result;
        }

        // 重新命名文件
        String fileName = StringUtils.isBlank(key) ? file.getOriginalFilename() : key;
        // 文件存放路径
        String relativeDir = StringUtils.isBlank(getRelativeDir()) ? StringUtils.EMPTY : getRelativeDir();
        String filePath = FileUploadUtil.settingFilePath(fileuploadBasedir, relativeDir) + fileName;
        // 保存文件
        File f = new File(filePath);
        String etag = StringUtils.EMPTY;
        try {
            file.transferTo(f);
            // 文件校验
            String msg = fileCheck(f);
            if (msg != null) {
                FileUploadUtil.deleteFile(f);
                result.setC(WebConstants.FILE_VALIDATE_CODE);
                result.setE(StringUtils.isBlank(msg) ? WebConstants.FILE_VALIDATE_MESSAGE : msg);
                return result;
            }
            etag = DigestUtils.md5Hex(new FileInputStream(f));
        } catch (IllegalStateException | IOException e) {
            logger.info("单文件直接上传 -> 保存文件失败 保存路径={}", filePath, e);
            result.setC(WebConstants.BUSINESS_ERROR_CODE);
            result.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
            return result;
        }

        // 构造返回值
        String location = relativeDir + FileUploadUtil.FILE_SEPARATOR + fileName;
        String url = fileContent + location;
        FileUploadDto d = new FileUploadDto();
        d.setETag(etag);
        d.setLocation(location);
        d.setUrl(url);
        result.setD(d);

        logger.debug("单文件直接上传成功 保存路径={} 访问URL={}", filePath, url);
        return result;

    }

}
