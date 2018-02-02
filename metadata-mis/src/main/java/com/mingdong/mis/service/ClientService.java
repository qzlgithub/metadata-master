package com.mingdong.mis.service;

import com.mingdong.mis.model.MetadataRes;

public interface ClientService
{
    void getClientAccessToken(String appId, String timestamp, String accessKey, String username, Integer refresh,
            MetadataRes res);
}
