package com.mingdong.bop.service.impl;

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
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserDictDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.DisableClientDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.OpenClientProductDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductOpenDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.RechargeDTO;
import com.mingdong.core.model.dto.RechargeInfoDTO;
import com.mingdong.core.model.dto.AccessDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.core.util.IDUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    @Resource
    private Param param;
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

    @Override
    public void checkIfUsernameExist(String username, RestResp resp)
    {
        resp.addData(Field.EXIST, commonRpcService.checkIfClientExist(username));
    }

    @Override
    public void getSimilarCorp(String name, Long clientId, RestListResp resp)
    {
        ListDTO<ClientInfoDTO> listDTO = clientRpcService.getSimilarCorpByName(name, clientId);
        List<ClientInfoDTO> dataList = listDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            resp.setTotal(dataList.size());
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ClientInfoDTO o : dataList)
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
        ListDTO<ClientInfoDTO> clientInfoListDTO = clientRpcService.getClientInfoListBy(keyword,
                industryId == null ? parentIndustryId : industryId, enabled, managerId, page);
        res.setTotal(clientInfoListDTO.getTotal());
        List<ClientInfoDTO> clientInfoList = clientInfoListDTO.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(clientInfoList))
        {
            for(ClientInfoDTO clientInfo : clientInfoList)
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
    public void getClientInfoForEdit(Long clientId, RestResp resp)
    {
        ClientDetailDTO dto = clientRpcService.getClientInfoForEdit(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.setError(dto.getResult());
            return;
        }
        resp.addData(Field.CLIENT_ID, clientId + "");
        resp.addData(Field.USERNAME, dto.getUsername());
        resp.addData(Field.CORP_NAME, dto.getCorpName());
        resp.addData(Field.SHORT_NAME, dto.getShortName());
        resp.addData(Field.LICENSE, dto.getLicense());
        resp.addData(Field.INDUSTRY_ID, dto.getIndustryId() + "");
        resp.addData(Field.USER_STATUS, dto.getUserStatus());
        resp.addData(Field.MANAGER_ID, dto.getManagerId());
        List<Map<String, Object>> contacts = new ArrayList<>(dto.getContacts().size());
        for(ClientContactDTO d : dto.getContacts())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, d.getId() + "");
            m.put(Field.NAME, d.getName());
            m.put(Field.POSITION, d.getPosition());
            m.put(Field.PHONE, d.getPhone());
            m.put(Field.EMAIL, d.getEmail());
            m.put(Field.IS_GENERAL, d.getGeneral());
            contacts.add(m);
        }
        resp.addData(Field.CONTACTS, contacts);

        IndustryDTO industry = systemRpcService.getIndustryDictOfTarget(dto.getIndustryId());
        if(RestResult.SUCCESS == industry.getResult())
        {
            resp.addData(Field.PARENT_INDUSTRY_ID, industry.getParentId() + "");
            List<Map<String, Object>> industryParentDict = new ArrayList<>();
            for(DictDTO d : industry.getParents())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.KEY, d.getKey());
                map.put(Field.VALUE, d.getValue());
                industryParentDict.add(map);
            }
            resp.addData(Field.INDUSTRY_PARENT_DICT, industryParentDict);
            List<Map<String, Object>> industryDict = new ArrayList<>();
            for(DictDTO d : industry.getPeers())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.KEY, d.getKey());
                map.put(Field.VALUE, d.getValue());
                industryDict.add(map);
            }
            resp.addData(Field.INDUSTRY_DICT, industryDict);
        }
    }

    @Override
    public void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, RestResp resp)
    {
        DisableClientDTO dto = new DisableClientDTO();
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
    public List<Map<String, Object>> getClientSubUserList(Long clientId)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<SubUserDTO> listDTO = clientRpcService.getSubUserList(clientId, false);
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(SubUserDTO o : listDTO.getList())
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
        return list;
    }

    @Override
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp)
    {
        ProductOpenDTO dto = new ProductOpenDTO();
        dto.setClientId(clientId);
        dto.setProductId(productId);
        dto.setRechargeType(rechargeType);
        dto.setBillPlan(billPlan);
        dto.setAmount(amount);
        dto.setFromDate(startDate);
        dto.setToDate(endDate);
        dto.setContractNo(contractNo);
        dto.setRemark(remark);
        dto.setManagerId(RequestThread.getOperatorId());
        ResponseDTO res = clientRpcService.openProduct(dto);
        resp.setError(res.getResult());
    }

    @Override
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp)
    {
        ProductOpenDTO dto = new ProductOpenDTO();
        dto.setClientId(clientId);
        dto.setProductId(productId);
        dto.setRechargeType(rechargeType);
        dto.setBillPlan(billPlan);
        dto.setAmount(amount);
        dto.setUnitAmt(unitAmt);
        dto.setContractNo(contractNo);
        dto.setRemark(remark);
        dto.setManagerId(RequestThread.getOperatorId());
        ResponseDTO res = clientRpcService.openProduct(dto);
        resp.setError(res.getResult());
    }

    @Override
    public void getProductRenewInfo(Long clientProductId, RestResp resp)
    {
        RechargeInfoDTO rechargeInfoDTO = clientRpcService.getLatestRechargeInfo(clientProductId);
        if(rechargeInfoDTO.getResult() != RestResult.SUCCESS)
        {
            resp.setError(rechargeInfoDTO.getResult());
            return;
        }
        resp.addData(Field.BILL_PLAN, rechargeInfoDTO.getBillPlan());
        if(BillPlan.BY_TIME.getId().equals(rechargeInfoDTO.getBillPlan()))
        {
            resp.addData(Field.START_DATE, DateUtils.format(rechargeInfoDTO.getStartDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.END_DATE, DateUtils.format(rechargeInfoDTO.getEndDate(), DateFormat.YYYY_MM_DD));
            resp.addData(Field.AMOUNT, NumberUtils.formatAmount(rechargeInfoDTO.getAmount()));
        }
        else
        {
            resp.addData(Field.BALANCE, NumberUtils.formatAmount(rechargeInfoDTO.getBalance()));
            resp.addData(Field.UNIT_AMT, NumberUtils.formatAmount(rechargeInfoDTO.getUnitAmt()));
        }
        resp.addData(Field.TOTAL_AMT, NumberUtils.formatAmount(rechargeInfoDTO.getTotalRecharge()));
    }

    @Override
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp)
    {
        OpenClientProductDTO openClientProductDTO = new OpenClientProductDTO();
        Date current = new Date();
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        //        pr.setClientId(cp.getClientId());
        //        pr.setProductId(cp.getProductId());
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        //        pr.setBalance(amount);
        pr.setStartDate(startDate);
        pr.setEndDate(endDate);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        openClientProductDTO.setProductRechargeDTO(pr);
        ClientProductDTO cp = new ClientProductDTO();
        cp.setId(clientProductId);
        cp.setUpdateTime(current);
        cp.setBillPlan(billPlan);
        //        cp.setBalance(amount);
        cp.setLatestRechargeId(productRechargeId);
        cp.setIsOpened(TrueOrFalse.TRUE);
        openClientProductDTO.setClientProductDTO(cp);
        ResponseDTO responseDTO = clientRpcService.renewClientProduct(openClientProductDTO);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp)
    {
        OpenClientProductDTO openClientProductDTO = new OpenClientProductDTO();
        Date current = new Date();
        Long productRechargeId = IDUtils.getProductRechargeId(param.getNodeId());
        ProductRechargeDTO pr = new ProductRechargeDTO();
        pr.setId(productRechargeId);
        pr.setCreateTime(current);
        pr.setUpdateTime(current);
        pr.setClientProductId(clientProductId);
        //        pr.setClientId(cp.getClientId());
        //        pr.setProductId(cp.getProductId());
        pr.setTradeNo(redisDao.createTradeNo(Trade.PRODUCT_RECHARGE));
        pr.setContractNo(contractNo);
        pr.setBillPlan(billPlan);
        pr.setRechargeType(rechargeType);
        pr.setAmount(amount);
        //        pr.setBalance(amount.add(cp.getBalance()));
        pr.setUnitAmt(unitAmt);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        openClientProductDTO.setProductRechargeDTO(pr);
        ClientProductDTO cpUpd = new ClientProductDTO();
        cpUpd.setId(clientProductId);
        cpUpd.setUpdateTime(current);
        cpUpd.setBillPlan(billPlan);
        //        cpUpd.setBalance(amount.add(cp.getBalance()));
        cpUpd.setLatestRechargeId(productRechargeId);
        cpUpd.setIsOpened(TrueOrFalse.TRUE);
        openClientProductDTO.setClientProductDTO(cpUpd);
        ResponseDTO responseDTO = clientRpcService.renewClientProduct(openClientProductDTO);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void getClientOperateLog(Long clientId, Page page, RestListResp resp)
    {
        ListDTO<ClientOperateLogDTO> listDTO = clientRpcService.getClientOperateLog(clientId, page);
        resp.setTotal(listDTO.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(ClientOperateLogDTO info : listDTO.getList())
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
    public void addClient(NewClientVO vo, RestResp resp)
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
        List<ClientContactDTO> contactList = new ArrayList<>(vo.getContacts().size());
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
        ResponseDTO res = clientRpcService.addNewClient(dto);
        resp.setError(res.getResult());
    }

    @Override
    public void editClient(NewClientVO vo, RestResp resp)
    {
        if(vo.getClientId() == null || StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(
                vo.getShortName()) || StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null ||
                (!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return;
        }
        NewClientDTO client = new NewClientDTO();
        client.setClientId(vo.getClientId());
        client.setCorpName(vo.getCorpName());
        client.setShortName(vo.getShortName());
        client.setLicense(vo.getLicense());
        client.setIndustryId(vo.getIndustryId());
        client.setEnabled(vo.getEnabled());
        client.setManagerId(vo.getManagerId());
        List<ClientContactDTO> contactList = new ArrayList<>();
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
                ClientContactDTO o = new ClientContactDTO();
                o.setId(c.getId());
                o.setName(c.getName());
                o.setPosition(c.getPosition());
                o.setPhone(c.getPhone());
                o.setEmail(c.getEmail());
                o.setGeneral(c.getGeneral());
                contactList.add(o);
            }
        }
        ResponseDTO dto = clientRpcService.editClient(client, contactList, vo.getContactDel());
        resp.setError(dto.getResult());
    }

    @Override
    public void findClientDetail(Long clientId, RestResp resp)
    {
        ClientDetailDTO dto = clientRpcService.getClientDetail(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.setError(dto.getResult());
            return;
        }
        resp.addData(Field.CLIENT_ID, clientId + "");
        resp.addData(Field.CORP_NAME, dto.getCorpName());
        resp.addData(Field.SHORT_NAME, dto.getShortName());
        resp.addData(Field.INDUSTRY_NAME, redisDao.getIndustryInfo(dto.getIndustryId()));
        resp.addData(Field.LICENSE, dto.getLicense());
        resp.addData(Field.USERNAME, dto.getUsername());
        resp.addData(Field.USER_ENABLED, dto.getUserStatus());
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
        List<ProductClientDetailDTO> productDTOList = productRpcService.getProductInfoList(clientId);
        List<Map<String, Object>> opened = new ArrayList<>();
        List<Map<String, Object>> toOpen = new ArrayList<>();
        List<Map<String, Object>> custom = new ArrayList<>();
        for(ProductClientDetailDTO d : productDTOList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.PRODUCT_ID, d.getProductId() + "");
            m.put(Field.PRODUCT_NAME, d.getName());
            m.put(Field.CUSTOM, d.getCustom());
            m.put(Field.TYPE_NAME,
                    ProdType.getById(d.getProductType()) != null ? ProdType.getById(d.getProductType()).getName() :
                            null);
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
        resp.addData(Field.OPENED, opened);
        resp.addData(Field.TO_OPEN, toOpen);
        resp.addData(Field.PRODUCT_CUSTOM_LIST, custom);
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
        ClientUserDictDTO res = clientRpcService.getClientAccountDict(clientId);
        map.put(Field.CORP_NAME, res.getCorpName());
        map.put(Field.ACCOUNT_DICT, res.getUserDict());
        return map;
    }

    @Override
    public void getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate, Page page,
            RestListResp res)
    {
        ListDTO<AccessDTO> listDTO = clientRpcService.getClientRequestList(clientId, userId, productId, fromDate,
                toDate, page);
        res.setTotal(listDTO.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(AccessDTO o : listDTO.getList())
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
        ListDTO<RechargeDTO> listDTO = clientRpcService.getClientRechargeList(clientId, productId, fromDate, toDate,
                page);
        res.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(RechargeDTO o : listDTO.getList())
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
        ListDTO<AccessDTO> listDTO = clientRpcService.getClientRequestList(clientId, userId, productId, startTime,
                endTime, page);
        List<AccessDTO> list = listDTO.getList();
        if(!CollectionUtils.isEmpty(list))
        {
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(
                    wb.getCreationHelper().createDataFormat().getFormat(DateFormat.YYYY_MM_DD_HH_MM_SS));
            for(int i = 0; i < list.size(); i++)
            {
                AccessDTO dataInfo = list.get(i);
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

        ListDTO<RechargeDTO> listDTO = clientRpcService.getClientRechargeList(clientId, productId, startTime, endTime,
                page);
        List<RechargeDTO> list = listDTO.getList();
        if(!CollectionUtils.isEmpty(list))
        {
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(
                    wb.getCreationHelper().createDataFormat().getFormat(DateFormat.YYYY_MM_DD_HH_MM_SS));
            for(int i = 0; i < list.size(); i++)
            {
                RechargeDTO pri = list.get(i);
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