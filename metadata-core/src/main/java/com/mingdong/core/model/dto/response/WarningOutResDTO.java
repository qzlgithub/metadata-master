package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class WarningOutResDTO implements Serializable
{
    private Long id;
    private String warningCode;
    private String productName;
    private String corpName;
    private String thirdCode;
    private Integer level;
    private Long count;
    private Date lastTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public String getThirdCode()
    {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode)
    {
        this.thirdCode = thirdCode;
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
