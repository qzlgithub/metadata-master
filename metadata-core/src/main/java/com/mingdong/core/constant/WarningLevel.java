package com.mingdong.core.constant;

public enum WarningLevel
{
    GENERAL(1, "一般"),
    SEVERITY(2, "严重");
    private Integer id;
    private String name;

    WarningLevel(Integer id, String name)
    {
        this.id = id;
        this.name = name;
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
