package com.mingdong.csp.service.impl;

import com.mingdong.core.model.BLResp;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.csp.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService
{
    @Resource
    private RemoteClientService clientApi;

    @Override
    public void userLogin(String username, String password, BLResp resp)
    {

    }

    @Override
    public void userLogout(String sessionId)
    {

    }

    @Override
    public void addChildAccount(Long clientId, String username, String password, String name, String phone, BLResp resp)
    {

    }

    @Override
    public BLResp getChildAccountMap(Long id)
    {
        return null;
    }

    @Override
    public void changeStatus(Long clientUserId, BLResp resp)
    {

    }

    @Override
    public void editChildAccount(Long clientUserId, String username, String password, String name, String phone,
            BLResp resp)
    {

    }

    @Override
    public void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp)
    {

    }

}
