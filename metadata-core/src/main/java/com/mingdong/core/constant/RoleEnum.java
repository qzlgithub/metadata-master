package com.mingdong.core.constant;

import com.mingdong.common.util.StringUtils;

public enum RoleEnum
{
    MANAGER("管理员", "MANAGER"),
    SALESMAN("业务员", "SALESMAN");
    private String name;
    private String code;

    RoleEnum(String name, String code)
    {
        this.name = name;
        this.code = code;
    }

    public static String getNameByCode(String code){
        if(StringUtils.isNullBlank(code)){
            return null;
        }
        for(RoleEnum item : RoleEnum.values()){
            if(item.code.equals(code)){
                return item.name;
            }
        }
        return null;
    }

    public static RoleEnum getByCode(String code){
        if(StringUtils.isNullBlank(code)){
            return null;
        }
        for(RoleEnum item : RoleEnum.values()){
            if(item.code.equals(code)){
                return item;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
