package com.mingdong.core.constant;

public enum Custom
{
    COMMON(0, "普通"),
    CUSTOMIZATION(1, "定制");

    private final Integer id;
    private final String name;

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

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
