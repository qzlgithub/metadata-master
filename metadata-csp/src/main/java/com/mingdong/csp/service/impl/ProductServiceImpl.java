package com.mingdong.csp.service.impl;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.ProductService;
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
    private CommonRpcService commonRpcService;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private ProductRpcService productRpcService;

    @Override
    public void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page,
            RestResp resp)
    {
        ListDTO<RechargeResDTO> productRecListDTO = clientRpcService.getClientRechargeRecord(clientId,
                productId, fromDate, toDate, page);
        resp.addData(Field.TOTAL, productRecListDTO.getTotal());
        List<RechargeResDTO> dataList = productRecListDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(RechargeResDTO o : dataList)
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.TRADE_TIME, DateUtils.format(o.getRechargeAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                m.put(Field.TRADE_NO, o.getRechargeNo());
                m.put(Field.RECHARGE_TYPE, o.getRechargeTypeName());
                m.put(Field.BILL_PLAN, o.getBillPlan());
                m.put(Field.PRODUCT_NAME, o.getProductName());
                m.put(Field.AMOUNT, NumberUtils.formatAmount(o.getAmount()));
                m.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                m.put(Field.CONTRACT_NO, o.getContractNo());
                list.add(m);
            }
            resp.addData(Field.LIST, list);
        }
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
        row.createCell(5).setCellValue("产品余额");
        row.createCell(6).setCellValue("合同编号");
        Page page = new Page(1, 1000);
        ListDTO<RechargeResDTO> productRecListDTO = clientRpcService.getClientRechargeRecord(clientId,
                productId, fromDate, toDate, page);
        List<RechargeResDTO> dataList = productRecListDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            RechargeResDTO dataDTO;
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            for(int i = 0; i < dataList.size(); i++)
            {
                dataDTO = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataDTO.getRechargeAt());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataDTO.getRechargeNo());
                dataRow.createCell(2).setCellValue(dataDTO.getProductName());
                dataRow.createCell(3).setCellValue(dataDTO.getRechargeTypeName());
                dataRow.createCell(4).setCellValue(NumberUtils.formatAmount(dataDTO.getAmount()));
                dataRow.createCell(5).setCellValue(NumberUtils.formatAmount(dataDTO.getBalance()));
                dataRow.createCell(6).setCellValue(dataDTO.getContractNo());
            }
        }
        return wb;
    }

    @Override
    public void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page,
            RestResp resp)
    {
        ListDTO<AccessResDTO> apiReqInfoListDTO = clientRpcService.getClientRequestRecord(clientId, null, productId,
                fromDate, toDate, page);
        List<AccessResDTO> dataList = apiReqInfoListDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(AccessResDTO item : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(item.getRequestAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, item.getRequestNo() + "");
                map.put(Field.PRODUCT_NAME, item.getProductName() == null ? "" : item.getProductName());
                map.put(Field.BILL_PLAN, BillPlan.getById(item.getBillPlan()).getName());
                map.put(Field.HIT, TrueOrFalse.TRUE.equals(item.getHit()) ? "是" : "否");
                map.put(Field.UNIT_AMT, NumberUtils.formatAmount(item.getFee()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        resp.addData(Field.TOTAL, apiReqInfoListDTO.getTotal());
        resp.addData(Field.PAGES, page.getTotalPage(apiReqInfoListDTO.getTotal()));
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
    }

    @Override
    public XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date toDate)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("历史消费记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("消费单号");
        row.createCell(2).setCellValue("产品服务");
        row.createCell(3).setCellValue("计费方式");
        row.createCell(4).setCellValue("是否成功");
        row.createCell(5).setCellValue("消费(元)");
        row.createCell(6).setCellValue("余额(元)");
        Page page = new Page(1, 1000);
        ListDTO<AccessResDTO> apiReqInfoListDTO = clientRpcService.getClientRequestRecord(clientId, null, productId,
                fromDate, toDate, page);
        List<AccessResDTO> dataList = apiReqInfoListDTO.getList();
        if(!CollectionUtils.isEmpty(dataList))
        {
            AccessResDTO dataDTO;
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            for(int i = 0; i < dataList.size(); i++)
            {
                dataDTO = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataDTO.getRequestAt());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataDTO.getRequestNo());
                dataRow.createCell(2).setCellValue(dataDTO.getProductName());
                dataRow.createCell(3).setCellValue(BillPlan.getById(dataDTO.getBillPlan()).getName());
                dataRow.createCell(4).setCellValue(TrueOrFalse.TRUE.equals(dataDTO.getHit()) ? "是" : "否");
                dataRow.createCell(5).setCellValue(NumberUtils.formatAmount(dataDTO.getFee()));
                dataRow.createCell(6).setCellValue(NumberUtils.formatAmount(dataDTO.getBalance()));
            }
        }
        return wb;
    }

    @Override
    public void getClientProductInfoData(Long clientId, Long productId, RestResp resp)
    {
        ProductResDTO dto = productRpcService.getClientProductInfo(clientId, productId);
        if(dto.getResult() != RestResult.SUCCESS)
        {
            resp.setError(dto.getResult());
            return;
        }
        resp.addData(Field.PRODUCT_ID, dto.getId() + "");
        resp.addData(Field.PRODUCT_NAME, dto.getName());
        resp.addData(Field.BILL_PLAN, dto.getBillPlan());
        resp.addData(Field.CONTENT, dto.getContent());
        if(BillPlan.BY_TIME.getId().equals(dto.getBillPlan()))
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
    public List<Dict> getClientProductDict(Long clientId)
    {
        ListDTO<Dict> listDTO = commonRpcService.getClientProductDict(clientId);
        List<Dict> dict = listDTO.getList();
        return dict != null ? dict : new ArrayList<>();
    }

    @Override
    public void getClientProductDetailList(Long clientId, RestResp resp)
    {
        List<Map<String, Object>> remindList = new ArrayList<>();
        List<Map<String, Object>> trashList = new ArrayList<>();
        List<Map<String, Object>> productList = new ArrayList<>();
        ListDTO<ProductResDTO> listDTO = productRpcService.getOpenedProductList(clientId);
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            Map<String, List<Map<String, Object>>> typeGroupProduct = new HashMap<>();
            for(ProductResDTO o : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                Integer status;
                map.put(Field.PRODUCT_ID, o.getId() + "");
                map.put(Field.PRODUCT_NAME, o.getName());
                map.put(Field.BILL_PLAN, o.getBillPlan());
                if(BillPlan.BY_TIME.getId().equals(o.getBillPlan()))
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
                        ProductStatus.INSUFFICIENT_BALANCE.getId().equals(status) ||
                        ProductStatus.IN_ARREAR.getId().equals(status))
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
        }
        resp.addData(Field.REMIND_LIST, remindList);
        resp.addData(Field.TRASH_LIST, trashList);
        resp.addData(Field.NORMAL_LIST, productList);
    }

    @Override
    public void getProductListBy(Long clientId, List<Integer> productTypeList, Integer incOpened, Page page,
            RestListResp res)
    {
        ListDTO<ProductResDTO> listDTO = productRpcService.getProductList(clientId, productTypeList, incOpened, page);
        res.setTotal(listDTO.getTotal());
        res.addData(Field.PAGES, page.getTotalPage(listDTO.getTotal()));
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>();
            for(ProductResDTO o : listDTO.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.PRODUCT_ID, o.getId() + "");
                map.put(Field.NAME, o.getName());
                map.put(Field.IS_OPENED, o.getOpened());
                if(TrueOrFalse.TRUE.equals(o.getOpened()))
                {
                    map.put(Field.BILL_PLAN, o.getBillPlan());
                    if(BillPlan.BY_TIME.getId().equals(o.getBillPlan()))
                    {
                        map.put(Field.STATUS, ProductStatus.getStatusByDate(o.getFromDate(), o.getToDate()));
                        map.put(Field.REMAIN_DAYS,
                                BusinessUtils.getDayDiffFromNow(o.getFromDate(), o.getToDate()) + "");
                        map.put(Field.FROM_DATE, DateUtils.format(o.getFromDate(), DateFormat.YYYY_MM_DD_2));
                        map.put(Field.TO_DATE, DateUtils.format(o.getToDate(), DateFormat.YYYY_MM_DD_2));
                    }
                    else
                    {
                        map.put(Field.STATUS, ProductStatus.getStatusByBalance(o.getCostAmt(), o.getBalance()));
                        map.put(Field.UNIT_AMT, NumberUtils.formatAmount(o.getCostAmt()));
                        map.put(Field.BALANCE, NumberUtils.formatAmount(o.getBalance()));
                    }
                }
                else
                {
                    map.put(Field.REMARK, o.getRemark());
                }
                list.add(map);
            }
            res.setList(list);
        }
    }

    @Override
    public void getProductInfo(Long productId, RestResp resp)
    {
        ProductResDTO productInfoData = productRpcService.getProductInfoData(productId);
        resp.addData(Field.NAME, productInfoData.getName());
        resp.addData(Field.CONTENT, productInfoData.getContent());
    }
}