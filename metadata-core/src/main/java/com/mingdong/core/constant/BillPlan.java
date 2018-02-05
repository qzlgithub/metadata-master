package com.mingdong.core.constant;

public enum BillPlan
{
    YEAR(1, "包年"),
    REQ(2, "请求计费"),
    RES(3, "请求成功计费");

    private Integer id;
    private String name;

    BillPlan(Integer id, String name)
    {
        this.id = id;
        this.name = name;
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

    public String getName()
    {
        return name;
    }
}
