package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.SysParam;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.ClientAccountDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.component.Param;
import com.mingdong.mis.domain.TransformDTO;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientAccount;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.entity.ClientOperateInfo;
import com.mingdong.mis.domain.entity.ClientOperateLog;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.Manager;
import com.mingdong.mis.domain.entity.SysConfig;
import com.mingdong.mis.domain.entity.UserProduct;
import com.mingdong.mis.domain.mapper.ClientAccountMapper;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.domain.mapper.ClientOperateInfoMapper;
import com.mingdong.mis.domain.mapper.ClientOperateLogMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ManagerMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;
import com.mingdong.mis.domain.mapper.SysConfigMapper;
import com.mingdong.mis.domain.mapper.UserProductMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteClientServiceImpl implements RemoteClientService
{

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
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ClientAccountMapper clientAccountMapper;
    @Resource
    private ClientOperateLogMapper clientOperateLogMapper;
    @Resource
    private ClientOperateInfoMapper clientOperateInfoMapper;
    @Resource
    private StatsClientMapper statsClientMapper;

    @Override
    public UserDTO userLogin(String username, String password)
    {
        ClientUser user = clientUserMapper.findByUsername(username);
        UserDTO userDTO = new UserDTO();
        if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()))
        {
            userDTO.getResultDTO().setResult(RestResult.ACCOUNT_NOT_EXIST);
            return userDTO;
        }
        else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            userDTO.getResultDTO().setResult(RestResult.ACCOUNT_DISABLED);
            return userDTO;
        }
        Client client = clientMapper.findById(user.getClientId());
        if(client == null || client.getPrimaryUserId() == null)
        {
            userDTO.getResultDTO().setResult(RestResult.INTERNAL_ERROR);
            return userDTO;
        }
        ClientUser primaryUser = clientUserMapper.findById(client.getPrimaryUserId());
        if(primaryUser == null || !TrueOrFalse.TRUE.equals(primaryUser.getEnabled()))
        {
            userDTO.getResultDTO().setResult(RestResult.ACCOUNT_DISABLED);
            return userDTO;
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            userDTO.getResultDTO().setResult(RestResult.INVALID_PASSCODE);
            return userDTO;
        }
        Manager manager = managerMapper.findById(client.getManagerId());
        userDTO.setClientId(user.getClientId());
        userDTO.setUserId(user.getId());
        userDTO.setPrimary(user.getId().equals(client.getPrimaryUserId()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        userDTO.setName(user.getName());
        userDTO.setManagerQq(manager == null ? "" : manager.getQq());
        userDTO.setFirstLogin(
                Constant.DEFAULT_ENC_PWD.equals(user.getPassword()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return userDTO;
    }

    @Override
    @Transactional
    public ResultDTO changeUserPassword(Long userId, String orgPassword, String newPassword)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            resultDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return resultDTO;
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(orgPassword)))
        {
            resultDTO.setResult(RestResult.INVALID_PASSCODE);
            return resultDTO;
        }
        ClientUser userUpd = new ClientUser();
        userUpd.setId(userId);
        userUpd.setUpdateTime(new Date());
        userUpd.setPassword(Md5Utils.encrypt(newPassword));
        clientUserMapper.updateSkipNull(userUpd);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public MessageListDTO getClientMessage(Long clientId, Page page)
    {
        MessageListDTO dto = new MessageListDTO();
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
    public ResultDTO setSubUserDeleted(Long primaryUserId, Long subUserId)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser primaryUser = clientUserMapper.findById(primaryUserId);
        if(primaryUser == null)
        {
            resultDTO.setResult(RestResult.INTERNAL_ERROR);
            return resultDTO;
        }
        Client client = clientMapper.findById(primaryUser.getClientId());
        if(client == null || !primaryUserId.equals(client.getPrimaryUserId()))
        {
            resultDTO.setResult(RestResult.ONLY_PRIMARY_USER);
            return resultDTO;
        }
        ClientUser subUser = clientUserMapper.findById(subUserId);
        if(subUser == null || !TrueOrFalse.FALSE.equals(subUser.getDeleted()) || !primaryUser.getClientId().equals(
                subUser.getClientId()))
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        Date current = new Date();
        ClientUser userUpd = new ClientUser();
        userUpd.setId(subUserId);
        userUpd.setUpdateTime(current);
        userUpd.setDeleted(TrueOrFalse.TRUE);
        clientUserMapper.updateSkipNull(userUpd);
        updateClientAccountQty(client.getId());
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public UserListDTO getSubUserList(Long clientId, Long primaryUserId)
    {
        Client client = clientMapper.findById(clientId);
        UserListDTO userListDTO = new UserListDTO();
        if(client == null)
        {
            userListDTO.getResultDTO().setResult(RestResult.INTERNAL_ERROR);
            return userListDTO;
        }
        else if(!primaryUserId.equals(client.getPrimaryUserId()))
        {
            userListDTO.getResultDTO().setResult(RestResult.ONLY_PRIMARY_USER);
            return userListDTO;
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
        userListDTO.setAllowedQty(config == null ? 5 : Integer.parseInt(config.getValue()));
        userListDTO.setUserList(list);
        return userListDTO;
    }

    @Override
    @Transactional
    public ResultDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone)
    {
        ResultDTO resultDTO = new ResultDTO();
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        if(client == null)
        {
            resultDTO.setResult(RestResult.ONLY_PRIMARY_USER);
            return resultDTO;
        }
        ClientUser account = clientUserMapper.findByUsername(username);
        if(account != null)
        {
            resultDTO.setResult(RestResult.USERNAME_EXIST);
            return resultDTO;
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
            resultDTO.setResult(RestResult.ACCOUNT_COUNT_MAX);
            return resultDTO;
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
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public UserDTO changeStatus(Long primaryAccountId, Long clientUserId)
    {
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        UserDTO userDTO = new UserDTO();
        if(client == null)
        {
            userDTO.getResultDTO().setResult(RestResult.ONLY_PRIMARY_USER);
            return userDTO;
        }
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(TrueOrFalse.TRUE.equals(clientUser.getEnabled()))
        {
            clientUser.setEnabled(TrueOrFalse.FALSE);
        }
        else
        {
            clientUser.setEnabled(TrueOrFalse.TRUE);
        }
        clientUserMapper.updateById(clientUser);
        TransformDTO.userToDTO(clientUser, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getAccountByUserId(Long clientUserId)
    {
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(clientUser == null)
        {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        TransformDTO.userToDTO(clientUser, userDTO);
        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password,
            String name, String phone, Integer enabled)
    {
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        UserDTO userDTO = new UserDTO();
        if(client == null)
        {
            userDTO.getResultDTO().setResult(RestResult.ONLY_PRIMARY_USER);
            return userDTO;
        }
        ClientUser clientUserByUserName = clientUserMapper.findByUsername(username);
        if(clientUserByUserName != null && clientUserByUserName.getId().longValue() != clientUserId.longValue())
        {
            userDTO.getResultDTO().setResult(RestResult.USERNAME_EXIST);
            return userDTO;
        }
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        clientUser.setUsername(username);
        clientUser.setPassword(
                StringUtils.isNullBlank(password) ? clientUser.getPassword() : Md5Utils.encrypt(password));
        clientUser.setName(name);
        clientUser.setPhone(phone);
        clientUser.setEnabled(enabled);
        clientUserMapper.updateById(clientUser);
        TransformDTO.userToDTO(clientUser, userDTO);
        return userDTO;
    }

    @Override
    public CredentialDTO getUserCredential(Long userId, String password, Long productId)
    {
        CredentialDTO dto = new CredentialDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null || !user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            dto.getResultDTO().setResult(RestResult.INVALID_PASSCODE);
            return dto;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            dto.getResultDTO().setResult(RestResult.PRODUCT_NOT_OPEN);
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
    public ResultDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            resultDTO.setCode(RestResult.INTERNAL_ERROR.getCode());
            return resultDTO;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            resultDTO.setCode(RestResult.PRODUCT_NOT_OPEN.getCode());
            return resultDTO;
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
        return resultDTO;
    }

    @Override
    public UserDTO findByUsername(String username)
    {
        ClientUser user = clientUserMapper.findByUsername(username);
        if(user == null)
        {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        TransformDTO.userToDTO(user, userDTO);
        return userDTO;
    }

    @Override
    public ClientInfoListDTO getSimilarCorpByName(String corpName, Long clientId)
    {
        List<ClientInfo> similarCorpByName = clientInfoMapper.getSimilarCorpByName(corpName, clientId);
        ClientInfoListDTO clientInfoListDTO = new ClientInfoListDTO();
        List<ClientInfoDTO> clientInfoDTOList = new ArrayList<>();
        clientInfoListDTO.setDataList(clientInfoDTOList);
        if(CollectionUtils.isNotEmpty(similarCorpByName))
        {
            ClientInfoDTO clientInfoDTO;
            for(ClientInfo item : similarCorpByName)
            {
                clientInfoDTO = new ClientInfoDTO();
                EntityUtils.copyProperties(item, clientInfoDTO);
                clientInfoDTOList.add(clientInfoDTO);
            }
        }
        return clientInfoListDTO;
    }

    @Override
    public ClientInfoListDTO getClientInfoListBy(Integer enabled, String username, String cropName, String shortName,
            List<Long> industryIdList, Page page)
    {
        ClientInfoListDTO clientInfoListDTO = new ClientInfoListDTO();
        List<ClientInfoDTO> clientInfoDTOList = new ArrayList<>();
        clientInfoListDTO.setDataList(clientInfoDTOList);
        ClientInfoDTO clientInfoDTO;
        if(page == null)
        {
            List<ClientInfo> clientInfoList = clientInfoMapper.getListBy(enabled, username, cropName, shortName,
                    industryIdList);
            if(CollectionUtils.isNotEmpty(clientInfoList))
            {
                for(ClientInfo item : clientInfoList)
                {
                    clientInfoDTO = new ClientInfoDTO();
                    EntityUtils.copyProperties(item, clientInfoDTO);
                    clientInfoDTOList.add(clientInfoDTO);
                }
            }
        }
        else
        {
            int total = clientInfoMapper.countBy(enabled, username, cropName, shortName, industryIdList);
            int pages = page.getTotalPage(total);
            clientInfoListDTO.setPages(pages);
            clientInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientInfo> clientInfoList = clientInfoMapper.getListBy(enabled, username, cropName, shortName,
                        industryIdList);
                for(ClientInfo item : clientInfoList)
                {
                    clientInfoDTO = new ClientInfoDTO();
                    EntityUtils.copyProperties(item, clientInfoDTO);
                    clientInfoDTOList.add(clientInfoDTO);
                }
            }
        }
        return clientInfoListDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveClientUser(ClientUserDTO clientUser)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser cu = new ClientUser();
        EntityUtils.copyProperties(clientUser, cu);
        clientUserMapper.add(cu);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveClient(ClientDTO client)
    {
        ResultDTO resultDTO = new ResultDTO();
        Client cu = new Client();
        EntityUtils.copyProperties(client, cu);
        clientMapper.add(cu);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientDTO getClientByClientId(Long clientId)
    {
        ClientDTO clientDTO = new ClientDTO();
        Client byId = clientMapper.findById(clientId);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, clientDTO);
        return clientDTO;
    }

    @Override
    public ClientUserDTO getClientUserByUserId(Long userId)
    {
        ClientUserDTO userDTO = new ClientUserDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            return null;
        }
        EntityUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientUserByUserId(ClientUserDTO clientUser)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser cu = new ClientUser();
        EntityUtils.copyProperties(clientUser, cu);
        clientUserMapper.updateById(cu);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientAccountDTO getClientAccountByClientId(Long clientId)
    {
        ClientAccountDTO clientAccountDTO = new ClientAccountDTO();
        ClientAccount byId = clientAccountMapper.findById(clientId);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, clientAccountDTO);
        return clientAccountDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientAccountById(ClientAccountDTO clientAccountDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientAccount clientAccount = new ClientAccount();
        EntityUtils.copyProperties(clientAccountDTO, clientAccount);
        clientAccountMapper.updateById(clientAccount);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveClientAccount(ClientAccountDTO clientAccountDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientAccount clientAccount = new ClientAccount();
        EntityUtils.copyProperties(clientAccountDTO, clientAccount);
        clientAccountMapper.add(clientAccount);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientById(ClientDTO clientDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Client client = new Client();
        EntityUtils.copyProperties(clientDTO, client);
        clientMapper.updateById(client);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientUserListDTO getListByClientAndStatus(Long clientId, Integer enabled, Integer deleted)
    {
        ClientUserListDTO clientUserListDTO = new ClientUserListDTO();
        List<ClientUserDTO> dataList = new ArrayList<>();
        clientUserListDTO.setDataList(dataList);
        List<ClientUser> subUserList = clientUserMapper.getListByClientAndStatus(clientId, enabled, deleted);
        if(CollectionUtils.isNotEmpty(subUserList))
        {
            ClientUserDTO clientUserDTO;
            for(ClientUser item : subUserList)
            {
                clientUserDTO = new ClientUserDTO();
                EntityUtils.copyProperties(item, clientUserDTO);
                dataList.add(clientUserDTO);
            }
        }
        return clientUserListDTO;
    }

    @Override
    public ClientUserListDTO getClientUserListByClientIds(List<Long> clientIdList)
    {
        ClientUserListDTO clientUserListDTO = new ClientUserListDTO();
        List<ClientUserDTO> dataList = new ArrayList<>();
        clientUserListDTO.setDataList(dataList);
        List<ClientUser> userList = clientUserMapper.getListByClientsAndPrimary(clientIdList);
        if(CollectionUtils.isNotEmpty(userList))
        {
            ClientUserDTO clientUserDTO;
            for(ClientUser item : userList)
            {
                clientUserDTO = new ClientUserDTO();
                dataList.add(clientUserDTO);
                EntityUtils.copyProperties(item, clientUserDTO);
            }
        }
        return clientUserListDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveClientOperateLogList(List<ClientOperateLogDTO> logList)
    {
        ResultDTO resultDTO = new ResultDTO();
        List<ClientOperateLog> clientOperateLogs = new ArrayList<>();
        ClientOperateLog clientOperateLog;
        for(ClientOperateLogDTO item : logList)
        {
            clientOperateLog = new ClientOperateLog();
            EntityUtils.copyProperties(item, clientOperateLog);
            clientOperateLogs.add(clientOperateLog);
        }
        clientOperateLogMapper.addList(clientOperateLogs);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientUserStatusByIds(Integer enabled, Date date, List<Long> idList)
    {
        ResultDTO resultDTO = new ResultDTO();
        clientUserMapper.updateStatusByIds(enabled, date, idList);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO setClientDeleted(List<Long> idList)
    {
        ResultDTO resultDTO = new ResultDTO();
        clientMapper.setClientDeleted(idList, new Date());
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientListDTO getClientListByIds(List<Long> idList)
    {
        ClientListDTO clientListDTO = new ClientListDTO();
        List<ClientDTO> dataList = new ArrayList<>();
        clientListDTO.setDataList(dataList);
        List<Client> clientList = clientMapper.getListByIdList(idList);
        if(CollectionUtils.isNotEmpty(clientList))
        {
            ClientDTO clientDTO;
            for(Client item : clientList)
            {
                clientDTO = new ClientDTO();
                EntityUtils.copyProperties(item, clientDTO);
                dataList.add(clientDTO);
            }
        }
        return clientListDTO;
    }

    @Override
    @Transactional
    public ResultDTO resetPasswordByIds(String pwd, List<Long> idList)
    {
        ResultDTO resultDTO = new ResultDTO();
        clientUserMapper.resetPasswordByIds(pwd, new Date(), idList);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientProductDTO getClientProductById(Long clientProductId)
    {
        ClientProductDTO clientProductDTO = new ClientProductDTO();
        ClientProduct cp = clientProductMapper.findById(clientProductId);
        if(cp == null)
        {
            return null;
        }
        EntityUtils.copyProperties(cp, clientProductDTO);
        return clientProductDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientUserSkipNull(ClientUserDTO clientUserDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientUser cu = new ClientUser();
        EntityUtils.copyProperties(clientUserDTO, cu);
        clientUserMapper.updateSkipNull(cu);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientProductDTO getClientProductByClientAndProduct(Long clientId, Long productId)
    {
        ClientProductDTO clientProductDTO = new ClientProductDTO();
        ClientProduct cp = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(cp == null)
        {
            return null;
        }
        EntityUtils.copyProperties(cp, clientProductDTO);
        return clientProductDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveClientProduct(ClientProductDTO clientProductDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientProduct cp = new ClientProduct();
        EntityUtils.copyProperties(clientProductDTO, cp);
        clientProductMapper.add(cp);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateClientProductSkipNull(ClientProductDTO clientProductDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        ClientProduct cp = new ClientProduct();
        EntityUtils.copyProperties(clientProductDTO, cp);
        clientProductMapper.updateSkipNull(cp);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ClientOperateInfoListDTO getClientOperateInfoListByUserId(Long primaryUserId, Page page)
    {
        ClientOperateInfoListDTO clientOperateInfoListDTO = new ClientOperateInfoListDTO();
        List<ClientOperateInfoDTO> dataList = new ArrayList<>();
        clientOperateInfoListDTO.setDataList(dataList);
        ClientOperateInfoDTO clientOperateInfoDTO;
        if(page == null)
        {
            List<ClientOperateInfo> clientOperateInfos = clientOperateInfoMapper.getListByClientUser(primaryUserId);
            if(CollectionUtils.isNotEmpty(clientOperateInfos))
            {
                for(ClientOperateInfo item : clientOperateInfos)
                {
                    clientOperateInfoDTO = new ClientOperateInfoDTO();
                    EntityUtils.copyProperties(item, clientOperateInfoDTO);
                    dataList.add(clientOperateInfoDTO);
                }
            }
        }
        else
        {
            int total = clientOperateLogMapper.countByClientUser(primaryUserId);
            int pages = page.getTotalPage(total);
            clientOperateInfoListDTO.setPages(pages);
            clientOperateInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientOperateInfo> clientOperateInfos = clientOperateInfoMapper.getListByClientUser(primaryUserId);
                if(CollectionUtils.isNotEmpty(clientOperateInfos))
                {
                    for(ClientOperateInfo item : clientOperateInfos)
                    {
                        clientOperateInfoDTO = new ClientOperateInfoDTO();
                        dataList.add(clientOperateInfoDTO);
                        EntityUtils.copyProperties(item, clientOperateInfoDTO);
                    }
                }
            }
        }
        return clientOperateInfoListDTO;
    }

    @Override
    public ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page)
    {
        ClientInfoListDTO clientInfoListDTO = new ClientInfoListDTO();
        List<ClientInfoDTO> clientInfoDTOList = new ArrayList<>();
        clientInfoListDTO.setDataList(clientInfoDTOList);
        ClientInfoDTO clientInfoDTO;
        if(page == null)
        {
            List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
            if(CollectionUtils.isNotEmpty(clientInfoList))
            {
                for(ClientInfo item : clientInfoList)
                {
                    clientInfoDTO = new ClientInfoDTO();
                    EntityUtils.copyProperties(item, clientInfoDTO);
                    clientInfoDTOList.add(clientInfoDTO);
                }
            }
        }
        else
        {
            int total = statsClientMapper.getClientCountByDate(date, currentDay);
            int pages = page.getTotalPage(total);
            clientInfoListDTO.setPages(pages);
            clientInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
                for(ClientInfo item : clientInfoList)
                {
                    clientInfoDTO = new ClientInfoDTO();
                    EntityUtils.copyProperties(item, clientInfoDTO);
                    clientInfoDTOList.add(clientInfoDTO);
                }
            }
        }
        return clientInfoListDTO;
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