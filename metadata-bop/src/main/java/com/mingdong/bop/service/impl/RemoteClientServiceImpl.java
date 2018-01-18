package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.configurer.Param;
import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.constant.SysParam;
import com.mingdong.bop.domain.entity.Client;
import com.mingdong.bop.domain.entity.ClientMessage;
import com.mingdong.bop.domain.entity.ClientProduct;
import com.mingdong.bop.domain.entity.ClientUser;
import com.mingdong.bop.domain.entity.Manager;
import com.mingdong.bop.domain.entity.SysConfig;
import com.mingdong.bop.domain.entity.UserProduct;
import com.mingdong.bop.domain.mapper.ClientMapper;
import com.mingdong.bop.domain.mapper.ClientMessageMapper;
import com.mingdong.bop.domain.mapper.ClientProductMapper;
import com.mingdong.bop.domain.mapper.ClientUserMapper;
import com.mingdong.bop.domain.mapper.ManagerMapper;
import com.mingdong.bop.domain.mapper.SysConfigMapper;
import com.mingdong.bop.domain.mapper.UserProductMapper;
import com.mingdong.bop.util.IDUtils;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.HomeDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;
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
    private static Logger logger = LoggerFactory.getLogger(RemoteClientService.class);
    @Resource
    private Param param;
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Resource
    private ManagerMapper managerMapper;
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientMessageMapper clientMessageMapper;
    @Resource
    private UserProductMapper userProductMapper;

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
        Manager manager = managerMapper.findById(client.getManagerId());
        UserDTO dto = new UserDTO(RestResult.SUCCESS);
        dto.setClientId(user.getClientId());
        dto.setUserId(user.getId());
        dto.setPrimary(user.getId().equals(client.getPrimaryUserId()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        dto.setName(user.getName());
        dto.setManagerQq(manager == null ? "" : manager.getQq());
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
        updateClientAccountQty(client.getId());
        return new BaseDTO(RestResult.SUCCESS);
    }

    @Override
    public UserListDTO getSubUserList(Long clientId, Long primaryUserId)
    {
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return new UserListDTO(RestResult.INTERNAL_ERROR);
        }
        else if(!primaryUserId.equals(client.getPrimaryUserId()))
        {
            return new UserListDTO(RestResult.ONLY_PRIMARY_USER);
        }
        List<SubUserDTO> list = new ArrayList<>();
        List<ClientUser> userList = clientUserMapper.getListByClientAndStatus(clientId, null, TrueOrFalse.FALSE);
        for(ClientUser cu : userList)
        {
            if(!cu.getId().equals(client.getPrimaryUserId()))
            {
                SubUserDTO dto = new SubUserDTO();
                dto.setUserId(cu.getId());
                dto.setUsername(cu.getUsername());
                dto.setName(cu.getName());
                dto.setPhone(cu.getPhone());
                dto.setEnabled(cu.getEnabled());
                list.add(dto);
            }
        }
        SysConfig config = sysConfigMapper.findByName(SysParam.CLIENT_SUB_USER_QTY);
        UserListDTO dto = new UserListDTO(RestResult.SUCCESS);
        dto.setAllowedQty(config == null ? 5 : Integer.parseInt(config.getValue()));
        dto.setUserList(list);
        return dto;
    }

    @Override
    @Transactional
    public BaseDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone)
    {
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        if(client == null)
        {
            return new BaseDTO(RestResult.ONLY_PRIMARY_USER);
        }
        ClientUser account = clientUserMapper.findByUsername(username);
        if(account != null)
        {
            return new BaseDTO(RestResult.USERNAME_EXIST);
        }
        SysConfig config = sysConfigMapper.findByName(SysParam.CLIENT_SUB_USER_QTY);
        List<ClientUser> userList = clientUserMapper.getListByClientAndStatus(client.getId(), null, TrueOrFalse.FALSE);
        int subAccountCount = 0;
        for(ClientUser cu : userList)
        {
            if(!cu.getId().equals(client.getPrimaryUserId()))
            {
                subAccountCount++;
            }
        }
        int canSubAccountCount = config == null ? 5 : Integer.parseInt(config.getValue());
        if(subAccountCount >= canSubAccountCount)
        {
            return new BaseDTO(RestResult.ACCOUNT_COUNT_MAX);
        }

        Date current = new Date();
        account = new ClientUser();
        account.setId(IDUtils.getClientUser(param.getNodeId()));
        account.setCreateTime(current);
        account.setUpdateTime(current);
        account.setClientId(client.getId());
        account.setName(name);
        account.setPhone(phone);
        account.setUsername(username);
        account.setPassword(Md5Utils.encrypt(password));
        account.setEnabled(TrueOrFalse.TRUE);
        account.setDeleted(TrueOrFalse.FALSE);
        clientUserMapper.add(account);
        updateClientAccountQty(client.getId());
        return new BaseDTO(RestResult.SUCCESS);
    }

    @Override
    @Transactional
    public UserDTO changeStatus(Long primaryAccountId, Long clientUserId)
    {
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        if(client == null)
        {
            return new UserDTO(RestResult.ONLY_PRIMARY_USER);
        }
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(TrueOrFalse.TRUE == clientUser.getEnabled())
        {
            clientUser.setEnabled(TrueOrFalse.FALSE);
        }
        else
        {
            clientUser.setEnabled(TrueOrFalse.TRUE);
        }
        clientUserMapper.updateById(clientUser);
        UserDTO userDTO = new UserDTO(RestResult.SUCCESS);
        userDTO.setUserId(clientUser.getId());
        userDTO.setClientId(clientUser.getClientId());
        userDTO.setName(clientUser.getName());
        userDTO.setPhone(clientUser.getPhone());
        userDTO.setUsername(clientUser.getUsername());
        userDTO.setEnabled(clientUser.getEnabled());
        return userDTO;
    }

    @Override
    public UserDTO getAccountByUserId(Long clientUserId)
    {
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        UserDTO userDTO = new UserDTO(RestResult.SUCCESS);
        userDTO.setUserId(clientUser.getId());
        userDTO.setClientId(clientUser.getClientId());
        userDTO.setName(clientUser.getName());
        userDTO.setPhone(clientUser.getPhone());
        userDTO.setUsername(clientUser.getUsername());
        userDTO.setEnabled(clientUser.getEnabled());
        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password,
            String name, String phone, Integer enabled)
    {
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        if(client == null)
        {
            return new UserDTO(RestResult.ONLY_PRIMARY_USER);
        }
        ClientUser clientUserByUserName = clientUserMapper.findByUsername(username);
        if(clientUserByUserName != null && clientUserByUserName.getId().longValue() != clientUserId.longValue())
        {
            return new UserDTO(RestResult.USERNAME_EXIST);
        }
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        clientUser.setUsername(username);
        clientUser.setPassword(
                StringUtils.isNullBlank(password) ? clientUser.getPassword() : Md5Utils.encrypt(password));
        clientUser.setName(name);
        clientUser.setPhone(phone);
        clientUser.setEnabled(enabled);
        clientUserMapper.updateById(clientUser);
        UserDTO userDTO = new UserDTO(RestResult.SUCCESS);
        userDTO.setUserId(clientUser.getId());
        userDTO.setClientId(clientUser.getClientId());
        userDTO.setName(clientUser.getName());
        userDTO.setPhone(clientUser.getPhone());
        userDTO.setUsername(clientUser.getUsername());
        userDTO.setEnabled(clientUser.getEnabled());
        return userDTO;
    }

    @Override
    public CredentialDTO getUserCredential(Long userId, String password, Long productId)
    {
        CredentialDTO dto = new CredentialDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null || !user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            dto.setErrCode(RestResult.INVALID_PASSCODE.getCode());
            return dto;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            dto.setErrCode(RestResult.PRODUCT_NOT_OPEN.getCode());
            return dto;
        }
        dto.setAppId(cp.getAppId());
        UserProduct up = userProductMapper.findByUserAndProduct(userId, productId);
        if(up != null)
        {
            dto.setAppKey(up.getAppKey());
            dto.setReqHost(up.getReqHost());
        }
        return dto;
    }

    @Override
    @Transactional
    public BaseDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost)
    {
        BaseDTO dto = new BaseDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            dto.setCode(RestResult.INTERNAL_ERROR.getCode());
            return dto;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            dto.setCode(RestResult.PRODUCT_NOT_OPEN.getCode());
            return dto;
        }
        Date current = new Date();
        UserProduct up = userProductMapper.findByUserAndProduct(userId, productId);
        if(up == null)
        {
            up = new UserProduct();
            up.setCreateTime(current);
            up.setUpdateTime(current);
            up.setUserId(userId);
            up.setProductId(productId);
            up.setAppKey(appKey);
            up.setReqHost(reqHost);
            userProductMapper.add(up);
        }
        else
        {
            UserProduct upUpd = new UserProduct();
            upUpd.setId(up.getId());
            upUpd.setUpdateTime(current);
            upUpd.setAppKey(appKey);
            upUpd.setReqHost(reqHost);
            userProductMapper.updateSkipNull(upUpd);
        }
        return dto;
    }

    /**
     * 更新企业的子账号个数
     */
    private void updateClientAccountQty(Long clientId)
    {
        int quantity = clientUserMapper.countByClientAndStatus(clientId, null, TrueOrFalse.FALSE);
        Client client = new Client();
        client.setId(clientId);
        client.setUpdateTime(new Date());
        client.setAccountQty(quantity > 1 ? (quantity - 1) : 0);
        clientMapper.updateSkipNull(client);
    }

}