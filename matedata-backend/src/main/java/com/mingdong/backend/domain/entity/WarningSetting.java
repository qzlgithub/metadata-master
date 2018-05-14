package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningSetting
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String code;
    private String content;
    private Integer type;
    private Integer send;
    private Integer play;
    private String fileName;
    private String filePath;
    private Integer generalLimit;
    private Integer severityLimit;
    private Integer warningLimit;
    private Integer enabled;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getSend()
    {
        return send;
    }

    public void setSend(Integer send)
    {
        this.send = send;
    }

    public Integer getPlay()
    {
        return play;
    }

    public void setPlay(Integer play)
    {
        this.play = play;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public Integer getGeneralLimit()
    {
        return generalLimit;
    }

    public void setGeneralLimit(Integer generalLimit)
    {
        this.generalLimit = generalLimit;
    }

    public Integer getSeverityLimit()
    {
        return severityLimit;
    }

    public void setSeverityLimit(Integer severityLimit)
    {
        this.severityLimit = severityLimit;
    }

    public Integer getWarningLimit()
    {
        return warningLimit;
    }

    public void setWarningLimit(Integer warningLimit)
    {
        this.warningLimit = warningLimit;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }
}
