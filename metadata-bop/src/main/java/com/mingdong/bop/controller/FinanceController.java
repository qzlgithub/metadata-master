package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@RestController
@RequestMapping(value = "finance")
public class FinanceController
{
    @Resource
    private TradeService tradeService;

    @GetMapping(value = "/rechargeList")
    public BLResp getRechargeList(@RequestParam(value = Field.SHORT_NAME, required = false) String shortName,
            @RequestParam(value = Field.TYPE_Id, required = false) Long typeId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.MANAGER_ID, required = false) Long managerId,
            @RequestParam(value = Field.START_DATE, required = false) Date startDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        tradeService.getProductRechargeInfoList(shortName, typeId, productId, managerId, startDate, endDate,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    @GetMapping(value = "/rechargeList/export")
    public void exportRechargeList(@RequestParam(value = Field.SHORT_NAME, required = false) String shortName,
            @RequestParam(value = Field.TYPE_Id, required = false) Long typeId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.MANAGER_ID, required = false) Long managerId,
            @RequestParam(value = Field.START_DATE, required = false) Date startDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = tradeService.createProductRechargeInfoListXlsx(shortName, typeId, productId, managerId,
                startDate, endDate, new Page(1, 1000));
        String filename = new String("充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "/billList")
    public BLResp getBillList(@RequestParam(value = Field.SHORT_NAME, required = false) String shortName,
            @RequestParam(value = Field.TYPE_Id, required = false) Long typeId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_DATE, required = false) Date startDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        tradeService.getClientBillList(shortName, typeId, productId, startDate, endDate, new Page(pageNum, pageSize),
                resp);
        return resp;
    }

    @GetMapping(value = "/billList/export")
    public void exportBillList(@RequestParam(value = Field.SHORT_NAME, required = false) String shortName,
            @RequestParam(value = Field.TYPE_Id, required = false) Long typeId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_DATE, required = false) Date startDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = tradeService.createClientBillListXlsx(shortName, typeId, productId, startDate, endDate,
                new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

}
