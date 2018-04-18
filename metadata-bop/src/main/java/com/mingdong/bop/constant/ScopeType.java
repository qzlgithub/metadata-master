package com.mingdong.bop.constant;

public enum ScopeType
{
    //今天
    TODAY("0"),
    //明天
    YESTERDAY("1"),
    //近7天
    NEARBY_7D("2"),
    //近15天
    NEARBY_15D("3"),
    //近30天
    NEARBY_30D("4"),
    //近90天
    NEARBY_90D("5"),
    //近365天
    NEARBY_365D("6"),
    //当前月
    THIS_MONTH("7"),
    //当前季度
    THIS_QUARTER("8");

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
