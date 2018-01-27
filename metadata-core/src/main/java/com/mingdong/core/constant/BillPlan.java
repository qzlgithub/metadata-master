package com.mingdong.core.constant;

public enum BillPlan
{
    YEAR(1),
    //包年
    REQ(2),
    RES(3);

    private final Integer id;

    BillPlan(Integer id)
    {
        this.id = id;
    }

    public static BillPlan getById(Integer id)
    {
        if(id != null)
        {
            for(BillPlan billPlan : BillPlan.values())
            {
                if(id.equals(billPlan.id))
                {
                    return billPlan;
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
