package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningOut
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String warningCode;
    private Long productId;
    private Long clientId;
    private String thirdCode;
    private Integer level;
    private Long count;
    private Date lastTime;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getThirdCode()
    {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode)
    {
        this.thirdCode = thirdCode;
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

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }

    public Date getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(Date lastTime)
    {
        this.lastTime = lastTime;
    }
}
