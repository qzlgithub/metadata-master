package com.mingdong.bop.model;

import com.mingdong.core.constant.RestResult;

import java.io.File;

public class FileResVO
{
    private RestResult result;
    private File file;
    private String fileName;
    private String fileOtherPath;//代理路径
    private String fileRealityPath;//真实存储路径

    public FileResVO()
    {

    }

    public FileResVO(RestResult restResult)
    {
        this.result = restResult;
    }

    public RestResult getResult()
    {
        return result;
    }

    public void setResult(RestResult result)
    {
        this.result = result;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileOtherPath()
    {
        return fileOtherPath;
    }

    public void setFileOtherPath(String fileOtherPath)
    {
        this.fileOtherPath = fileOtherPath;
    }

    public String getFileRealityPath()
    {
        return fileRealityPath;
    }

    public void setFileRealityPath(String fileRealityPath)
    {
        this.fileRealityPath = fileRealityPath;
    }
}
