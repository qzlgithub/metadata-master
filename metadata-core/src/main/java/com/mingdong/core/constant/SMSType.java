package com.mingdong.core.constant;

public enum SMSType
{
    PRODUCT_RECHARGE(1, "产品开通续费"),
    WARNING(2, "警报提醒");
    private Integer id;
    private String name;

    SMSType(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

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
}
