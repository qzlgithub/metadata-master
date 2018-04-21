package com.mingdong.mis.model;

import com.mingdong.core.constant.QueryStatus;
import com.mingdong.mis.constant.APIProduct;

class RequestHolder
{
    private long timestamp;
    private int payloadId;
    private Boolean hit;
    private QueryStatus queryStatus;
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

    public int getPayloadId()
    {
        return payloadId;
    }

    public void setPayloadId(int payloadId)
    {
        this.payloadId = payloadId;
    }

    public Boolean getHit()
    {
        return hit;
    }

    public void setHit(Boolean hit)
    {
        this.hit = hit;
    }

    public QueryStatus getQueryStatus()
    {
        return queryStatus;
    }

    public void setQueryStatus(QueryStatus queryStatus)
    {
        this.queryStatus = queryStatus;
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
