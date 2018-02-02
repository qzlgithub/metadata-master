package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class TradeServiceImpl implements TradeService
{
    @Resource
    private RemoteProductService remoteProductService;
    @Resource
    private RemoteClientService remoteClientService;

//    @Override
//    public BLResp testList2(Long productId, Long clientId, Date time, Page page)
//    {
//        BLResp resp = BLResp.build();
//        int total = 10;
//        int pages = page.getTotalPage(total);
//        resp.addData(Field.TOTAL, total);
//        resp.addData(Field.PAGES, pages);
//        resp.addData(Field.PAGE_NUM, page.getPageNum());
//        resp.addData(Field.PAGE_SIZE, page.getPageSize());
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++)
//        {
//            Map<String, Object> map = new HashMap<>();
//            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
//            map.put("tradeNo", "123456");
//            map.put("clientName", "荣耀科技");
//            map.put("shortName", "荣科");
//            map.put("username", "王科");
//            map.put("productName", "白名单");
//            map.put("pillBlan", "包年");
//            map.put("enabled", "0");
//            map.put("unitAmt", "10.00");
//            map.put("amount", "150000.00");
//            list.add(map);
//        }
//
//        resp.addData(Field.LIST, list);
//        return resp;
//    }
//
//    /**
//     * 测试数据
//     */
//    @Override
//    public BLResp testList3(Long clientId, Date time, Page page)
//    {
//        BLResp resp = BLResp.build();
//        int total = 10;
//        int pages = page.getTotalPage(total);
//        resp.addData(Field.TOTAL, total);
//        resp.addData(Field.PAGES, pages);
//        resp.addData(Field.PAGE_NUM, page.getPageNum());
//        resp.addData(Field.PAGE_SIZE, page.getPageSize());
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++)
//        {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
//            map.put("tradeNo", "123456");
//            map.put("clientName", "辉煌科技");
//            map.put("shortName", "辉科");
//            map.put("username", "李辉");
//            map.put("amount", "10.50");
//            map.put("rechargeType", "代充");
//            map.put("balance", "100000.00");
//            map.put("manager", "商务经理");
//            map.put("remark", "荣耀科技公司的充值记录");
//            list.add(map);
//        }
//
//        resp.addData(Field.LIST, list);
//        return resp;
//    }
//
//    @Override
//    public BLResp testList4(Long clientId, Date time, Page page)
//    {
//        BLResp resp = BLResp.build();
//        int total = 10;
//        int pages = page.getTotalPage(total);
//        resp.addData(Field.TOTAL, total);
//        resp.addData(Field.PAGES, pages);
//        resp.addData(Field.PAGE_NUM, page.getPageNum());
//        resp.addData(Field.PAGE_SIZE, page.getPageSize());
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for(int i = 0; i < 10; i++)
//        {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
//            map.put("tradeNo", "123456");
//            map.put("clientName", "辉煌科技");
//            map.put("shortName", "辉科");
//            map.put("username", "李辉");
//            map.put("purpose", "续费");
//            map.put("productName", "白名单");
//            map.put("consume", "2000.00");
//            map.put("balance", "100000.00");
//            list.add(map);
//        }
//
//        resp.addData(Field.LIST, list);
//        return resp;
//    }

    /**
     * 产品充值列表
     */
    @Override
    public void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page,
            BLResp resp)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeInfoList(
                clientId, productId, startTime, endTime, page);
        resp.addData(Field.TOTAL, productRechargeInfoListDTO.getTotal());
        resp.addData(Field.PAGES, productRechargeInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            for(ProductRechargeInfoDTO pri : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(pri.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, pri.getTradeNo());
                map.put(Field.CORP_NAME, pri.getCorpName());
                map.put(Field.SHORT_NAME, pri.getShortName());
                map.put(Field.USERNAME, pri.getUsername());
                map.put(Field.PRODUCT_NAME, pri.getProductName() == null?"":pri.getProductName());
                map.put(Field.RECHARGE_TYPE, pri.getRechargeType());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(pri.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(pri.getBalance()));
                map.put(Field.MANAGER_NAME, pri.getManagerName());
                map.put(Field.CONTRACT_NO, pri.getContractNo());
                map.put(Field.REMARK, pri.getRemark());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public void getProductRechargeInfoList(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate, Page page, BLResp resp)
    {
        if(StringUtils.isNotBlank(shortName))
        {
            BigDecimal allRecharge = remoteProductService.getProductRechargeInfoSumBy(shortName, typeId, productId,
                    managerId, startDate, endDate);
            resp.addData(Field.SHOW_STATS, "共充值了 " + (allRecharge == null? 0 : NumberUtils.formatAmount(allRecharge)) + " 元");
        }
        else
        {
            shortName = null;
        }
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeInfoListBy(
                shortName, typeId, productId, managerId, startDate, endDate, page);
        resp.addData(Field.TOTAL, productRechargeInfoListDTO.getTotal());
        resp.addData(Field.PAGES, productRechargeInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            for(ProductRechargeInfoDTO pri : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(pri.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, pri.getTradeNo());
                map.put(Field.CORP_NAME, pri.getCorpName());
                map.put(Field.SHORT_NAME, pri.getShortName());
                map.put(Field.USERNAME, pri.getUsername());
                map.put(Field.PRODUCT_NAME, pri.getProductName() == null?"":pri.getProductName());
                map.put(Field.RECHARGE_TYPE, pri.getRechargeType());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(pri.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(pri.getBalance()));
                map.put(Field.MANAGER_NAME, pri.getManagerName());
                map.put(Field.REMARK, pri.getRemark());
                map.put(Field.CONTRACT_NO, pri.getContractNo());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public XSSFWorkbook createProductRechargeInfoListXlsx(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate, Page page)
    {
        shortName = StringUtils.isNotBlank(shortName) ? shortName : null;
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
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeInfoListBy(
                shortName, typeId, productId, managerId, startDate, endDate, page);
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
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
            dataRow.createCell(5).setCellValue(dataInfo.getProductName() == null?"":dataInfo.getProductName());
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
    public void getClientBillList(String shortName, Long typeId, Long productId, Date startDate, Date endDate,
            Page page, BLResp resp)
    {
        if(StringUtils.isNotBlank(shortName))
        {
            BigDecimal billFeeSum = remoteClientService.getClientBillFeeSum(shortName, typeId, productId, startDate,
                    endDate);
            resp.addData(Field.SHOW_STATS, "计次消耗 " + (billFeeSum == null? 0 : NumberUtils.formatAmount(billFeeSum)) + " 元");
        }
        shortName = StringUtils.isNotBlank(shortName) ? shortName : null;
        ApiReqInfoListDTO apiReqInfoListDTO = remoteClientService.getClientBillListBy(shortName, typeId, productId,
                startDate, endDate, page);
        resp.addData(Field.TOTAL, apiReqInfoListDTO.getTotal());
        resp.addData(Field.PAGES, apiReqInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ApiReqInfoDTO> dataList = apiReqInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            for(ApiReqInfoDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(item.getCreateTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, item.getId() + "");
                map.put(Field.CORP_NAME, item.getCorpName());
                map.put(Field.SHORT_NAME, item.getShortName());
                map.put(Field.USERNAME, item.getUsername());
                map.put(Field.PRODUCT_NAME, item.getProductName() == null?"":item.getProductName());
                map.put(Field.BILL_PLAN, BillPlan.getById(item.getBillPlan()).getName());
                map.put(Field.HIT, TrueOrFalse.TRUE.equals(item.getHit())?"是":"否");
                map.put(Field.UNIT_AMT, NumberUtils.formatAmount(item.getFee()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public XSSFWorkbook createClientBillListXlsx(String shortName, Long typeId, Long productId, Date startDate,
            Date endDate, Page page)
    {
        shortName = StringUtils.isNotBlank(shortName) ? shortName : null;
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
        row.createCell(7).setCellValue("是否成功");
        row.createCell(8).setCellValue("消费(元)");
        row.createCell(9).setCellValue("余额(元)");
        Row dataRow;
        Cell cell;
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        ApiReqInfoListDTO apiReqInfoListDTO = remoteClientService.getClientBillListBy(shortName, typeId, productId,
                startDate, endDate, page);
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
            dataRow.createCell(5).setCellValue(dataInfo.getProductName() == null?"":dataInfo.getProductName());
            dataRow.createCell(6).setCellValue(BillPlan.getById(dataInfo.getBillPlan()).getName());
            dataRow.createCell(7).setCellValue(TrueOrFalse.TRUE.equals(dataInfo.getHit())?"是":"否");
            dataRow.createCell(8).setCellValue(NumberUtils.formatAmount(dataInfo.getFee()));
            dataRow.createCell(9).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
        }
        return wb;
    }

}
