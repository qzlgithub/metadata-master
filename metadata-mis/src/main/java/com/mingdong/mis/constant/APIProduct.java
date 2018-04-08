package com.mingdong.mis.constant;

public enum APIProduct
{
    FIN_CDK("CDK", "/credit/overdue"),
    FIN_HMD("HMD", "/credit/blacklist");

    private final String code;
    private final String uri;

    APIProduct(String code, String uri)
    {
        this.code = code;
        this.uri = uri;
    }

    public static APIProduct getByCode(String code)
    {
        if(code != null)
        {
            for(APIProduct o : APIProduct.values())
            {
                if(code.equals(o.code))
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
