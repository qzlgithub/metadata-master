package com.mingdong.core.constant;

import com.mingdong.common.util.StringUtils;

public enum WarningCode
{
    PRODUCT_ANOMALY("PRODUCT_ANOMALY", WarningType.PRODUCT, "请求异常"),
    CLIENT_FAILURE("CLIENT_FAILURE", WarningType.CLIENT, "请求失败");

    private String code;
    private WarningType warningType;
    private String name;

    WarningCode(String code, WarningType warningType, String name)
    {
        this.code = code;
        this.warningType = warningType;
        this.name = name;
    }

    public static WarningCode findByCode(String code)
    {
        if(!StringUtils.isNullBlank(code))
        {
            for(WarningCode item : values())
            {
                if(item.code.equals(code))
                {
                    return item;
                }
            }
        }
        return null;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public WarningType getWarningType()
    {
        return warningType;
    }

    public void setWarningType(WarningType warningType)
    {
        this.warningType = warningType;
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
