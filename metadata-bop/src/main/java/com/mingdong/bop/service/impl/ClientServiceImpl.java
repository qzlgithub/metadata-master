package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ClientVO;
import com.mingdong.bop.model.ContactVO;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ClientContactReqDTO;
import com.mingdong.core.model.dto.request.ClientReqDTO;
import com.mingdong.core.model.dto.request.DisableClientReqDTO;
import com.mingdong.core.model.dto.request.RechargeReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientOperateLogResDTO;
import com.mingdong.core.model.dto.response.ClientUserDictResDTO;
import com.mingdong.core.model.dto.response.ClientUserResDTO;
import com.mingdong.core.model.dto.response.IndustryResDTO;
import com.mingdong.core.model.dto.response.ProductDetailResDTO;
import com.mingdong.core.model.dto.response.RechargeInfoResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.SubUserResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.core.service.TradeRpcService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

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
    private RedisDao redisDao;
    @Resource
    private SystemService systemService;
    @Resource
    private CommonRpcService commonRpcService;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private ProductRpcService productRpcService;
    @Resource
    private SystemRpcService systemRpcService;
    @Resource
    private TradeRpcService tradeRpcService;

    @Override
    public void checkIfUsernameExist(String username, RestResp resp)
    {
        resp.addData(Field.EXIST, commonRpcService.checkIfClientExist(username));
    }

    @Override
    public void getSimilarCorp(String name, Long clientId, RestListResp resp)
    {
        ListDTO<ClientInfoResDTO> listDTO = clientRpcService.getSimilarCorpByName(name, clientId);
        List<ClientInfoResDTO> dataList = listDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            resp.setTotal(dataList.size());
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ClientInfoResDTO o : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.REGISTER_DATE, DateUtils.format(o.getRegisterTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.CORP_NAME, o.getCorpName());
                map.put(Field.LICENSE, o.getLicense());
                map.put(Field.MANAGER_NAME, o.getManagerName());
                list.add(map);
            }
            resp.setList(list);
        }
        else
        {
            resp.setTotal(0);
        }
    }

    @Override
    public void getClientList(String keyword, Long parentIndustryId, Long industryId, Integer enabled, Long managerId,
            Page page, RestListResp res)
    {
        ListDTO<ClientInfoResDTO> clientInfoListDTO = clientRpcService.getClientInfoListBy(keyword,
                industryId == null ? parentIndustryId : industryId, enabled, managerId, page);
        res.setTotal(clientInfoListDTO.getTotal());
        List<ClientInfoResDTO> clientInfoList = clientInfoListDTO.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(clientInfoList))
        {
            for(ClientInfoResDTO clientInfo : clientInfoList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, clientInfo.getClientId() + "");
                map.put(Field.USERNAME, clientInfo.getUsername());
                map.put(Field.CORP_NAME, clientInfo.getCorpName());
                map.put(Field.SHORT_NAME, clientInfo.getShortName());
                map.put(Field.INDUSTRY, systemService.getIndustryName(clientInfo.getIndustryId()));
                map.put(Field.MANAGER_NAME, clientInfo.getManagerName());
                map.put(Field.ACCOUNT_QTY, clientInfo.getAccountQty());
                map.put(Field.REGISTER_DATE, DateUtils.format(clientInfo.getRegisterTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.USER_ENABLED, clientInfo.getUserEnabled());
                list.add(map);
            }
        }
        res.setList(list);
    }

    @Override
    public void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, RestResp resp)
    {
        DisableClientReqDTO dto = new DisableClientReqDTO();
        dto.setClientIdList(clientIdList);
        dto.setEnabled(enabled);
        dto.setReason(reason);
        dto.setManagerId(RequestThread.getOperatorId());
        ResponseDTO responseDTO = clientRpcService.changeClientStatus(dto);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void deleteClient(List<Long> idList)
    {
        clientRpcService.setClientDeleted(idList);
    }

    @Override
    public void resetPassword(List<Long> idList)
    {
        clientRpcService.setClientPassword(idList, Constant.DEFAULT_ENC_PWD);
    }

    @Override
    public void getClientSubUserList(Long clientId, Page page, RestListResp resp)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<SubUserResDTO> listDTO = clientRpcService.getSubUserList(clientId, page);
        resp.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(SubUserResDTO o : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, o.getUserId() + "");
                map.put(Field.USERNAME, o.getUsername());
                map.put(Field.NAME, o.getName());
                map.put(Field.PHONE, o.getPhone());
                map.put(Field.IS_DELETED, o.getDeleted());
                list.add(map);
            }
        }
        resp.setList(list);
    }

    @Override
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp)
    {
        RechargeReqDTO reqDTO = new RechargeReqDTO();
        reqDTO.setRenew(TrueOrFalse.FALSE);
        reqDTO.setClientId(clientId);
        reqDTO.setProductId(productId);
        reqDTO.setRechargeType(rechargeType);
        reqDTO.setAmount(amount);
        reqDTO.setContractNo(contractNo);
        reqDTO.setRemark(remark);
        reqDTO.setBillPlan(billPlan);
        reqDTO.setStartDate(startDate);
        reqDTO.setEndDate(endDate);
        reqDTO.setManagerId(RequestThread.getOperatorId());
        ResponseDTO respDTO = tradeRpcService.productRecharge(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp)
    {
        RechargeReqDTO reqDTO = new RechargeReqDTO();
        reqDTO.setRenew(TrueOrFalse.FALSE);
        reqDTO.setClientId(clientId);
        reqDTO.setProductId(productId);
        reqDTO.setRechargeType(rechargeType);
        reqDTO.setAmount(amount);
        reqDTO.setContractNo(contractNo);
        reqDTO.setRemark(remark);
        reqDTO.setBillPlan(billPlan);
        reqDTO.setUnitAmt(unitAmt);
        reqDTO.setManagerId(RequestThread.getOperatorId());
        ResponseDTO respDTO = tradeRpcService.productRecharge(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void getProductRenewInfo(Long clientId, Long productId, RestResp resp)
    {
        RechargeInfoResDTO resDTO = clientRpcService.getLatestRechargeInfo(clientId, productId);
        if(resDTO.getResult() != RestResult.SUCCESS)
        {
            resp.setError(resDTO.getResult());
            return;
        }
        resp.addData(Field.BILL_PLAN, resDTO.getBillPlan());
        if(BillPlan.BY_TIME.getId().equals(resDTO.getBillPlan()))
        {
            resp.addData(Field.START_DATE, DateUtils.format(resDTO.getStartDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.END_DATE, DateUtils.format(resDTO.getEndDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.AMOUNT, NumberUtils.formatAmount(resDTO.getAmount()));
        }
        else
        {
            resp.addData(Field.BALANCE, NumberUtils.formatAmount(resDTO.getBalance()));
            resp.addData(Field.UNIT_AMT, NumberUtils.formatAmount(resDTO.getUnitAmt()));
        }
        resp.addData(Field.TOTAL_AMT, NumberUtils.formatAmount(resDTO.getTotalRecharge()));
    }

    @Override
    public void renewProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp)
    {
        RechargeReqDTO reqDTO = new RechargeReqDTO();
        reqDTO.setRenew(TrueOrFalse.TRUE);
        reqDTO.setClientId(clientId);
        reqDTO.setProductId(productId);
        reqDTO.setRechargeType(rechargeType);
        reqDTO.setAmount(amount);
        reqDTO.setContractNo(contractNo);
        reqDTO.setRemark(remark);
        reqDTO.setBillPlan(billPlan);
        reqDTO.setStartDate(startDate);
        reqDTO.setEndDate(endDate);
        reqDTO.setManagerId(RequestThread.getOperatorId());
        ResponseDTO respDTO = tradeRpcService.productRecharge(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void renewProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp)
    {
        RechargeReqDTO reqDTO = new RechargeReqDTO();
        reqDTO.setRenew(TrueOrFalse.TRUE);
        reqDTO.setClientId(clientId);
        reqDTO.setProductId(productId);
        reqDTO.setRechargeType(rechargeType);
        reqDTO.setAmount(amount);
        reqDTO.setContractNo(contractNo);
        reqDTO.setRemark(remark);
        reqDTO.setBillPlan(billPlan);
        reqDTO.setUnitAmt(unitAmt);
        reqDTO.setManagerId(RequestThread.getOperatorId());
        ResponseDTO respDTO = tradeRpcService.productRecharge(reqDTO);
        resp.setError(respDTO.getResult());
    }

    @Override
    public void getClientOperateLog(Long clientId, Page page, RestListResp resp)
    {
        ListDTO<ClientOperateLogResDTO> listDTO = clientRpcService.getClientOperateLog(clientId, page);
        resp.setTotal(listDTO.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(ClientOperateLogResDTO info : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.OPERATE_TIME, DateUtils.format(info.getOperateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TYPE, info.getType());
                map.put(Field.MANAGER_NAME, info.getManagerName());
                map.put(Field.REASON, info.getReason());
                list.add(map);
            }
        }
        resp.setList(list);
    }

    @Override
    public void checkIfContractExist(String contractNo, RestResp resp)
    {
        resp.addData(Field.EXIST, productRpcService.checkIfContractExist(contractNo));
    }

    @Override
    public void addClient(ClientVO vo, RestResp resp)
    {
        if(StringUtils.isNullBlank(vo.getUsername()) || StringUtils.isNullBlank(vo.getPassword()) ||
                StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(vo.getShortName()) ||
                StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null || (!TrueOrFalse.TRUE.equals(
                vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())) || CollectionUtils.isEmpty(
                vo.getContacts()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return;
        }
        List<ClientContactReqDTO> contactList = new ArrayList<>(vo.getContacts().size());
        for(ContactVO o : vo.getContacts())
        {
            if(StringUtils.isNullBlank(o.getName()) || StringUtils.isNullBlank(o.getPosition()) ||
                    StringUtils.isNullBlank(o.getPhone()))
            {
                resp.setError(RestResult.KEY_FIELD_MISSING);
                return;
            }
            if(!TrueOrFalse.TRUE.equals(o.getGeneral()) && !TrueOrFalse.FALSE.equals(o.getGeneral()))
            {
                resp.setError(RestResult.KEY_FIELD_MISSING);
                return;
            }
            ClientContactReqDTO reqDTO = new ClientContactReqDTO();
            reqDTO.setName(o.getName());
            reqDTO.setPosition(o.getPosition());
            reqDTO.setPhone(o.getPhone());
            reqDTO.setEmail(o.getEmail());
            reqDTO.setGeneral(o.getGeneral());
            contactList.add(reqDTO);
        }
        ClientReqDTO dto = new ClientReqDTO();
        dto.setCorpName(vo.getCorpName());
        dto.setShortName(vo.getShortName());
        dto.setIndustryId(vo.getIndustryId());
        dto.setLicense(vo.getLicense());
        dto.setUsername(vo.getUsername());
        dto.setContactList(contactList);
        dto.setEnabled(vo.getEnabled());
        dto.setManagerId(RequestThread.getOperatorId());
        ResponseDTO res = clientRpcService.addClient(dto);
        resp.setError(res.getResult());
    }

    @Override
    public void editClient(ClientVO vo, RestResp resp)
    {
        if(vo.getClientId() == null || StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(
                vo.getShortName()) || StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null ||
                (!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return;
        }
        ClientReqDTO reqDTO = new ClientReqDTO();
        reqDTO.setClientId(vo.getClientId());
        reqDTO.setCorpName(vo.getCorpName());
        reqDTO.setShortName(vo.getShortName());
        reqDTO.setLicense(vo.getLicense());
        reqDTO.setIndustryId(vo.getIndustryId());
        reqDTO.setEnabled(vo.getEnabled());
        reqDTO.setManagerId(vo.getManagerId());
        List<ClientContactReqDTO> contactList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(vo.getContacts()))
        {
            for(ContactVO c : vo.getContacts())
            {
                if(StringUtils.isNullBlank(c.getName()) || StringUtils.isNullBlank(c.getPosition()) ||
                        StringUtils.isNullBlank(c.getPhone()) || (!TrueOrFalse.TRUE.equals(c.getGeneral()) &&
                        !TrueOrFalse.FALSE.equals(c.getGeneral())))
                {
                    resp.setError(RestResult.KEY_FIELD_MISSING);
                    return;
                }
                ClientContactReqDTO dto = new ClientContactReqDTO();
                dto.setId(c.getId());
                dto.setName(c.getName());
                dto.setPosition(c.getPosition());
                dto.setPhone(c.getPhone());
                dto.setEmail(c.getEmail());
                dto.setGeneral(c.getGeneral());
                contactList.add(dto);
            }
        }
        ResponseDTO dto = clientRpcService.editClient(reqDTO, contactList, vo.getContactDel());
        resp.setError(dto.getResult());
    }

    @Override
    public Map<String, Object> getClientInfoAndProduct(Long clientId) // TODO 客户详情
    {
        Map<String, Object> map = new HashMap<>();
        ClientDetailResDTO dto = clientRpcService.getClientDetail(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            return map;
        }
        map.put(Field.CLIENT_ID, clientId + "");
        map.put(Field.CORP_NAME, dto.getCorpName());
        map.put(Field.SHORT_NAME, dto.getShortName());
        map.put(Field.INDUSTRY_NAME, redisDao.getIndustryInfo(dto.getIndustryId()));
        map.put(Field.LICENSE, dto.getLicense());
        map.put(Field.USERNAME, dto.getUsername());
        map.put(Field.USER_ENABLED, dto.getEnabled());
        map.put(Field.REGISTER_DATE, DateUtils.format(dto.getAddTime(), DateFormat.YYYY_MM_DD));
        map.put(Field.MANAGER_NAME, dto.getManagerName());
        List<Map<String, Object>> userList = new ArrayList<>();
        for(ClientUserResDTO o : dto.getUsers())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId() + "");
            m.put(Field.USERNAME, o.getUsername());
            m.put(Field.NAME, o.getName());
            m.put(Field.PHONE, o.getPhone());
            userList.add(m);
        }
        map.put(Field.USER_LIST, userList);
        List<Map<String, Object>> contactList = new ArrayList<>();
        for(ClientContactReqDTO o : dto.getContacts())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.NAME, o.getName());
            m.put(Field.POSITION, o.getPosition());
            m.put(Field.PHONE, o.getPhone());
            m.put(Field.EMAIL, o.getEmail());
            m.put(Field.IS_GENERAL, o.getGeneral());
            contactList.add(m);
        }
        map.put(Field.CONTACT_LIST, contactList);
        List<ProductDetailResDTO> productDTOList = productRpcService.getProductInfoList(clientId);
        List<Map<String, Object>> opened = new ArrayList<>();
        List<Map<String, Object>> toOpen = new ArrayList<>();
        List<Map<String, Object>> custom = new ArrayList<>();
        for(ProductDetailResDTO d : productDTOList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.PRODUCT_ID, d.getProductId() + "");
            m.put(Field.PRODUCT_NAME, d.getName());
            m.put(Field.CUSTOM, d.getCustom());
            m.put(Field.TYPE_NAME, ProductType.getNameById(d.getProductType()));
            m.put(Field.CODE, d.getCode());
            m.put(Field.REMARK, d.getRemark());
            if(d.getClientProductId() != null && TrueOrFalse.TRUE.equals(d.getIsOpened()))
            {
                m.put(Field.CLIENT_PRODUCT_ID, d.getClientProductId() + "");
                m.put(Field.APP_ID, d.getAppId());
                m.put(Field.BILL_PLAN, d.getBillPlan());
                if(BillPlan.BY_TIME.getId().equals(d.getBillPlan()))
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
            else if(d.getClientProductId() != null && TrueOrFalse.FALSE.equals(d.getIsOpened()))
            {
                m.put(Field.IS_SELECTED, TrueOrFalse.TRUE);
                toOpen.add(m);
            }
            else if(Custom.COMMON == Custom.getById(d.getCustom()))
            {
                m.put(Field.IS_SELECTED, TrueOrFalse.FALSE);
                toOpen.add(m);
            }
            if(TrueOrFalse.TRUE.equals(d.getCustom()))
            {
                if(d.getClientProductId() != null)
                {
                    m.put(Field.CLIENT_PRODUCT_ID, d.getClientProductId() + "");
                    m.put(Field.IS_SELECTED, TrueOrFalse.TRUE);
                }
                else
                {
                    m.put(Field.IS_SELECTED, TrueOrFalse.FALSE);
                }
                custom.add(m);
            }
        }
        map.put(Field.OPENED, opened);
        map.put(Field.TO_OPEN, toOpen);
        map.put(Field.PRODUCT_CUSTOM_LIST, custom);
        return map;
    }

    @Override
    public Map<String, Object> getClientInfo(Long clientId)
    {
        Map<String, Object> data = new HashMap<>();
        ClientDetailResDTO dto = clientRpcService.getClientInfo(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            return data;
        }
        data.put(Field.CLIENT_ID, clientId + "");
        data.put(Field.USERNAME, dto.getUsername());
        data.put(Field.CORP_NAME, dto.getCorpName());
        data.put(Field.SHORT_NAME, dto.getShortName());
        data.put(Field.LICENSE, dto.getLicense());
        data.put(Field.INDUSTRY_ID, dto.getIndustryId() + "");
        data.put(Field.USER_STATUS, dto.getEnabled());
        data.put(Field.MANAGER_ID, dto.getManagerId());
        List<Map<String, Object>> contacts = new ArrayList<>(dto.getContacts().size());
        for(ClientContactReqDTO o : dto.getContacts())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId() + "");
            m.put(Field.NAME, o.getName());
            m.put(Field.POSITION, o.getPosition());
            m.put(Field.PHONE, o.getPhone());
            m.put(Field.EMAIL, o.getEmail());
            m.put(Field.IS_GENERAL, o.getGeneral());
            contacts.add(m);
        }
        data.put(Field.CONTACTS, contacts);

        IndustryResDTO industry = systemRpcService.getIndustryDictOfTarget(dto.getIndustryId());
        if(RestResult.SUCCESS == industry.getResult())
        {
            data.put(Field.PARENT_INDUSTRY_ID, industry.getParentId() + "");
            data.put(Field.INDUSTRY_PARENT_DICT, industry.getParents());
            data.put(Field.INDUSTRY_DICT, industry.getPeers());
        }
        return data;
    }

    @Override
    public void selectCustomProduct(Long clientId, List<Long> productIds, RestResp resp)
    {
        ResponseDTO responseDTO = clientRpcService.selectCustomProduct(clientId, productIds);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void removeCustomClientProduct(Long clientProductId, RestResp resp)
    {
        ResponseDTO responseDTO = clientRpcService.removeCustomClientProduct(clientProductId);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public Map<String, Object> getClientAccountDict(Long clientId)
    {
        Map<String, Object> map = new HashMap<>();
        ClientUserDictResDTO res = clientRpcService.getClientAccountDict(clientId);
        map.put(Field.CORP_NAME, res.getCorpName());
        map.put(Field.ACCOUNT_DICT, res.getUserDict());
        return map;
    }

    @Override
    public void getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate, Page page,
            RestListResp res)
    {
        ListDTO<AccessResDTO> listDTO = clientRpcService.getClientRequestRecord(clientId, userId, productId, fromDate,
                toDate, page);
        res.setTotal(listDTO.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(AccessResDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.REQUEST_AT, DateUtils.format(o.getRequestAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                m.put(Field.REQUEST_NO, o.getRequestNo());
                m.put(Field.USERNAME, o.getUsername());
                m.put(Field.PRODUCT_NAME, o.getProductName());
                m.put(Field.BILL_PLAN, BillPlan.getNameById(o.getBillPlan()));
                m.put(Field.IS_HIT, o.getHit());
                if(BillPlan.BY_TIME.equals(o.getBillPlan()))
                {
                    m.put(Field.FEE, "-");
                    m.put(Field.BALANCE, "-");
                }
                else
                {
                    m.put(Field.FEE, NumberUtils.formatAmount(o.getFee()));
                    m.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                }
                list.add(m);
            }
            res.setList(list);
        }
    }

    @Override
    public String getClientCorpName(Long clientId)
    {
        return clientRpcService.getClientCorpName(clientId);
    }

    @Override
    public void getProductRechargeList(Long clientId, Long productId, Date fromDate, Date toDate, Page page,
            RestListResp res)
    {
        ListDTO<RechargeResDTO> listDTO = clientRpcService.getClientRechargeRecord(null, clientId, productId, null,
                null, fromDate, toDate, page);
        res.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(RechargeResDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.RECHARGE_AT, DateUtils.format(o.getRechargeAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                m.put(Field.RECHARGE_NO, o.getRechargeNo());
                m.put(Field.PRODUCT_NAME, o.getProductName());
                m.put(Field.RECHARGE_TYPE_NAME, o.getRechargeTypeName());
                m.put(Field.BILL_PLAN_NAME, BillPlan.getNameById(o.getBillPlan()));
                m.put(Field.AMOUNT, NumberUtils.formatAmount(o.getAmount()));
                m.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                m.put(Field.MANAGER_NAME, o.getManagerName());
                m.put(Field.CONTRACT_NO, o.getContractNo());
                list.add(m);
            }
            res.setList(list);
        }
    }

    @Override
    public XSSFWorkbook createClientRequestXlsx(Long clientId, Long userId, Long productId, Date startTime,
            Date endTime, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("消费数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("请求时间");
        row.createCell(1).setCellValue("请求单号");
        row.createCell(2).setCellValue("客户账号");
        row.createCell(3).setCellValue("产品服务");
        row.createCell(4).setCellValue("计费方式");
        row.createCell(5).setCellValue("是否击中");
        row.createCell(6).setCellValue("费用(元)");
        row.createCell(7).setCellValue("余额(元)");
        ListDTO<AccessResDTO> listDTO = clientRpcService.getClientRequestRecord(clientId, userId, productId, startTime,
                endTime, page);
        List<AccessResDTO> list = listDTO.getList();
        if(!CollectionUtils.isEmpty(list))
        {
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(
                    wb.getCreationHelper().createDataFormat().getFormat(DateFormat.YYYY_MM_DD_HH_MM_SS));
            for(int i = 0; i < list.size(); i++)
            {
                AccessResDTO dataInfo = list.get(i);
                Row dataRow = sheet.createRow(i + 1);
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(dataInfo.getRequestAt());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataInfo.getRequestNo());
                dataRow.createCell(2).setCellValue(dataInfo.getUsername());
                dataRow.createCell(3).setCellValue(dataInfo.getProductName());
                dataRow.createCell(4).setCellValue(BillPlan.getNameById(dataInfo.getBillPlan()));
                dataRow.createCell(5).setCellValue(TrueOrFalse.TRUE.equals(dataInfo.getHit()) ? "击中" : "未击中");
                if(BillPlan.BY_TIME.getId().equals(dataInfo.getBillPlan()))
                {
                    dataRow.createCell(6).setCellValue("-");
                    dataRow.createCell(7).setCellValue("-");
                }
                else
                {
                    dataRow.createCell(6).setCellValue(NumberUtils.formatAmount(dataInfo.getFee()));
                    dataRow.createCell(7).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
                }
            }
        }
        return wb;
    }

    @Override
    public XSSFWorkbook createClientRechargeXlsx(Long clientId, Long productId, Date startTime, Date endTime, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("充值记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("充值时间");
        row.createCell(1).setCellValue("充值单号");
        row.createCell(2).setCellValue("产品服务");
        row.createCell(3).setCellValue("充值类型");
        row.createCell(4).setCellValue("计费方式");
        row.createCell(5).setCellValue("充值金额");
        row.createCell(6).setCellValue("产品余额");
        row.createCell(7).setCellValue("经手人");
        row.createCell(8).setCellValue("合同编号");

        ListDTO<RechargeResDTO> listDTO = clientRpcService.getClientRechargeRecord(null, clientId, productId, null,
                null, startTime, endTime, page);
        List<RechargeResDTO> list = listDTO.getList();
        if(!CollectionUtils.isEmpty(list))
        {
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(
                    wb.getCreationHelper().createDataFormat().getFormat(DateFormat.YYYY_MM_DD_HH_MM_SS));
            for(int i = 0; i < list.size(); i++)
            {
                RechargeResDTO pri = list.get(i);
                Row dataRow = sheet.createRow(i + 1);
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(pri.getRechargeAt());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(pri.getRechargeNo());
                dataRow.createCell(2).setCellValue(pri.getProductName());
                dataRow.createCell(3).setCellValue(pri.getRechargeTypeName());
                dataRow.createCell(4).setCellValue(BillPlan.getNameById(pri.getBillPlan()));
                dataRow.createCell(5).setCellValue(NumberUtils.formatAmount(pri.getAmount()));
                dataRow.createCell(6).setCellValue(NumberUtils.formatAmount(pri.getBalance()));
                dataRow.createCell(7).setCellValue(pri.getManagerName());
                dataRow.createCell(8).setCellValue(pri.getContractNo());
            }
        }
        return wb;
    }
}