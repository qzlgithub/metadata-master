package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class ArticlesResDTO implements Serializable
{
    private Long id;
    private Integer orderId;
    private Integer type;
    private String title;
    private Integer published;
    private String synopsis;
    private String imagePath;
    private Date publishTime;

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public String getSynopsis()
    {
        return synopsis;
    }

    public void setSynopsis(String synopsis)
    {
        this.synopsis = synopsis;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Integer orderId)
    {
        this.orderId = orderId;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getPublished()
    {
        return published;
    }

    public void setPublished(Integer published)
    {
        this.published = published;
    }
}
