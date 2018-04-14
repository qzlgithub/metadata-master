package com.mingdong.core.constant;

public enum Warning
{
    PRODUCT_ABNORMAL("PRODUCT_ANOMALY", "请求异常"),
    CLIENT_FAILURE("CLIENT_FAILURE", "请求失败");
    private String code;
    private String name;

    Warning(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
}
