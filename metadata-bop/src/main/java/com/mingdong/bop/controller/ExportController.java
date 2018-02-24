package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.model.Page;
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

    @GetMapping(value = "/client/request/export")
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

    @GetMapping(value = "/client/recharge/export")
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
}
