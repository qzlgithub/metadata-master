package com.mingdong.core.constant;

public enum QueryStatus
{
    SUCCESS(0, "请求成功"),
    DATA_BASE_ERROR(1, "数据库错误"),
    OTHER_ERROR(99, "其他错误");

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
