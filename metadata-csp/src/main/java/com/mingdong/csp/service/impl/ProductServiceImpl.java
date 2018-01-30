package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.DictProductTypeListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ProductReqInfoListDTO;
import com.mingdong.core.model.dto.ProductRequestInfoDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService
{
    @Resource
    private RemoteProductService productApi;

    @Override
    public void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page,
            BLResp resp)
    {
        ProductRechargeInfoListDTO productRecListDTO = productApi.getProductRechargeRecord(clientId, productId,
                fromDate, toDate, page);
        if(productRecListDTO.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(productRecListDTO.getResultDTO().getResult());
            return;
        }
        List<ProductRechargeInfoDTO> dataList = productRecListDTO.getDataList();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfoDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                // map.put(Field.ID, item.getId());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(item.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                map.put(Field.CONTRACT_NO, item.getContractNo());
                // map.put(Field.CORP_NAME, item.getCorpName());
                map.put(Field.PRODUCT_NAME, item.getProductName());
                map.put(Field.BILL_PLAN, item.getBillPlan()); // TODO
                map.put(Field.RECHARGE_TYPE, item.getRechargeType());
                // map.put(Field.REMARK, item.getRemark());
                // map.put(Field.SHORT_NAME, item.getShortName());
                map.put(Field.TRADE_NO, item.getTradeNo());
                map.put(Field.TRADE_TIME, DateUtils.format(item.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        resp.addData(Field.TOTAL, productRecListDTO.getTotal());
        resp.addData(Field.PAGES, productRecListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
    }

    @Override
    public XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date toDate)
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
        ProductRechargeInfoListDTO productRecListDTO = productApi.getProductRechargeRecord(clientId, productId,
                fromDate, toDate, page);
        List<ProductRechargeInfoDTO> dataList = productRecListDTO.getDataList();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            ProductRechargeInfoDTO dataDTO;
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
    public void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page,
            BLResp resp)
    {
        ProductReqInfoListDTO productReqListDTO = productApi.getProductRequestRecord(clientId, productId, fromDate,
                toDate, page);
        if(productReqListDTO.getResultDTO().getResult() != RestResult.SUCCESS)
        {
            resp.result(productReqListDTO.getResultDTO().getResult());
            return;
        }
        List<ProductRequestInfoDTO> dataList = productReqListDTO.getProductRequestDTOList();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ProductRequestInfoDTO item : dataList)
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
        resp.addData(Field.CODE, productReqListDTO.getResultDTO().getCode());
        resp.addData(Field.TOTAL, productReqListDTO.getTotal());
        resp.addData(Field.PAGES, productReqListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
    }

    @Override
    public XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date toDate)
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
        ProductReqInfoListDTO productReqListDTO = productApi.getProductRequestRecord(clientId, productId, fromDate,
                toDate, page);
        List<ProductRequestInfoDTO> dataList = productReqListDTO.getProductRequestDTOList();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            ProductRequestInfoDTO dataDTO;
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
                dataRow.createCell(2).setCellValue(dataDTO.getSuc() + "");
                dataRow.createCell(3).setCellValue(NumberUtils.formatAmount(dataDTO.getUnitAmt()));
                dataRow.createCell(4).setCellValue(NumberUtils.formatAmount(dataDTO.getBalance()));
            }
        }
        return wb;
    }

    @Override
    public void getClientProductDetail(Long clientId, Long productId, BLResp resp)
    {
        ProductDTO dto = productApi.getClientProductDetail(clientId, productId);
        if(dto.getResult() != RestResult.SUCCESS)
        {
            resp.result(dto.getResult());
            return;
        }
        resp.addData(Field.PRODUCT_ID, dto.getId() + "");
        resp.addData(Field.PRODUCT_NAME, dto.getName());
        resp.addData(Field.BILL_PLAN, dto.getBillPlan());
        if(BillPlan.YEAR.getId().equals(dto.getBillPlan()))
        {
            resp.addData(Field.STATUS, ProductStatus.getStatusByDate(dto.getFromDate(), dto.getToDate()));
            resp.addData(Field.FROM_DATE, DateUtils.format(dto.getFromDate(), DateFormat.YYYY_MM_DD_2));
            resp.addData(Field.TO_DATE, DateUtils.format(dto.getToDate(), DateFormat.YYYY_MM_DD_2));
            resp.addData(Field.REMAIN_DAYS, BusinessUtils.getDayDiffFromNow(dto.getFromDate(), dto.getToDate()) + "");
        }
        else
        {
            resp.addData(Field.STATUS, ProductStatus.getStatusByBalance(dto.getCostAmt(), dto.getBalance()));
            resp.addData(Field.UNIT_AMT, NumberUtils.formatAmount(dto.getCostAmt()));
            resp.addData(Field.BALANCE, NumberUtils.formatAmount(dto.getBalance()));
        }
    }

    @Override
    public List<Map<String, Object>> getClientProductList(Long clientId)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ProductDictDTO dto = productApi.getClientProductDictDTO(clientId);
        if(dto.getResultDTO().getResult() == RestResult.SUCCESS)
        {
            for(DictDTO d : dto.getProductDictList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, d.getKey());
                map.put(Field.NAME, d.getValue());
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public void getClientProductDetailList(Long clientId, BLResp resp)
    {
        List<Map<String, Object>> remindList = new ArrayList<>();
        List<Map<String, Object>> trashList = new ArrayList<>();
        List<Map<String, Object>> productList = new ArrayList<>();
        ProductListDTO dto = productApi.getIndexProductList(clientId, null, null, null);
        List<ProductDTO> list = dto.getOpened();
        Map<String, List<Map<String, Object>>> typeGroupProduct = new HashMap<>();
        for(ProductDTO o : list)
        {
            Map<String, Object> map = new HashMap<>();
            Integer status;
            map.put(Field.PRODUCT_ID, o.getId() + "");
            map.put(Field.PRODUCT_NAME, o.getName());
            map.put(Field.BILL_PLAN, o.getBillPlan());
            if(BillPlan.YEAR.getId().equals(o.getBillPlan()))
            {
                status = ProductStatus.getStatusByDate(o.getFromDate(), o.getToDate());
                map.put(Field.FROM_DATE, DateUtils.format(o.getFromDate(), DateFormat.YYYY_MM_DD_2));
                map.put(Field.TO_DATE, DateUtils.format(o.getToDate(), DateFormat.YYYY_MM_DD_2));
                map.put(Field.REMAIN_DAYS, BusinessUtils.getDayDiffFromNow(o.getFromDate(), o.getToDate()) + "");
            }
            else
            {
                status = ProductStatus.getStatusByBalance(o.getCostAmt(), o.getBalance());
                map.put(Field.UNIT_AMT, NumberUtils.formatAmount(o.getCostAmt()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
            }
            map.put(Field.STATUS, status);
            if(ProductStatus.NEARLY_EXPIRE.getId().equals(status) || ProductStatus.EXPIRED.getId().equals(status) ||
                    ProductStatus.INSUFFICIENT_BALANCE.getId().equals(status) || ProductStatus.IN_ARREAR.getId().equals(
                    status))
            {
                if((ProductStatus.EXPIRED.getId().equals(status) &&
                        System.currentTimeMillis() - o.getToDate().getTime() > 7 * 24 * 3600 * 1000) ||
                        (ProductStatus.IN_ARREAR.getId().equals(status) &&
                                System.currentTimeMillis() - o.getArrearTime().getTime() > 7 * 24 * 3600 * 1000))
                {
                    trashList.add(map);
                    continue;
                }
                remindList.add(map);
            }
            List<Map<String, Object>> l = typeGroupProduct.get(o.getTypeName());
            if(CollectionUtils.isEmpty(l))
            {
                l = new ArrayList<>();
            }
            l.add(map);
            typeGroupProduct.put(o.getTypeName(), l);
        }
        for(Map.Entry entry : typeGroupProduct.entrySet())
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.TYPE_NAME, entry.getKey());
            map.put(Field.PRODUCT_LIST, entry.getValue());
            productList.add(map);
        }
        resp.addData(Field.REMIND_LIST, remindList);
        resp.addData(Field.TRASH_LIST, trashList);
        resp.addData(Field.NORMAL_LIST, productList);
    }

    @Override
    public List<Map<String, Object>> getDictProductTypeList(Integer enabled)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        DictProductTypeListDTO dictProductTypeListDTO = productApi.getDictProductTypeList(enabled, null);
        List<DictProductTypeDTO> dataDTOList = dictProductTypeListDTO.getDataList();
        if(CollectionUtils.isNotEmpty(dataDTOList))
        {
            for(DictProductTypeDTO item : dataDTOList)
            {
                map = new HashMap<>();
                list.add(map);
                map.put(Field.ID, item.getId() + "");
                map.put(Field.CODE, item.getCode());
                map.put(Field.NAME, item.getName());
                map.put(Field.REMARK, item.getRemark());
                map.put(Field.ENABLED, item.getEnabled() + "");
            }
        }
        return list;
    }

    @Override
    public void getProductListBy(Long clientId, Integer isOpen, Integer[] selectedType, Page page, BLResp resp)
    {
        ProductListDTO productListDTO = productApi.getIndexProductList(RequestThread.getClientId(), isOpen,
                selectedType, page);
        List<Integer> typeList = new ArrayList<>();
        if(selectedType != null)
        {
            typeList = Arrays.asList(selectedType);
        }
        List<Map<String, Object>> allList = new ArrayList<>();
        Map<String, Object> map;
        if(productListDTO.getResultDTO().getResult() == RestResult.SUCCESS)
        {

            for(ProductDTO d : productListDTO.getOpened())
            {
                map = new HashMap<>();
                if(selectedType == null || typeList.contains(d.getTypeId()))
                {
                    allList.add(map);
                }
                else
                {
                    continue;
                }
                map.put(Field.PRODUCT_ID, d.getId() + "");
                map.put(Field.NAME, d.getName());
                map.put(Field.STATUS, d.getStatus());
                map.put(Field.BILL_PLAN, d.getBillPlan());
                if(BillPlan.YEAR.getId().equals(d.getBillPlan()))
                {
                    map.put(Field.REMAIN_DAYS, BusinessUtils.getDayDiffFromNow(d.getFromDate(), d.getToDate()) + "");
                    map.put(Field.FROM_DATE, DateUtils.format(d.getFromDate(), DateFormat.YYYY_MM_DD_2));
                    map.put(Field.TO_DATE, DateUtils.format(d.getToDate(), DateFormat.YYYY_MM_DD_2));
                }
                else
                {
                    map.put(Field.UNIT_AMT, NumberUtils.formatAmount(d.getCostAmt()));
                    map.put(Field.BALANCE, NumberUtils.formatAmount(d.getBalance()));
                }
            }
            if(TrueOrFalse.FALSE.equals(isOpen))
            {
                for(ProductDTO d : productListDTO.getToOpen())
                {
                    map = new HashMap<>();
                    if(selectedType == null || typeList.contains(d.getTypeId()))
                    {
                        allList.add(map);
                    }
                    else
                    {
                        continue;
                    }
                    map.put(Field.PRODUCT_ID, d.getId() + "");
                    map.put(Field.REMARK, d.getRemark());
                    map.put(Field.NAME, d.getName());
                }
            }
        }
        resp.addData(Field.LIST, allList);
        resp.addData(Field.PAGES, productListDTO.getPages());
        resp.addData(Field.TOTAL, productListDTO.getTotal());
    }
}
