package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.domain.entity.Client;
import com.mingdong.bop.domain.entity.ClientMessage;
import com.mingdong.bop.domain.entity.ClientUser;
import com.mingdong.bop.domain.mapper.ClientMapper;
import com.mingdong.bop.domain.mapper.ClientMessageMapper;
import com.mingdong.bop.domain.mapper.ClientUserMapper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.HomeDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.RemoteClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteClientServiceImpl implements RemoteClientService
{
    private static Logger logger = LoggerFactory.getLogger(RemoteProductServiceImpl.class);
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientMessageMapper clientMessageMapper;

    @Override
    public UserDTO userLogin(String username, String password)
    {
        ClientUser user = clientUserMapper.findByUsername(username);
        if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()))
        {
            return new UserDTO(RestResult.ACCOUNT_NOT_EXIST);
        }
        else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            return new UserDTO(RestResult.ACCOUNT_DISABLED);
        }
        Client client = clientMapper.findById(user.getClientId());
        if(client == null || client.getPrimaryUserId() == null)
        {
            return new UserDTO(RestResult.INTERNAL_ERROR);
        }
        ClientUser primaryUser = clientUserMapper.findById(client.getPrimaryUserId());
        if(primaryUser == null || !TrueOrFalse.TRUE.equals(primaryUser.getEnabled()))
        {
            return new UserDTO(RestResult.ACCOUNT_DISABLED);
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            return new UserDTO(RestResult.INVALID_PASSCODE);
        }
        UserDTO dto = new UserDTO(RestResult.SUCCESS);
        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setManagerQq("");
        dto.setFirstLogin(Constant.DEFAULT_ENC_PWD.equals(user.getPassword()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return dto;
    }

    @Override
    @Transactional
    public BaseDTO changeUserPassword(Long userId, String orgPassword, String newPassword)
    {
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            return new BaseDTO(RestResult.ACCOUNT_NOT_EXIST);
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(orgPassword)))
        {
            return new BaseDTO(RestResult.INVALID_PASSCODE);
        }
        ClientUser userUpd = new ClientUser();
        userUpd.setId(userId);
        userUpd.setUpdateTime(new Date());
        userUpd.setPassword(Md5Utils.encrypt(newPassword));
        clientUserMapper.updateSkipNull(userUpd);
        return new BaseDTO(RestResult.SUCCESS);
    }

    @Override
    public HomeDTO getUserHomeData(Long clientId, Long clientUserId)
    {
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return new HomeDTO(RestResult.OBJECT_NOT_FOUND);
        }
        return null;
    }

    @Override
    public MessageListDTO getClientMessage(Long clientId, Page page)
    {
        MessageListDTO dto = new MessageListDTO(RestResult.SUCCESS);
        int total = clientMessageMapper.countByClient(clientId);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        dto.setPages(pages);
        List<MessageDTO> list = new ArrayList<>();
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientMessage> messageList = clientMessageMapper.getListByClient(clientId);
            for(ClientMessage cm : messageList)
            {
                MessageDTO o = new MessageDTO();
                o.setAddAt(cm.getCreateTime());
                o.setType(cm.getType());
                o.setContent(cm.getContent());
                list.add(o);
            }
        }
        dto.setMessages(list);
        return dto;
    }

    @Override
    @Transactional
    public BaseDTO setSubUserDeleted(Long primaryUserId, Long subUserId)
    {
        ClientUser primaryUser = clientUserMapper.findById(primaryUserId);
        if(primaryUser == null)
        {
            return new BaseDTO(RestResult.INTERNAL_ERROR);
        }
        Client client = clientMapper.findById(primaryUser.getClientId());
        if(client == null || !primaryUserId.equals(client.getPrimaryUserId()))
        {
            return new BaseDTO(RestResult.ONLY_PRIMARY_USER);
        }
        ClientUser subUser = clientUserMapper.findById(subUserId);
        if(subUser == null || !TrueOrFalse.FALSE.equals(subUser.getDeleted()) || !primaryUser.getClientId().equals(
                subUser.getClientId()))
        {
            return new BaseDTO(RestResult.OBJECT_NOT_FOUND);
        }
        Date current = new Date();
        ClientUser userUpd = new ClientUser();
        userUpd.setId(subUserId);
        userUpd.setUpdateTime(current);
        userUpd.setDeleted(TrueOrFalse.TRUE);
        clientUserMapper.updateSkipNull(userUpd);
        int qty = clientUserMapper.countByClient(client.getId());
        Client clientUpd = new Client();
        clientUpd.setId(client.getId());
        clientUpd.setUpdateTime(current);
        clientUpd.setAccountQty(qty > 1 ? (qty - 1) : 0);
        clientMapper.updateSkipNull(client);
        return new BaseDTO(RestResult.SUCCESS);
    }
}