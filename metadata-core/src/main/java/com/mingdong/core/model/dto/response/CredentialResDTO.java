package com.mingdong.core.model.dto.response;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;

public class CredentialResDTO implements Serializable
{
    private String appId;
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

    public String getReqHost()
    {
        return reqHost;
    }

    public void setReqHost(String reqHost)
    {
        this.reqHost = reqHost;
    }

}
