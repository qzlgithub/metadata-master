package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRequestDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.ProductService;
import org.apache.commons.collections4.CollectionUtils;
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
public class ProductServiceImpl implements ProductService
{

    @Resource
    RemoteProductService remoteProductService;

    @Override
    public void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp)
    {
        List<ProductRechargeDTO> dataList = remoteProductService.getProductRechargeRecord(clientId, productId, fromDate, endDate, page,
                resp);
        if(CollectionUtils.isNotEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(dataList.size());
            for(ProductRechargeDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, item.getId());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(item.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                map.put(Field.CONTRACT_NO, item.getContractNo());
                map.put(Field.CORP_NAME, item.getCorpName());
                map.put(Field.PRODUCT_NAME, item.getProductName());
                map.put(Field.RECHARGE_TYPE, item.getRechargeType());
                map.put(Field.REMARK, item.getRemark());
                map.put(Field.SHORT_NAME, item.getShortName());
                map.put(Field.TRADE_NO, item.getTradeNo());
                map.put(Field.TRADE_TIME, DateUtils.format(item.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
    }

    @Override
    public XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date endDate)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("充值记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("充值时间");
        row.createCell(1).setCellValue("充值单号");
        row.createCell(2).setCellValue("产品服务");
        row.createCell(3).setCellValue("充值类型");
        row.createCell(4).setCellValue("充值金额");
        row.createCell(5).setCellValue("产品服务余额");
        row.createCell(6).setCellValue("合同编号");
        Page page = new Page(1, 1000);
        List<ProductRechargeDTO> dataList = remoteProductService.getProductRechargeRecord(clientId, productId, fromDate, endDate, page,
                BLResp.build());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            ProductRechargeDTO dataDTO;
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
            for(int i = 0; i < dataList.size(); i++)
            {
                dataDTO = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataDTO.getTradeTime());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataDTO.getTradeNo());
                dataRow.createCell(2).setCellValue(dataDTO.getProductName());
                dataRow.createCell(3).setCellValue(dataDTO.getRechargeType());
                dataRow.createCell(4).setCellValue(NumberUtils.formatAmount(dataDTO.getAmount()));
                dataRow.createCell(5).setCellValue(NumberUtils.formatAmount(dataDTO.getBalance()));
                dataRow.createCell(6).setCellValue(dataDTO.getContractNo());
            }
        }
        return wb;
    }

    @Override
    public void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp)
    {
        List<ProductRequestDTO> dataList = remoteProductService.getProductRequestRecord(clientId, productId, fromDate, endDate, page, resp);
        if(CollectionUtils.isNotEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(dataList.size());
            for(ProductRequestDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, item.getId());
                map.put(Field.CALL_TIME, DateUtils.format(item.getCallTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.CORP_NAME, item.getCorpName());
                map.put(Field.USERNAME, item.getUsername());
                map.put(Field.PRODUCT_NAME, item.getProductName());
                map.put(Field.SHORT_NAME, item.getShortName());
                map.put(Field.SUC, item.getSuc() + "");
                map.put(Field.UNIT_AMT, NumberUtils.formatAmount(item.getUnitAmt()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
    }

    @Override
    public XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date endDate)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("接口调用记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("调用时间");
        row.createCell(1).setCellValue("产品服务");
        row.createCell(2).setCellValue("2");
        row.createCell(3).setCellValue("3");
        row.createCell(4).setCellValue("4");
        Page page = new Page(1, 1000);
        List<ProductRequestDTO> dataList = remoteProductService.getProductRequestRecord(clientId, productId, fromDate, endDate, page,
                BLResp.build());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            ProductRequestDTO dataDTO;
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
            for(int i = 0; i < dataList.size(); i++)
            {
                dataDTO = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataDTO.getCallTime());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataDTO.getProductName());
                dataRow.createCell(2).setCellValue(dataDTO.getSuc()+"");
                dataRow.createCell(3).setCellValue(NumberUtils.formatAmount(dataDTO.getUnitAmt()));
                dataRow.createCell(4).setCellValue(NumberUtils.formatAmount(dataDTO.getBalance()));
            }
        }
        return wb;
    }
}
