package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.Date;

public class RechargeTypeDTO implements Serializable
{
    private Integer id;
    private String name;
    private String remark;
    private Integer enabled;
    private Date addAt;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public Date getAddAt()
    {
        return addAt;
    }

    public void setAddAt(Date addAt)
    {
        this.addAt = addAt;
    }
}
