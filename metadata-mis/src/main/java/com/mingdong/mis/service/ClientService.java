package com.mingdong.mis.service;

import com.mingdong.mis.model.MDResp;

public interface ClientService
{
    void getClientAccessToken(String appId, String timestamp, String accessKey, String username, Integer refresh,
            MDResp res);
}
