package com.mingdong.core.constant;

public enum WarningType
{
    PRODUCT(1, "产品"),
    CLIENT(2, "客户"),
    THIRD(3, "第三方");

    private int id;
    private String name;

    WarningType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static WarningType findById(int id)
    {
        for(WarningType item : values())
        {
            if(item.id == id)
            {
                return item;
            }
        }
        return null;
    }

    public int getId()
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
