package com.jee.learn.manager.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.dto.FileUploadDto;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.base.Platforms;
import com.jee.learn.manager.util.idgen.IdGenerate;
import com.jee.learn.manager.util.io.FilePathUtil;
import com.jee.learn.manager.util.io.FileUtil;

@Component
public class FileUploadService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemConfig systemConfig;

    /**
     * 接收并保存文件
     * 
     * @param file
     * @param relativeDir
     * @return
     * @throws IOException
     */
    public FileUploadDto writeToFile(MultipartFile file, String relativeDir) throws IOException {

        String fileName = IdGenerate.numid() + Constants.PERIOD + FileUtil.getFileExtension(file.getOriginalFilename());
        String relativePath = systemConfig.getFileRelativePath() + relativeDir
                + Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR + fileName;

        // 构建存储路径
        String diskPath = pathToDiskPath(relativePath);
        logger.debug("文件保存路径={}", diskPath);

        // 写入本地文件
        FileUtil.createDir(FilePathUtil.getParentPath(diskPath));// 通过获取上层目录来创建所需目录
        file.transferTo(Paths.get(diskPath).toFile());

        // 构建访问路径
        String vistPath = pathToVistPath(relativePath);
        logger.debug("文件访问路径={}", vistPath);

        FileUploadDto fileUploadDto = new FileUploadDto(file.getOriginalFilename(), fileName, vistPath);
        fileUploadDto.setDiskPath(diskPath);
        return fileUploadDto;
    }

    /**
     * 文件访问路径转绝对路径
     * 
     * @param vistPath
     * @return
     */
    public String vistPathToDiskPath(String vistPath) {
        String diskPath = vistPath;
        if (StringUtils.startsWith(diskPath, systemConfig.getFileContentPath())) {
            diskPath = diskPath.replace(systemConfig.getFileContentPath(),
                    FilePathUtil.normalizePath(systemConfig.getFileUploadPath()));
        }
        return diskPath;
    }

    /**
     * 已存储的文件路径转绝对路径
     * 
     * @param path
     * @return
     */
    public String pathToDiskPath(String path) {
        return FilePathUtil.normalizePath(systemConfig.getFileUploadPath() + path);
    }

    /**
     * 已存储的文件路径转访问路径
     * 
     * @param path
     * @return
     */
    public String pathToVistPath(String path) {
        return systemConfig.getFileContentPath() + path;
    }

    /**
     * 文件删除
     * 
     * @param filePath
     * @throws IOException
     */
    public void deleteFile(String filePath) throws IOException {
        // 文件路径校验, 如果文件存在则删除
        Path path = Paths.get(filePath);
        if (FileUtil.isFileExists(path)) {
            // 删除文件
            FileUtil.deleteFile(path);
        }
    }

}
