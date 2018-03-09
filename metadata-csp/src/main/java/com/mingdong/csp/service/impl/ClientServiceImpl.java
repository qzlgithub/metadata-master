package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.ProductRpcService;
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
    private ClientRpcService clientRpcService;
    @Resource
    private ProductRpcService productRpcService;

    @Override
    public void userLogin(String username, String password, String sessionId, RestResp resp)
    {
        UserDTO dto = clientRpcService.userLogin(username, password);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.setError(dto.getResult());
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
    public void changePassword(Long userId, String oldPwd, String newPwd, RestResp resp)
    {
        ResponseDTO dto = clientRpcService.changeUserPassword(userId, oldPwd, newPwd);
        resp.setError(dto.getResult());
    }

    @Override
    public void addAccount(Long primaryAccountId, String username, String password, String name, String phone,
            RestResp resp)
    {
        ResponseDTO dto = clientRpcService.addAccount(primaryAccountId, username, password, name, phone);
        resp.setError(dto.getResult());
    }

    @Override
    public void getClientSubAccountList(RestListResp res)
    {
        ListDTO<SubUserDTO> listDTO = clientRpcService.getSubUserList(RequestThread.getClientId(),
                RequestThread.getUserId());
        res.addData(Field.ALLOWED_QTY, listDTO.getExtradata().get(Field.SUB_ACCOUNT_MAX));
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            res.setTotal(listDTO.getTotal());
            for(SubUserDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.USER_ID, o.getUserId() + "");
                m.put(Field.USERNAME, o.getUsername());
                m.put(Field.NAME, o.getName());
                m.put(Field.PHONE, o.getPhone());
                m.put(Field.ENABLED, o.getEnabled());
                list.add(m);
            }
        }
        res.setList(list);
    }

    @Override
    public void changeSubUserStatus(Long clientUserId, Integer enabled, RestResp resp)
    {
        ResponseDTO responseDTO = clientRpcService.changeSubUserStatus(RequestThread.getClientId(), clientUserId, enabled);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void editSubUser(Long clientUserId, String username, String password, String name, String phone,
            Integer enabled, RestResp resp)
    {
        SubUserDTO subUserDTO = new SubUserDTO();
        subUserDTO.setClientId(RequestThread.getClientId());
        subUserDTO.setUserId(clientUserId);
        subUserDTO.setUsername(username);
        subUserDTO.setPassword(password == null ? null : Md5Utils.encrypt(password));
        subUserDTO.setName(name);
        subUserDTO.setPhone(phone);
        subUserDTO.setEnabled(enabled);
        ResponseDTO responseDTO = clientRpcService.editSubUser(subUserDTO);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void getHomeData(Long clientId, Long clientUserId, RestResp resp)
    {
        if(TrueOrFalse.TRUE.equals(RequestThread.getPrimary()))
        {
            ListDTO<SubUserDTO> subUserList = clientRpcService.getSubUserList(RequestThread.getClientId(),
                    RequestThread.getUserId());
            List<Map<String, Object>> list = new ArrayList<>();
            for(SubUserDTO u : subUserList.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.NAME, u.getName());
                list.add(map);
            }
            resp.addData(Field.ALLOWED_QTY, subUserList.getExtradata().get(Field.SUB_ACCOUNT_MAX));
            resp.addData(Field.SUB_USER_LIST, list);
        }
        ListDTO<ProductDTO> openedList = productRpcService.getOpenedProductList(RequestThread.getClientId());
        List<Map<String, Object>> opened = new ArrayList<>();
        if(!CollectionUtils.isEmpty(openedList.getList()))
        {
            for(ProductDTO d : openedList.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.PRODUCT_ID, d.getId() + "");
                map.put(Field.NAME, d.getName());
                map.put(Field.STATUS, d.getStatus());
                map.put(Field.BILL_PLAN, d.getBillPlan());
                if(BillPlan.BY_TIME.getId().equals(d.getBillPlan()))
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
        }
        ListDTO<ProductDTO> unopenedList = productRpcService.getUnopenedProductList(RequestThread.getClientId());
        List<Map<String, Object>> unopened = new ArrayList<>();
        if(!CollectionUtils.isEmpty(openedList.getList()))
        {
            for(ProductDTO d : unopenedList.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.PRODUCT_ID, d.getId() + "");
                map.put(Field.NAME, d.getName());
                map.put(Field.REMARK, d.getRemark());
                unopened.add(map);
            }
        }
        resp.addData(Field.OPENED_LIST, opened);
        resp.addData(Field.TO_OPEN_LIST, unopened);
        resp.addData(Field.IS_PRIMARY, RequestThread.getPrimary());
    }

    @Override
    public void getClientMessageList(Page page, RestListResp res)
    {
        ListDTO<MessageDTO> listDTO = clientRpcService.getClientMessage(RequestThread.getClientId(), page);
        List<Map<String, Object>> list = new ArrayList<>();
        for(MessageDTO o : listDTO.getList())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ADD_AT, DateUtils.format(o.getAddAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            m.put(Field.TYPE, o.getType());
            m.put(Field.CONTENT, o.getContent());
            list.add(m);
        }
        res.setTotal(listDTO.getTotal());
        res.setList(list);
    }

    @Override
    public void setSubUserDeleted(Long primaryUserId, Long subUserId, RestResp resp)
    {
        ResponseDTO dto = clientRpcService.setSubUserDeleted(primaryUserId, subUserId);
        resp.setError(dto.getResult());
    }

    @Override
    public void getClientUserInfo(Long userId, RestResp resp)
    {
        UserDTO userDTO = clientRpcService.getAccountByUserId(userId);
        if(userDTO.getResult() != RestResult.SUCCESS)
        {
            resp.setError(userDTO.getResult());
            return;
        }
        resp.addData(Field.CLIENT_ID, userDTO.getClientId() + "");
        resp.addData(Field.CLIENT_USER_ID, userDTO.getUserId() + "");
        resp.addData(Field.USERNAME, userDTO.getUsername());
        resp.addData(Field.NAME, userDTO.getName());
        resp.addData(Field.PHONE, userDTO.getPhone());
        resp.addData(Field.ENABLED, userDTO.getEnabled());
    }

    @Override
    public void getUserCredential(Long userId, String password, Long productId, RestResp resp)
    {
        CredentialDTO dto = clientRpcService.getUserCredential(userId, password, productId);
        if(dto.getResponseDTO().getResult() != RestResult.SUCCESS)
        {
            resp.setError(dto.getResponseDTO().getResult());
            return;
        }
        resp.addData(Field.APP_ID, dto.getAppId());
        resp.addData(Field.APP_KEY, dto.getAppKey() != null ? dto.getAppKey() : "");
        resp.addData(Field.REQ_HOST, dto.getReqHost() != null ? dto.getReqHost() : "");
    }

    @Override
    public void saveUserCredential(Long userId, Long productId, String appKey, String reqHost, RestResp resp)
    {
        ResponseDTO dto = clientRpcService.saveUserCredential(userId, productId, appKey, reqHost);
        resp.setError(dto.getResult());
    }
}