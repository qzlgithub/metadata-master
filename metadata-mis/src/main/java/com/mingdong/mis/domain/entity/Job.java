package com.mingdong.mis.domain.entity;

import java.util.Date;

public class Job
{
    private String code;
    private String name;
    private Date lastSucTime;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getLastSucTime()
    {
        return lastSucTime;
    }

    public void setLastSucTime(Date lastSucTime)
    {
        this.lastSucTime = lastSucTime;
    }
}
