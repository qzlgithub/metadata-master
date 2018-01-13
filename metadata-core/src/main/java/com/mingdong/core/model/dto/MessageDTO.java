package com.mingdong.core.model.dto;

import java.util.Date;

public class MessageDTO
{
    private Date addAt;
    private Integer type;
    private String content;

    public Date getAddAt()
    {
        return addAt;
    }

    public void setAddAt(Date addAt)
    {
        this.addAt = addAt;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
