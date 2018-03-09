package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.SysParam;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.AccessDTO;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserDictDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.DisableClientDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.RechargeInfoDTO;
import com.mingdong.core.model.dto.RechargeReqDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.component.Param;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.domain.entity.ApiReqInfo;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientContact;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.entity.ClientOperateInfo;
import com.mingdong.mis.domain.entity.ClientOperateLog;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.entity.Sistem;
import com.mingdong.mis.domain.entity.User;
import com.mingdong.mis.domain.mapper.ApiReqInfoMapper;
import com.mingdong.mis.domain.mapper.ClientContactMapper;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.domain.mapper.ClientOperateInfoMapper;
import com.mingdong.mis.domain.mapper.ClientOperateLogMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.domain.mapper.SistemMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;
import com.mingdong.mis.domain.mapper.UserMapper;
import com.mingdong.mis.service.ChargeService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientRpcServiceImpl implements ClientRpcService
{
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SistemMapper sistemMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientContactMapper clientContactMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientMessageMapper clientMessageMapper;
    @Resource
    private ClientUserProductMapper clientUserProductMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ClientOperateLogMapper clientOperateLogMapper;
    @Resource
    private ClientOperateInfoMapper clientOperateInfoMapper;
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ApiReqInfoMapper apiReqInfoMapper;
    @Resource
    private RechargeMapper rechargeMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;
    @Resource
    private ProductClientInfoMapper productClientInfoMapper;
    @Resource
    private ChargeService chargeService;
    @Resource
    private ProductMapper productMapper;

    @Override
    public UserDTO userLogin(String username, String password)
    {
        UserDTO userDTO = new UserDTO();
        ClientUser user = clientUserMapper.findByUsername(username);
        if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()))
        {
            userDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return userDTO;
        }
        else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            userDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return userDTO;
        }
        Client client = clientMapper.findById(user.getClientId());
        if(client == null || client.getPrimaryUserId() == null)
        {
            userDTO.setResult(RestResult.INTERNAL_ERROR);
            return userDTO;
        }
        ClientUser primaryUser = clientUserMapper.findById(client.getPrimaryUserId());
        if(primaryUser == null || !TrueOrFalse.TRUE.equals(primaryUser.getEnabled()))
        {
            userDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return userDTO;
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            userDTO.setResult(RestResult.INVALID_PASSCODE);
            return userDTO;
        }
        User manager = userMapper.findById(client.getManagerId());
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
    public ResponseDTO changeUserPassword(Long userId, String orgPassword, String newPassword)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            responseDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return responseDTO;
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(orgPassword)))
        {
            responseDTO.setResult(RestResult.INVALID_PASSCODE);
            return responseDTO;
        }
        ClientUser userUpd = new ClientUser();
        userUpd.setId(userId);
        userUpd.setUpdateTime(new Date());
        userUpd.setPassword(Md5Utils.encrypt(newPassword));
        clientUserMapper.updateSkipNull(userUpd);
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    public ListDTO<MessageDTO> getClientMessage(Long clientId, Page page)
    {
        ListDTO<MessageDTO> listDTO = new ListDTO<>();
        int total = clientMessageMapper.countByClient(clientId);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        List<MessageDTO> list = new ArrayList<>();
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientMessage> messageList = clientMessageMapper.getListByClient(clientId);
            for(ClientMessage o : messageList)
            {
                MessageDTO m = new MessageDTO();
                m.setAddAt(o.getCreateTime());
                m.setType(o.getType());
                m.setContent(o.getContent());
                list.add(m);
            }
        }
        listDTO.setList(list);
        return listDTO;
    }

    @Override
    @Transactional
    public ResponseDTO setSubUserDeleted(Long primaryUserId, Long subUserId)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser primaryUser = clientUserMapper.findById(primaryUserId);
        if(primaryUser == null)
        {
            responseDTO.setResult(RestResult.INTERNAL_ERROR);
            return responseDTO;
        }
        Client client = clientMapper.findById(primaryUser.getClientId());
        if(client == null || !primaryUserId.equals(client.getPrimaryUserId()))
        {
            responseDTO.setResult(RestResult.ONLY_PRIMARY_USER);
            return responseDTO;
        }
        ClientUser subUser = clientUserMapper.findById(subUserId);
        if(subUser == null || !TrueOrFalse.FALSE.equals(subUser.getDeleted()) || !primaryUser.getClientId().equals(
                subUser.getClientId()))
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Date current = new Date();
        ClientUser userUpd = new ClientUser();
        userUpd.setId(subUserId);
        userUpd.setUpdateTime(current);
        userUpd.setDeleted(TrueOrFalse.TRUE);
        clientUserMapper.updateSkipNull(userUpd);
        updateClientAccountQty(client.getId());
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    public ListDTO<SubUserDTO> getSubUserList(Long clientId, Long userId)
    {
        ListDTO<SubUserDTO> res = new ListDTO<>();
        // 查询子账号个数限制
        String max = sistemMapper.getSubAccountMaximum();
        res.addExtra(Field.SUB_ACCOUNT_MAX, max);

        Client client = clientMapper.findById(clientId);
        if(client == null || !userId.equals(client.getPrimaryUserId()))
        {
            return res;
        }
        List<ClientUser> userList = clientUserMapper.getAvailableListByClient(clientId);
        List<SubUserDTO> list = new ArrayList<>();
        for(ClientUser o : userList)
        {
            if(!client.getPrimaryUserId().equals(o.getId()))
            {
                SubUserDTO su = new SubUserDTO();
                su.setUserId(o.getId());
                su.setUsername(o.getUsername());
                su.setName(o.getName());
                su.setPhone(o.getPhone());
                su.setEnabled(o.getEnabled());
                list.add(su);
            }
        }
        res.setTotal(userList.size());
        res.setList(list);
        return res;
    }

    @Override
    public ListDTO<SubUserDTO> getSubUserList(Long clientId, boolean includeDeleted)
    {
        ListDTO<SubUserDTO> res = new ListDTO<>();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return res;
        }
        List<ClientUser> userList;
        if(includeDeleted)
        {
            userList = clientUserMapper.getListByClient(clientId);
        }
        else
        {
            userList = clientUserMapper.getAvailableListByClient(clientId);
        }
        List<SubUserDTO> list = new ArrayList<>();
        for(ClientUser o : userList)
        {
            if(!client.getPrimaryUserId().equals(o.getId()))
            {
                SubUserDTO su = new SubUserDTO();
                su.setUserId(o.getId());
                su.setUsername(o.getUsername());
                su.setName(o.getName());
                su.setPhone(o.getPhone());
                su.setDeleted(o.getDeleted());
                list.add(su);
            }
        }
        res.setTotal(userList.size());
        res.setList(list);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Client client = clientMapper.findByPrimaryAccount(primaryAccountId);
        if(client == null)
        {
            responseDTO.setResult(RestResult.ONLY_PRIMARY_USER);
            return responseDTO;
        }
        ClientUser account = clientUserMapper.findByUsername(username);
        if(account != null)
        {
            responseDTO.setResult(RestResult.USERNAME_EXIST);
            return responseDTO;
        }
        Sistem config = sistemMapper.findByName(SysParam.CLIENT_SUB_USER_QTY);
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
            responseDTO.setResult(RestResult.ACCOUNT_COUNT_MAX);
            return responseDTO;
        }

        Date current = new Date();
        account = new ClientUser();
        account.setId(IDUtils.createUserId(param.getNodeId()));
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
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeSubUserStatus(Long clientId, Long clientUserId, Integer enabled)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(clientUser == null || !clientId.equals(clientUser.getClientId()))
        {
            responseDTO.setResult(RestResult.SUB_USER_NOT_EXIST);
            return responseDTO;
        }
        ClientUser tempUser = new ClientUser();
        tempUser.setId(clientUserId);
        tempUser.setUpdateTime(new Date());
        tempUser.setEnabled(enabled);
        clientUserMapper.updateSkipNull(tempUser);
        return responseDTO;
    }

    @Override
    public UserDTO getAccountByUserId(Long clientUserId)
    {
        UserDTO userDTO = new UserDTO();
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(clientUser == null)
        {
            userDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return userDTO;
        }
        userDTO.setClientId(clientUser.getClientId());
        userDTO.setUserId(clientUser.getId());
        userDTO.setName(clientUser.getName());
        userDTO.setPhone(clientUser.getPhone());
        userDTO.setUsername(clientUser.getUsername());
        userDTO.setEnabled(clientUser.getEnabled());
        return userDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editSubUser(SubUserDTO subUserDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser clientUser = clientUserMapper.findById(subUserDTO.getUserId());
        if(clientUser == null || !subUserDTO.getClientId().equals(clientUser.getClientId()))
        {
            responseDTO.setResult(RestResult.SUB_USER_NOT_EXIST);
            return responseDTO;
        }
        ClientUser temp = clientUserMapper.findByUsername(subUserDTO.getUsername());
        if(temp != null && !subUserDTO.getUserId().equals(temp.getId()))
        {
            responseDTO.setResult(RestResult.USERNAME_EXIST);
            return responseDTO;
        }
        temp = new ClientUser();
        temp.setId(subUserDTO.getUserId());
        temp.setUpdateTime(new Date());
        temp.setUsername(subUserDTO.getUsername());
        temp.setPassword(subUserDTO.getPassword());
        temp.setName(subUserDTO.getName());
        temp.setPhone(subUserDTO.getPhone());
        temp.setEnabled(subUserDTO.getEnabled());
        clientUserMapper.updateSkipNull(temp);
        return responseDTO;
    }

    @Override
    public CredentialDTO getUserCredential(Long userId, String password, Long productId)
    {
        CredentialDTO dto = new CredentialDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null || !user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            dto.getResponseDTO().setResult(RestResult.INVALID_PASSCODE);
            return dto;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            dto.getResponseDTO().setResult(RestResult.PRODUCT_NOT_OPEN);
            return dto;
        }
        dto.setAppId(cp.getAppId());
        ClientUserProduct up = clientUserProductMapper.findByUserAndProduct(userId, productId);
        if(up != null)
        {
            dto.setAppKey(up.getAppSecret());
            dto.setReqHost(up.getReqHost());
        }
        return dto;
    }

    @Override
    @Transactional
    public ResponseDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            responseDTO.setResult(RestResult.INTERNAL_ERROR);
            return responseDTO;
        }
        ClientProduct cp = clientProductMapper.findByClientAndProduct(user.getClientId(), productId);
        if(cp == null)
        {
            responseDTO.setResult(RestResult.PRODUCT_NOT_OPEN);
            return responseDTO;
        }
        Date current = new Date();
        ClientUserProduct up = clientUserProductMapper.findByUserAndProduct(userId, productId);
        if(up == null)
        {
            up = new ClientUserProduct();
            up.setCreateTime(current);
            up.setUpdateTime(current);
            up.setUserId(userId);
            up.setProductId(productId);
            up.setAppSecret(appKey);
            up.setReqHost(reqHost);
            clientUserProductMapper.add(up);
        }
        else
        {
            ClientUserProduct upUpd = new ClientUserProduct();
            upUpd.setId(up.getId());
            upUpd.setUpdateTime(current);
            upUpd.setAppSecret(appKey);
            upUpd.setReqHost(reqHost);
            clientUserProductMapper.updateSkipNull(upUpd);
        }
        return responseDTO;
    }

    @Override
    public ListDTO<ClientInfoDTO> getSimilarCorpByName(String name, Long clientId)
    {
        ListDTO<ClientInfoDTO> listDTO = new ListDTO<>();
        List<ClientInfo> clientInfoList = clientInfoMapper.getSimilarCorpByName(name, clientId);
        if(!CollectionUtils.isEmpty(clientInfoList))
        {
            List<ClientInfoDTO> list = new ArrayList<>(clientInfoList.size());
            for(ClientInfo o : clientInfoList)
            {
                ClientInfoDTO ci = new ClientInfoDTO();
                ci.setCorpName(o.getCorpName());
                ci.setLicense(o.getLicense());
                ci.setManagerName(o.getManagerName());
                ci.setRegisterTime(o.getRegisterTime());
                list.add(ci);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<ClientInfoDTO> getClientInfoListBy(String keyword, Long industryId, Integer enabled, Long managerId,
            Page page)
    {
        List<Long> industryIdList = new ArrayList<>();
        if(industryId != null)
        {
            DictIndustry industry = dictIndustryMapper.findById(industryId);
            if(industry != null)
            {
                if(industry.getParentId() != 0L)
                {
                    industryIdList.add(industryId);
                }
                else
                {
                    List<DictIndustry> industryList = dictIndustryMapper.getByParentAndStatus(industryId,
                            TrueOrFalse.TRUE);
                    for(DictIndustry o : industryList)
                    {
                        industryIdList.add(o.getId());
                    }
                }
            }
        }
        ListDTO<ClientInfoDTO> dto = new ListDTO<>();
        int total = clientMapper.countBy(keyword, industryIdList, enabled, managerId);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getListBy(keyword, industryIdList, enabled, managerId);
            List<ClientInfoDTO> list = new ArrayList<>(dataList.size());
            for(ClientInfo o : dataList)
            {
                ClientInfoDTO ci = new ClientInfoDTO();
                ci.setClientId(o.getClientId());
                ci.setRegisterTime(o.getRegisterTime());
                ci.setCorpName(o.getCorpName());
                ci.setShortName(o.getShortName());
                ci.setIndustryId(o.getIndustryId());
                ci.setUsername(o.getUsername());
                ci.setAccountQty(o.getAccountQty());
                ci.setManagerName(o.getManagerName());
                ci.setUserEnabled(o.getUserEnabled());
                list.add(ci);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    @Transactional
    public void setClientDeleted(List<Long> idList)
    {
        clientMapper.setClientDeleted(idList, new Date());
    }

    @Override
    public void setClientPassword(List<Long> idList, String password)
    {
        if(!CollectionUtils.isEmpty(idList))
        {
            clientUserMapper.resetPasswordByIds(password, new Date(), idList);
        }
    }

    @Override
    public ListDTO<ClientInfoDTO> getClientInfoListByDate(Date startTime, Date endTime, Page page)
    {
        ListDTO<ClientInfoDTO> listDTO = new ListDTO<>();
        int total = statsClientMapper.getClientCountByDate(startTime, endTime);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(startTime, endTime);
            List<ClientInfoDTO> list = new ArrayList<>(dataList.size());
            for(ClientInfo item : dataList)
            {
                ClientInfoDTO clientInfoDTO = new ClientInfoDTO();
                clientInfoDTO.setRegisterTime(item.getRegisterTime());
                clientInfoDTO.setCorpName(item.getCorpName());
                clientInfoDTO.setShortName(item.getShortName());
                clientInfoDTO.setUsername(item.getUsername());
                clientInfoDTO.setManagerName(item.getManagerName());
                list.add(clientInfoDTO);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addNewClient(NewClientDTO req)
    {
        ResponseDTO res = new ResponseDTO();
        ClientUser user = clientUserMapper.findByUsername(req.getUsername());
        if(user != null)
        {
            res.setResult(RestResult.USERNAME_EXIST);
            return res;
        }
        Client ct = clientMapper.findByCorpNameOrCorpLicense(req.getCorpName(), req.getLicense());
        if(ct != null && ct.getCorpName().equals(req.getCorpName()))
        {
            res.setResult(RestResult.CLIENT_NAME_EXIST);
            return res;
        }
        if(ct != null && ct.getLicense().equals(req.getLicense()))
        {
            res.setResult(RestResult.CLIENT_LICENSE_EXIST);
            return res;
        }
        Date current = new Date();
        Long clientId = IDUtils.getClientId(param.getNodeId());
        Long userId = IDUtils.createUserId(param.getNodeId());
        // build corporation contact data list
        List<ClientContact> contactList = new ArrayList<>();
        for(ClientContactDTO o : req.getContactList())
        {
            ClientContact cc = new ClientContact();
            cc.setCreateTime(current);
            cc.setUpdateTime(current);
            cc.setClientId(clientId);
            cc.setName(o.getName());
            cc.setPosition(o.getPosition());
            cc.setPhone(o.getPhone());
            cc.setEmail(o.getEmail());
            cc.setGeneral(o.getGeneral());
            contactList.add(cc);
        }
        // build client primary user
        user = new ClientUser();
        user.setId(userId);
        user.setCreateTime(current);
        user.setUpdateTime(current);
        user.setClientId(clientId);
        user.setName("");
        user.setPhone("");
        user.setEmail("");
        user.setUsername(req.getUsername());
        user.setPassword(Constant.DEFAULT_ENC_PWD);
        user.setEnabled(req.getEnabled());
        user.setDeleted(TrueOrFalse.FALSE);
        // build client
        Client client = new Client();
        client.setId(clientId);
        client.setCreateTime(current);
        client.setUpdateTime(current);
        client.setCorpName(req.getCorpName());
        client.setShortName(req.getShortName());
        client.setLicense(req.getLicense());
        client.setIndustryId(req.getIndustryId());
        client.setPrimaryUserId(userId);
        client.setUsername(req.getUsername());
        client.setManagerId(req.getManagerId());
        client.setAccountQty(0);
        client.setEnabled(TrueOrFalse.TRUE);
        client.setDeleted(TrueOrFalse.FALSE);
        // save data
        clientContactMapper.addList(contactList);
        clientUserMapper.add(user);
        clientMapper.add(client);
        return res;
    }

    @Override
    public ClientDetailDTO getClientDetail(Long clientId)
    {
        ClientDetailDTO res = new ClientDetailDTO();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        List<ClientContact> clientContactList = clientContactMapper.getListByClient(clientId);
        List<ClientUser> clientUserList = clientUserMapper.getListByClientAndStatus(clientId, null, TrueOrFalse.FALSE);
        User manager = userMapper.findById(client.getManagerId());
        res.setClientId(client.getId());
        res.setCorpName(client.getCorpName());
        res.setShortName(client.getShortName());
        res.setLicense(client.getLicense());
        res.setIndustryId(client.getIndustryId());
        res.setAddTime(client.getCreateTime());
        res.setManagerName(manager.getName());
        List<ClientContactDTO> contacts = new ArrayList<>();
        for(ClientContact cc : clientContactList)
        {
            ClientContactDTO clientContactDTO = new ClientContactDTO();
            clientContactDTO.setName(cc.getName());
            clientContactDTO.setPosition(cc.getPosition());
            clientContactDTO.setPhone(cc.getPhone());
            clientContactDTO.setEmail(cc.getEmail());
            clientContactDTO.setGeneral(cc.getGeneral());
            contacts.add(clientContactDTO);
        }
        res.setContacts(contacts);
        List<ClientUserDTO> users = new ArrayList<>();
        for(ClientUser user : clientUserList)
        {
            if(client.getPrimaryUserId().equals(user.getId()))
            {
                res.setUsername(user.getUsername());
                res.setUserStatus(user.getEnabled());
            }
            else
            {
                ClientUserDTO clientUserDTO = new ClientUserDTO();
                clientUserDTO.setId(user.getId());
                clientUserDTO.setUsername(user.getUsername());
                clientUserDTO.setName(user.getName());
                clientUserDTO.setPhone(user.getPhone());
                users.add(clientUserDTO);
            }
        }
        res.setUsers(users);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO changeClientStatus(DisableClientDTO disableClientDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        List<Client> clientList = clientMapper.getListByIdList(disableClientDTO.getClientIdList());
        if(CollectionUtils.isEmpty(clientList))
        {
            return responseDTO;
        }
        Date date = new Date();
        List<Long> clientIdList = new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        List<ClientOperateLog> logList = new ArrayList<>(clientIdList.size());
        for(Client o : clientList)
        {
            if(!o.getEnabled().equals(disableClientDTO.getEnabled()))
            {
                clientIdList.add(o.getId());
                userIdList.add(o.getPrimaryUserId());
                ClientOperateLog log = new ClientOperateLog();
                log.setCreateTime(date);
                log.setUpdateTime(date);
                log.setClientId(o.getId());
                log.setClientUserId(o.getPrimaryUserId());
                log.setManagerId(disableClientDTO.getManagerId());
                log.setType(disableClientDTO.getEnabled());
                log.setReason(disableClientDTO.getReason());
                logList.add(log);
            }
        }
        if(clientIdList.size() > 0)
        {
            clientOperateLogMapper.addList(logList);
            clientUserMapper.updateStatusByIds(disableClientDTO.getEnabled(), date, userIdList);
            clientMapper.updateStatusByIds(disableClientDTO.getEnabled(), date, clientIdList);
        }
        return responseDTO;
    }

    @Override
    public ClientDetailDTO getClientInfoForEdit(Long clientId)
    {
        ClientDetailDTO res = new ClientDetailDTO();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        res.setManagerId(client.getManagerId());
        res.setClientId(client.getId());
        res.setCorpName(client.getCorpName());
        res.setShortName(client.getShortName());
        res.setLicense(client.getLicense());
        res.setIndustryId(client.getIndustryId());
        ClientUser clientUser = clientUserMapper.findById(client.getPrimaryUserId());
        res.setUsername(clientUser.getUsername());
        res.setUserStatus(clientUser.getEnabled());
        List<ClientContact> clientContactList = clientContactMapper.getListByClient(clientId);
        List<ClientContactDTO> contacts = new ArrayList<>();
        for(ClientContact cc : clientContactList)
        {
            ClientContactDTO clientContactDTO = new ClientContactDTO();
            clientContactDTO.setId(cc.getId());
            clientContactDTO.setName(cc.getName());
            clientContactDTO.setPosition(cc.getPosition());
            clientContactDTO.setPhone(cc.getPhone());
            clientContactDTO.setEmail(cc.getEmail());
            clientContactDTO.setGeneral(cc.getGeneral());
            contacts.add(clientContactDTO);
        }
        res.setContacts(contacts);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editClient(NewClientDTO dto, List<ClientContactDTO> contacts, List<Long> delIds)
    {
        ResponseDTO res = new ResponseDTO();
        Client client = clientMapper.findById(dto.getClientId());
        if(client == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        Date current = new Date();
        Client clientUpd = new Client();
        clientUpd.setId(dto.getClientId());
        clientUpd.setUpdateTime(current);
        clientUpd.setCorpName(dto.getCorpName());
        clientUpd.setShortName(dto.getShortName());
        clientUpd.setLicense(dto.getLicense());
        clientUpd.setIndustryId(dto.getIndustryId());
        clientUpd.setManagerId(dto.getManagerId());
        clientMapper.updateSkipNull(clientUpd);
        ClientUser userUpd = new ClientUser();
        userUpd.setId(client.getPrimaryUserId());
        userUpd.setUpdateTime(current);
        userUpd.setEnabled(dto.getEnabled());
        clientUserMapper.updateSkipNull(userUpd);
        if(!CollectionUtils.isEmpty(delIds))
        {
            clientContactMapper.deleteByIds(delIds);
        }
        if(!CollectionUtils.isEmpty(contacts))
        {
            List<ClientContact> addList = new ArrayList<>();
            for(ClientContactDTO o : contacts)
            {
                if(o.getId() == null)
                {
                    ClientContact cc = new ClientContact();
                    cc.setCreateTime(current);
                    cc.setUpdateTime(current);
                    cc.setClientId(dto.getClientId());
                    cc.setName(o.getName());
                    cc.setPosition(o.getPosition());
                    cc.setPhone(o.getPhone());
                    cc.setEmail(o.getEmail());
                    cc.setGeneral(o.getGeneral());
                    addList.add(cc);
                }
                else
                {
                    ClientContact cc = clientContactMapper.findById(o.getId());
                    if(cc != null)
                    {
                        cc.setUpdateTime(current);
                        cc.setClientId(dto.getClientId());
                        cc.setName(o.getName());
                        cc.setPosition(o.getPosition());
                        cc.setPhone(o.getPhone());
                        cc.setEmail(o.getEmail());
                        cc.setGeneral(o.getGeneral());
                        clientContactMapper.updateById(cc);
                    }
                }
            }
            if(addList.size() > 0)
            {
                clientContactMapper.addList(addList);
            }
        }
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO selectCustomProduct(Long clientId, List<Long> productIds)
    {
        ResponseDTO res = new ResponseDTO();
        List<ProductClientInfo> productClientInfoList = productClientInfoMapper.getClientProductCustomBy(clientId);
        if(productIds == null)
        {
            productIds = new ArrayList<>();
        }
        boolean canDelete = true;
        List<ClientProduct> addList = new ArrayList<>();
        List<Long> deleteIds = new ArrayList<>();
        ClientProduct clientProduct;
        for(ProductClientInfo item : productClientInfoList)
        {
            if(!productIds.contains(item.getProductId()))
            {
                deleteIds.add(item.getClientProductId());
                if(TrueOrFalse.TRUE.equals(item.getCustom()) && TrueOrFalse.TRUE.equals(item.getOpened()))
                {
                    canDelete = false;
                    break;
                }
            }
            else
            {
                productIds.remove(item.getProductId());
            }
        }
        if(canDelete)
        {
            if(!CollectionUtils.isEmpty(deleteIds))
            {
                clientProductMapper.deleteByIds(deleteIds.toArray(new Long[0]));
            }
            if(!CollectionUtils.isEmpty(productIds))
            {
                Date date = new Date();
                for(Long productId : productIds)
                {
                    clientProduct = new ClientProduct();
                    clientProduct.setId(IDUtils.getClientProductId(1));
                    clientProduct.setClientId(clientId);
                    clientProduct.setProductId(productId);
                    clientProduct.setOpened(TrueOrFalse.FALSE);
                    clientProduct.setCreateTime(date);
                    clientProduct.setUpdateTime(date);
                    clientProduct.setAppId(StringUtils.getUuid());
                    addList.add(clientProduct);
                }
                clientProductMapper.addAll(addList);
            }
        }
        else
        {
            res.setResult(RestResult.CLIENT_PRODUCT_NO_DELETE);
        }
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO removeCustomClientProduct(Long clientProductId)
    {
        ResponseDTO res = new ResponseDTO();
        ProductClientInfo clientProductInfo = productClientInfoMapper.getClientProductInfo(clientProductId);
        if(clientProductInfo == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        if(TrueOrFalse.TRUE.equals(clientProductInfo.getOpened()))
        {
            res.setResult(RestResult.CLIENT_PRODUCT_NO_DELETE);
            return res;
        }
        clientProductMapper.deleteByIds(new Long[]{clientProductId});
        return res;
    }

    @Override
    public ListDTO<ApiReqInfoDTO> getClientBillListBy(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Long managerId, Page page)
    {
        ListDTO<ApiReqInfoDTO> dto = new ListDTO<>();
        int total = apiReqInfoMapper.countBy1(keyword, productId, billPlan, fromDate, toDate, managerId);
        int pages = page.getTotalPage(total);
        BigDecimal totalFee = apiReqInfoMapper.sumFeeBy(keyword, productId, billPlan, fromDate, toDate, managerId);
        int missCount = apiReqInfoMapper.countMiss(keyword, productId, billPlan, fromDate, toDate, managerId);
        dto.setTotal(total);
        dto.addExtra(Field.MISS_COUNT, missCount + "");
        dto.addExtra(Field.TOTAL_FEE, NumberUtils.formatAmount(totalFee));
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> dataList = apiReqInfoMapper.getListBy1(keyword, productId, billPlan, fromDate, toDate,
                    managerId);
            List<ApiReqInfoDTO> list = new ArrayList<>(dataList.size());
            for(ApiReqInfo o : dataList)
            {
                ApiReqInfoDTO ari = new ApiReqInfoDTO();
                ari.setCreateTime(o.getCreateTime());
                ari.setRequestNo(o.getRequestNo());
                ari.setCorpName(o.getCorpName());
                ari.setShortName(o.getShortName());
                ari.setUsername(o.getUsername());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(o.getFee());
                ari.setBalance(o.getBalance());
                ari.setUserId(o.getUserId());
                ari.setPrimaryUserId(o.getPrimaryUserId());
                list.add(ari);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    public ClientUserDictDTO getClientAccountDict(Long clientId)
    {
        ClientUserDictDTO res = new ClientUserDictDTO();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return res;
        }
        res.setCorpName(client.getCorpName());
        List<ClientUser> userList = clientUserMapper.getAvailableListByClient(clientId);
        List<Dict> userDict = new ArrayList<>();
        for(ClientUser o : userList)
        {
            if(o.getId().equals(client.getPrimaryUserId()))
            {
                userDict.add(new Dict(o.getId() + "", o.getUsername() + "（主）"));
            }
            else
            {
                userDict.add(new Dict(o.getId() + "", o.getUsername() + "（" + o.getName() + "）"));
            }
        }
        res.setUserDict(userDict);
        return res;
    }

    @Override
    public ListDTO<AccessDTO> getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate,
            Date toDate, Page page)
    {
        ListDTO<AccessDTO> listDTO = new ListDTO<>();
        int total = apiReqInfoMapper.countByClient(clientId, userId, productId, fromDate, toDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> dataList = apiReqInfoMapper.getListByClient(clientId, userId, productId, fromDate, toDate);
            List<AccessDTO> list = new ArrayList<>();
            for(ApiReqInfo o : dataList)
            {
                AccessDTO r = new AccessDTO();
                r.setRequestAt(o.getCreateTime());
                r.setRequestNo(o.getRequestNo());
                r.setUsername(o.getUsername());
                r.setProductName(o.getProductName());
                r.setBillPlan(o.getBillPlan());
                r.setHit(o.getHit());
                r.setFee(o.getFee());
                r.setBalance(o.getBalance());
                list.add(r);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public String getClientCorpName(Long clientId)
    {
        Client client = clientMapper.findById(clientId);
        return client != null ? client.getCorpName() : null;
    }

    @Override
    public ListDTO<RechargeReqDTO> getClientRechargeList(Long clientId, Long productId, Date fromDate, Date toDate,
            Page page)
    {
        ListDTO<RechargeReqDTO> listDTO = new ListDTO<>();
        int total = productRechargeInfoMapper.countByClient(clientId, productId, fromDate, toDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListByClient(clientId, productId,
                    fromDate, toDate);
            List<RechargeReqDTO> list = new ArrayList<>();
            for(ProductRechargeInfo o : dataList)
            {
                RechargeReqDTO r = new RechargeReqDTO();
                r.setRechargeAt(o.getTradeTime());
                r.setRechargeNo(o.getTradeNo());
                r.setProductName(o.getProductName());
                r.setRechargeTypeName(o.getRechargeType());
                r.setBillPlan(o.getBillPlan());
                r.setAmount(o.getAmount());
                r.setBalance(o.getBalance());
                r.setManagerName(o.getManagerName());
                r.setContractNo(o.getContractNo());
                list.add(r);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<ApiReqInfoDTO> getRevenueList(Date fromDate, Date toDate, Page page)
    {
        ListDTO<ApiReqInfoDTO> listDTO = new ListDTO<>();
        BigDecimal totalFee = apiReqInfoMapper.sumFeeBy(null, null, null, fromDate, toDate, null);
        listDTO.addExtra(Field.TOTAL_FEE, NumberUtils.formatAmount(totalFee));
        int total = apiReqInfoMapper.getRevenueListCount(fromDate, toDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> dataList = apiReqInfoMapper.getRevenueList(fromDate, toDate);
            List<ApiReqInfoDTO> list = new ArrayList<>();
            for(ApiReqInfo o : dataList)
            {
                ApiReqInfoDTO r = new ApiReqInfoDTO();
                EntityUtils.copyProperties(o, r);
                list.add(r);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<ClientOperateLogDTO> getClientOperateLog(Long clientId, Page page)
    {
        ListDTO<ClientOperateLogDTO> listDTO = new ListDTO<>();
        int total = clientOperateInfoMapper.countByClient(clientId);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientOperateInfo> dataList = clientOperateInfoMapper.getListByClient(clientId);
            if(!CollectionUtils.isEmpty(dataList))
            {
                List<ClientOperateLogDTO> list = new ArrayList<>(dataList.size());
                for(ClientOperateInfo o : dataList)
                {
                    ClientOperateLogDTO col = new ClientOperateLogDTO();
                    col.setOperateTime(o.getOperateTime());
                    col.setType(o.getType());
                    col.setReason(o.getReason());
                    col.setManagerName(o.getManagerName());
                    list.add(col);
                }
                listDTO.setList(list);
            }
        }
        return listDTO;
    }

    @Override
    public RechargeInfoDTO getLatestRechargeInfo(Long clientId, Long productId)
    {
        RechargeInfoDTO rechargeInfoDTO = new RechargeInfoDTO();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(clientProduct == null || !TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
        {
            rechargeInfoDTO.setResult(RestResult.PRODUCT_NOT_OPEN);
            return rechargeInfoDTO;
        }
        Recharge recharge = rechargeMapper.findById(clientProduct.getLatestRechargeId());
        if(recharge != null)
        {
            rechargeInfoDTO.setBillPlan(recharge.getBillPlan());
            rechargeInfoDTO.setAmount(recharge.getAmount());
            rechargeInfoDTO.setBalance(recharge.getBalance());
            rechargeInfoDTO.setStartDate(recharge.getStartDate());
            rechargeInfoDTO.setEndDate(recharge.getEndDate());
            rechargeInfoDTO.setUnitAmt(recharge.getUnitAmt());
        }
        BigDecimal totalRecharge = rechargeMapper.sumRechargeAmount(clientId, productId);
        rechargeInfoDTO.setTotalRecharge(totalRecharge);
        return rechargeInfoDTO;
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