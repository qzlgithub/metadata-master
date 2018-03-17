package com.mingdong.bop.constant;

public enum RangeType
{
    LATEST_30(1),
    LATEST_90(2),
    LATEST_YEAR(3),
    CUSTOM(9);

    private final Integer id;

    RangeType(Integer id)
    {
        this.id = id;
    }

    public static RangeType getById(Integer id)
    {
        if(id != null)
        {
            for(RangeType o : RangeType.values())
            {
                if(id.equals(o.id))
                {
                    return o;
                }
            }
        }
        return null;
    }
}
