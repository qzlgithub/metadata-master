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
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.OpenClientProductDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductOpenDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UpdateClientUserStatusDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.service.RemoteSystemService;
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
    private RemoteClientService remoteClientService;
    @Resource
    private RemoteProductService remoteProductService;
    @Resource
    private RemoteSystemService remoteSystemService;

    @Override
    public void checkIfUsernameExist(String username, BLResp resp)
    {
        UserDTO user = remoteClientService.findByUsername(username);
        resp.addData(Field.EXIST, user == null ? 0 : 1);
    }

    @Override
    public void getSimilarCorp(String name, Long clientId, BLResp resp)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ClientInfoListDTO similarCorpByName = remoteClientService.getSimilarCorpByName(name, clientId);
        List<ClientInfoDTO> dataList = similarCorpByName.getDataList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(ClientInfoDTO client : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.CORP_NAME, client.getCorpName());
                map.put(Field.LICENSE, client.getLicense());
                map.put(Field.REGISTER_DATE, DateUtils.format(client.getRegisterTime(), DateFormat.YYYY_MM_DD));
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        resp.addData(Field.TOTAL, dataList.size());
    }

    @Override
    public void getClientList(String keyword, Long parentIndustryId, Long industryId, Integer enabled, Page page,
            ListRes res)
    {
        ListDTO<ClientInfoDTO> clientInfoListDTO = remoteClientService.getClientInfoListBy(keyword,
                industryId == null ? parentIndustryId : industryId, enabled, page);
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
    public void getClientInfoForEdit(Long clientId, BLResp resp)
    {
        ClientDetailDTO dto = remoteClientService.getClientInfoForEdit(clientId);
        if(RestResult.SUCCESS != dto.getResult())
        {
            resp.result(dto.getResult());
            return;
        }
        resp.addData(Field.CLIENT_ID, clientId + "");
        resp.addData(Field.USERNAME, dto.getUsername());
        resp.addData(Field.CORP_NAME, dto.getCorpName());
        resp.addData(Field.SHORT_NAME, dto.getShortName());
        resp.addData(Field.LICENSE, dto.getLicense());
        resp.addData(Field.INDUSTRY_ID, dto.getIndustryId() + "");
        resp.addData(Field.USER_STATUS, dto.getUserStatus());

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

        IndustryDTO industry = remoteSystemService.getIndustryDictOfTarget(dto.getIndustryId());
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
    public void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, Long managerId, BLResp resp)
    {
        UpdateClientUserStatusDTO dto = new UpdateClientUserStatusDTO();
        dto.setClientIdList(clientIdList);
        dto.setEnabled(enabled);
        dto.setManagerId(managerId);
        dto.setReason(reason);
        ResultDTO resultDTO = remoteClientService.updateClientUserStatus(dto);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void deleteClient(List<Long> idList)
    {
        ResultDTO dto = remoteClientService.setClientDeleted(idList);
        logger.info("set client[{}] deleted: ", idList, dto.getCode());
    }

    @Override
    public void resetPassword(List<Long> idList, BLResp resp)
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
            ResultDTO dto = remoteClientService.resetPasswordByIds(Constant.DEFAULT_ENC_PWD, clientUserIdList);
            logger.info("set client[{}] deleted: ", idList, dto.getCode());
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
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
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
        ResultDTO res = remoteClientService.openProduct(dto);
        resp.result(res.getResult());
    }

    @Override
    public void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan,
            Integer rechargeType, BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
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
        ResultDTO res = remoteClientService.openProduct(dto);
        resp.result(res.getResult());
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
        if(BillPlan.BY_TIME.getId().equals(pr.getBillPlan()))
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
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp)
    {
        OpenClientProductDTO openClientProductDTO = new OpenClientProductDTO();
        openClientProductDTO.setYear(true);
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
        pr.setBalance(new BigDecimal(0));
        pr.setStartDate(startDate);
        pr.setEndDate(endDate);
        pr.setRemark(remark);
        pr.setManagerId(RequestThread.getOperatorId());
        openClientProductDTO.setProductRechargeDTO(pr);
        ClientProductDTO cp = new ClientProductDTO();
        cp.setId(clientProductId);
        cp.setUpdateTime(current);
        cp.setBillPlan(billPlan);
        cp.setBalance(new BigDecimal(0));
        cp.setLatestRechargeId(productRechargeId);
        cp.setIsOpened(TrueOrFalse.TRUE);
        openClientProductDTO.setClientProductDTO(cp);
        ResultDTO resultDTO = remoteClientService.renewClientProduct(openClientProductDTO);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp)
    {
        OpenClientProductDTO openClientProductDTO = new OpenClientProductDTO();
        openClientProductDTO.setYear(false);
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
        ResultDTO resultDTO = remoteClientService.renewClientProduct(openClientProductDTO);
        resp.result(resultDTO.getResult());
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
        if(!CollectionUtils.isEmpty(dataList))
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
                StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null || (!TrueOrFalse.TRUE.equals(
                vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())) || CollectionUtils.isEmpty(
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
    public void editClient(NewClientVO vo, BLResp resp)
    {
        if(vo.getClientId() == null || StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(
                vo.getShortName()) || StringUtils.isNullBlank(vo.getLicense()) || vo.getIndustryId() == null ||
                (!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())))
        {
            resp.result(RestResult.KEY_FIELD_MISSING);
            return;
        }
        NewClientDTO client = new NewClientDTO();
        client.setClientId(vo.getClientId());
        client.setCorpName(vo.getCorpName());
        client.setShortName(vo.getShortName());
        client.setLicense(vo.getLicense());
        client.setIndustryId(vo.getIndustryId());
        client.setEnabled(vo.getEnabled());
        List<ClientContactDTO> contactList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(vo.getContacts()))
        {
            for(ContactVO c : vo.getContacts())
            {
                if(StringUtils.isNullBlank(c.getName()) || StringUtils.isNullBlank(c.getPosition()) ||
                        StringUtils.isNullBlank(c.getPhone()) || (!TrueOrFalse.TRUE.equals(c.getGeneral()) &&
                        !TrueOrFalse.FALSE.equals(c.getGeneral())))
                {
                    resp.result(RestResult.KEY_FIELD_MISSING);
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
        ResultDTO dto = remoteClientService.editClient(client, contactList, vo.getContactDel());
        resp.result(dto.getResult());
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
        List<Map<String, Object>> custom = new ArrayList<>();
        for(ProductClientDetailDTO d : productDTOList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.PRODUCT_ID, d.getProductId() + "");
            m.put(Field.PRODUCT_NAME, d.getName());
            m.put(Field.CUSTOM, d.getCustom());
            m.put(Field.TYPE_NAME, ProdType.getById(d.getProductType()).getName());
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
    public void selectCustomProduct(Long clientId, List<Long> productIds, BLResp resp)
    {
        ResultDTO resultDTO = remoteClientService.selectCustomProduct(clientId, productIds);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void removeCustomClientProduct(Long clientProductId, BLResp resp)
    {
        ResultDTO resultDTO = remoteClientService.removeCustomClientProduct(clientProductId);
        resp.result(resultDTO.getResult());
    }
}