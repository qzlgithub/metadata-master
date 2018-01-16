package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.Date;

public class MessageDTO implements Serializable
{
    private Date addAt;
    private Integer type;
    private String typeName;
    private String content;

    public Date getAddAt()
    {
        return addAt;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
