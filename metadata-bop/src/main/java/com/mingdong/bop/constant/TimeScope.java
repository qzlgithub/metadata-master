package com.mingdong.bop.constant;

public enum TimeScope
{
    CUSTOM(0),
    TODAY(1),
    YESTERDAY(2),
    LATEST_7_DAYS(3),
    LATEST_30_DAYS(4),
    LATEST_90_DAYS(5),
    LATEST_1_YEAR(6);

    private final int code;

    TimeScope(int code)
    {
        this.code = code;
    }

    public static TimeScope getByCode(int code)
    {
        for(TimeScope o : TimeScope.values())
        {
            if(code == o.code)
            {
                return o;
            }
        }
        return null;
    }

    public int getCode()
    {
        return code;
    }
}
