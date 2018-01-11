package com.mingdong.csp.service.impl;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.UserSession;
import com.mingdong.csp.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private RemoteClientService clientApi;

    @Override
    public void userLogin(String username, String password, String sessionId, BLResp resp)
    {
        UserDTO dto = clientApi.userLogin(username, password);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.result(dto.getResult());
            return;
        }
        UserSession session = new UserSession(dto.getUserId(), dto.getName());
        redisDao.saveUserSession(sessionId, session);
        resp.addData(Field.FIRST_LOGIN, dto.getFirstLogin());
    }

    @Override
    public void userLogout(String sessionId)
    {
        redisDao.dropUserSession(sessionId);
    }

    @Override
    public void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp)
    {
        BaseDTO dto = clientApi.changeUserPassword(userId, oldPwd, newPwd);
        resp.result(dto.getResult());
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

}
