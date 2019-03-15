package com.jee.learn.interfaces.support.web.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传通用参数
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月13日 下午5:39:10 ccp 新建
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadParam extends DParam {

    private String key; // 文件唯一标识, 可用文件名(带后缀)
    
    @NotNull
    private MultipartFile file;
}
