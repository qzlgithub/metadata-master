package com.mingdong.core.constant;

import com.mingdong.core.model.Dict;

import java.util.ArrayList;
import java.util.List;

public enum BillPlan
{
    BY_TIME(1, "按时间计费"),
    PER_USE(2, "按次计费"),
    PER_HIT(3, "按击中计费");

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

    public static String getNameById(Integer id)
    {
        if(id != null)
        {
            for(BillPlan o : BillPlan.values())
            {
                if(id.equals(o.id))
                {
                    return o.name;
                }
            }
        }
        return null;
    }

    public static List<Dict> getAllList()
    {
        List<Dict> dictDTOList = new ArrayList<>();
        for(BillPlan billPlan : BillPlan.values())
        {
            dictDTOList.add(new Dict(billPlan.id + "", billPlan.name));
        }
        return dictDTOList;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean equals(Integer id)
    {
        return id != null && id.equals(this.id);
    }
}
