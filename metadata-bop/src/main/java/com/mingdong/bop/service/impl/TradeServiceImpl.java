package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.ProductRpcService;
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
    private ProductRpcService productRpcService;
    @Resource
    private ClientRpcService clientRpcService;

    @Override
    public void getProductRechargeInfoList(String keyword, Long productId, Long managerId, Long rechargeType,
            Date fromDate, Date toDate, Page page, RestListResp res)
    {
        ListDTO<ProductRechargeInfoDTO> listDTO = productRpcService.getRechargeInfoList(keyword, productId,
                managerId, rechargeType, fromDate, toDate, page);
        res.setTotal(listDTO.getTotal());
        res.addData(Field.TOTAL_AMT, listDTO.getExtradata().get(Field.TOTAL_AMT));
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
        row.createCell(9).setCellValue("经手人");
        row.createCell(10).setCellValue("合同编号");
        row.createCell(11).setCellValue("备注");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        ListDTO<ProductRechargeInfoDTO> listDTO = productRpcService.getRechargeInfoList(keyword, productId,
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
    public void getClientBillList(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate,
            Long managerId, Page page, RestListResp res)
    {
        ListDTO<ApiReqInfoDTO> listDTO = clientRpcService.getClientBillListBy(keyword, productId, billPlan, fromDate,
                toDate, managerId, page);
        res.setTotal(listDTO.getTotal());
        res.addData(Field.TOTAL_FEE, listDTO.getExtradata().get(Field.TOTAL_FEE));
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
            Date toDate, Long manager, Page page)
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
        ListDTO<ApiReqInfoDTO> apiReqInfoListDTO = clientRpcService.getClientBillListBy(keyword, productId, billPlan,
                fromDate, toDate, manager, page);
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
}
