package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.RequestDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService
{
    @Resource
    private RemoteProductService remoteProductService;
    @Resource
    private RemoteClientService remoteClientService;

    /**
     * 产品充值列表
     */
    @Override
    public void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page,
            ListRes res)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeInfoList(
                clientId, productId, startTime, endTime, page);
        res.setTotal(productRechargeInfoListDTO.getTotal());
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(ProductRechargeInfoDTO pri : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(pri.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, pri.getTradeNo());
                map.put(Field.CORP_NAME, pri.getCorpName());
                map.put(Field.SHORT_NAME, pri.getShortName());
                map.put(Field.USERNAME, pri.getUsername());
                map.put(Field.PRODUCT_NAME, pri.getProductName() == null ? "" : pri.getProductName());
                map.put(Field.RECHARGE_TYPE, pri.getRechargeType());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(pri.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(pri.getBalance()));
                map.put(Field.MANAGER_NAME, pri.getManagerName());
                map.put(Field.CONTRACT_NO, pri.getContractNo());
                map.put(Field.REMARK, pri.getRemark());
                list.add(map);
            }
        }
        res.setList(list);
    }

    @Override
    public void getProductRechargeInfoList(String keyword, Long productId, Long managerId, Long rechargeType,
            Date fromDate, Date toDate, Page page, ListRes res)
    {
        ListDTO<ProductRechargeInfoDTO> listDTO = remoteProductService.getRechargeInfoList(keyword, productId,
                managerId, rechargeType, fromDate, toDate, page);
        res.setTotal(listDTO.getTotal());
        res.addExtra(Field.TOTAL_AMT, listDTO.getExtradata().get(Field.TOTAL_AMT));
        if(listDTO.getList() != null)
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(ProductRechargeInfoDTO o : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.RECHARGE_AT, DateUtils.format(o.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, o.getTradeNo());
                map.put(Field.CORP_NAME, o.getCorpName());
                map.put(Field.SHORT_NAME, o.getShortName());
                map.put(Field.USERNAME, o.getUsername());
                map.put(Field.PRODUCT, o.getProductName());
                map.put(Field.RECHARGE_TYPE, o.getRechargeType());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(o.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                map.put(Field.MANAGER, o.getManagerName());
                map.put(Field.CONTRACT_NO, o.getContractNo());
                map.put(Field.REMARK, o.getRemark());
                list.add(map);
            }
            res.setList(list);
        }
    }

    @Override
    public XSSFWorkbook createProductRechargeInfoListXlsx(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("充值数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("充值单号");
        row.createCell(2).setCellValue("公司名称");
        row.createCell(3).setCellValue("公司简称");
        row.createCell(4).setCellValue("账号");
        row.createCell(5).setCellValue("产品服务");
        row.createCell(6).setCellValue("充值类型");
        row.createCell(7).setCellValue("充值金额");
        row.createCell(8).setCellValue("产品服务余额");
        row.createCell(9).setCellValue("客户归属");
        row.createCell(10).setCellValue("合同编号");
        row.createCell(11).setCellValue("备注");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        ListDTO<ProductRechargeInfoDTO> listDTO = remoteProductService.getRechargeInfoList(keyword, productId,
                managerId, rechargeType, fromDate, toDate, page);
        List<ProductRechargeInfoDTO> dataList = listDTO.getList();
        ProductRechargeInfoDTO dataInfo;
        for(int i = 0; i < dataList.size(); i++)
        {
            dataInfo = dataList.get(i);
            dataRow = sheet.createRow(i + 1);
            cell = dataRow.createCell(0);
            cell.setCellValue(dataInfo.getTradeTime());
            cell.setCellStyle(timeStyle);
            dataRow.createCell(1).setCellValue(dataInfo.getTradeNo());
            dataRow.createCell(2).setCellValue(dataInfo.getCorpName());
            dataRow.createCell(3).setCellValue(dataInfo.getShortName());
            dataRow.createCell(4).setCellValue(dataInfo.getUsername());
            dataRow.createCell(5).setCellValue(dataInfo.getProductName() == null ? "" : dataInfo.getProductName());
            dataRow.createCell(6).setCellValue(dataInfo.getRechargeType());
            dataRow.createCell(7).setCellValue(NumberUtils.formatAmount(dataInfo.getAmount()));
            dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
            dataRow.createCell(9).setCellValue(dataInfo.getManagerName());
            dataRow.createCell(10).setCellValue(dataInfo.getContractNo());
            dataRow.createCell(11).setCellValue(dataInfo.getRemark());
        }
        return wb;
    }

    @Override
    public void getClientBillList(String shortName, Long typeId, Long clientId, Long userId, Long productId,
            Date startDate, Date endDate, Page page, ListRes res)
    {
        shortName = StringUtils.isNullBlank(shortName) ? null : shortName;
        ApiReqInfoListDTO apiReqInfoListDTO = remoteClientService.getClientBillListBy(shortName, typeId, clientId,
                userId, productId, startDate, endDate, page);
        res.setTotal(apiReqInfoListDTO.getTotal());
        List<ApiReqInfoDTO> dataList = apiReqInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(!CollectionUtils.isEmpty(dataList))
        {
            for(ApiReqInfoDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(item.getCreateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, item.getId() + "");
                map.put(Field.CORP_NAME, item.getCorpName());
                map.put(Field.SHORT_NAME, item.getShortName());
                map.put(Field.USERNAME, item.getUsername());
                map.put(Field.PRODUCT_NAME, item.getProductName() == null ? "" : item.getProductName());
                map.put(Field.BILL_PLAN, BillPlan.getById(item.getBillPlan()).getName());
                map.put(Field.HIT, TrueOrFalse.TRUE.equals(item.getHit()) ? "是" : "否");
                map.put(Field.UNIT_AMT, NumberUtils.formatAmount(item.getFee()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                list.add(map);
            }
        }
        res.setList(list);
    }

    @Override
    public XSSFWorkbook createClientBillListXlsx(String shortName, Long typeId, Long clientId, Long userId,
            Long productId, Date startDate, Date endDate, Page page)
    {
        shortName = StringUtils.isNullBlank(shortName) ? null : shortName;
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("消费数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("消费单号");
        row.createCell(2).setCellValue("公司名称");
        row.createCell(3).setCellValue("公司简称");
        row.createCell(4).setCellValue("账号");
        row.createCell(5).setCellValue("产品服务");
        row.createCell(6).setCellValue("计费方式");
        row.createCell(7).setCellValue("是否击中");
        row.createCell(8).setCellValue("消费(元)");
        row.createCell(9).setCellValue("余额(元)");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        ApiReqInfoListDTO apiReqInfoListDTO = remoteClientService.getClientBillListBy(shortName, typeId, clientId,
                userId, productId, startDate, endDate, page);
        List<ApiReqInfoDTO> dataList = apiReqInfoListDTO.getDataList();
        ApiReqInfoDTO dataInfo;
        for(int i = 0; i < dataList.size(); i++)
        {
            dataInfo = dataList.get(i);
            dataRow = sheet.createRow(i + 1);
            cell = dataRow.createCell(0);
            cell.setCellValue(dataInfo.getCreateTime());
            cell.setCellStyle(timeStyle);
            dataRow.createCell(1).setCellValue(dataInfo.getId() + "");
            dataRow.createCell(2).setCellValue(dataInfo.getCorpName());
            dataRow.createCell(3).setCellValue(dataInfo.getShortName());
            dataRow.createCell(4).setCellValue(dataInfo.getUsername());
            dataRow.createCell(5).setCellValue(dataInfo.getProductName() == null ? "" : dataInfo.getProductName());
            dataRow.createCell(6).setCellValue(BillPlan.getById(dataInfo.getBillPlan()).getName());
            dataRow.createCell(7).setCellValue(TrueOrFalse.TRUE.equals(dataInfo.getHit()) ? "是" : "否");
            dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(dataInfo.getFee()));
            dataRow.createCell(9).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
        }
        return wb;
    }

    @Override
    public void getClientBillList(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate,
            Page page, ListRes res)
    {
        ListDTO<ApiReqInfoDTO> listDTO = remoteClientService.getClientBillListBy(keyword, productId, billPlan, fromDate,
                toDate, page);
        res.setTotal(listDTO.getTotal());
        res.addExtra(Field.TOTAL_FEE, listDTO.getExtradata().get(Field.TOTAL_FEE));
        if(listDTO.getList() != null)
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(ApiReqInfoDTO o : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.REQUEST_AT, DateUtils.format(o.getCreateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, o.getRequestNo());
                map.put(Field.CORP_NAME, o.getCorpName());
                map.put(Field.SHORT_NAME, o.getShortName());
                map.put(Field.USERNAME, o.getUsername());
                map.put(Field.PRODUCT, o.getProductName());
                map.put(Field.BILL_PLAN, BillPlan.getNameById(o.getBillPlan()));
                if(BillPlan.BY_TIME.getId().equals(o.getBillPlan()))
                {
                    map.put(Field.FEE, "/");
                    map.put(Field.BALANCE, "/");
                }
                else
                {
                    map.put(Field.FEE, NumberUtils.formatAmount(o.getFee()));
                    map.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                }
                map.put(Field.IS_HIT, o.getHit());
                list.add(map);
            }
            res.setList(list);
        }
    }

    @Override
    public XSSFWorkbook createClientBillListXlsx(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("消费数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("请求时间");
        row.createCell(1).setCellValue("请求单号");
        row.createCell(2).setCellValue("公司名称");
        row.createCell(3).setCellValue("公司简称");
        row.createCell(4).setCellValue("账号");
        row.createCell(5).setCellValue("产品服务");
        row.createCell(6).setCellValue("计费方式");
        row.createCell(7).setCellValue("是否击中");
        row.createCell(8).setCellValue("消费(元)");
        row.createCell(9).setCellValue("余额(元)");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        ListDTO<ApiReqInfoDTO> apiReqInfoListDTO = remoteClientService.getClientBillListBy(keyword, productId, billPlan,
                fromDate, toDate, page);
        List<ApiReqInfoDTO> dataList = apiReqInfoListDTO.getList();
        ApiReqInfoDTO dataInfo;
        for(int i = 0; i < dataList.size(); i++)
        {
            dataInfo = dataList.get(i);
            dataRow = sheet.createRow(i + 1);
            cell = dataRow.createCell(0);
            cell.setCellValue(dataInfo.getCreateTime());
            cell.setCellStyle(timeStyle);
            dataRow.createCell(1).setCellValue(dataInfo.getRequestNo());
            dataRow.createCell(2).setCellValue(dataInfo.getCorpName());
            dataRow.createCell(3).setCellValue(dataInfo.getShortName());
            dataRow.createCell(4).setCellValue(dataInfo.getUsername());
            dataRow.createCell(5).setCellValue(dataInfo.getProductName());
            dataRow.createCell(6).setCellValue(BillPlan.getById(dataInfo.getBillPlan()).getName());
            dataRow.createCell(7).setCellValue(TrueOrFalse.TRUE.equals(dataInfo.getHit()) ? "击中" : "未击中");
            if(BillPlan.BY_TIME.getId().equals(dataInfo.getBillPlan()))
            {
                dataRow.createCell(8).setCellValue("/");
                dataRow.createCell(9).setCellValue("/");
            }
            else
            {
                dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(dataInfo.getFee()));
                dataRow.createCell(9).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
            }
        }
        return wb;
    }

    @Override
    public XSSFWorkbook createClientBillListXlsx(Long clientId, Long userId, Long productId, Date fromDate, Date toDate,
            Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("消费数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("请求时间");
        row.createCell(1).setCellValue("请求单号");
        row.createCell(4).setCellValue("客户账号");
        row.createCell(5).setCellValue("产品服务");
        row.createCell(6).setCellValue("计费方式");
        row.createCell(7).setCellValue("是否击中");
        row.createCell(8).setCellValue("费用(元)");
        row.createCell(9).setCellValue("余额(元)");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(DateFormat.YYYY_MM_DD_HH_MM_SS));
        ListDTO<RequestDTO> listDTO = remoteClientService.getClientRequestList(clientId, userId, productId, fromDate,
                toDate, page);
        List<RequestDTO> list = listDTO.getList();
        RequestDTO dataInfo;
        for(int i = 0; i < list.size(); i++)
        {
            dataInfo = list.get(i);
            dataRow = sheet.createRow(i + 1);
            cell = dataRow.createCell(0);
            cell.setCellValue(dataInfo.getRequestAt());
            cell.setCellStyle(timeStyle);
            dataRow.createCell(1).setCellValue(dataInfo.getRequestNo());
            dataRow.createCell(4).setCellValue(dataInfo.getUsername());
            dataRow.createCell(5).setCellValue(dataInfo.getProductName());
            dataRow.createCell(6).setCellValue(BillPlan.getNameById(dataInfo.getBillPlan()));
            dataRow.createCell(7).setCellValue(TrueOrFalse.TRUE.equals(dataInfo.getHit()) ? "击中" : "未击中");
            if(BillPlan.BY_TIME.getId().equals(dataInfo.getBillPlan()))
            {
                dataRow.createCell(8).setCellValue("-");
                dataRow.createCell(9).setCellValue("-");
            }
            else
            {
                dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(dataInfo.getFee()));
                dataRow.createCell(9).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
            }
        }
        return wb;
    }
}
