package com.mingdong.mis.constant;

public enum APIProduct
{
    // 黑名单
    FIN_HMD("FIN-HMD", "/credit/blacklist"),
    // 常欠客
    FIN_CDK("FIN-CDK", "/credit/overdue"),
    // 多头客
    FIN_DTK("FIN-DTK", "/credit/multi-register"),
    // 通过客
    FIN_TGK("FIN-TGK", "/credit/creditable"),
    // 优良客
    FIN_YLK("FIN-YLK", "/credit/favourable"),
    // 拒贷客
    FIN_JDK("FIN-JDK", "/credit/rejectee"),
    // 天镜全息报告
    FIN_TJQXBG("FIN_TJQXBG", "/credit/hologram");

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
