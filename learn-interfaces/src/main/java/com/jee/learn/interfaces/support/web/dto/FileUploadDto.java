package com.jee.learn.interfaces.support.web.dto;

import lombok.Data;

/**
 * 文件上传通用返回值
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月14日 上午9:54:03 ccp 新建
 */
@Data
public class FileUploadDto {

    private String eTag; // 文件MD5
    private String location; // 文件相对存放目录
    private String url;// 文件访问路径

}
