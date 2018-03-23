package com.mingdong.mis.model.vo;

public class MultipleAppVO extends AbsPayload
{
    private String idNo;
    private String name;
    private String phone;
    private String orderNo;//以这个是否有值来判定是否获取报告

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

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
        return false;
    }
}
