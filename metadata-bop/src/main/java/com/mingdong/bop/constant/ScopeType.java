package com.mingdong.bop.constant;

public enum ScopeType
{
    NOW("0"),
    YESTERDAY("1"),
    WEEK("2"),
    HALF_MONTH("3"),
    MONTH("4"),
    QUARTER("5"),
    YEAR("6");

    private String type;

    ScopeType(String type)
    {
        this.type = type;
    }

    public static ScopeType getScopeType(String type)
    {
        if(type != null)
        {
            for(ScopeType o : ScopeType.values())
            {
                if(type.equals(o.type))
                {
                    return o;
                }
            }
        }
        return null;
    }

    public String getType()
    {
        return type;
    }
}
