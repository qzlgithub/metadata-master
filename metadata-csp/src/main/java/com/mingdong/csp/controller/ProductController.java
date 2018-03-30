package com.mingdong.csp.controller;

import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ProductService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
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
public class ProductController
{
    @Resource
    private ProductService productService;

    /**
     * 查询产品充值记录
     */
    @LoginRequired
    @GetMapping(value = "/product/recharge")
    public RestResp getProductRechargeRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp resp = new RestResp();
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
    @GetMapping(value = "/product/recharge/export")
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
    @GetMapping(value = "/product/request/list")
    public RestResp getProductRequestRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.HIT, required = false) Integer hit,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp resp = new RestResp();
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        productService.getProductRequestRecord(RequestThread.getClientId(), productId, fromDate, toDate, hit,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    /**
     * 导出产品服务消费记录
     */
    @LoginRequired
    @GetMapping(value = "/product/request/export")
    public void exportProductRequestRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.HIT, required = false) Integer hit, HttpServletResponse response)
            throws IOException
    {
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        XSSFWorkbook wb = productService.createProductRequestXlsx(RequestThread.getClientId(), productId, fromDate,
                toDate, hit);
        String filename = new String("产品服务请求记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "/product/all")
    public RestListResp getProductByParam(
            @RequestParam(value = Field.PRODUCT_TYPE, required = false) String productType,
            @RequestParam(value = Field.INC_OPENED, required = false) Integer incOpened,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        List<Integer> productTypeList;
        if(StringUtils.isNullBlank(productType))
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
