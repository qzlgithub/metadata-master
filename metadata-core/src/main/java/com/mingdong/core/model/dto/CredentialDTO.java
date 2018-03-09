package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.base.ResponseDTO;

import java.io.Serializable;

public class CredentialDTO implements Serializable
{
    private String appId;
    private String appKey;
    private String reqHost;
    private ResponseDTO responseDTO;

    public CredentialDTO()
    {
        this.responseDTO = new ResponseDTO();
        responseDTO.setResult(RestResult.SUCCESS);
    }

    public ResponseDTO getResponseDTO()
    {
        return responseDTO;
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

}
