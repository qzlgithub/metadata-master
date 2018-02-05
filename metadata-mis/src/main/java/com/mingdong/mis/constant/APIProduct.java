package com.mingdong.mis.constant;

public enum APIProduct
{
    DS_DATA_BLACKLIST("DS-FHMD");

    private final String code;

    APIProduct(String code)
    {
        this.code = code;
    }

    public static APIProduct getByCode(String code)
    {
        if(code != null)
        {
            for(APIProduct product : APIProduct.values())
            {
                if(code.equals(product.code))
                {
                    return product;
                }
            }
        }
        return null;
    }

    public String getCode()
    {
        return code;
    }
}
