package com.mingdong.bop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.mingdong.bop.component.Param;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.Trade;
import com.mingdong.bop.model.ContactVO;
import com.mingdong.bop.model.NewClientVO;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ClientAccountDTO;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductClientInfoDTO;
import com.mingdong.core.model.dto.ProductClientInfoListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.service.RemoteSystemService;
import com.mingdong.core.util.IDUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService
{
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SystemService systemService;
    @Resource
    private RemoteClientService remoteClientService;
    @Resource
    private RemoteProductService remoteProductService;
    @Resource
    private RemoteManagerService remoteManagerService;
    @Resource
    private RemoteSystemService remoteSystemService;

    @Override
    public void checkIfUsernameExist(String username, BLResp resp)
    {
        UserDTO user = remoteClientService.findByUsername(username);
        resp.addData(Field.EXIST, user == null ? 0 : 1);
    }

    @Override
    public void getSimilarCorp(String corpName, Long clientId, BLResp resp)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ClientInfoListDTO similarCorpByName = remoteClientService.getSimilarCorpByName(corpName, clientId);
        List<ClientInfoDTO> dataList = similarCorpByName.getDataList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(ClientInfoDTO client : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.CORP_NAME, client.getCorpName());
                map.put(Field.NAME, client.getName());
                map.put(Field.PHONE, client.getPhone());
                map.put(Field.EMAIL, client.getEmail());
                map.put(Field.REGISTER_DATE, DateUtils.format(client.getRegisterTime(), DateFormat.YYYY_MM_DD));
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        resp.addData(Field.TOTAL, dataList.size());
    }

    @Override
    public BLResp getCorp(Integer enabled, String username, String cropName, String shortName, Long parentIndustryId,
            Long industryId, Page page)
    {
        BLResp resp = BLResp.build();
        List<Long> industryList = new ArrayList<>();
        if(industryId == null)
        {
            if(parentIndustryId != null)
            {
                DictIndustryListDTO byParentAndStatus = remoteSystemService.getDictIndustryListByParentAndStatus(
                        parentIndustryId, TrueOrFalse.TRUE);
                List<DictIndustryDTO> parentList = byParentAndStatus.getDataList();
                for(DictIndustryDTO d : parentList)
                {
                    industryList.add(d.getId());
                }
            }
        }
        else
        {
            industryList.add(industryId);
        }
        ClientInfoListDTO clinetInfoListBy = remoteClientService.getClientInfoListBy(enabled, username, cropName,
                shortName, industryList, page);
        resp.addData(Field.TOTAL, clinetInfoListBy.getTotal());
        resp.addData(Field.PAGES, clinetInfoListBy.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ClientInfoDTO> clientInfoList = clinetInfoListBy.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(clientInfoList))
        {
            for(ClientInfoDTO clientInfo : clientInfoList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, clientInfo.getClientId() + "");
                map.put(Field.USERNAME, clientInfo.getUsername());
                map.put(Field.CORP_NAME, clientInfo.getCorpName());
                map.put(Field.SHORT_NAME, clientInfo.getShortName());
                map.put(Field.INDUSTRY, systemService.getIndustryName(clientInfo.getIndustryId()));
                map.put(Field.NAME, clientInfo.getName());
                map.put(Field.PHONE, clientInfo.getPhone());
                map.put(Field.MANAGER_NAME, clientInfo.getManagerName());
                map.put(Field.ACCOUNT_QTY, clientInfo.getAccountQty());
                map.put(Field.REGISTER_DATE, DateUtils.format(clientInfo.getRegisterTime(), "yyyy-MM-dd"));
                map.put(Field.USER_ENABLED, clientInfo.getUserEnabled());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        return resp;
    }

    @Override
    @Transactional
    public void editClientInfo(Long clientId, String corpName, String shortName, String license, Long industryId,
            String name, String phone, String email, Integer userEnabled, Integer accountEnabled, BLResp resp)
    {
        Date current = new Date();
        ClientDTO client = remoteClientService.getClientByClientId(clientId);
        if(client == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 更新用户信息
        ClientUserDTO clientUser = remoteClientService.getClientUserByUserId(client.getPrimaryUserId());
        clientUser.setUpdateTime(current);
        clientUser.setName(name);
        clientUser.setPhone(phone);
        clientUser.setEmail(email);
        clientUser.setEnabled(userEnabled);
        remoteClientService.updateClientUserByUserId(clientUser);
        // 更新客户账户状态
        ClientAccountDTO clientAccount = remoteClientService.getClientAccountByClientId(clientId);
        if(clientAccount != null)
        {
            clientAccount.setUpdateTime(current);
            clientAccount.setEnabled(accountEnabled);
            remoteClientService.updateClientAccountById(clientAccount);
        }
        else
        {
            clientAccount = new ClientAccountDTO();
            clientAccount.setId(clientId);
            clientAccount.setCreateTime(current);
            clientAccount.setUpdateTime(current);
            clientAccount.setBalance(new BigDecimal(0));
            clientAccount.setEnabled(accountEnabled);
            remoteClientService.saveClientAccount(clientAccount);
        }
        // 更新客户信息
        client.setUpdateTime(current);
        client.setCorpName(corpName);
        client.setShortName(shortName);
        client.setLicense(license);
        client.setIndustryId(industryId);
        remoteClientService.updateClientById(client);
    }

    @Override
    public Map<String, Object> findClientInfo(Long clientId)
    {
        Map<String, Object> map = new HashMap<>();

        ClientDTO client = remoteClientService.getClientByClientId(clientId);
        if(client != null)
        {
            ClientUserDTO user = remoteClientService.getClientUserByUserId(client.getPrimaryUserId());
            ClientAccountDTO clientAccount = remoteClientService.getClientAccountByClientId(clientId);
            DictIndustryDTO industry = remoteSystemService.getDictIndustryById(client.getIndustryId());
            map.put(Field.CLIENT_ID, clientId + "");
            map.put(Field.USERNAME, user.getUsername());
            map.put(Field.CORP_NAME, client.getCorpName());
            map.put(Field.SHORT_NAME, client.getShortName());
            map.put(Field.PARENT_INDUSTRY_ID, industry.getParentId() + "");
            map.put(Field.INDUSTRY_ID, client.getIndustryId() + "");
            map.put(Field.NAME, user.getName());
            map.put(Field.PHONE, user.getPhone());
            map.put(Field.EMAIL, user.getEmail());
            map.put(Field.LICENSE, client.getLicense());
            map.put(Field.USER_STATUS, user.getEnabled());
            map.put(Field.ACCOUNT_STATUS, clientAccount != null ? clientAccount.getEnabled() : TrueOrFalse.TRUE);

            List<Map<String, Object>> parentIndustryList = systemService.getIndustryMap(0L, TrueOrFalse.TRUE);
            map.put(Field.PARENT_INDUSTRY_LIST, parentIndustryList);
            List<Map<String, Object>> childIndustryList = systemService.getIndustryMap(industry.getParentId(),
                    TrueOrFalse.TRUE);
            map.put(Field.CHILD_INDUSTRY_LIST, childIndustryList);
        }
        return map;
    }

    @Override
    public Map<String, Object> findClientDetail(Long clientId)
    {
        Map<String, Object> map = new HashMap<>();
        ClientDTO client = remoteClientService.getClientByClientId(clientId);
        if(client != null)
        {
            // 主账号
            ClientUserDTO masterUser = remoteClientService.getClientUserByUserId(client.getPrimaryUserId());
            map.put(Field.USERNAME, masterUser.getUsername());
            map.put(Field.USER_ENABLED, masterUser.getEnabled());
            // 子账号
            ClientUserListDTO listByClientAndStatus = remoteClientService.getListByClientAndStatus(clientId,
                    TrueOrFalse.TRUE, TrueOrFalse.FALSE);
            List<ClientUserDTO> subUserList = listByClientAndStatus.getDataList();
            List<Map<String, Object>> userList = new ArrayList<>();
            for(ClientUserDTO user : subUserList)
            {
                if(!client.getPrimaryUserId().equals(user.getId()))
                {
                    Map<String, Object> m = new HashMap<>();
                    m.put(Field.ID, user.getId());
                    m.put(Field.USERNAME, user.getUsername());
                    m.put(Field.NAME, user.getName());
                    m.put(Field.PHONE, user.getPhone());
                    userList.add(m);
                }
            }
            // 开通产品
            ProductClientInfoListDTO productClientInfoListByClientId =
                    remoteProductService.getProductClientInfoListByClientId(clientId);
            List<ProductClientInfoDTO> pciList = productClientInfoListByClientId.getDataList();
            List<Map<String, Object>> opened = new ArrayList<>();
            List<Map<String, Object>> toOpen = new ArrayList<>();
            for(ProductClientInfoDTO pci : pciList)
            {
                Map<String, Object> m = new HashMap<>();
                if(pci.getClientProductId() != null)
                {
                    m.put(Field.CLIENT_PRODUCT_ID, pci.getClientProductId() + "");
                    m.put(Field.PRODUCT_NAME, pci.getProductName());
                    m.put(Field.APP_ID, pci.getAppId());
                    m.put(Field.BILL_PLAN, pci.getBillPlan());
                    if(BillPlan.YEAR.getId().equals(pci.getBillPlan()))
                    {
                        m.put(Field.START_DATE, DateUtils.format(pci.getStartDate(), DateFormat.YYYY_MM_DD));
                        m.put(Field.END_DATE, DateUtils.format(pci.getEndDate(), DateFormat.YYYY_MM_DD));
                        m.put(Field.AMOUNT, NumberUtils.formatAmount(pci.getAmount()));
                    }
                    else
                    {
                        m.put(Field.BALANCE, NumberUtils.formatAmount(pci.getBalance()));
                        m.put(Field.UNIT_AMT, NumberUtils.formatAmount(pci.getUnitAmt()));
                    }
                    opened.add(m);
                }
                else
                {
                    m.put(Field.PRODUCT_ID, pci.getProductId() + "");
                    m.put(Field.PRODUCT_NAME, pci.getProductName());
                    toOpen.add(m);
                }
            }
            map.put(Field.OPENED, opened);
            map.put(Field.TO_OPEN, toOpen);
            // 账户余额
            ClientAccountDTO account = remoteClientService.getClientAccountByClientId(clientId);
            map.put(Field.BALANCE, account == null ? "0.00" : NumberUtils.formatAmount(account.getBalance()));
            // 其他
            ManagerDTO manager = remoteManagerService.getManagerById(client.getManagerId());
            map.put(Field.CLIENT_ID, clientId + "");
            map.put(Field.CORP_NAME, client.getCorpName());
            map.put(Field.SHORT_NAME, client.getShortName());
            map.put(Field.INDUSTRY_NAME, redisDao.getIndustryInfo(client.getIndustryId()));
            map.put(Field.LICENSE, client.getLicense());
            map.put(Field.ACCOUNT_ENABLED, account != null ? account.getEnabled() : TrueOrFalse.TRUE);
            map.put(Field.REGISTER_DATE, DateUtils.format(client.getCreateTime(), DateFormat.YYYY_MM_DD));
            map.put(Field.MANAGER_NAME, manager != null ? manager.getName() : "");
            map.put(Field.USER_LIST, userList);
        }
        return map;
    }

    @Override
    @Transactional
    public void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, Long managerId, BLResp resp)
    {
        ClientUserListDTO clientUserListByClientIds = remoteClientService.getClientUserListByClientIds(clientIdList);
        List<ClientUserDTO> userList = clientUserListByClientIds.getDataList();
        if(CollectionUtils.isEmpty(userList))
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        List<Long> clientUserIdList = new ArrayList<>();
        List<ClientOperateLogDTO> logList = new ArrayList<>();
        for(ClientUserDTO user : userList)
        {
            if(!enabled.equals(user.getEnabled()))
            {
                clientUserIdList.add(user.getId());
                ClientOperateLogDTO log = new ClientOperateLogDTO();
                log.setCreateTime(current);
                log.setUpdateTime(current);
                log.setClientId(user.getClientId());
                log.setClientUserId(user.getId());
                log.setManagerId(managerId);
                log.setType(enabled);
                log.setReason(reason);
                logList.add(log);
            }
        }
        if(logList.size() > 0)
        {
            remoteClientService.saveClientOperateLogList(logList);
            remoteClientService.updateClientUserStatusByIds(enabled, current, clientUserIdList);
        }
    }

    @Override
    @Transactional
    public void setClientDeleted(List<Long> idList, BLResp resp)
    {
        remoteClientService.setClientDeleted(idList);
    }

    @Override
    @Transactional
    public void resetClientPassword(List<Long> idList, BLResp resp)
    {
        ClientListDTO clientListByIds = remoteClientService.getClientListByIds(idList);
        List<ClientDTO> clientList = clientListByIds.getDataList();
        if(!CollectionUtils.isEmpty(clientList))
        {
            List<Long> clientUserIdList = new ArrayList<>(clientList.size());
            for(ClientDTO client : clientList)
            {
                clientUserIdList.add(client.getPrimaryUserId());
            }
            remoteClientService.resetPasswordByIds(Constant.DEFAULT_ENC_PWD, clientUserIdList);
        }
    }

    @Override
    public Map<String, Object> getClientProductInfo(Long clientProductId)
    {
        ClientProductDTO cp = remoteClientService.getClientProductById(clientProductId);
        Map<String, Object> map = new HashMap<>();
        if(cp != null)
        {
            ClientDTO client = remoteClientService.getClientByClientId(cp.getClientId());
            map.put(Field.CLIENT_ID, cp.getClientId() + "");
            map.put(Field.PRODUCT_ID, cp.getProductId() + "");
            map.put(Field.CORP_NAME, client != null ? client.getCorpName() : "");
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getSubAccountList(Long clientId)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ClientDTO client = remoteClientService.getClientByClientId(clientId);
        if(client == null)
        {
            return list;
        }
        ClientUserListDTO listByClientAndStatus = remoteClientService.getListByClientAndStatus(clientId,
                TrueOrFalse.TRUE, TrueOrFalse.FALSE);
        List<ClientUserDTO> cuList = listByClientAndStatus.getDataList();
        for(ClientUserDTO cu : cuList)
        {
            if(!client.getPrimaryUserId().equals(cu.getId()))
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, cu.getId() + "");
                map.put(Field.USERNAME, cu.getUsername());
                map.put(Field.NAME, cu.getName());
                map.put(Field.PHONE, cu.getPhone());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    @Transactional
    public void resetClientUserPassword(Long clientUserId, BLResp resp)
    {
        ClientUserDTO cu = remoteClientService.getClientUserByUserId(clientUserId);
        if(cu == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        cu = new ClientUserDTO();
        cu.setId(clientUserId);
        cu.setUpdateTime(new Date());
        cu.setPassword(Md5Utils.encrypt(Md5Utils.encrypt(Constant.DEFAULT_PASSWORD)));
        remoteClientService.updateClientUserSkipNull(cu);
    }

    @Override
    @Transactional
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
    {

        Date current = new Date();
        ClientProductDTO cp = remoteClientService.getClientProductByClientAndProduct(clientId, productId);
        if(cp != null)
        {
            resp.result(RestResult.PRODUCT_OPENED);
            return;
        }
        ProductRechargeDTO pro = remoteProductService.getProductRechargeByContractNo(contractNo);
        if(pro != null)
        {
            resp.result(RestResult.CONTRACT_IS_EXIST);
            return;
        }
        Long clientProductId = IDUtils.getClientProductId(param.getNodeId());
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        // 保存产品的充值记录
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        pr.setClientId(clientId);
        pr.setProductId(productId);
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        pr.setBalance(new BigDecimal(0));
        pr.setStartDate(startDate);
        pr.setEndDate(endDate);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        remoteProductService.saveProductRecharge(pr);

        // 保存客户产品的账户变更信息
        cp = new ClientProductDTO();
        cp.setId(clientProductId);
        cp.setCreateTime(current);
        cp.setUpdateTime(current);
        cp.setClientId(clientId);
        cp.setProductId(productId);
        cp.setAppId(StringUtils.getUuid());
        cp.setBillPlan(billPlan);
        cp.setBalance(new BigDecimal(0));
        cp.setLatestRechargeId(productRechargeId);
        remoteClientService.saveClientProduct(cp);
    }

    @Override
    @Transactional
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProductDTO cp = remoteClientService.getClientProductByClientAndProduct(clientId, productId);
        if(cp != null)
        {
            resp.result(RestResult.PRODUCT_OPENED);
            return;
        }
        Long clientProductId = IDUtils.getClientProductId(param.getNodeId());
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        pr.setClientId(clientId);
        pr.setProductId(productId);
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        pr.setBalance(amount);
        pr.setUnitAmt(unitAmt);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        remoteProductService.saveProductRecharge(pr);
        // 保存客户产品的账户变更信息
        cp = new ClientProductDTO();
        cp.setId(clientProductId);
        cp.setCreateTime(current);
        cp.setUpdateTime(current);
        cp.setClientId(clientId);
        cp.setProductId(productId);
        cp.setAppId(StringUtils.getUuid());
        cp.setBillPlan(billPlan);
        cp.setBalance(amount);
        cp.setLatestRechargeId(productRechargeId);
        remoteClientService.saveClientProduct(cp);
    }

    @Override
    public void getProductRenewInfo(Long clientProductId, BLResp resp)
    {
        ClientProductDTO cp = remoteClientService.getClientProductById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        ProductRechargeDTO pr = remoteProductService.getProductRechargeById(cp.getLatestRechargeId());

        if(pr == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        resp.addData(Field.BILL_PLAN, pr.getBillPlan());
        if(BillPlan.YEAR.getId().equals(pr.getBillPlan()))
        {
            resp.addData(Field.START_DATE, DateUtils.format(pr.getStartDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.END_DATE, DateUtils.format(pr.getEndDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.AMOUNT, NumberUtils.formatAmount(pr.getAmount()));
        }
        else
        {
            resp.addData(Field.BALANCE, NumberUtils.formatAmount(pr.getBalance()));
            resp.addData(Field.UNIT_AMT, NumberUtils.formatAmount(pr.getUnitAmt()));
        }
        BigDecimal totalAmt = remoteProductService.sumAmountByClientProduct(clientProductId);
        resp.addData(Field.TOTAL_AMT, NumberUtils.formatAmount(totalAmt));
    }

    @Override
    @Transactional
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProductDTO cp = remoteClientService.getClientProductById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 保存产品续费记录
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        pr.setClientId(cp.getClientId());
        pr.setProductId(cp.getProductId());
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        pr.setBalance(new BigDecimal(0));
        pr.setStartDate(startDate);
        pr.setEndDate(endDate);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        remoteProductService.saveProductRecharge(pr);
        // 更新客户产品信息
        cp = new ClientProductDTO();
        cp.setId(clientProductId);
        cp.setUpdateTime(current);
        cp.setBillPlan(billPlan);
        cp.setBalance(new BigDecimal(0));
        cp.setLatestRechargeId(productRechargeId);
        remoteClientService.updateClientProductSkipNull(cp);
    }

    @Override
    @Transactional
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProductDTO cp = remoteClientService.getClientProductById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 保存产品续费记录
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        pr.setClientId(cp.getClientId());
        pr.setProductId(cp.getProductId());
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        pr.setBalance(amount.add(cp.getBalance()));
        pr.setUnitAmt(unitAmt);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        remoteProductService.saveProductRecharge(pr);
        // 更新客户产品信息
        ClientProductDTO cpUpd = new ClientProductDTO();
        cpUpd.setId(clientProductId);
        cpUpd.setUpdateTime(current);
        cpUpd.setBillPlan(billPlan);
        cpUpd.setBalance(amount.add(cp.getBalance()));
        cpUpd.setLatestRechargeId(productRechargeId);
        remoteClientService.updateClientProductSkipNull(cpUpd);
    }

    @Override
    public void getClientOperateLog(Long clientId, Page page, BLResp resp)
    {
        ClientDTO client = remoteClientService.getClientByClientId(clientId);
        if(client == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        ClientOperateInfoListDTO clientOperateInfoListByUserId = remoteClientService.getClientOperateInfoListByUserId(
                client.getPrimaryUserId(), page);
        resp.addData(Field.TOTAL, clientOperateInfoListByUserId.getTotal());
        resp.addData(Field.PAGES, clientOperateInfoListByUserId.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ClientOperateInfoDTO> dataList = clientOperateInfoListByUserId.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            for(ClientOperateInfoDTO info : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.OPERATE_TIME, DateUtils.format(info.getOperateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TYPE, info.getType());
                map.put(Field.MANAGER_NAME, info.getManagerName());
                map.put(Field.REASON, info.getReason());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date startTime, Date endTime)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("充值记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("充值时间");
        row.createCell(1).setCellValue("充值单号");
        row.createCell(2).setCellValue("公司名称");
        row.createCell(3).setCellValue("公司简称");
        row.createCell(4).setCellValue("账号");
        row.createCell(5).setCellValue("产品服务");
        row.createCell(6).setCellValue("充值类型");
        row.createCell(7).setCellValue("充值金额");
        row.createCell(8).setCellValue("产品服务余额");
        row.createCell(9).setCellValue("商务经理");
        row.createCell(10).setCellValue("合同编号");
        row.createCell(11).setCellValue("备注");

        ProductRechargeInfoListDTO productRechargeInfoDTOS = remoteProductService.getProductRechargeInfoList(clientId,
                productId, startTime, endTime, new Page(1, 1000));
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoDTOS.getDataList();
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        for(int i = 0; i < dataList.size(); i++)
        {
            ProductRechargeInfoDTO pri = dataList.get(i);
            Row dataRow = sheet.createRow(i + 1);
            Cell cell = dataRow.createCell(0);
            cell.setCellValue(pri.getTradeTime());
            cell.setCellStyle(timeStyle);
            dataRow.createCell(1).setCellValue(pri.getTradeNo());
            dataRow.createCell(2).setCellValue(pri.getCorpName());
            dataRow.createCell(3).setCellValue(pri.getShortName());
            dataRow.createCell(4).setCellValue(pri.getUsername());
            dataRow.createCell(5).setCellValue(pri.getProductName());
            dataRow.createCell(6).setCellValue(pri.getRechargeType());
            dataRow.createCell(7).setCellValue(NumberUtils.formatAmount(pri.getAmount()));
            dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(pri.getBalance()));
            dataRow.createCell(9).setCellValue(pri.getManagerName());
            dataRow.createCell(10).setCellValue(pri.getContractNo());
            dataRow.createCell(11).setCellValue(pri.getRemark());
        }
        return wb;
    }

    @Override
    public void checkIfContractExist(String contractNo, BLResp resp)
    {
        ProductRechargeDTO pr = remoteProductService.getProductRechargeByContractNo(contractNo);
        resp.addData(Field.EXIST, pr == null ? 0 : 1);
    }

    @Override
    public void addClient(NewClientVO vo, BLResp resp)
    {
        if(StringUtils.isNullBlank(vo.getUsername()) || StringUtils.isNullBlank(vo.getPassword()) ||
                StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(vo.getShortName()) ||
                StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null || CollectionUtils.isEmpty(
                vo.getContacts()))
        {
            resp.result(RestResult.KEY_FIELD_MISSING);
            return;
        }
        List<ClientContactDTO> contactList = new ArrayList<>(vo.getContacts().size());
        for(ContactVO o : vo.getContacts())
        {
            if(StringUtils.isNullBlank(o.getName()) || StringUtils.isNullBlank(o.getPosition()) ||
                    StringUtils.isNullBlank(o.getPhone()))
            {
                resp.result(RestResult.KEY_FIELD_MISSING);
                return;
            }
            if(!TrueOrFalse.TRUE.equals(o.getGeneral()) && !TrueOrFalse.FALSE.equals(o.getGeneral()))
            {
                resp.result(RestResult.KEY_FIELD_MISSING);
                return;
            }
            ClientContactDTO cd = new ClientContactDTO();
            cd.setName(o.getName());
            cd.setPosition(o.getPosition());
            cd.setPhone(o.getPhone());
            cd.setEmail(o.getEmail());
            cd.setGeneral(o.getGeneral());
            contactList.add(cd);
        }
        NewClientDTO dto = new NewClientDTO();
        dto.setCorpName(vo.getCorpName());
        dto.setShortName(vo.getShortName());
        dto.setIndustryId(vo.getIndustryId());
        dto.setLicense(vo.getLicense());
        dto.setUsername(vo.getUsername());
        dto.setContactList(contactList);
        dto.setEnabled(vo.getEnabled());
        dto.setManagerId(RequestThread.getOperatorId());
        ResultDTO res = remoteClientService.addNewClient(dto);
        resp.result(res.getResult());
    }

    @Override
    public void findClientDetail(Long clientId, BLResp resp)
    {
        ClientDetailDTO dto = remoteClientService.getClientDetail(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.result(dto.getResult());
            return;
        }
        resp.addData(Field.CLIENT_ID, clientId + "");
        resp.addData(Field.CORP_NAME, dto.getCorpName());
        resp.addData(Field.SHORT_NAME, dto.getShortName());
        resp.addData(Field.INDUSTRY_NAME, redisDao.getIndustryInfo(dto.getIndustryId()));
        resp.addData(Field.LICENSE, dto.getLicense());
        resp.addData(Field.USERNAME, dto.getUsername());
        resp.addData(Field.USER_ENABLED, dto.getUserStatus());
        resp.addData(Field.ACCOUNT_ENABLED, dto.getAccountStatus());
        resp.addData(Field.BALANCE, NumberUtils.formatAmount(dto.getBalance()));
        resp.addData(Field.REGISTER_DATE, DateUtils.format(dto.getAddTime(), DateFormat.YYYY_MM_DD));
        resp.addData(Field.MANAGER_NAME, dto.getManagerName());
        List<Map<String, Object>> userList = new ArrayList<>();
        for(ClientUserDTO cu : dto.getUsers())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, cu.getId() + "");
            m.put(Field.USERNAME, cu.getUsername());
            m.put(Field.NAME, cu.getName());
            m.put(Field.PHONE, cu.getPhone());
            userList.add(m);
        }
        resp.addData(Field.USER_LIST, userList);
        List<Map<String, Object>> contactList = new ArrayList<>();
        for(ClientContactDTO cc : dto.getContacts())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.NAME, cc.getName());
            m.put(Field.POSITION, cc.getPosition());
            m.put(Field.PHONE, cc.getPhone());
            m.put(Field.EMAIL, cc.getEmail());
            m.put(Field.IS_GENERAL, cc.getGeneral());
            contactList.add(m);
        }
        resp.addData(Field.CONTACT_LIST, contactList);
        List<ProductClientDetailDTO> productDTOList = remoteProductService.getProductInfoList(clientId);
        List<Map<String, Object>> opened = new ArrayList<>();
        List<Map<String, Object>> toOpen = new ArrayList<>();
        for(ProductClientDetailDTO d : productDTOList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.PRODUCT_ID, d.getProductId() + "");
            m.put(Field.PRODUCT_NAME, d.getName());
            if(d.getClientProductId() != null)
            {
                m.put(Field.CLIENT_PRODUCT_ID, d.getClientProductId() + "");
                m.put(Field.APP_ID, d.getAppId());
                m.put(Field.BILL_PLAN, d.getBillPlan());
                if(BillPlan.YEAR.getId().equals(d.getBillPlan()))
                {
                    m.put(Field.START_DATE, DateUtils.format(d.getFromDate(), DateFormat.YYYY_MM_DD));
                    m.put(Field.END_DATE, DateUtils.format(d.getToDate(), DateFormat.YYYY_MM_DD));
                    m.put(Field.AMOUNT, NumberUtils.formatAmount(d.getAmount()));
                }
                else
                {
                    m.put(Field.UNIT_AMT, NumberUtils.formatAmount(d.getUnitAmt()));
                    m.put(Field.BALANCE, NumberUtils.formatAmount(d.getBalance()));
                }
                opened.add(m);
            }
            else
            {
                toOpen.add(m);
            }
        }
        resp.addData(Field.OPENED, opened);
        resp.addData(Field.TO_OPEN, toOpen);
    }
}