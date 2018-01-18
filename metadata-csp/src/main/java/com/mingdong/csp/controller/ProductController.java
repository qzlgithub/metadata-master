package com.mingdong.csp.controller;

import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ProductService;
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
            @RequestParam(value = Field.END_DATE, required = false) Date endDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        productService.getProductRechargeRecord(RequestThread.getClientId(), productId, fromDate, endDate,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    /**
     * 导出产品充值记录
     */
    @GetMapping(value = "recharge/export")
    public void exportProductRechargeRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = productService.createProductRechargeXlsx(RequestThread.getClientId(), productId, fromDate,
                endDate);
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
            @RequestParam(value = Field.END_DATE, required = false) Date endDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        productService.getProductRequestRecord(RequestThread.getClientId(), productId, fromDate, endDate,
                new Page(pageNum, pageSize), resp);
        return resp;
    }

    /**
     * 导出产品服务消费记录
     */
    @GetMapping(value = "request/export")
    public void exportProductRequestRecord(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.END_DATE, required = false) Date endDate, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = productService.createProductRequestXlsx(RequestThread.getClientId(), productId, fromDate,
                endDate);
        String filename = new String("产品服务请求记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @LoginRequired
    @GetMapping(value = "allList")
    public BLResp getProductByParam(@RequestParam(value = Field.SELECTED_TYPE, required = false) String selectedType,
            @RequestParam(value = Field.IS_OPEN, required = false) Integer isOpen,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        if(StringUtils.isNullBlank(selectedType))
        {
            resp.result(RestResult.PARAMETER_ERROR);
            return resp;
        }
        String[] types = selectedType.split(",");
        Integer[] typeInts = new Integer[types.length];
        for(int i = 0; i < types.length; i++)
        {
            typeInts[i] = Integer.valueOf(types[i]);
        }
        productService.getProductListBy(RequestThread.getClientId(), isOpen, typeInts, new Page(pageNum, pageSize),
                resp);
        return resp;
    }
}
