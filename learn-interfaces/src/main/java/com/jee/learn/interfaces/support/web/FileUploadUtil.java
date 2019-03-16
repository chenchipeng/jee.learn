package com.jee.learn.interfaces.support.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.interfaces.util.io.FileUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传工具包
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月14日 上午11:30:42 ccp 新建
 */
@Slf4j
public class FileUploadUtil {

    public static final String FILE_SEPARATOR = "/";

    //////// 常见文件类型 ////////

    public static final String JPG = "image/jpeg";
    public static final String JPEG = "image/jpeg";
    public static final String PNG = "image/png";
    public static final String GIF = "image/gif";

    public static final String MP3 = "audio/mpeg";
    public static final String MP4 = "video/mp4";

    //////// 文件操作 ////////

    /**
     * 统一配置文件上传后的存放路径
     * 
     * @param baseDir     基础路径, 形如 "/baseDir"
     * @param relativeDir 指定文件夹, 形如 "/relativeDir"
     * @return 形如 "/baseDir/relativeDir/"
     */
    public static final String settingFilePath(String baseDir, String relativeDir) {
        return settingFilePath(baseDir, StringUtils.EMPTY, relativeDir);
    }

    /**
     * 统一配置文件上传后的存放路径
     * 
     * @param baseDir     基础路径, 形如 "/baseDir"
     * @param userTag     用户标识路径, 形如 "/userTag"
     * @param relativeDir 指定文件夹, 形如 "/relativeDir"
     * @return 形如 "/baseDir/userTag/relativeDir/"
     */
    public static final String settingFilePath(String baseDir, String userTag, String relativeDir) {
        StringBuffer sb = new StringBuffer();
        sb.append(baseDir);
        if (StringUtils.isNotBlank(userTag) && !FILE_SEPARATOR.equals(userTag)) {
            sb.append(userTag);
        }
        if (StringUtils.isNotBlank(relativeDir) && !FILE_SEPARATOR.equals(relativeDir)) {
            sb.append(relativeDir);
        }
        // 检测文件夹是否存在
        File f = new File(sb.toString());
        if (!f.exists()) {
            f.mkdirs();
        }
        return sb.append(FILE_SEPARATOR).toString();
    }

    /**
     * 删除指定文件
     * 
     * @param file
     * @return
     */
    public static final boolean deleteFile(File file) {
        try {
            FileUtil.deleteFile(file);
            log.debug("文件 {} 删除成功", file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            log.info("文件删除失败", e);
            return false;
        }
    }

    //////// 文件校验 ///////

    /**
     * 文件类型校验
     * 
     * @param file
     * @param contentType 文件对应的Content-Type类型, 例如 image/jpeg, image/gif,
     *                    image/png
     * @return true is pass
     * @see 文件对应的Content-Type类型 https://www.cnblogs.com/liu-heng/p/7520564.html
     */
    public static final boolean checkFileType(File file, String... contentTypes) {
        if (contentTypes == null || contentTypes.length == 0) {
            return true;
        }
        boolean b = false;
        try {
            String type = Files.probeContentType(file.toPath());
            for (String ct : contentTypes) {
                if (type.equals(ct)) {
                    b = true;
                    break;
                }
            }
            return b;
        } catch (IOException e) {
            log.info("文件类型校验异常", e);
            return false;
        }
    }

    /**
     * 文件大小校验<br/>
     * 使用该校验时, 注意检查配置项: <br/>
     * spring.servlet.multipart.max-file-size=1MB # Max file size. 单文件上传大小<br/>
     * spring.servlet.multipart.max-request-size=10MB # Max request size.
     * 上传请求总大小
     * 
     * @param file
     * @param fileSize 字节(b)
     * @param fnc      [0:不校验,1:等于,2:大于或等于3:小于或等于]
     * @return true is pass
     */
    public static final boolean checkFileSize(File file, long fileSize, int fnc) {
        if (fnc == 0) {
            return true;
        }
        if (file == null) {
            return false;
        }
        long size = file.length();
        boolean b = false;
        switch (fnc) {
        case 1:
            b = size == fileSize;
            break;
        case 2:
            b = size >= fileSize;
            break;
        case 3:
            b = size <= fileSize;
            break;
        default:
            break;
        }
        return b;
    }

    /**
     * 校验上传的图片是否满足宽高要求
     * 
     * @param file
     * @param width
     * @param height
     * @param fnc    [0:不校验,1:等于,2:大于或等于3:小于或等于]
     * @return true is pass
     */
    public static final boolean checkImgPixel(File file, int width, int height, int fnc) {
        if (fnc == 0) {
            return true;
        }
        BufferedImage bufferedImage = getBufferedImage(file);
        if (bufferedImage == null) {
            return false;
        }

        boolean b = false;
        switch (fnc) {
        case 1:
            b = (bufferedImage.getWidth() == width && bufferedImage.getHeight() == height);
            break;
        case 2:
            b = (bufferedImage.getWidth() >= width && bufferedImage.getHeight() >= height);
            break;
        case 3:
            b = (bufferedImage.getWidth() <= width && bufferedImage.getHeight() <= height);
            break;
        default:
            break;
        }
        return b;
    }

    /**
     * 校验上传的图片是否为正方形
     * 
     * @param file
     * @return the true is square img
     */
    public final static boolean isSquareImg(File file) {
        try {
            BufferedImage sourceImg = getBufferedImage(file);
            if (sourceImg == null) {
                return false;
            }
            return (sourceImg.getWidth() == sourceImg.getHeight());

        } catch (Exception e) {
            log.info("校验上传的图片是否为正方形", e);
            return false;
        }
    }

    /**
     * 获取图像的像素数据和颜色数据
     * 
     * @param file
     * @return
     */
    private static final BufferedImage getBufferedImage(File file) {
        BufferedImage bufferedImage = null;
        FileInputStream stream = null;
        Exception lastException = null;
        try {
            stream = new FileInputStream(file);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);

            while (iter.hasNext()) {
                ImageReader reader = null;
                try {
                    reader = (ImageReader) iter.next();
                    ImageReadParam param = reader.getDefaultReadParam();
                    reader.setInput(stream, true, true);
                    Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
                    while (imageTypes.hasNext()) {
                        ImageTypeSpecifier imageTypeSpecifier = imageTypes.next();
                        int bufferedImageType = imageTypeSpecifier.getBufferedImageType();
                        if (bufferedImageType == BufferedImage.TYPE_BYTE_GRAY) {
                            param.setDestinationType(imageTypeSpecifier);
                            break;
                        }
                    }
                    bufferedImage = reader.read(0, param);
                    if (null != bufferedImage)
                        break;
                } catch (Exception e) {
                    lastException = e;
                } finally {
                    if (null != reader)
                        reader.dispose();
                }
            }
            // If you don't have an image at the end of all readers
            if (null == bufferedImage) {
                if (null != lastException) {
                    throw lastException;
                }
            }
        } catch (Exception e) {
            log.info("获取图像的像素数据和颜色数据异常", e);
            return null;
        }
        return bufferedImage;
    }

}
