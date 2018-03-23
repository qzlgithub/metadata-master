package com.mingdong.mis.constant;

public enum APIProduct
{
    CDK("/credit/overdue"),
    DS_DATA_BLACKLIST("/ds-data/blacklist"),
    DS_DATA_MULTI_APP("/ds-data/multi-app");

    private final String uri;

    APIProduct(String uri)
    {
        this.uri = uri;
    }

    public static APIProduct targetOf(String name)
    {
        if(name != null)
        {
            for(APIProduct o : APIProduct.values())
            {
                if(name.equals(o.name()))
                {
                    return o;
                }
            }
        }
        return null;
    }

    public String getUri()
    {
        return uri;
    }
}
