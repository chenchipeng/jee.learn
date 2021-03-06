package com.jee.learn.manager.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileUploadDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orignFileName;
    private String fileName;
    private String path;
    private String diskPath;

    public FileUploadDto() {
        super();
    }

    public FileUploadDto(String orignFileName, String fileName, String path) {
        super();
        this.orignFileName = orignFileName;
        this.fileName = fileName;
        this.path = path;
    }

    public String getOrignFileName() {
        return orignFileName;
    }

    public void setOrignFileName(String orignFileName) {
        this.orignFileName = orignFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonIgnore
    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

}
