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
        for(ScopeType item : ScopeType.values())
        {
            if(item.type.equals(type))
            {
                return item;
            }
        }
        return null;
    }

    public String getType()
    {
        return type;
    }
}
