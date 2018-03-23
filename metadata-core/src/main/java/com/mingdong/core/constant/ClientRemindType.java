package com.mingdong.core.constant;

public enum ClientRemindType
{
    DATE(1, "时间服务"),
    TIMES(2, "计次服务");
    private int id;
    private String name;

    ClientRemindType(int id, String name)
    {
        this.id = id;
        this.name = name;
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
