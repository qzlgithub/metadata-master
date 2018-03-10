package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.StatsService;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.util.BusinessUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Controller
public class ExportController
{
    @Resource
    private ClientService clientService;
    @Resource
    private StatsService statsService;
    @Resource
    private TradeService tradeService;

    /**
     * 导出客户请求记录
     */
    @LoginRequired
    @GetMapping(value = "/exp/client/request")
    public void exportConsumeList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.USER_ID, required = false) Long userId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        XSSFWorkbook wb = clientService.createClientRequestXlsx(clientId, userId, productId, fromDate, toDate,
                new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 导出客户充值记录
     */
    @LoginRequired
    @GetMapping(value = "/exp/client/recharge")
    public void exportProductRechargeRecord(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        XSSFWorkbook wb = clientService.createClientRechargeXlsx(clientId, productId, fromDate, toDate,
                new Page(1, 1000));
        String filename = new String("充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 导出客户数据
     */
    @LoginRequired
    @GetMapping(value = "/exp/stats/client")
    public void exportClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            HttpServletResponse response) throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createClientListXlsx(scopeTypeEnum, new Page(1, 1000));
        String filename = new String("客户数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "/exp/stats/recharge")
    public void exportRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            HttpServletResponse response) throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createRechargeListXlsx(scopeTypeEnum, new Page(1, 1000));
        String filename = new String("充值数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "/exp/stats/request")
    public void exportRequestList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.NAME, required = false) String name,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId, HttpServletResponse response)
            throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createRequestListXlsx(scopeTypeEnum, new Page(1, 1000), name, productId);
        String filename = new String("产品请求数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * TODO TBD：营收数据导出
     */
    @LoginRequired
    @GetMapping(value = "/exp/stats/revenue")
    public void exportRevenueList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.NAME, required = false) String name,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId, HttpServletResponse response)
            throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createRevenueListXlsx(scopeTypeEnum, new Page(1, 1000));
        String filename = new String("营收数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "/exp/finance/recharge")
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
        to = BusinessUtils.getLastDayStartTime(to);
        XSSFWorkbook wb = tradeService.createProductRechargeInfoListXlsx(keyword, product,
                RequestThread.isManager() ? manager : RequestThread.getOperatorId(), rechargeType, from, to,
                new Page(1, 1000));
        String filename = new String("充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "/exp/finance/request")
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
        to = BusinessUtils.getLastDayStartTime(to);
        XSSFWorkbook wb = tradeService.createClientBillListXlsx(keyword, productId, billPlan, from, to,
                RequestThread.isManager() ? null : RequestThread.getOperatorId(), new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }
}
