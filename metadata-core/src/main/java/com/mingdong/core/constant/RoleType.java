package com.mingdong.core.constant;

public enum RoleType
{
    ADMIN(1, "管理员"),
    SALESMAN(2, "业务员"),
    OPERATION(3, "运营人员");

    private final Integer id;
    private final String name;

    RoleType(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static RoleType getById(Integer id)
    {
        if(id != null)
        {
            for(RoleType o : RoleType.values())
            {
                if(id.equals(o.id))
                {
                    return o;
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
