package com.mingdong.mis.constant;

import org.apache.commons.lang3.StringUtils;

public enum APIProduct
{
    DS_DATA_BLACKLIST("/ds-data/blacklist", "DS_DATA_BLACKLIST"),
    DS_DATA_MULTI_APP("/ds-data/multi-app", "DS_DATA_MULTI_APP");

    private final String uri;
    private final String code;

    APIProduct(String uri, String code)
    {
        this.uri = uri;
        this.code = code;
    }

    public static APIProduct getByCode(String code)
    {
        if(StringUtils.isNotBlank(code))
        {
            for(APIProduct item : APIProduct.values())
            {
                if(code.equals(item.code))
                {
                    return item;
                }
            }
        }
        return null;
    }

    public String getUri()
    {
        return uri;
    }

    public String getCode()
    {
        return code;
    }
}
