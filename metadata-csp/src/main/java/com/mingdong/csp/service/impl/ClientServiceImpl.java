package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
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
    @Resource
    private RemoteProductService productApi;

    @Override
    public void userLogin(String username, String password, String sessionId, BLResp resp)
    {
        UserDTO dto = clientApi.userLogin(username, password);
        if(RestResult.SUCCESS != dto.getResultDTO().getResult())
        {
            resp.result(dto.getResultDTO().getResult());
            return;
        }
        UserSession session = new UserSession(dto.getClientId(), dto.getUserId(), dto.getName(), username,
                dto.getPrimary());
        redisDao.saveUserSession(sessionId, session);
        resp.addData(Field.NAME, dto.getName());
        resp.addData(Field.MANAGER_QQ, dto.getManagerQq());
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
        ResultDTO dto = clientApi.changeUserPassword(userId, oldPwd, newPwd);
        resp.result(dto.getResult());
    }

    @Override
    public void addAccount(Long primaryAccountId, String username, String password, String name, String phone,
            BLResp resp)
    {
        ResultDTO dto = clientApi.addAccount(primaryAccountId, username, password, name, phone);
        resp.result(dto.getResult());
    }

    @Override
    public void getAccountList(Long clientId, Long primaryUserId, BLResp resp)
    {
        UserListDTO dto = clientApi.getSubUserList(clientId, primaryUserId);
        if(dto.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResultDTO().getResult());
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
        resp.addData(Field.ALLOWED_QTY, dto.getAllowedQty());
        resp.addData(Field.LIST, list);
    }

    @Override
    public void changeStatus(Long primaryAccountId, Long clientUserId, BLResp resp)
    {
        UserDTO userDTO = clientApi.changeStatus(primaryAccountId, clientUserId);
        if(userDTO.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(userDTO.getResultDTO().getResult());
            return;
        }
        resp.addData(Field.USER_ID, userDTO.getUserId() + "");
        resp.addData(Field.CLIENT_ID, userDTO.getClientId() + "");
        resp.addData(Field.NAME, userDTO.getName());
        resp.addData(Field.PHONE, userDTO.getPhone());
        resp.addData(Field.ENABLED, userDTO.getEnabled());
    }

    @Override
    public void editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password,
            String name, String phone, Integer enabled, BLResp resp)
    {
        UserDTO userDTO = clientApi.editChildAccount(primaryAccountId, clientUserId, username, password, name, phone,
                enabled);
        if(userDTO.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(userDTO.getResultDTO().getResult());
            return;
        }
        resp.addData(Field.USER_ID, userDTO.getUserId() + "");
        resp.addData(Field.CLIENT_ID, userDTO.getClientId() + "");
        resp.addData(Field.NAME, userDTO.getName());
        resp.addData(Field.PHONE, userDTO.getPhone());
        resp.addData(Field.ENABLED, userDTO.getEnabled());
    }

    @Override
    public void getHomeData(Long clientId, Long clientUserId, BLResp resp)
    {
        if(TrueOrFalse.TRUE.equals(RequestThread.getPrimary()))
        {
            UserListDTO userListDTO = clientApi.getSubUserList(RequestThread.getClientId(), RequestThread.getUserId());
            List<Map<String, Object>> subUserList = new ArrayList<>();
            for(SubUserDTO u : userListDTO.getUserList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.NAME, u.getName());
                subUserList.add(map);
            }
            resp.addData(Field.ALLOWED_QTY, userListDTO.getAllowedQty());
            resp.addData(Field.SUB_USER_LIST, subUserList);
        }
        ProductListDTO productListDTO = productApi.getIndexProductList(RequestThread.getClientId(), null, null, null);
        List<Map<String, Object>> opened = new ArrayList<>();
        List<Map<String, Object>> toOpen = new ArrayList<>();
        if(productListDTO.getResultDTO().getResult() == RestResult.SUCCESS)
        {
            for(ProductDTO d : productListDTO.getOpened())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.PRODUCT_ID, d.getId() + "");
                map.put(Field.NAME, d.getName());
                map.put(Field.STATUS, d.getStatus());
                map.put(Field.BILL_PLAN, d.getBillPlan());
                if(BillPlan.YEAR.getId().equals(d.getBillPlan()))
                {
                    map.put(Field.FROM_DATE, DateUtils.format(d.getFromDate(), DateFormat.YYYY_MM_DD_2));
                    map.put(Field.TO_DATE, DateUtils.format(d.getToDate(), DateFormat.YYYY_MM_DD_2));
                    map.put(Field.REMAIN_DAYS, BusinessUtils.getDayDiffFromNow(d.getFromDate(), d.getToDate()) + "");
                }
                else
                {
                    map.put(Field.UNIT_AMT, NumberUtils.formatAmount(d.getCostAmt()));
                    map.put(Field.BALANCE, NumberUtils.formatAmount(d.getBalance()));
                }
                opened.add(map);
            }
            for(ProductDTO d : productListDTO.getToOpen())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.PRODUCT_ID, d.getId() + "");
                map.put(Field.NAME, d.getName());
                map.put(Field.REMARK, d.getRemark());
                toOpen.add(map);
            }
        }
        resp.addData(Field.OPENED_LIST, opened);
        resp.addData(Field.TO_OPEN_LIST, toOpen);
        resp.addData(Field.IS_PRIMARY, RequestThread.getPrimary());
    }

    @Override
    public void getClientMessage(Long clientId, Page page, BLResp resp)
    {
        MessageListDTO dto = clientApi.getClientMessage(clientId, page);
        if(dto.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResultDTO().getResult());
            return;
        }
        resp.addData(Field.TOTAL, dto.getTotal());
        resp.addData(Field.PAGES, dto.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
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
        ResultDTO dto = clientApi.setSubUserDeleted(primaryUserId, subUserId);
        resp.result(dto.getResult());
    }

    @Override
    public void getAccountByUserId(Long userId, BLResp resp)
    {
        UserDTO userDTO = clientApi.getAccountByUserId(userId);
        if(userDTO.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(userDTO.getResultDTO().getResult());
            return;
        }
        resp.addData(Field.CLIENT_USER_ID, userDTO.getUserId() + "");
        resp.addData(Field.CLIENT_ID, userDTO.getClientId() + "");
        resp.addData(Field.USERNAME, userDTO.getUsername());
        resp.addData(Field.NAME, userDTO.getName());
        resp.addData(Field.PHONE, userDTO.getPhone());
        resp.addData(Field.ENABLED, userDTO.getEnabled());
    }

    @Override
    public void getUserCredential(Long userId, String password, Long productId, BLResp resp)
    {
        CredentialDTO dto = clientApi.getUserCredential(userId, password, productId);
        if(dto.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResultDTO().getResult());
            return;
        }
        resp.addData(Field.APP_ID, dto.getAppId());
        resp.addData(Field.APP_KEY, dto.getAppKey() != null ? dto.getAppKey() : "");
        resp.addData(Field.REQ_HOST, dto.getReqHost() != null ? dto.getReqHost() : "");
    }

    @Override
    public void saveUserCredential(Long userId, Long productId, String appKey, String reqHost, BLResp resp)
    {
        ResultDTO dto = clientApi.saveUserCredential(userId, productId, appKey, reqHost);
        resp.result(dto.getResult());
    }
}