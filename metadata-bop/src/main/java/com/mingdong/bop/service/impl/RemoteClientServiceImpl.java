package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.domain.entity.Client;
import com.mingdong.bop.domain.entity.ClientUser;
import com.mingdong.bop.domain.mapper.ClientMapper;
import com.mingdong.bop.domain.mapper.ClientUserMapper;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.RemoteClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

public class RemoteClientServiceImpl implements RemoteClientService
{
    private static Logger logger = LoggerFactory.getLogger(RemoteProductServiceImpl.class);
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientUserMapper clientUserMapper;

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
        UserDTO dto = new UserDTO();
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

}
