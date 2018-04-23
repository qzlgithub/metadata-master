package com.mingdong.core.constant;

public enum QueryStatus
{
    SUCCESS(0, "请求成功"),
    INTERNAL_ERROR(1, "系统内部错误"),
    THIRD_API_ERROR(2, "第三方接口调用失败");

    private final int code;
    private final String name;

    QueryStatus(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static QueryStatus getByCode(int code)
    {
        for(QueryStatus o : QueryStatus.values())
        {
            if(code == o.code)
            {
                return o;
            }
        }
        return SUCCESS;
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
