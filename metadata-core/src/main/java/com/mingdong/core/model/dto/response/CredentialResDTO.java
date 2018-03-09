package com.mingdong.core.model.dto.response;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.ResponseDTO;

import java.io.Serializable;

public class CredentialResDTO implements Serializable
{
    private String appId;
    private String appKey;
    private String reqHost;
    private ResponseDTO responseDTO;

    public CredentialResDTO()
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
