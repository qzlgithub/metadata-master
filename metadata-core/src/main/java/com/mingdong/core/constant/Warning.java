package com.mingdong.core.constant;

public enum Warning
{
    PRODUCT_ABNORMAL(1, "请求异常"),
    CLIENT_FAILURE(2, "请求失败"),
    OTHER_ABNORMAL(3, "请求异常"),
    OTHER_FAILURE(4, "请求失败");
    private int id;
    private String name;

    Warning(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
