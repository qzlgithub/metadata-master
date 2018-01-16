package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;

public class CredentialDTO implements Serializable
{
    private String errCode;
    private String appId;
    private String appKey;
    private String reqHost;

    public CredentialDTO()
    {
        errCode = RestResult.SUCCESS.getCode();
    }

    public String getErrCode()
    {
        return errCode;
    }

    public void setErrCode(String errCode)
    {
        this.errCode = errCode;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getReqHost()
    {
        return reqHost;
    }

    public void setReqHost(String reqHost)
    {
        this.reqHost = reqHost;
    }

    public RestResult getResult()
    {
        return RestResult.getByCode(errCode);
    }
}
