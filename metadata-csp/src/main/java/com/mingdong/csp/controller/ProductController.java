package com.mingdong.csp.controller;

import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "product")
public class ProductController
{
    @Resource
    private ProductService productService;

    /**
     * 查询产品充值记录
     */
    @LoginRequired
    @GetMapping(value = "recharge")
    public BLResp getProductRechargeRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        productService.getProductRechargeRecord(RequestThread.getClientId(), productId, fromDate, toDate,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    /**
     * 导出产品充值记录
     */
    @LoginRequired
    @GetMapping(value = "recharge/export")
    public void exportProductRechargeRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        XSSFWorkbook wb = productService.createProductRechargeXlsx(RequestThread.getClientId(), productId, fromDate,
                toDate);
        String filename = new String("产品充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 查询产品服务消费记录
     */
    @LoginRequired
    @GetMapping(value = "request/list")
    public BLResp getProductRequestRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        productService.getProductRequestRecord(RequestThread.getClientId(), productId, fromDate, toDate,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    /**
     * 导出产品服务消费记录
     */
    @LoginRequired
    @GetMapping(value = "request/export")
    public void exportProductRequestRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        XSSFWorkbook wb = productService.createProductRequestXlsx(RequestThread.getClientId(), productId, fromDate,
                toDate);
        String filename = new String("产品服务请求记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "all")
    public ListRes getProductByParam(@RequestParam(value = Field.PRODUCT_TYPE, required = false) String productType,
            @RequestParam(value = Field.INC_OPENED, required = false) Integer incOpened,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        List<Integer> productTypeList;
        if(StringUtils.isBlank(productType))
        {
            productTypeList = null;
        }
        else
        {
            String[] types = productType.split(",");
            productTypeList = new ArrayList<>();
            for(String s : types)
            {
                productTypeList.add(Integer.valueOf(s));
            }
        }
        productService.getProductListBy(RequestThread.getClientId(), productTypeList, incOpened,
                new Page(pageNum, pageSize), res);
        return res;
    }
}
