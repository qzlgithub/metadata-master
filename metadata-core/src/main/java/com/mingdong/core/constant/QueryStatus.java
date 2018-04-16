package com.mingdong.core.constant;

public enum QueryStatus
{
    NORMAL(0, "请求成功"),
    FAILED(1, "请求失败");

    private final int code;
    private final String name;

    QueryStatus(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code)
    {
        for(QueryStatus o : QueryStatus.values())
        {
            if(code == o.code)
            {
                return o.name;
            }
        }
        return NORMAL.name;
    }

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
}
