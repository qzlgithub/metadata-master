package com.mingdong.core.constant;

import com.mingdong.core.model.Dict;

import java.util.ArrayList;
import java.util.List;

public enum ProductType
{
    INTERNET_FINANCE(1, "互联网金融");

    private final int id;
    private final String name;

    ProductType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static ProductType getById(int id)
    {
        for(ProductType item : ProductType.values())
        {
            if(id == item.id)
            {
                return item;
            }
        }
        return null;
    }

    public static String getNameById(int id)
    {
        for(ProductType o : ProductType.values())
        {
            if(id == o.id)
            {
                return o.name;
            }
        }
        return null;
    }

    public static List<Dict> getProductTypeDict()
    {
        List<Dict> list = new ArrayList<>();
        for(ProductType o : ProductType.values())
        {
            list.add(new Dict(o.getId() + "", o.getName()));
        }
        return list;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
