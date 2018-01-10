package com.mingdong.core.service;

import com.mingdong.core.model.BLResp;

public interface RemoteClientService
{
    void userLogin(String username, String password, BLResp resp);
    void userLogout(String sessionId);
}
