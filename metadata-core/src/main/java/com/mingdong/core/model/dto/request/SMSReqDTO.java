package com.mingdong.core.model.dto.request;

import java.io.Serializable;

public class SMSReqDTO implements Serializable
{
    private String phone;
    private String content;
    private Integer smsType;

    public Integer getSmsType()
    {
        return smsType;
    }

    public void setSmsType(Integer smsType)
    {
        this.smsType = smsType;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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
