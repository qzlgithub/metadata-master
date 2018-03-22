package com.mingdong.bop.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class Param
{
    @Value("${custom.tempFilePath}")
    private String tempFilePath;

    @Value("${custom.saveFilePath}")
    private String saveFilePath;

    @Value("${custom.fileNginxUrl}")
    private String fileNginxUrl;

    public String getFileNginxUrl()
    {
        return fileNginxUrl;
    }

    public String getTempFilePath()
    {
        return tempFilePath;
    }

    public String getSaveFilePath()
    {
        return saveFilePath;
    }
}
