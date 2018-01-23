package com.mingdong.bop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.configurer.Param;
import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.Trade;
import com.mingdong.bop.domain.entity.Client;
import com.mingdong.bop.domain.entity.ClientAccount;
import com.mingdong.bop.domain.entity.ClientInfo;
import com.mingdong.bop.domain.entity.ClientOperateInfo;
import com.mingdong.bop.domain.entity.ClientOperateLog;
import com.mingdong.bop.domain.entity.ClientProduct;
import com.mingdong.bop.domain.entity.ClientUser;
import com.mingdong.bop.domain.entity.DictIndustry;
import com.mingdong.bop.domain.entity.Manager;
import com.mingdong.bop.domain.entity.ProductClientInfo;
import com.mingdong.bop.domain.entity.ProductRecharge;
import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import com.mingdong.bop.domain.mapper.ClientAccountMapper;
import com.mingdong.bop.domain.mapper.ClientInfoMapper;
import com.mingdong.bop.domain.mapper.ClientMapper;
import com.mingdong.bop.domain.mapper.ClientOperateInfoMapper;
import com.mingdong.bop.domain.mapper.ClientOperateLogMapper;
import com.mingdong.bop.domain.mapper.ClientProductMapper;
import com.mingdong.bop.domain.mapper.ClientUserMapper;
import com.mingdong.bop.domain.mapper.DictIndustryMapper;
import com.mingdong.bop.domain.mapper.ManagerMapper;
import com.mingdong.bop.domain.mapper.ProductClientInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeMapper;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.util.IDUtils;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
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
    private ClientMapper clientMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientAccountMapper clientAccountMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ManagerMapper managerMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private ProductClientInfoMapper productClientInfoMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;
    @Resource
    private ClientOperateLogMapper clientOperateLogMapper;
    @Resource
    private ClientOperateInfoMapper clientOperateInfoMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;

    @Override
    public void checkIfUsernameExist(String username, BLResp resp)
    {
        ClientUser user = clientUserMapper.findByUsername(username);
        resp.addData(Field.EXIST, user == null ? 0 : 1);
    }

    @Override
    public void getSimilarCorp(String corpName, Long clientId, BLResp resp)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        List<ClientInfo> dataList = clientInfoMapper.getSimilarCorpByName(corpName, clientId);
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(ClientInfo client : dataList)
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
                List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(parentIndustryId,
                        TrueOrFalse.TRUE);
                for(DictIndustry d : parentList)
                {
                    industryList.add(d.getId());
                }
            }
        }
        else
        {
            industryList.add(industryId);
        }
        int total = clientInfoMapper.countBy(enabled, username, cropName, shortName, industryList);
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);

            List<ClientInfo> clientInfoList = clientInfoMapper.getListBy(enabled, username, cropName, shortName,
                    industryList);
            List<Map<String, Object>> list = new ArrayList<>(clientInfoList.size());
            for(ClientInfo clientInfo : clientInfoList)
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
            resp.addData(Field.LIST, list);
        }
        return resp;
    }

    @Override
    @Transactional
    public void addCorp(String username, String password, String corpName, String shortName, Long industryId,
            String name, String phone, String email, String license, Integer enabled, BLResp resp)
    {
        Date curr = new Date();
        ClientUser clientUser = clientUserMapper.findByUsername(username);
        if(clientUser != null)// 校验帐号是否重复
        {
            resp.result(RestResult.ACCOUNT_IS_EXIST);
            return;
        }
        Long clientId = IDUtils.getClientId(param.getNodeId());
        Long clientUserId = IDUtils.getClientUser(param.getNodeId());
        // 创建客户主账号
        clientUser = new ClientUser();
        clientUser.setId(clientUserId);
        clientUser.setCreateTime(curr);
        clientUser.setUpdateTime(curr);
        clientUser.setClientId(clientId);
        clientUser.setName(name);
        clientUser.setPhone(phone);
        clientUser.setEmail(email);
        clientUser.setUsername(username);
        clientUser.setPassword(password);
        clientUser.setEnabled(enabled);
        clientUser.setDeleted(TrueOrFalse.FALSE);
        // 创建客户
        Client client = new Client();
        client.setId(clientId);
        client.setCreateTime(curr);
        client.setUpdateTime(curr);
        client.setCorpName(corpName);
        client.setShortName(shortName);
        client.setLicense(license);
        client.setIndustryId(industryId);
        client.setPrimaryUserId(clientUserId);
        client.setManagerId(RequestThread.getOperatorId());
        client.setAccountQty(1);
        client.setDeleted(TrueOrFalse.FALSE);
        // 保存客户及其账号
        clientUserMapper.add(clientUser);
        clientMapper.add(client);
    }

    @Override
    @Transactional
    public void editClientInfo(Long clientId, String corpName, String shortName, String license, Long industryId,
            String name, String phone, String email, Integer userEnabled, Integer accountEnabled, BLResp resp)
    {
        Date current = new Date();
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 更新用户信息
        ClientUser clientUser = clientUserMapper.findById(client.getPrimaryUserId());
        clientUser.setUpdateTime(current);
        clientUser.setName(name);
        clientUser.setPhone(phone);
        clientUser.setEmail(email);
        clientUser.setEnabled(userEnabled);
        clientUserMapper.updateById(clientUser);
        // 更新客户账户状态
        ClientAccount clientAccount = clientAccountMapper.findById(clientId);
        if(clientAccount != null)
        {
            clientAccount.setUpdateTime(current);
            clientAccount.setEnabled(accountEnabled);
            clientAccountMapper.updateById(clientAccount);
        }
        else
        {
            clientAccount = new ClientAccount();
            clientAccount.setId(clientId);
            clientAccount.setCreateTime(current);
            clientAccount.setUpdateTime(current);
            clientAccount.setBalance(new BigDecimal(0));
            clientAccount.setEnabled(accountEnabled);
            clientAccountMapper.add(clientAccount);
        }
        // 更新客户信息
        client.setUpdateTime(current);
        client.setCorpName(corpName);
        client.setShortName(shortName);
        client.setLicense(license);
        client.setIndustryId(industryId);
        clientMapper.updateById(client);
    }

    @Override
    public Map<String, Object> findClientInfo(Long clientId)
    {
        Map<String, Object> map = new HashMap<>();
        Client client = clientMapper.findById(clientId);
        if(client != null)
        {
            ClientUser user = clientUserMapper.findById(client.getPrimaryUserId());
            ClientAccount clientAccount = clientAccountMapper.findById(clientId);
            DictIndustry industry = dictIndustryMapper.findById(client.getIndustryId());
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
        Client client = clientMapper.findById(clientId);
        if(client != null)
        {
            // 主账号
            ClientUser masterUser = clientUserMapper.findById(client.getPrimaryUserId());
            map.put(Field.NAME, masterUser.getName());
            map.put(Field.PHONE, masterUser.getPhone());
            map.put(Field.EMAIL, masterUser.getEmail());
            map.put(Field.USERNAME, masterUser.getUsername());
            map.put(Field.USER_ENABLED, masterUser.getEnabled());
            // 子账号
            List<ClientUser> subUserList = clientUserMapper.getListByClientAndStatus(clientId, TrueOrFalse.TRUE,
                    TrueOrFalse.FALSE);
            List<Map<String, Object>> userList = new ArrayList<>();
            for(ClientUser user : subUserList)
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
            List<ProductClientInfo> pciList = productClientInfoMapper.getListByClient(clientId);
            List<Map<String, Object>> opened = new ArrayList<>();
            List<Map<String, Object>> toOpen = new ArrayList<>();
            for(ProductClientInfo pci : pciList)
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
            ClientAccount account = clientAccountMapper.findById(clientId);
            map.put(Field.BALANCE, account == null ? "0.00" : NumberUtils.formatAmount(account.getBalance()));
            // 其他
            Manager manager = managerMapper.findById(client.getManagerId());
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
        List<ClientUser> userList = clientUserMapper.getListByClientsAndPrimary(clientIdList);
        if(CollectionUtils.isEmpty(userList))
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        List<Long> clientUserIdList = new ArrayList<>();
        List<ClientOperateLog> logList = new ArrayList<>();
        for(ClientUser user : userList)
        {
            if(!enabled.equals(user.getEnabled()))
            {
                clientUserIdList.add(user.getId());
                ClientOperateLog log = new ClientOperateLog();
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
        clientOperateLogMapper.addList(logList);
        clientUserMapper.updateStatusByIds(enabled, current, clientUserIdList);
    }

    @Override
    @Transactional
    public void setClientDeleted(List<Long> idList, BLResp resp)
    {
        clientMapper.setClientDeleted(idList, new Date());
    }

    @Override
    @Transactional
    public void resetClientPassword(List<Long> idList, BLResp resp)
    {
        List<Client> clientList = clientMapper.getListByIdList(idList);
        if(!CollectionUtils.isEmpty(clientList))
        {
            List<Long> clientUserIdList = new ArrayList<>(clientList.size());
            for(Client client : clientList)
            {
                clientUserIdList.add(client.getPrimaryUserId());
            }
            clientUserMapper.resetPasswordByIds(Constant.DEFAULT_ENC_PWD, new Date(), clientUserIdList);
        }
    }

    @Override
    public Map<String, Object> getClientProductInfo(Long clientProductId)
    {
        ClientProduct cp = clientProductMapper.findById(clientProductId);
        Map<String, Object> map = new HashMap<>();
        if(cp != null)
        {
            Client client = clientMapper.findById(cp.getClientId());
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
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            return list;
        }
        List<ClientUser> cuList = clientUserMapper.getListByClientAndStatus(clientId, TrueOrFalse.TRUE,
                TrueOrFalse.FALSE);
        for(ClientUser cu : cuList)
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
        ClientUser cu = clientUserMapper.findById(clientUserId);
        if(cu == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        cu = new ClientUser();
        cu.setId(clientUserId);
        cu.setUpdateTime(new Date());
        cu.setPassword(Md5Utils.encrypt(Md5Utils.encrypt(Constant.DEFAULT_PASSWORD)));
        clientUserMapper.updateSkipNull(cu);
    }

    @Override
    @Transactional
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
    {

        Date current = new Date();
        ClientProduct cp = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(cp != null)
        {
            resp.result(RestResult.PRODUCT_OPENED);
            return;
        }
        ProductRecharge pro = productRechargeMapper.findByContractNo(contractNo);
        if(pro != null)
        {
            resp.result(RestResult.CONTRACT_IS_EXIST);
            return;
        }
        Long clientProductId = IDUtils.getClientProductId(param.getNodeId());
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        // 保存产品的充值记录
        ProductRecharge pr = new ProductRecharge();
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
        productRechargeMapper.add(pr);
        // 保存客户产品的账户变更信息
        cp = new ClientProduct();
        cp.setId(clientProductId);
        cp.setCreateTime(current);
        cp.setUpdateTime(current);
        cp.setClientId(clientId);
        cp.setProductId(productId);
        cp.setAppId(StringUtils.getUuid());
        cp.setBillPlan(billPlan);
        cp.setBalance(new BigDecimal(0));
        cp.setLatestRechargeId(productRechargeId);
        clientProductMapper.add(cp);
    }

    @Override
    @Transactional
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProduct cp = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(cp != null)
        {
            resp.result(RestResult.PRODUCT_OPENED);
            return;
        }
        Long clientProductId = IDUtils.getClientProductId(param.getNodeId());
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRecharge pr = new ProductRecharge();
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
        productRechargeMapper.add(pr);
        // 保存客户产品的账户变更信息
        cp = new ClientProduct();
        cp.setId(clientProductId);
        cp.setCreateTime(current);
        cp.setUpdateTime(current);
        cp.setClientId(clientId);
        cp.setProductId(productId);
        cp.setAppId(StringUtils.getUuid());
        cp.setBillPlan(billPlan);
        cp.setBalance(amount);
        cp.setLatestRechargeId(productRechargeId);
        clientProductMapper.add(cp);
    }

    @Override
    public void getProductRenewInfo(Long clientProductId, BLResp resp)
    {
        ClientProduct cp = clientProductMapper.findById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        ProductRecharge pr = productRechargeMapper.findById(cp.getLatestRechargeId());
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
        BigDecimal totalAmt = productRechargeMapper.sumAmountByClientProduct(clientProductId);
        resp.addData(Field.TOTAL_AMT, NumberUtils.formatAmount(totalAmt));
    }

    @Override
    @Transactional
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProduct cp = clientProductMapper.findById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 保存产品续费记录
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRecharge pr = new ProductRecharge();
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
        productRechargeMapper.add(pr);
        // 更新客户产品信息
        cp = new ClientProduct();
        cp.setId(clientProductId);
        cp.setUpdateTime(current);
        cp.setBillPlan(billPlan);
        cp.setBalance(new BigDecimal(0));
        cp.setLatestRechargeId(productRechargeId);
        clientProductMapper.updateSkipNull(cp);
    }

    @Override
    @Transactional
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
    {
        Date current = new Date();
        ClientProduct cp = clientProductMapper.findById(clientProductId);
        if(cp == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        // 保存产品续费记录
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRecharge pr = new ProductRecharge();
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
        productRechargeMapper.add(pr);
        // 更新客户产品信息
        ClientProduct cpUpd = new ClientProduct();
        cpUpd.setId(clientProductId);
        cpUpd.setUpdateTime(current);
        cpUpd.setBillPlan(billPlan);
        cpUpd.setBalance(amount.add(cp.getBalance()));
        cpUpd.setLatestRechargeId(productRechargeId);
        clientProductMapper.updateSkipNull(cpUpd);
    }

    @Override
    public void getClientOperateLog(Long clientId, Page page, BLResp resp)
    {
        Client client = clientMapper.findById(clientId);
        if(client == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        int total = clientOperateLogMapper.countByClientUser(client.getPrimaryUserId());
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientOperateInfo> dataList = clientOperateInfoMapper.getListByClientUser(client.getPrimaryUserId());
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ClientOperateInfo info : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.OPERATE_TIME, DateUtils.format(info.getOperateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TYPE, info.getType());
                map.put(Field.MANAGER_NAME, info.getManagerName());
                map.put(Field.REASON, info.getReason());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
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

        PageHelper.startPage(1, 1000, false);
        List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, startTime,
                endTime);
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        for(int i = 0; i < dataList.size(); i++)
        {
            ProductRechargeInfo pri = dataList.get(i);
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
        ProductRecharge pr = productRechargeMapper.findByContractNo(contractNo);
        resp.addData(Field.EXIST, pr == null ? 0 : 1);
    }
}