package com.mingdong.core.constant;

import com.mingdong.core.model.dto.DictDTO;

import java.util.ArrayList;
import java.util.List;

public enum Custom
{
    COMMON(0, "普通"),
    CUSTOMIZATION(1, "定制");

    private Integer id;
    private String name;

    Custom(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static Custom getById(Integer id)
    {
        if(id != null)
        {
            for(Custom productGroupType : Custom.values())
            {
                if(id.equals(productGroupType.id))
                {
                    return productGroupType;
                }
            }
        }
        return null;
    }

    public static List<DictDTO> getAllList()
    {
        List<DictDTO> dictDTOList = new ArrayList<>();
        for(Custom productGroupType : Custom.values())
        {
            dictDTOList.add(new DictDTO(productGroupType.id + "", productGroupType.name));
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
}
