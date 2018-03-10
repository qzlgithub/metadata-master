package com.mingdong.core.constant;

public enum MessageType
{
    RECHARGE_OPEN(1, "产品开通"),
    RECHARGE_RENEW(2, "产品续费");

    private Integer id;
    private String name;

    MessageType(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static String getNameById(Integer id)
    {
        if(id != null)
        {
            for(MessageType item : MessageType.values())
            {
                if(item.id.equals(id))
                {
                    return item.name;
                }
            }
        }
        return null;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
