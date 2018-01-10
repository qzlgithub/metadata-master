package com.mingdong.csp.service;

import com.mingdong.core.model.BLResp;

public interface UserService
{
    void userLogin(String username, String password, BLResp resp);

    void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp);
    void userLogout(String sessionId);
}
