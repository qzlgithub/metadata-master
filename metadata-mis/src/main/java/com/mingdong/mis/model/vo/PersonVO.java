package com.mingdong.mis.model.vo;

import com.mingdong.common.util.RegexUtils;

public class PersonVO extends AbsPayload
{
    private String idNo;
    private String name;
    private String phone;

    public String getIdNo()
    {
        return idNo;
    }

    public void setIdNo(String idNo)
    {
        this.idNo = idNo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

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
        return idNo != null && name != null && phone != null && RegexUtils.matchIdNo(idNo) && RegexUtils.matchPhone(
                phone);
    }
}
