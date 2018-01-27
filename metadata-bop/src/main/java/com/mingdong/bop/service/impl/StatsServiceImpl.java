package com.mingdong.bop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.service.RemoteClientService;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.service.RemoteStatsService;
import com.mingdong.core.service.RemoteSystemService;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.core.util.DateCalculateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private RemoteStatsService remoteStatsService;
    @Resource
    private RemoteSystemService remoteSystemService;
    @Resource
    private RemoteProductService remoteProductService;
    @Resource
    private RemoteClientService remoteClientService;

    @Override
    public BLResp getIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date nowDay = DateCalculateUtils.getCurrentDate(currentDay);//今日00:00:00
        Date previousDay = DateCalculateUtils.getBeforeDayDate(currentDay, 1, true);//昨天00:00:00
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);//近7天00:00:00
        Date monthDay = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);//近30天00:00:00
        Integer allClientCount = remoteStatsService.getAllClientCount();
        Integer clientCountByDate = remoteStatsService.getClientCountByDate(monthDay, currentDay);
        BigDecimal clientRechargeByWeek = remoteStatsService.getClientRechargeStatsByDate(weekDay, currentDay);
        BigDecimal clientRechargeByMonth = remoteStatsService.getClientRechargeStatsByDate(monthDay, currentDay);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount + "");
        resp.addData(Field.CLIENT_COUNT_BY_DATE, clientCountByDate + "");
        resp.addData(Field.CLIENT_RECHARGE_BY_WEEK, NumberUtils.formatAmount(clientRechargeByWeek));
        resp.addData(Field.CLIENT_RECHARGE_BY_MONTH, NumberUtils.formatAmount(clientRechargeByMonth));

        return resp;
    }

    @Override
    public BLResp getClientIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);//近7天00:00:00
        Date monthFrist = DateCalculateUtils.getCurrentMonthFirst(currentDay, true);//当前月第一天00:00:00
        Integer allClientCount = remoteStatsService.getAllClientCount();
        Integer clientCountByWeek = remoteStatsService.getClientCountByDate(weekDay, currentDay);
        Integer clientCountByMonthFrist = remoteStatsService.getClientCountByDate(monthFrist, currentDay);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount + "");
        resp.addData(Field.CLIENT_COUNT_BY_WEEK, clientCountByWeek + "");
        resp.addData(Field.CLIENT_COUNT_BY_MONTH_FRIST, clientCountByMonthFrist + "");
        return resp;
    }

    @Override
    public BLResp getClientList(ScopeType scopeTypeEnum, Page page)
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        Date beforeDate;
        switch(scopeTypeEnum)
        {
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case QUARTER:
                beforeDate = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                return resp.result(RestResult.PARAMETER_ERROR);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = sdf.format(beforeDate);
        String currentDayStr = sdf.format(currentDay);
        ClientInfoListDTO clientInfoListDTO = remoteClientService.getClientInfoListByDate(beforeDate, currentDay, page);
        resp.addData(Field.TOTAL, clientInfoListDTO.getTotal());
        resp.addData(Field.PAGES, clientInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ClientInfoDTO> clientInfoListByDate = clientInfoListDTO.getDataList();
        Integer total = clientInfoListDTO.getTotal();
        resp.addData(Field.TITLE, dateStr + "-" + currentDayStr + " 新增客户数量" + total + "个");
        List<Map<String, Object>> dataList = new ArrayList<>(clientInfoListByDate.size());
        Map<String, Object> map;
        for(ClientInfoDTO item : clientInfoListByDate)
        {
            map = new HashMap<>();
            map.put(Field.REGISTER_DATE, DateUtils.format(item.getRegisterTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            map.put(Field.CORP_NAME, item.getCorpName());
            map.put(Field.SHORT_NAME, item.getShortName());
            map.put(Field.USERNAME, item.getUsername());
            map.put(Field.NAME, item.getName());
            map.put(Field.PHONE, item.getPhone());
            map.put(Field.EMAIL, item.getEmail());
            map.put(Field.MANAGER_NAME, item.getManagerName());
            dataList.add(map);
        }
        resp.addData(Field.LIST, dataList);
        return resp;
    }

    @Override
    public XSSFWorkbook createClientListXlsx(ScopeType scopeTypeEnum, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("客户数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("公司名称");
        row.createCell(2).setCellValue("公司简称");
        row.createCell(3).setCellValue("账号");
        row.createCell(4).setCellValue("联系人");
        row.createCell(5).setCellValue("联系方式");
        row.createCell(6).setCellValue("邮箱");
        row.createCell(7).setCellValue("商务经理");
        Date currentDay = new Date();
        Date beforeDate = new Date();
        switch(scopeTypeEnum)
        {
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case QUARTER:
                beforeDate = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                break;
        }
        ClientInfoListDTO clientInfoListDTO = remoteStatsService.getClientInfoListByDate(beforeDate, currentDay, page);
        List<ClientInfoDTO> dataList = clientInfoListDTO.getDataList();

        if(CollectionUtils.isNotEmpty(dataList))
        {
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
            ClientInfoDTO dataInfo;
            for(int i = 0; i < dataList.size(); i++)
            {
                dataInfo = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataInfo.getRegisterTime());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataInfo.getCorpName());
                dataRow.createCell(2).setCellValue(dataInfo.getShortName());
                dataRow.createCell(3).setCellValue(dataInfo.getUsername());
                dataRow.createCell(4).setCellValue(dataInfo.getName());
                dataRow.createCell(5).setCellValue(dataInfo.getPhone());
                dataRow.createCell(6).setCellValue(dataInfo.getEmail());
                dataRow.createCell(7).setCellValue(dataInfo.getManagerName());
            }
        }
        return wb;
    }

    @Override
    public JSONArray getClientListJson(ScopeType scopeTypeEnum)
    {
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArraySec;
        Date currentDay = new Date();
        Date beforeDate = new Date();
        switch(scopeTypeEnum)
        {
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case QUARTER:
                beforeDate = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                break;
        }
        ClientInfoListDTO clientInfoListDTO = remoteStatsService.getClientInfoListByDate(beforeDate, currentDay, null);
        List<ClientInfoDTO> dataList = clientInfoListDTO.getDataList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> dateMap = new LinkedHashMap<>();
        Calendar c = Calendar.getInstance();
        c.setTime(beforeDate);
        int difInt = (int) BusinessUtils.getDayDiff(beforeDate, currentDay) + 1;
        for(int i = 0; i < difInt; i++)
        {
            dateMap.put(sdf.format(c.getTime()), 0);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        Integer intTemp;
        for(ClientInfoDTO item : dataList)
        {
            String strTemp = sdf.format(item.getRegisterTime());
            intTemp = dateMap.get(strTemp);
            dateMap.put(strTemp, intTemp + 1);
        }
        for(Map.Entry<String, Integer> entry : dateMap.entrySet())
        {
            jsonArraySec = new JSONArray();
            jsonArraySec.add(entry.getKey());
            jsonArraySec.add(entry.getValue());
            jsonArray.add(jsonArraySec);
        }
        return jsonArray;
    }

    @Override
    public BLResp getRechargeIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date nowDay = DateCalculateUtils.getCurrentDate(currentDay);
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);
        Date monthFirst = DateCalculateUtils.getCurrentMonthFirst(currentDay, true);
        BigDecimal rechargeByNow = remoteStatsService.getClientRechargeStatsByDate(nowDay, currentDay);
        BigDecimal rechargeByWeek = remoteStatsService.getClientRechargeStatsByDate(weekDay, currentDay);
        BigDecimal rechargeByMonthFirst = remoteStatsService.getClientRechargeStatsByDate(monthFirst, currentDay);
        BigDecimal rechargeByAll = remoteStatsService.getClientRechargeStatsAll();
        resp.addData(Field.CLIENT_RECHARGE_BY_NOW, NumberUtils.formatAmount(rechargeByNow));
        resp.addData(Field.CLIENT_RECHARGE_BY_WEEK, NumberUtils.formatAmount(rechargeByWeek));
        resp.addData(Field.CLIENT_RECHARGE_BY_MONTH_FIRST, NumberUtils.formatAmount(rechargeByMonthFirst));
        resp.addData(Field.CLIENT_RECHARGE_BY_ALL, NumberUtils.formatAmount(rechargeByAll));
        return resp;
    }

    @Override
    public BLResp getRechargeList(ScopeType scopeTypeEnum, Page page)
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date beforeDate;
        switch(scopeTypeEnum)
        {
            case WEEK:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);
                break;
            case HALF_MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 14, true);
                break;
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                return resp.result(RestResult.PARAMETER_ERROR);
        }
        getProductRechargeInfoList(page, beforeDate, currentDay, resp);
        return resp;
    }

    @Override
    public XSSFWorkbook createRechargeListXlsx(ScopeType scopeTypeEnum, Page page)
    {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("充值数据");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("公司名称");
        row.createCell(2).setCellValue("公司简称");
        row.createCell(3).setCellValue("账号");
        row.createCell(4).setCellValue("产品");
        row.createCell(5).setCellValue("金额(元)");
        row.createCell(6).setCellValue("充值类型");
        row.createCell(7).setCellValue("账户余额(不包含服务)");
        row.createCell(8).setCellValue("经手人");
        Date currentDay = new Date();
        Date beforeDate = new Date();
        switch(scopeTypeEnum)
        {
            case WEEK:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);
                break;
            case HALF_MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 14, true);
                break;
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                break;
        }
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteStatsService.getProductRechargeInfoListBy(
                beforeDate, currentDay, page);
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
            ProductRechargeInfoDTO dataInfo;
            for(int i = 0; i < dataList.size(); i++)
            {
                dataInfo = dataList.get(i);
                dataRow = sheet.createRow(i + 1);
                cell = dataRow.createCell(0);
                cell.setCellValue(dataInfo.getTradeTime());
                cell.setCellStyle(timeStyle);
                dataRow.createCell(1).setCellValue(dataInfo.getCorpName());
                dataRow.createCell(2).setCellValue(dataInfo.getShortName());
                dataRow.createCell(3).setCellValue(dataInfo.getUsername());
                dataRow.createCell(4).setCellValue(dataInfo.getProductName());
                dataRow.createCell(5).setCellValue(NumberUtils.formatAmount(dataInfo.getAmount()));
                dataRow.createCell(6).setCellValue(dataInfo.getRechargeType());
                dataRow.createCell(7).setCellValue(NumberUtils.formatAmount(dataInfo.getBalance()));
                dataRow.createCell(8).setCellValue(dataInfo.getManagerName());
            }
        }
        return wb;
    }

    @Override
    public JSONObject getRechargeListJson(ScopeType scopeTypeEnum)
    {
        JSONObject jsonObject = new JSONObject();
        JSONObject leftObject = new JSONObject();
        JSONObject rightObject = new JSONObject();
        JSONArray jsonArrayTemp;
        Date currentDay = new Date();
        Date beforeDate = null;
        switch(scopeTypeEnum)
        {
            case WEEK:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);
                break;
            case HALF_MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 14, true);
                break;
            case MONTH:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
                break;
            case YEAR:
                beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
                break;
            default:
                break;
        }
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteStatsService.getProductRechargeInfoListBy(
                beforeDate, currentDay, null);
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        //left
        Map<String, BigDecimal> typeNameBigDecMap = new HashMap<>();
        Set<String> setTemp = new HashSet<>();
        BigDecimal bigDecimalTemp;
        for(ProductRechargeInfoDTO item : dataList)
        {
            bigDecimalTemp = typeNameBigDecMap.get(item.getRechargeType());
            if(bigDecimalTemp == null)
            {
                bigDecimalTemp = new BigDecimal(0);
            }
            typeNameBigDecMap.put(item.getRechargeType(), bigDecimalTemp.add(item.getAmount()));
            setTemp.add(item.getRechargeType());
        }
        jsonArrayTemp = new JSONArray();
        JSONObject jsonObjectTemp;
        JSONArray jsonArrayTemp1 = new JSONArray();
        for(Map.Entry<String, BigDecimal> entry : typeNameBigDecMap.entrySet())
        {
            jsonObjectTemp = new JSONObject();
            jsonObjectTemp.put("name", entry.getKey());
            jsonObjectTemp.put("value", entry.getValue());
            jsonArrayTemp.add(jsonObjectTemp);
        }
        for(String item : setTemp)
        {
            jsonArrayTemp1.add(item);
        }
        leftObject.put("legendData", jsonArrayTemp1);
        leftObject.put("data", jsonArrayTemp);
        //right
        DictRechargeTypeListDTO dictRechargeTypeListDTO = remoteSystemService.getDictRechargeTypeListByStatus(
                TrueOrFalse.TRUE, TrueOrFalse.FALSE);
        List<DictRechargeTypeDTO> dictRechargeTypeList = dictRechargeTypeListDTO.getDataList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Map<String, BigDecimal>> dateMap = new LinkedHashMap<>();
        Map<String, BigDecimal> bigMapTemp;
        Calendar c = Calendar.getInstance();
        c.setTime(beforeDate);
        int difInt = (int) BusinessUtils.getDayDiff(beforeDate, currentDay) + 1;
        for(int i = 0; i < difInt; i++)
        {
            bigMapTemp = new LinkedHashMap<>();
            for(DictRechargeTypeDTO item : dictRechargeTypeList)
            {
                bigMapTemp.put(item.getName(), new BigDecimal(0));
            }
            dateMap.put(sdf.format(c.getTime()), bigMapTemp);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        for(ProductRechargeInfoDTO item : dataList)
        {
            String dateStrTemp = sdf.format(item.getTradeTime());
            bigMapTemp = dateMap.get(dateStrTemp);
            bigDecimalTemp = bigMapTemp.get(item.getRechargeType());
            if(bigDecimalTemp == null)
            {
                bigDecimalTemp = new BigDecimal(0);
            }
            bigMapTemp.put(item.getRechargeType(), bigDecimalTemp.add(item.getAmount()));
        }
        jsonArrayTemp = new JSONArray();
        jsonArrayTemp1 = new JSONArray();
        Map<String, List<BigDecimal>> nameBigDecMap = new LinkedHashMap<>();
        List<BigDecimal> bigListTemp;
        for(Map.Entry<String, Map<String, BigDecimal>> entry : dateMap.entrySet())
        {
            jsonArrayTemp1.add(entry.getKey());
            bigMapTemp = entry.getValue();
            for(Map.Entry<String, BigDecimal> entrySec : bigMapTemp.entrySet())
            {
                bigListTemp = nameBigDecMap.get(entrySec.getKey());
                if(bigListTemp == null)
                {
                    bigListTemp = new ArrayList<>();
                    nameBigDecMap.put(entrySec.getKey(), bigListTemp);
                }
                bigListTemp.add(entrySec.getValue());
            }
        }
        for(Map.Entry<String, List<BigDecimal>> entry : nameBigDecMap.entrySet())
        {
            jsonObjectTemp = new JSONObject();
            jsonObjectTemp.put("name", entry.getKey());
            jsonObjectTemp.put("type", "bar");
            jsonObjectTemp.put("data", entry.getValue());
            jsonArrayTemp.add(jsonObjectTemp);
        }
        rightObject.put("series", jsonArrayTemp);
        rightObject.put("names", jsonArrayTemp1);
        jsonObject.put("leftJson", leftObject);
        jsonObject.put("rightJson", rightObject);
        return jsonObject;
    }

    private void getProductRechargeInfoList(Page page, Date date, Date currentDay, BLResp resp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Integer total = remoteStatsService.getClientRechargeCountByDate(date, currentDay);
        BigDecimal sumRec = remoteStatsService.getClientRechargeStatsByDate(date, currentDay);
        String dateStr = sdf.format(date);
        String currentDayStr = sdf.format(currentDay);
        resp.addData(Field.TITLE, dateStr + "-" + currentDayStr + " 共充值" + NumberUtils.formatAmount(sumRec) + "元");
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        if(total > 0 && page.getPageNum() <= pages)
        {
            resp.addData(Field.LIST, getProductRechargeInfoList(page, date, currentDay));
        }
    }

    private List<Map<String, Object>> getProductRechargeInfoList(Page page, Date start, Date end)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeRecord(null,
                null, start, end, page);
        List<ProductRechargeInfoDTO> infoDateList = productRechargeInfoListDTO.getDataList();
        List<Map<String, Object>> dataList = getProductRechargeInfoDateList(infoDateList);
        return dataList;
    }

    private List<Map<String, Object>> getProductRechargeInfoDateList(List<ProductRechargeInfoDTO> infoDateList)
    {
        List<Map<String, Object>> dataList = new ArrayList<>(infoDateList.size());
        Map<String, Object> map;
        for(ProductRechargeInfoDTO item : infoDateList)
        {
            map = new HashMap<>();
            dataList.add(map);
            map.put(Field.TRADE_TIME, DateUtils.format(item.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            map.put(Field.CORP_NAME, item.getCorpName());
            map.put(Field.SHORT_NAME, item.getShortName());
            map.put(Field.USERNAME, item.getUsername());
            map.put(Field.PRODUCT_NAME, item.getProductName());
            map.put(Field.AMOUNT, NumberUtils.formatAmount(item.getAmount()));
            map.put(Field.RECHARGE_TYPE, item.getRechargeType());
            map.put(Field.BALANCE, NumberUtils.formatAmount(item.getBalance()));
            map.put(Field.MANAGER_NAME, item.getManagerName());
        }
        return dataList;
    }

}
