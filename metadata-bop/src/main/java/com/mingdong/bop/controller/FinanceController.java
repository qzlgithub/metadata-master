package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.model.ListRes;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "finance")
public class FinanceController
{
    @Resource
    private TradeService tradeService;

    @GetMapping(value = "/recharge")
    public ListRes getRechargeList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.RECHARGE_TYPE, required = false) Long rechargeType,
            @RequestParam(value = Field.PRODUCT, required = false) Long product,
            @RequestParam(value = Field.MANAGER, required = false) Long manager,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = setToTomorrow(to);
        tradeService.getProductRechargeInfoList(keyword, product, manager, rechargeType, from, to,
                new Page(pageNum, pageSize), res);
        return res;
    }

    @GetMapping(value = "recharge/export")
    public void exportRechargeList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.RECHARGE_TYPE, required = false) Long rechargeType,
            @RequestParam(value = Field.PRODUCT, required = false) Long product,
            @RequestParam(value = Field.MANAGER, required = false) Long manager,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = setToTomorrow(to);
        XSSFWorkbook wb = tradeService.createProductRechargeInfoListXlsx(keyword, product, manager, rechargeType, from,
                to, new Page(1, 1000));
        String filename = new String("充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "/bill")
    public ListRes getBillList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.PRODUCT, required = false) Long productId,
            @RequestParam(value = Field.BILL_PLAN, required = false) Integer billPlan,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = setToTomorrow(to);
        tradeService.getClientBillList(keyword, productId, billPlan, from, to, new Page(pageNum, pageSize), res);
        return res;
    }

    @GetMapping(value = "/bill/export")
    public void exportBillList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.PRODUCT, required = false) Long productId,
            @RequestParam(value = Field.BILL_PLAN, required = false) Integer billPlan,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = setToTomorrow(to);
        XSSFWorkbook wb = tradeService.createClientBillListXlsx(keyword, productId, billPlan, from, to,
                new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    private Date setToTomorrow(Date date)
    {
        if(date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }
        return null;
    }
}
