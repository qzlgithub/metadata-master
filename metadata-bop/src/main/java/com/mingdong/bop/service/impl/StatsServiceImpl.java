package com.mingdong.bop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.domain.entity.ClientInfo;
import com.mingdong.bop.domain.mapper.ClientInfoMapper;
import com.mingdong.bop.domain.mapper.StatsClientMapper;
import com.mingdong.bop.service.StatsService;
import com.mingdong.bop.util.DateCalculateUtils;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.util.BusinessUtils;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;

    @Override
    public BLResp getIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date nowDay = DateCalculateUtils.getCurrentDate(currentDay);//今日00:00:00
        Date previousDay = DateCalculateUtils.getBeforeDayDate(currentDay, 1, true);//昨天00:00:00
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);//近7天00:00:00
        Date monthDay = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);//近30天00:00:00
        Long allClientCount = statsClientMapper.getAllClientCount();
        Long clientCountByDate = statsClientMapper.getClientCountByDate(monthDay,currentDay);
        BigDecimal clientRechargeByWeek = statsClientMapper.getClientRechargeByDate(weekDay);
        BigDecimal clientRechargeByMonth = statsClientMapper.getClientRechargeByDate(monthDay);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount);
        resp.addData(Field.CLIENT_COUNT_BY_DATE, clientCountByDate);
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
        Long allClientCount = statsClientMapper.getAllClientCount();
        Long clientCountByWeek = statsClientMapper.getClientCountByDate(weekDay,currentDay);
        Long clientCountByMonthFrist = statsClientMapper.getClientCountByDate(monthFrist,currentDay);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount);
        resp.addData(Field.CLIENT_COUNT_BY_WEEK, clientCountByWeek);
        resp.addData(Field.CLIENT_COUNT_BY_MONTH_FRIST, clientCountByMonthFrist);
        return resp;
    }

    @Override
    public BLResp getClientList(ScopeType scopeTypeEnum, Page page)
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(ScopeType.MONTH == scopeTypeEnum)
        {

            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
            getClientInfoList(page, date,currentDay,resp);
        }
        else if(ScopeType.QUARTER == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
            getClientInfoList(page, date,currentDay,resp);
        }
        else if(ScopeType.TYPE == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
            getClientInfoList(page, date,currentDay,resp);
        }
        else
        {
            return resp.result(RestResult.PARAMETER_ERROR);
        }
        return resp;
    }

    @Override
    public XSSFWorkbook createClientListXlsx(ScopeType scopeTypeEnum, Page page){
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
        List<ClientInfo> dataList = new ArrayList<>();
        if(ScopeType.MONTH == scopeTypeEnum)
        {

            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
            Long total = statsClientMapper.getClientCountByDate(date,currentDay);
            int pages = page.getTotalPage(total.intValue());
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                dataList = clientInfoMapper.getClientInfoListByDate(date,currentDay);
            }
        }
        else if(ScopeType.QUARTER == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
            Long total = statsClientMapper.getClientCountByDate(date,currentDay);
            int pages = page.getTotalPage(total.intValue());
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                dataList = clientInfoMapper.getClientInfoListByDate(date,currentDay);
            }
        }
        else if(ScopeType.TYPE == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
            Long total = statsClientMapper.getClientCountByDate(date,currentDay);
            int pages = page.getTotalPage(total.intValue());
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                dataList = clientInfoMapper.getClientInfoListByDate(date,currentDay);
            }
        }

        if(CollectionUtils.isNotEmpty(dataList)){
            Row dataRow;
            Cell cell;
            CellStyle timeStyle = wb.createCellStyle();
            timeStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
            ClientInfo dataInfo;
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
        List<ClientInfo> dataList = new ArrayList<>();
        Date beforeDate = null;
        if(ScopeType.MONTH == scopeTypeEnum)
        {

            beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
            dataList = clientInfoMapper.getClientInfoListByDate(beforeDate,currentDay);
        }
        else if(ScopeType.QUARTER == scopeTypeEnum)
        {
            beforeDate = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
            dataList = clientInfoMapper.getClientInfoListByDate(beforeDate,currentDay);
        }
        else if(ScopeType.TYPE == scopeTypeEnum)
        {
            beforeDate = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
            dataList = clientInfoMapper.getClientInfoListByDate(beforeDate,currentDay);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Integer> dateMap = new LinkedHashMap<>();
        Calendar c = Calendar.getInstance();
        c.setTime(beforeDate);
        int difInt = (int) BusinessUtils.getDayDiff(beforeDate,currentDay) + 1;
        for(int i = 0 ; i < difInt ; i++){
            dateMap.put(sdf.format(c.getTime()),0);
            c.add(Calendar.DAY_OF_MONTH,1);
        }
        Integer intTemp;
        for(ClientInfo item : dataList){
            String strTemp = sdf.format(item.getRegisterTime());
            intTemp = dateMap.get(strTemp);
            dateMap.put(strTemp,intTemp+1);
        }
        for(Map.Entry<String,Integer> entry : dateMap.entrySet()){
            jsonArraySec = new JSONArray();
            jsonArraySec.add(entry.getKey());
            jsonArraySec.add(entry.getValue());
            jsonArray.add(jsonArraySec);
        }
        return jsonArray;
    }

    private void getClientInfoList(Page page, Date date,Date currentDay,BLResp resp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Long total = statsClientMapper.getClientCountByDate(date,currentDay);
        String dateStr = sdf.format(date);
        String currentDayStr = sdf.format(currentDay);
        resp.addData(Field.TITLE, dateStr+"-"+currentDayStr+" 新增客户数量"+total+"个");
        int pages = page.getTotalPage(total.intValue());
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        if(total > 0 && page.getPageNum() <= pages)
        {
            resp.addData(Field.LIST, getClientInfoList(page, date,currentDay));
        }
    }

    private List<Map<String, Object>> getClientInfoList(Page page, Date start,Date end)
    {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
        List<ClientInfo> clientInfoListByDate = clientInfoMapper.getClientInfoListByDate(start,end);
        List<Map<String, Object>> dataList = getClientInfoDateList(clientInfoListByDate);
        return dataList;
    }

    private List<Map<String, Object>> getClientInfoDateList(List<ClientInfo> clientInfoListByDate)
    {
        List<Map<String, Object>> dataList = new ArrayList<>(clientInfoListByDate.size());
        Map<String, Object> map;
        for(ClientInfo item : clientInfoListByDate)
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
        return dataList;
    }

}
