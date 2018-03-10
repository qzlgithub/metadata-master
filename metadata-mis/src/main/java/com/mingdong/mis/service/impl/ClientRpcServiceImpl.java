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
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ClientContactReqDTO;
import com.mingdong.core.model.dto.request.DisableClientReqDTO;
import com.mingdong.core.model.dto.request.NewClientReqDTO;
import com.mingdong.core.model.dto.request.SubUserReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientOperateLogResDTO;
import com.mingdong.core.model.dto.response.ClientUserDictResDTO;
import com.mingdong.core.model.dto.response.ClientUserResDTO;
import com.mingdong.core.model.dto.response.CredentialResDTO;
import com.mingdong.core.model.dto.response.MessageResDTO;
import com.mingdong.core.model.dto.response.ProductRechargeResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.SubUserResDTO;
import com.mingdong.core.model.dto.response.UserResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.component.Param;
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
import com.mingdong.mis.domain.mapper.ApiReqMapper;
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
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.domain.mapper.SistemMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;
import com.mingdong.mis.domain.mapper.UserMapper;
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
    private ApiReqMapper apiReqMapper;

    @Override
    public UserResDTO userLogin(String username, String password)
    {
        UserResDTO userResDTO = new UserResDTO();
        ClientUser user = clientUserMapper.findByUsername(username);
        if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()))
        {
            userResDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return userResDTO;
        }
        else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            userResDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return userResDTO;
        }
        Client client = clientMapper.findById(user.getClientId());
        if(client == null || client.getPrimaryUserId() == null)
        {
            userResDTO.setResult(RestResult.INTERNAL_ERROR);
            return userResDTO;
        }
        ClientUser primaryUser = clientUserMapper.findById(client.getPrimaryUserId());
        if(primaryUser == null || !TrueOrFalse.TRUE.equals(primaryUser.getEnabled()))
        {
            userResDTO.setResult(RestResult.ACCOUNT_DISABLED);
            return userResDTO;
        }
        if(!user.getPassword().equals(Md5Utils.encrypt(password)))
        {
            userResDTO.setResult(RestResult.INVALID_PASSCODE);
            return userResDTO;
        }
        User manager = userMapper.findById(client.getManagerId());
        userResDTO.setClientId(user.getClientId());
        userResDTO.setUserId(user.getId());
        userResDTO.setPrimary(user.getId().equals(client.getPrimaryUserId()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        userResDTO.setName(user.getName());
        userResDTO.setManagerQq(manager == null ? "" : manager.getQq());
        userResDTO.setFirstLogin(
                Constant.DEFAULT_ENC_PWD.equals(user.getPassword()) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return userResDTO;
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
            responseDTO.setResult(RestResult.OLD_PASSCODE);
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
    public ListDTO<MessageResDTO> getClientMessage(Long clientId, Page page)
    {
        ListDTO<MessageResDTO> listDTO = new ListDTO<>();
        int total = clientMessageMapper.countByClient(clientId);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        List<MessageResDTO> list = new ArrayList<>();
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientMessage> messageList = clientMessageMapper.getListByClient(clientId);
            for(ClientMessage o : messageList)
            {
                MessageResDTO m = new MessageResDTO();
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
    public ListDTO<SubUserResDTO> getSubUserList(Long clientId, Long userId)
    {
        ListDTO<SubUserResDTO> res = new ListDTO<>();
        // 查询子账号个数限制
        String max = sistemMapper.getSubAccountMaximum();
        res.addExtra(Field.SUB_ACCOUNT_MAX, max);

        Client client = clientMapper.findById(clientId);
        if(client == null || !userId.equals(client.getPrimaryUserId()))
        {
            return res;
        }
        List<ClientUser> userList = clientUserMapper.getAvailableListByClient(clientId);
        List<SubUserResDTO> list = new ArrayList<>();
        for(ClientUser o : userList)
        {
            if(!client.getPrimaryUserId().equals(o.getId()))
            {
                SubUserResDTO su = new SubUserResDTO();
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
    public ListDTO<SubUserResDTO> getSubUserList(Long clientId, boolean includeDeleted)
    {
        ListDTO<SubUserResDTO> res = new ListDTO<>();
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
        List<SubUserResDTO> list = new ArrayList<>();
        for(ClientUser o : userList)
        {
            if(!client.getPrimaryUserId().equals(o.getId()))
            {
                SubUserResDTO su = new SubUserResDTO();
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
    public UserResDTO getAccountByUserId(Long clientUserId)
    {
        UserResDTO userResDTO = new UserResDTO();
        ClientUser clientUser = clientUserMapper.findById(clientUserId);
        if(clientUser == null)
        {
            userResDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return userResDTO;
        }
        userResDTO.setClientId(clientUser.getClientId());
        userResDTO.setUserId(clientUser.getId());
        userResDTO.setName(clientUser.getName());
        userResDTO.setPhone(clientUser.getPhone());
        userResDTO.setUsername(clientUser.getUsername());
        userResDTO.setEnabled(clientUser.getEnabled());
        return userResDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editSubUser(SubUserReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser clientUser = clientUserMapper.findById(reqDTO.getUserId());
        if(clientUser == null || !reqDTO.getClientId().equals(clientUser.getClientId()))
        {
            responseDTO.setResult(RestResult.SUB_USER_NOT_EXIST);
            return responseDTO;
        }
        ClientUser temp = clientUserMapper.findByUsername(reqDTO.getUsername());
        if(temp != null && !reqDTO.getUserId().equals(temp.getId()))
        {
            responseDTO.setResult(RestResult.USERNAME_EXIST);
            return responseDTO;
        }
        temp = new ClientUser();
        temp.setId(reqDTO.getUserId());
        temp.setUpdateTime(new Date());
        temp.setUsername(reqDTO.getUsername());
        temp.setPassword(reqDTO.getPassword());
        temp.setName(reqDTO.getName());
        temp.setPhone(reqDTO.getPhone());
        temp.setEnabled(reqDTO.getEnabled());
        clientUserMapper.updateSkipNull(temp);
        return responseDTO;
    }

    @Override
    public CredentialResDTO getUserCredential(Long userId, String password, Long productId)
    {
        CredentialResDTO dto = new CredentialResDTO();
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
    public ListDTO<ClientInfoResDTO> getSimilarCorpByName(String name, Long clientId)
    {
        ListDTO<ClientInfoResDTO> listDTO = new ListDTO<>();
        List<ClientInfo> clientInfoList = clientInfoMapper.getSimilarCorpByName(name, clientId);
        if(!CollectionUtils.isEmpty(clientInfoList))
        {
            List<ClientInfoResDTO> list = new ArrayList<>(clientInfoList.size());
            for(ClientInfo o : clientInfoList)
            {
                ClientInfoResDTO ci = new ClientInfoResDTO();
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
    public ListDTO<ClientInfoResDTO> getClientInfoListBy(String keyword, Long industryId, Integer enabled,
            Long managerId, Page page)
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
        ListDTO<ClientInfoResDTO> dto = new ListDTO<>();
        int total = clientMapper.countBy(keyword, industryIdList, enabled, managerId);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getListBy(keyword, industryIdList, enabled, managerId);
            List<ClientInfoResDTO> list = new ArrayList<>(dataList.size());
            for(ClientInfo o : dataList)
            {
                ClientInfoResDTO ci = new ClientInfoResDTO();
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
    public ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date startTime, Date endTime, Page page)
    {
        ListDTO<ClientInfoResDTO> listDTO = new ListDTO<>();
        int total = statsClientMapper.getClientCountByDate(startTime, endTime);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(startTime, endTime);
            List<ClientInfoResDTO> list = new ArrayList<>(dataList.size());
            for(ClientInfo item : dataList)
            {
                ClientInfoResDTO clientInfoReqDTO = new ClientInfoResDTO();
                clientInfoReqDTO.setRegisterTime(item.getRegisterTime());
                clientInfoReqDTO.setCorpName(item.getCorpName());
                clientInfoReqDTO.setShortName(item.getShortName());
                clientInfoReqDTO.setUsername(item.getUsername());
                clientInfoReqDTO.setManagerName(item.getManagerName());
                list.add(clientInfoReqDTO);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addNewClient(NewClientReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        ClientUser user = clientUserMapper.findByUsername(reqDTO.getUsername());
        if(user != null)
        {
            res.setResult(RestResult.USERNAME_EXIST);
            return res;
        }
        Client ct = clientMapper.findByCorpNameOrCorpLicense(reqDTO.getCorpName(), reqDTO.getLicense());
        if(ct != null && ct.getCorpName().equals(reqDTO.getCorpName()))
        {
            res.setResult(RestResult.CLIENT_NAME_EXIST);
            return res;
        }
        if(ct != null && ct.getLicense().equals(reqDTO.getLicense()))
        {
            res.setResult(RestResult.CLIENT_LICENSE_EXIST);
            return res;
        }
        Date current = new Date();
        Long clientId = IDUtils.getClientId(param.getNodeId());
        Long userId = IDUtils.createUserId(param.getNodeId());
        // build corporation contact data list
        List<ClientContact> contactList = new ArrayList<>();
        for(ClientContactReqDTO o : reqDTO.getContactList())
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
        user.setName(reqDTO.getCorpName());
        user.setPhone("");
        user.setEmail("");
        user.setUsername(reqDTO.getUsername());
        user.setPassword(Constant.DEFAULT_ENC_PWD);
        user.setEnabled(reqDTO.getEnabled());
        user.setDeleted(TrueOrFalse.FALSE);
        // build client
        Client client = new Client();
        client.setId(clientId);
        client.setCreateTime(current);
        client.setUpdateTime(current);
        client.setCorpName(reqDTO.getCorpName());
        client.setShortName(reqDTO.getShortName());
        client.setLicense(reqDTO.getLicense());
        client.setIndustryId(reqDTO.getIndustryId());
        client.setPrimaryUserId(userId);
        client.setUsername(reqDTO.getUsername());
        client.setManagerId(reqDTO.getManagerId());
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
    public ClientDetailResDTO getClientDetail(Long clientId)
    {
        ClientDetailResDTO res = new ClientDetailResDTO();
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
        List<ClientContactReqDTO> contacts = new ArrayList<>();
        for(ClientContact o : clientContactList)
        {
            ClientContactReqDTO reqDTO = new ClientContactReqDTO();
            reqDTO.setName(o.getName());
            reqDTO.setPosition(o.getPosition());
            reqDTO.setPhone(o.getPhone());
            reqDTO.setEmail(o.getEmail());
            reqDTO.setGeneral(o.getGeneral());
            contacts.add(reqDTO);
        }
        res.setContacts(contacts);
        List<ClientUserResDTO> users = new ArrayList<>();
        for(ClientUser user : clientUserList)
        {
            if(client.getPrimaryUserId().equals(user.getId()))
            {
                res.setUsername(user.getUsername());
                res.setUserStatus(user.getEnabled());
            }
            else
            {
                ClientUserResDTO clientUserResDTO = new ClientUserResDTO();
                clientUserResDTO.setId(user.getId());
                clientUserResDTO.setUsername(user.getUsername());
                clientUserResDTO.setName(user.getName());
                clientUserResDTO.setPhone(user.getPhone());
                users.add(clientUserResDTO);
            }
        }
        res.setUsers(users);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO changeClientStatus(DisableClientReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        List<Client> clientList = clientMapper.getListByIdList(reqDTO.getClientIdList());
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
            if(!o.getEnabled().equals(reqDTO.getEnabled()))
            {
                clientIdList.add(o.getId());
                userIdList.add(o.getPrimaryUserId());
                ClientOperateLog log = new ClientOperateLog();
                log.setCreateTime(date);
                log.setUpdateTime(date);
                log.setClientId(o.getId());
                log.setClientUserId(o.getPrimaryUserId());
                log.setManagerId(reqDTO.getManagerId());
                log.setType(reqDTO.getEnabled());
                log.setReason(reqDTO.getReason());
                logList.add(log);
            }
        }
        if(clientIdList.size() > 0)
        {
            clientOperateLogMapper.addList(logList);
            clientUserMapper.updateStatusByIds(reqDTO.getEnabled(), date, userIdList);
            clientMapper.updateStatusByIds(reqDTO.getEnabled(), date, clientIdList);
        }
        return responseDTO;
    }

    @Override
    public ClientDetailResDTO getClientInfoForEdit(Long clientId)
    {
        ClientDetailResDTO res = new ClientDetailResDTO();
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
        List<ClientContactReqDTO> contacts = new ArrayList<>();
        for(ClientContact o : clientContactList)
        {
            ClientContactReqDTO reqDTO = new ClientContactReqDTO();
            reqDTO.setId(o.getId());
            reqDTO.setName(o.getName());
            reqDTO.setPosition(o.getPosition());
            reqDTO.setPhone(o.getPhone());
            reqDTO.setEmail(o.getEmail());
            reqDTO.setGeneral(o.getGeneral());
            contacts.add(reqDTO);
        }
        res.setContacts(contacts);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editClient(NewClientReqDTO dto, List<ClientContactReqDTO> contacts, List<Long> delIds)
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
            for(ClientContactReqDTO o : contacts)
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
    public ListDTO<AccessResDTO> getClientBillListBy(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Long managerId, Page page)
    {
        ListDTO<AccessResDTO> dto = new ListDTO<>();
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
            List<AccessResDTO> list = new ArrayList<>(dataList.size());
            for(ApiReqInfo o : dataList)
            {
                AccessResDTO ari = new AccessResDTO();
                ari.setRequestAt(o.getCreateTime());
                ari.setRequestNo(o.getRequestNo());
                ari.setCorpName(o.getCorpName());
                ari.setShortName(o.getShortName());
                ari.setPrimaryUsername(o.getPrimaryUsername());
                ari.setUsername(o.getUsername());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(o.getFee());
                ari.setBalance(o.getBalance());
                list.add(ari);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    public ClientUserDictResDTO getClientAccountDict(Long clientId)
    {
        ClientUserDictResDTO res = new ClientUserDictResDTO();
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
    public String getClientCorpName(Long clientId)
    {
        Client client = clientMapper.findById(clientId);
        return client != null ? client.getCorpName() : null;
    }

    @Override
    public ListDTO<AccessResDTO> getClientRequestList(Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page)
    {
        ListDTO<AccessResDTO> listDTO = new ListDTO<>();
        int total = apiReqInfoMapper.countByClient(clientId, userId, productId, startDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> dataList = apiReqInfoMapper.getListByClient(clientId, userId, productId, startDate,
                    endDate);
            List<AccessResDTO> list = new ArrayList<>();
            for(ApiReqInfo o : dataList)
            {
                AccessResDTO r = new AccessResDTO();
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
    public ListDTO<AccessResDTO> getApiRequestRecord(Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page)
    {
        ListDTO<AccessResDTO> listDTO = new ListDTO<>();
        int total = apiReqMapper.countBy(clientId, userId, productId, startDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, userId, productId, startDate,
                    endDate);
            List<AccessResDTO> list = new ArrayList<>(apiReqInfoList.size());
            for(ApiReqInfo o : apiReqInfoList)
            {
                AccessResDTO ari = new AccessResDTO();
                ari.setRequestAt(o.getCreateTime());
                ari.setRequestNo(o.getRequestNo());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(o.getFee());
                ari.setBalance(o.getBalance());
                list.add(ari);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<RechargeResDTO> getClientRechargeList(Long clientId, Long productId, Date fromDate, Date toDate,
            Page page)
    {
        ListDTO<RechargeResDTO> listDTO = new ListDTO<>();
        int total = productRechargeInfoMapper.countByClient(clientId, productId, fromDate, toDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListByClient(clientId, productId,
                    fromDate, toDate);
            List<RechargeResDTO> list = new ArrayList<>();
            for(ProductRechargeInfo o : dataList)
            {
                RechargeResDTO r = new RechargeResDTO();
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
    public ListDTO<ProductRechargeResDTO> getProductRechargeRecord(Long clientId, Long productId, Date startDate,
            Date endDate, Page page)
    {
        ListDTO<ProductRechargeResDTO> listDTO = new ListDTO<>();
        int total = rechargeMapper.countBy(clientId, productId, startDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, startDate,
                    endDate);
            List<ProductRechargeResDTO> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo o : dataList)
            {
                ProductRechargeResDTO pri = new ProductRechargeResDTO();
                pri.setTradeTime(o.getTradeTime());
                pri.setTradeNo(o.getTradeNo());
                pri.setRechargeType(o.getRechargeType());
                pri.setBillPlan(o.getBillPlan());
                pri.setProductName(o.getProductName());
                pri.setAmount(o.getAmount());
                pri.setBalance(o.getBalance());
                pri.setContractNo(o.getContractNo());
                pri.setCorpName(o.getCorpName());
                pri.setShortName(o.getShortName());
                pri.setUsername(o.getUsername());
                pri.setManagerName(o.getManagerName());
                list.add(pri);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<AccessResDTO> getRevenueList(Date fromDate, Date toDate, Page page)
    {
        ListDTO<AccessResDTO> listDTO = new ListDTO<>();
        BigDecimal totalFee = apiReqInfoMapper.sumFeeBy(null, null, null, fromDate, toDate, null);
        listDTO.addExtra(Field.TOTAL_FEE, NumberUtils.formatAmount(totalFee));
        int total = apiReqInfoMapper.getRevenueListCount(fromDate, toDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> dataList = apiReqInfoMapper.getRevenueList(fromDate, toDate);
            List<AccessResDTO> list = new ArrayList<>();
            for(ApiReqInfo o : dataList)
            {
                AccessResDTO r = new AccessResDTO();
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
    public ListDTO<ClientOperateLogResDTO> getClientOperateLog(Long clientId, Page page)
    {
        ListDTO<ClientOperateLogResDTO> listDTO = new ListDTO<>();
        int total = clientOperateInfoMapper.countByClient(clientId);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientOperateInfo> dataList = clientOperateInfoMapper.getListByClient(clientId);
            if(!CollectionUtils.isEmpty(dataList))
            {
                List<ClientOperateLogResDTO> list = new ArrayList<>(dataList.size());
                for(ClientOperateInfo o : dataList)
                {
                    ClientOperateLogResDTO col = new ClientOperateLogResDTO();
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
    public RechargeResDTO getLatestRechargeInfo(Long clientId, Long productId)
    {
        RechargeResDTO resDTO = new RechargeResDTO();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(clientProduct == null || !TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
        {
            resDTO.setResult(RestResult.PRODUCT_NOT_OPEN);
            return resDTO;
        }
        Recharge recharge = rechargeMapper.findById(clientProduct.getLatestRechargeId());
        if(recharge != null)
        {
            resDTO.setBillPlan(recharge.getBillPlan());
            resDTO.setAmount(recharge.getAmount());
            resDTO.setBalance(recharge.getBalance());
            resDTO.setStartDate(recharge.getStartDate());
            resDTO.setEndDate(recharge.getEndDate());
            resDTO.setUnitAmt(recharge.getUnitAmt());
        }
        BigDecimal totalRecharge = rechargeMapper.sumRechargeAmount(clientId, productId);
        resDTO.setTotalRecharge(totalRecharge);
        return resDTO;
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