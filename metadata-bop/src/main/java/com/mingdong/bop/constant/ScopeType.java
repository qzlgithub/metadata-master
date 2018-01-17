package com.mingdong.bop.constant;

public enum ScopeType
{
    MONTH("1"),
    QUARTER("2"),
    TYPE("3");

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
