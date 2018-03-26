package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.ClientRemindType;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ClientContactReqDTO;
import com.mingdong.core.model.dto.request.ClientReqDTO;
import com.mingdong.core.model.dto.request.ClientUserReqDTO;
import com.mingdong.core.model.dto.request.DisableClientReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientOperateLogResDTO;
import com.mingdong.core.model.dto.response.ClientRemindResInfoDTO;
import com.mingdong.core.model.dto.response.ClientUserDictResDTO;
import com.mingdong.core.model.dto.response.ClientUserResDTO;
import com.mingdong.core.model.dto.response.CredentialResDTO;
import com.mingdong.core.model.dto.response.MessageResDTO;
import com.mingdong.core.model.dto.response.RechargeInfoResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.SubUserResDTO;
import com.mingdong.core.model.dto.response.UserResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.util.DateCalculateUtils;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientContact;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.entity.ClientOperateInfo;
import com.mingdong.mis.domain.entity.ClientOperateLog;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientProductInfo;
import com.mingdong.mis.domain.entity.ClientRemind;
import com.mingdong.mis.domain.entity.ClientRemindInfo;
import com.mingdong.mis.domain.entity.ClientRemindProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.entity.Stats;
import com.mingdong.mis.domain.entity.StatsRecharge;
import com.mingdong.mis.domain.entity.User;
import com.mingdong.mis.domain.mapper.ClientContactMapper;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.domain.mapper.ClientOperateInfoMapper;
import com.mingdong.mis.domain.mapper.ClientOperateLogMapper;
import com.mingdong.mis.domain.mapper.ClientProductInfoMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientRemindInfoMapper;
import com.mingdong.mis.domain.mapper.ClientRemindMapper;
import com.mingdong.mis.domain.mapper.ClientRemindProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.domain.mapper.SistemMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;
import com.mingdong.mis.domain.mapper.StatsMapper;
import com.mingdong.mis.domain.mapper.StatsRechargeMapper;
import com.mingdong.mis.domain.mapper.UserMapper;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientRpcServiceImpl implements ClientRpcService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientRpcServiceImpl.class);
    private static final Integer INC_STAT = 1;
    private static final Integer REQ_STAT = 2;
    private static final Integer RCG_STAT = 3;
    @Resource
    private SistemMapper sistemMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StatsMapper statsMapper;
    @Resource
    private StatsRechargeMapper statsRechargeMapper;
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
    private RechargeMapper rechargeMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;
    @Resource
    private ProductClientInfoMapper productClientInfoMapper;
    @Resource
    private RequestLogDao requestLogDao;
    @Resource
    private ClientRemindInfoMapper clientRemindInfoMapper;
    @Resource
    private ClientProductInfoMapper clientProductInfoMapper;
    @Resource
    private ClientRemindMapper clientRemindMapper;
    @Resource
    private ClientRemindProductMapper clientRemindProductMapper;

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
    public ResponseDTO changeUserPassword(Long userId, String orgPassword, String newPassword, String repeatPassword)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser user = clientUserMapper.findById(userId);
        if(user == null)
        {
            responseDTO.setResult(RestResult.ACCOUNT_NOT_EXIST);
            return responseDTO;
        }
        if(!newPassword.equals(repeatPassword))
        {
            responseDTO.setResult(RestResult.OLD_AND_REPEAT_PASSCODE);
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
        long pages = page.getPages(total);
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
    public ListDTO<SubUserResDTO> getSubUserList(Long clientId, Long userId, Page page)
    {
        ListDTO<SubUserResDTO> res = new ListDTO<>();
        // 查询子账号个数限制
        String max = sistemMapper.getClientUserMax();
        res.addExtra(Field.SUB_ACCOUNT_MAX, max);

        Client client = clientMapper.findById(clientId);
        if(client == null || !userId.equals(client.getPrimaryUserId()))
        {
            return res;
        }
        if(page == null)
        {
            List<ClientUser> userList = clientUserMapper.getSubUserListByClient(clientId);
            List<SubUserResDTO> list = new ArrayList<>();
            for(ClientUser o : userList)
            {
                SubUserResDTO su = new SubUserResDTO();
                su.setUserId(o.getId());
                su.setUsername(o.getUsername());
                su.setName(o.getName());
                su.setPhone(o.getPhone());
                su.setEnabled(o.getEnabled());
                list.add(su);
            }
            res.setTotal(userList.size());
            res.setList(list);
        }
        else
        {
            int total = clientUserMapper.countSubUserListByClient(clientId);
            long pages = page.getPages(total);
            res.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientUser> userList = clientUserMapper.getSubUserListByClient(clientId);
                List<SubUserResDTO> list = new ArrayList<>();
                for(ClientUser o : userList)
                {
                    SubUserResDTO su = new SubUserResDTO();
                    su.setUserId(o.getId());
                    su.setUsername(o.getUsername());
                    su.setName(o.getName());
                    su.setPhone(o.getPhone());
                    su.setEnabled(o.getEnabled());
                    list.add(su);
                }
                res.setList(list);
            }
        }
        return res;
    }

    @Override
    public ListDTO<SubUserResDTO> getSubUserList(Long clientId, Page page)
    {
        ListDTO<SubUserResDTO> res = new ListDTO<>();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return res;
        }
        int total = clientUserMapper.countSubUserListByClient(clientId);
        long pages = page.getPages(total);
        res.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientUser> userList = clientUserMapper.getSubUserListByClient(clientId);
            List<SubUserResDTO> list = new ArrayList<>();
            for(ClientUser o : userList)
            {
                SubUserResDTO su = new SubUserResDTO();
                su.setUserId(o.getId());
                su.setUsername(o.getUsername());
                su.setName(o.getName());
                su.setPhone(o.getPhone());
                su.setEnabled(o.getEnabled());
                su.setDeleted(o.getDeleted());
                list.add(su);
            }
            res.setList(list);
        }
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
        ClientUser user = clientUserMapper.findByUsername(username);
        if(user != null)
        {
            responseDTO.setResult(RestResult.USERNAME_EXIST);
            return responseDTO;
        }
        String config = sistemMapper.getClientUserMax();
        List<ClientUser> userList = clientUserMapper.getListByClientAndStatus(client.getId(), null, TrueOrFalse.FALSE);
        int subAccountCount = 0;
        for(ClientUser cu : userList)
        {
            if(!cu.getId().equals(client.getPrimaryUserId()))
            {
                subAccountCount++;
            }
        }
        int canSubAccountCount = Integer.parseInt(config);
        if(subAccountCount >= canSubAccountCount)
        {
            responseDTO.setResult(RestResult.ACCOUNT_COUNT_MAX);
            return responseDTO;
        }

        Date current = new Date();
        user = new ClientUser();
        user.setCreateTime(current);
        user.setUpdateTime(current);
        user.setClientId(client.getId());
        user.setName(name);
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(Md5Utils.encrypt(password));
        user.setEnabled(TrueOrFalse.TRUE);
        user.setDeleted(TrueOrFalse.FALSE);
        clientUserMapper.add(user);
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
        userResDTO.setAppKey(clientUser.getAppSecret());
        return userResDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editSubUser(ClientUserReqDTO reqDTO)
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
        if(productId == null)
        {
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
            dto.setReqHost(up.getReqHost());
        }
        return dto;
    }

    @Override
    @Transactional
    public ResponseDTO saveUserProductCredential(Long userId, Long productId, String reqHost)
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
            up.setReqHost(reqHost);
            clientUserProductMapper.add(up);
        }
        else
        {
            ClientUserProduct upUpd = new ClientUserProduct();
            upUpd.setId(up.getId());
            upUpd.setUpdateTime(current);
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
        long pages = page.getPages(total);
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
        int total = statsClientMapper.getClientCountByDate(startTime, endTime, null);
        long pages = page.getPages(total);
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
    public ResponseDTO addClient(ClientReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        ClientUser user = clientUserMapper.findByUsername(reqDTO.getUsername());
        if(user != null)
        {
            res.setResult(RestResult.USERNAME_EXIST);
            return res;
        }
        Client client = clientMapper.findByCorpNameOrCorpLicense(reqDTO.getCorpName(), reqDTO.getLicense());
        if(client != null && client.getCorpName().equals(reqDTO.getCorpName()))
        {
            res.setResult(RestResult.CLIENT_NAME_EXIST);
            return res;
        }
        if(client != null && client.getLicense().equals(reqDTO.getLicense()))
        {
            res.setResult(RestResult.CLIENT_LICENSE_EXIST);
            return res;
        }
        Date date = new Date();
        // build & save client
        client = new Client();
        client.setCreateTime(date);
        client.setUpdateTime(date);
        client.setCorpName(reqDTO.getCorpName());
        client.setShortName(reqDTO.getShortName());
        client.setLicense(reqDTO.getLicense());
        client.setIndustryId(reqDTO.getIndustryId());
        client.setPrimaryUserId(0L);
        client.setUsername(reqDTO.getUsername());
        client.setManagerId(reqDTO.getManagerId());
        client.setAccountQty(0);
        client.setEnabled(TrueOrFalse.TRUE);
        client.setDeleted(TrueOrFalse.FALSE);
        clientMapper.add(client);
        // build & save client primary user
        user = new ClientUser();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setClientId(client.getId());
        user.setName(reqDTO.getCorpName());
        user.setPhone("");
        user.setEmail("");
        user.setUsername(reqDTO.getUsername());
        user.setPassword(Constant.DEFAULT_ENC_PWD);
        user.setEnabled(reqDTO.getEnabled());
        user.setDeleted(TrueOrFalse.FALSE);
        clientUserMapper.add(user);
        // update client set primary user id
        Client tempClient = new Client();
        tempClient.setId(client.getId());
        tempClient.setUpdateTime(date);
        tempClient.setPrimaryUserId(user.getId());
        clientMapper.updateSkipNull(tempClient);
        // build & save corporation contact data list
        List<ClientContact> contactList = new ArrayList<>();
        for(ClientContactReqDTO o : reqDTO.getContactList())
        {
            ClientContact cc = new ClientContact();
            cc.setCreateTime(date);
            cc.setUpdateTime(date);
            cc.setClientId(client.getId());
            cc.setName(o.getName());
            cc.setPosition(o.getPosition());
            cc.setPhone(o.getPhone());
            cc.setEmail(o.getEmail());
            cc.setGeneral(o.getGeneral());
            contactList.add(cc);
        }
        clientContactMapper.addList(contactList);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editClient(ClientReqDTO dto, List<ClientContactReqDTO> contacts, List<Long> delIds)
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
                res.setEnabled(user.getEnabled());
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
    public ClientDetailResDTO getClientInfo(Long clientId)
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
        res.setUsername(client.getUsername());
        res.setEnabled(client.getEnabled());
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
            Date toDate, Page page)
    {
        ListDTO<AccessResDTO> dto = new ListDTO<>();
        long total = requestLogDao.countByParam(keyword, productId, billPlan, null, fromDate, toDate);
        long pages = page.getPages(total);
        BigDecimal totalFee = requestLogDao.sumFeeByParam(keyword, productId, billPlan, fromDate, toDate);
        long missCount = requestLogDao.countByParam(keyword, productId, billPlan, TrueOrFalse.FALSE, fromDate, toDate);
        dto.setTotal(total);
        dto.addExtra(Field.MISS_COUNT, missCount + "");
        dto.addExtra(Field.TOTAL_FEE, NumberUtils.formatAmount(totalFee));
        if(total > 0 && page.getPageNum() <= pages)
        {
            List<RequestLog> dataList = requestLogDao.findByParam(keyword, null, null, productId, billPlan, fromDate,
                    toDate, page);
            List<AccessResDTO> list = new ArrayList<>(dataList.size());
            for(RequestLog o : dataList)
            {
                AccessResDTO ari = new AccessResDTO();
                ari.setRequestAt(o.getTimestamp());
                ari.setRequestNo(o.getRequestNo());
                ari.setCorpName(o.getCorpName());
                ari.setShortName(null);
                ari.setPrimaryUsername(o.getPrimaryUsername());
                ari.setUsername(o.getRequestUsername());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(NumberUtils.centAmtToYuan(o.getFee()));
                ari.setBalance(NumberUtils.centAmtToYuan(o.getBalance()));
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
    public ListDTO<AccessResDTO> getClientRequestRecord(Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page)
    {
        ListDTO<AccessResDTO> listDTO = new ListDTO<>();
        long total = requestLogDao.countByParam(clientId, userId, productId, startDate, endDate);
        listDTO.setTotal(total);
        long pages = page.getPages(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            List<RequestLog> requestLogList = requestLogDao.findByParam(null, clientId, userId, productId, null,
                    startDate, endDate, page);
            List<AccessResDTO> list = new ArrayList<>(requestLogList.size());
            for(RequestLog o : requestLogList)
            {
                AccessResDTO ari = new AccessResDTO();
                ari.setRequestAt(o.getTimestamp());
                ari.setRequestNo(o.getRequestNo());
                ari.setUsername(o.getRequestUsername());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(NumberUtils.centAmtToYuan(o.getFee()));
                ari.setBalance(NumberUtils.centAmtToYuan(o.getBalance()));
                list.add(ari);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<RechargeResDTO> getClientRechargeRecord(String keyword, Long clientId, Long productId,
            Long managerId, Integer rechargeType, Date startDate, Date endDate, Page page)
    {
        ListDTO<RechargeResDTO> listDTO = new ListDTO<>();
        int total = productRechargeInfoMapper.countBy(keyword, clientId, productId, managerId, rechargeType, startDate,
                endDate);
        long pages = page.getPages(total);
        listDTO.setTotal(total);
        if(clientId == null)
        {
            BigDecimal totalAmt = productRechargeInfoMapper.sumRechargeAmountBy(keyword, productId, managerId,
                    rechargeType, startDate, endDate);
            listDTO.addExtra(Field.TOTAL_AMT, NumberUtils.formatAmount(totalAmt));
        }
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(keyword, clientId, productId,
                    managerId, rechargeType, startDate, endDate);
            List<RechargeResDTO> list = new ArrayList<>();
            for(ProductRechargeInfo o : dataList)
            {
                RechargeResDTO r = new RechargeResDTO();
                r.setRechargeAt(o.getTradeTime());
                r.setRechargeNo(o.getTradeNo());
                r.setCorpName(o.getCorpName());
                r.setShortName(o.getShortName());
                r.setUsername(o.getUsername());
                r.setProductName(o.getProductName());
                r.setRechargeTypeName(o.getRechargeType());
                r.setBillPlan(o.getBillPlan());
                r.setAmount(o.getAmount());
                r.setBalance(o.getBalance());
                r.setContractNo(o.getContractNo());
                r.setManagerName(o.getManagerName());
                r.setStartDate(o.getStartDate());
                r.setEndDate(o.getEndDate());
                r.setUnitAmt(o.getUnitAmt());
                r.setRemark(o.getRemark());
                list.add(r);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<RechargeResDTO> getRechargeInfoListBy(Date fromDate, Date toDate)
    {
        ListDTO<RechargeResDTO> listDTO = new ListDTO<>();
        List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListByTime(fromDate, toDate);
        listDTO.setTotal(productRechargeInfoList.size());
        if(!CollectionUtils.isEmpty(productRechargeInfoList))
        {
            List<RechargeResDTO> list = new ArrayList<>();
            for(ProductRechargeInfo o : productRechargeInfoList)
            {
                RechargeResDTO pri = new RechargeResDTO();
                pri.setRechargeTypeName(o.getRechargeType());
                pri.setAmount(o.getAmount());
                pri.setBalance(o.getBalance());
                pri.setRechargeAt(o.getTradeTime());
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
        BigDecimal totalFee = requestLogDao.sumFeeByParam(null, null, null, fromDate, toDate);
        listDTO.addExtra(Field.TOTAL_FEE, NumberUtils.formatAmount(totalFee));
        long total = requestLogDao.countByRequestTime(fromDate, toDate);
        long pages = page.getPages(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<RequestLog> dataList = requestLogDao.findByParam(null, null, null, null, null, fromDate, toDate, page);
            List<AccessResDTO> list = new ArrayList<>();
            for(RequestLog o : dataList)
            {
                AccessResDTO r = new AccessResDTO();
                r.setRequestAt(o.getTimestamp());
                r.setRequestNo(o.getRequestNo());
                r.setUsername(o.getRequestUsername());
                r.setProductName(o.getProductName());
                r.setBillPlan(o.getBillPlan());
                r.setHit(o.getHit());
                r.setFee(NumberUtils.centAmtToYuan(o.getFee()));
                r.setBalance(NumberUtils.centAmtToYuan(o.getBalance()));
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
        long pages = page.getPages(total);
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
    public RechargeInfoResDTO getLatestRechargeInfo(Long clientId, Long productId)
    {
        RechargeInfoResDTO resDTO = new RechargeInfoResDTO();
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

    @Override
    public ResponseDTO saveUserCredential(ClientUserReqDTO clientUserReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientUser clientUser = new ClientUser();
        clientUser.setId(clientUserReqDTO.getUserId());
        clientUser.setAppSecret(clientUserReqDTO.getAppKey());
        clientUserMapper.updateSkipNull(clientUser);
        return responseDTO;
    }

    @Override
    public int getClientIncrementFrom(Date date)
    {
        return clientMapper.countByStartTime(date);
    }

    @Override
    public Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit)
    {
        Map<String, Integer> map = new HashMap<>();
        if(rangeUnit == RangeUnit.HOUR)
        {
            List<Stats> statsList = statsMapper.getListGroupByHour(dateRange.getStart(), dateRange.getEnd(), INC_STAT);
            for(Stats o : statsList)
            {
                if(o.getClientIncrement() != 0)
                {
                    map.put(o.getStatsHour() + "", o.getClientIncrement());
                }
            }
        }
        else if(rangeUnit == RangeUnit.DAY || rangeUnit == RangeUnit.WEEK || rangeUnit == RangeUnit.MONTH)
        {
            if(rangeUnit == RangeUnit.DAY)
            {
                List<Stats> statsList1 = statsMapper.getListGroupByDay(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList1)
                {
                    map.put(DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2), o.getClientIncrement());
                }
            }
            else if(rangeUnit == RangeUnit.WEEK)
            {
                List<Stats> statsList2 = statsMapper.getListGroupByWeek(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList2)
                {
                    map.put(o.getStatsYear() + String.format("%02d", o.getStatsWeek()), o.getClientIncrement());
                }
            }
            else
            {
                List<Stats> statsList3 = statsMapper.getListGroupByMonth(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList3)
                {
                    map.put(o.getStatsYear() + String.format("/%02d", o.getStatsMonth()), o.getClientIncrement());
                }
            }

        }
        return map;
    }

    @Override
    public Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit)
    {
        Map<String, List<RechargeStatsDTO>> map = new HashMap<>();
        List<StatsRecharge> dataList;
        if(rangeUnit == RangeUnit.DAY)
        {
            dataList = statsRechargeMapper.getListGroupByDay(dateRange.getStart(), dateRange.getEnd());
        }
        else if(rangeUnit == RangeUnit.WEEK)
        {
            dataList = statsRechargeMapper.getListGroupByWeek(dateRange.getStart(), dateRange.getEnd());
        }
        else if(rangeUnit == RangeUnit.MONTH)
        {
            dataList = statsRechargeMapper.getListGroupByMonth(dateRange.getStart(), dateRange.getEnd());
        }
        else
        {
            dataList = statsRechargeMapper.getListGroupByHour(dateRange.getStart(), dateRange.getEnd());
        }
        for(StatsRecharge o : dataList)
        {
            String name;
            if(rangeUnit == RangeUnit.DAY)
            {
                name = DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2);
            }
            else if(rangeUnit == RangeUnit.WEEK)
            {
                name = o.getStatsYear() + String.format("%02d", o.getStatsWeek());
            }
            else if(rangeUnit == RangeUnit.MONTH)
            {
                name = o.getStatsYear() + String.format("/%02d", o.getStatsMonth());
            }
            else
            {
                name = o.getStatsHour() + "";
            }
            List<RechargeStatsDTO> list = map.get(name);
            if(CollectionUtils.isEmpty(list))
            {
                list = new ArrayList<>();
            }
            RechargeStatsDTO dto = new RechargeStatsDTO();
            dto.setRechargeTypeName(o.getRechargeTypeName());
            dto.setAmount(o.getAmount());
            list.add(dto);
            map.put(name, list);
        }
        return map;
    }

    @Override
    public ListDTO<ClientRemindResInfoDTO> getClientRemindList(Long managerId, Integer type, Date date, Integer dispose,
            Page page)
    {
        ListDTO<ClientRemindResInfoDTO> listDTO = new ListDTO<>();
        int total = clientRemindInfoMapper.countBy(managerId, type, date, dispose);
        long pages = page.getPages(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientRemindInfo> dataList = clientRemindInfoMapper.getClientRemindListBy(managerId, type, date,
                    dispose);
            if(!CollectionUtils.isEmpty(dataList))
            {
                List<ClientRemindResInfoDTO> list = new ArrayList<>(dataList.size());
                for(ClientRemindInfo o : dataList)
                {
                    ClientRemindResInfoDTO col = new ClientRemindResInfoDTO();
                    col.setCorpName(o.getCorpName());
                    col.setCount(o.getCount());
                    col.setDay(o.getDay());
                    col.setDispose(o.getDispose());
                    col.setId(o.getId());
                    col.setLinkName(o.getLinkName());
                    col.setLinkPhone(o.getLinkPhone());
                    col.setProductName(o.getProductName());
                    col.setRemark(o.getRemark());
                    col.setRemindDate(o.getRemindDate());
                    col.setType(o.getType());
                    col.setClientId(o.getClientId());
                    list.add(col);
                }
                listDTO.setList(list);
            }
        }
        return listDTO;
    }

    @Override
    @Transactional
    public void quartzClientRemind(Date date)
    {
        //先删除未处理的记录
        List<ClientRemind> clientReminds = clientRemindMapper.getListByDispose(TrueOrFalse.FALSE);
        if(!CollectionUtils.isEmpty(clientReminds))
        {
            List<Long> crIds = new ArrayList<>();
            for(ClientRemind item : clientReminds)
            {
                crIds.add(item.getId());
            }
            clientRemindProductMapper.deleteByRemindIds(crIds);
            clientRemindMapper.deleteByIds(crIds);
        }
        //找出需要提醒的客户服务
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date before;
        Date after = calendar.getTime();
        try
        {
            before = shortSdf.parse(shortSdf.format(date));
            after = shortSdf.parse(shortSdf.format(after));
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            return;
        }
        Set<Long> clientIdSet = new HashSet<>();
        List<ClientProductInfo> willOverByDate = clientProductInfoMapper.getWillOverByDate(before, after);
        for(ClientProductInfo item : willOverByDate)
        {
            clientIdSet.add(item.getClientId());
        }
        List<ClientProductInfo> willOverByTimes = clientProductInfoMapper.getWillOverByTimes(new BigDecimal(500));
        for(ClientProductInfo item : willOverByTimes)
        {
            clientIdSet.add(item.getClientId());
        }
        if(!CollectionUtils.isEmpty(clientIdSet))
        {
            List<ClientContact> listByClients = clientContactMapper.getListByClients(new ArrayList<>(clientIdSet));
            Map<Long, List<ClientContact>> clientIdContactListMap = new HashMap<>();
            List<ClientContact> clientContactsTemp;
            for(ClientContact item : listByClients)
            {
                clientContactsTemp = clientIdContactListMap.get(item.getClientId());
                if(clientContactsTemp == null)
                {
                    clientContactsTemp = new ArrayList<>();
                    clientIdContactListMap.put(item.getClientId(), clientContactsTemp);
                }
                clientContactsTemp.add(item);
            }
            Map<Long, Map<Long, List<ClientProductInfo>>> managerIdInfoListByDateMap = new HashMap<>();
            Map<Long, Map<Long, List<ClientProductInfo>>> managerIdInfoListByTimesMap = new HashMap<>();
            Map<Long, List<ClientProductInfo>> clientProductInfosMapTemp;
            List<ClientProductInfo> clientProductInfosTemp;
            for(ClientProductInfo item : willOverByDate)
            {
                clientProductInfosMapTemp = managerIdInfoListByDateMap.get(item.getManagerId());
                if(clientProductInfosMapTemp == null)
                {
                    clientProductInfosMapTemp = new HashMap<>();
                    managerIdInfoListByDateMap.put(item.getManagerId(), clientProductInfosMapTemp);
                }
                clientProductInfosTemp = clientProductInfosMapTemp.get(item.getClientId());
                if(clientProductInfosTemp == null)
                {
                    clientProductInfosTemp = new ArrayList<>();
                    clientProductInfosMapTemp.put(item.getClientId(), clientProductInfosTemp);
                }
                clientProductInfosTemp.add(item);
            }
            for(ClientProductInfo item : willOverByTimes)
            {
                clientProductInfosMapTemp = managerIdInfoListByTimesMap.get(item.getManagerId());
                if(clientProductInfosMapTemp == null)
                {
                    clientProductInfosMapTemp = new HashMap<>();
                    managerIdInfoListByTimesMap.put(item.getManagerId(), clientProductInfosMapTemp);
                }
                clientProductInfosTemp = clientProductInfosMapTemp.get(item.getClientId());
                if(clientProductInfosTemp == null)
                {
                    clientProductInfosTemp = new ArrayList<>();
                    clientProductInfosMapTemp.put(item.getClientId(), clientProductInfosTemp);
                }
                clientProductInfosTemp.add(item);
            }
            ClientRemind clientRemind = null;
            ClientRemindProduct clientRemindProduct = null;
            List<ClientRemindProduct> clientRemindProductsTemp = null;
            Date currDate = new Date();
            //时间
            for(Map.Entry<Long, Map<Long, List<ClientProductInfo>>> entry : managerIdInfoListByDateMap.entrySet())
            {
                clientProductInfosMapTemp = entry.getValue();
                for(Map.Entry<Long, List<ClientProductInfo>> entry2 : clientProductInfosMapTemp.entrySet())
                {
                    clientProductInfosTemp = entry2.getValue();
                    clientRemind = new ClientRemind();
                    clientRemind.setCreateTime(currDate);
                    clientRemind.setUpdateTime(currDate);
                    clientRemind.setUserId(entry.getKey());
                    clientRemind.setRemindDate(before);
                    clientRemind.setType(ClientRemindType.DATE.getId());
                    clientRemind.setClientId(entry2.getKey());
                    clientContactsTemp = clientIdContactListMap.get(entry2.getKey());
                    if(clientContactsTemp != null)
                    {
                        clientRemind.setLinkName(clientContactsTemp.get(0).getName());
                        clientRemind.setLinkPhone(clientContactsTemp.get(0).getPhone());
                    }
                    clientRemind.setProductId(clientProductInfosTemp.get(0).getProductId());
                    clientRemind.setCount(clientProductInfosTemp.size());
                    clientRemind.setDay(1 +
                            DateCalculateUtils.getBetweenDayDif(before, clientProductInfosTemp.get(0).getEndDate()));
                    clientRemind.setDispose(TrueOrFalse.FALSE);
                    clientRemindMapper.add(clientRemind);
                    clientRemindProductsTemp = new ArrayList<>();
                    for(ClientProductInfo item : clientProductInfosTemp)
                    {
                        clientRemindProduct = new ClientRemindProduct();
                        clientRemindProductsTemp.add(clientRemindProduct);
                        clientRemindProduct.setCreateTime(currDate);
                        clientRemindProduct.setUpdateTime(currDate);
                        clientRemindProduct.setRemindId(clientRemind.getId());
                        clientRemindProduct.setProductId(item.getProductId());
                        clientRemindProduct.setRechargeId(item.getRechargeId());
                        clientRemindProduct.setRemind(TrueOrFalse.FALSE);
                        clientRemindProductsTemp.add(clientRemindProduct);
                    }
                    clientRemindProductMapper.addList(clientRemindProductsTemp);
                }
            }
            //计次
            for(Map.Entry<Long, Map<Long, List<ClientProductInfo>>> entry : managerIdInfoListByTimesMap.entrySet())
            {
                clientProductInfosMapTemp = entry.getValue();
                for(Map.Entry<Long, List<ClientProductInfo>> entry2 : clientProductInfosMapTemp.entrySet())
                {
                    clientProductInfosTemp = entry2.getValue();
                    clientRemind = new ClientRemind();
                    clientRemind.setCreateTime(currDate);
                    clientRemind.setUpdateTime(currDate);
                    clientRemind.setUserId(entry.getKey());
                    clientRemind.setRemindDate(before);
                    clientRemind.setType(ClientRemindType.TIMES.getId());
                    clientRemind.setClientId(entry2.getKey());
                    clientContactsTemp = clientIdContactListMap.get(entry2.getKey());
                    if(clientContactsTemp != null)
                    {
                        clientRemind.setLinkName(clientContactsTemp.get(0).getName());
                        clientRemind.setLinkPhone(clientContactsTemp.get(0).getPhone());
                    }
                    clientRemind.setProductId(clientProductInfosTemp.get(0).getProductId());
                    clientRemind.setCount(clientProductInfosTemp.size());
                    clientRemind.setDispose(TrueOrFalse.FALSE);
                    clientRemindMapper.add(clientRemind);
                    clientRemindProductsTemp = new ArrayList<>();
                    for(ClientProductInfo item : clientProductInfosTemp)
                    {
                        clientRemindProduct = new ClientRemindProduct();
                        clientRemindProductsTemp.add(clientRemindProduct);
                        clientRemindProduct.setCreateTime(currDate);
                        clientRemindProduct.setUpdateTime(currDate);
                        clientRemindProduct.setRemindId(clientRemind.getId());
                        clientRemindProduct.setProductId(item.getProductId());
                        clientRemindProduct.setRechargeId(item.getRechargeId());
                        clientRemindProduct.setRemind(TrueOrFalse.FALSE);
                        clientRemindProductsTemp.add(clientRemindProduct);
                    }
                    clientRemindProductMapper.addList(clientRemindProductsTemp);
                }
            }
        }
        System.out.println("======================客户服务提醒统计完成");
    }

    @Override
    public ResponseDTO updateClientRemind(Long remindId, String remark)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        ClientRemind cr = clientRemindMapper.getClientRemindById(remindId);
        if(cr == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        ClientRemind clientRemind = new ClientRemind();
        clientRemind.setId(remindId);
        clientRemind.setDispose(TrueOrFalse.TRUE);
        clientRemind.setRemark(remark);
        clientRemindMapper.updateSkipNull(clientRemind);
        clientRemindProductMapper.disposeByClientRemindId(remindId);
        return responseDTO;
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