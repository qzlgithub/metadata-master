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
    @Resource
    private RedisBaseDao redisBaseDao;

    @Override
    public void userLogin(String username, String password, BLResp resp)
    {

    }

    @Override
    public void userLogout(String sessionId)
    {

    }

    @Override
    public void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp)
    {

    }

}
