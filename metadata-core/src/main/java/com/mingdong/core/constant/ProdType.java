package com.mingdong.core.constant;

import com.mingdong.core.model.dto.DictDTO;

import java.util.ArrayList;
import java.util.List;

public enum ProdType
{
    INTERNET_FINANCE(1, "互联网金融");

    private final int id;
    private final String name;

    ProdType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static ProdType getById(int id)
    {
        for(ProdType item : ProdType.values())
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
        for(ProdType o : ProdType.values())
        {
            if(id == o.id)
            {
                return o.name;
            }
        }
        return null;
    }

    public static List<DictDTO> getProdTypeDict()
    {
        List<DictDTO> list = new ArrayList<>();
        for(ProdType o : ProdType.values())
        {
            list.add(new DictDTO(o.getId() + "", o.getName()));
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
