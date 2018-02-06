package com.mingdong.mis.constant;

public enum APIProduct
{
    DS_DATA_BLACKLIST("/ds-data/blacklist"),
    DS_DATA_MULTI_APP("/ds-data/multi-app");

    private final String uri;

    APIProduct(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }
}
