package com.mingdong.mis.model.vo;

public class PhoneVO extends AbsPayload
{
    private String phone;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Override
    public boolean check()
    {
        return false;
    }
}
