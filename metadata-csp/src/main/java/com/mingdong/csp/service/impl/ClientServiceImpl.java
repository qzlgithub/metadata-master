package com.mingdong.csp.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.HomeDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.UserSession;
import com.mingdong.csp.service.ClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService
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
        UserSession session = new UserSession(dto.getClientId(), dto.getUserId(), dto.getName());
        redisDao.saveUserSession(sessionId, session);
        resp.addData(Field.NAME, dto.getName()).addData(Field.MANAGER_QQ, dto.getManagerQq()).addData(Field.FIRST_LOGIN,
                dto.getFirstLogin());
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
    public void addAccount(Long primaryAccountId, String username, String password, String name, String phone,
            BLResp resp)
    {
        BaseDTO dto = clientApi.addAccount(primaryAccountId, username, password, name, phone);
        resp.result(dto.getResult());
    }

    @Override
    public void getAccountList(Long clientId, Long primaryUserId, BLResp resp)
    {
        UserListDTO dto = clientApi.getSubUserList(clientId, primaryUserId);
        if(dto.getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResult());
            return;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for(SubUserDTO o : dto.getUserList())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.USER_ID, o.getUserId() + "");
            m.put(Field.USERNAME, o.getUsername());
            m.put(Field.NAME, o.getName());
            m.put(Field.PHONE, o.getPhone());
            m.put(Field.ENABLED, o.getEnabled());
            list.add(m);
        }
        resp.addData(Field.ALLOWED_QTY, dto.getAllowedQty()).addData(Field.LIST, list);
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
    public void getHomeData(Long clientId, Long clientUserId, BLResp resp)
    {
        HomeDTO dto = clientApi.getUserHomeData(clientId, clientUserId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.result(dto.getResult());
            return;
        }
        int allowedQty = dto.getTotalAllowedQty() - (CollectionUtils.isEmpty(dto.getSubUsers()) ? 0 :
                dto.getSubUsers().size());
        resp.addData(Field.ALLOWED_QTY, allowedQty).addData(Field.SUB_USER_LIST, dto.getSubUsers()).addData(
                Field.OPENED_LIST, dto.getOpened()).addData(Field.TO_OPEN_LIST, dto.getToOpen());
    }

    @Override
    public void getClientMessage(Long clientId, Page page, BLResp resp)
    {
        MessageListDTO dto = clientApi.getClientMessage(clientId, page);
        if(dto.getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResult());
            return;
        }
        resp.addData(Field.TOTAL, dto.getTotal()).addData(Field.PAGES, dto.getPages()).addData(Field.PAGE_NUM,
                page.getPageNum()).addData(Field.PAGE_SIZE, page.getPageSize());
        List<Map<String, Object>> list = new ArrayList<>();
        for(MessageDTO m : dto.getMessages())
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ADD_AT, DateUtils.format(m.getAddAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            map.put(Field.TYPE, m.getType());
            map.put(Field.CONTENT, m.getContent());
            list.add(map);
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public void setSubUserDeleted(Long primaryUserId, Long subUserId, BLResp resp)
    {
        BaseDTO dto = clientApi.setSubUserDeleted(primaryUserId, subUserId);
        resp.result(dto.getResult());
    }
}