package com.mingdong.mis.model;

import com.mingdong.mis.constant.APIProduct;

class RequestHolder
{
    private long timestamp;
    private APIProduct product;
    private UserAuth userAuth;
    private MDResp resp;

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public APIProduct getProduct()
    {
        return product;
    }

    public void setProduct(APIProduct product)
    {
        this.product = product;
    }

    public UserAuth getUserAuth()
    {
        return userAuth;
    }

    public void setUserAuth(UserAuth userAuth)
    {
        this.userAuth = userAuth;
    }

    public MDResp getResp()
    {
        return resp;
    }

    public void setResp(MDResp resp)
    {
        this.resp = resp;
    }
}
