package com.mingdong.core.constant;

public enum RangeUnit
{
    HOUR(1),
    DAY(2),
    WEEK(3),
    MONTH(4);

    private final Integer id;

    RangeUnit(Integer id)
    {
        this.id = id;
    }

    public static RangeUnit getById(Integer id)
    {
        if(id != null)
        {
            for(RangeUnit o : RangeUnit.values())
            {
                if(id.equals(o.id))
                {
                    return o;
                }
            }
        }
        return null;
    }

    public Integer getId()
    {
        return id;
    }
}
