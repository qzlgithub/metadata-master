package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MonitoringController
{
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/allCustomer")
    public RestListResp allCustomer()
    {
        RestListResp res = new RestListResp();
        clientService.getAllClient(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/request")
    public RestResp customerRequest(@RequestParam(value = Field.CLIENT_ID, required = false) String clientId)
    {
        RestResp res = new RestResp();
        List<Long> clientIdList = null;
        if(!StringUtils.isNullBlank(clientId))
        {
            List<String> strings = Arrays.asList(clientId.split(","));
            clientIdList = new ArrayList<>();
            for(String item : strings)
            {
                clientIdList.add(Long.valueOf(item));
            }
        }
        clientService.getStatsClientRequestCache(clientIdList, res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/detail")
    public RestListResp customerDetail()
    {
        RestListResp res = new RestListResp();
        clientService.getCustomerRequestList(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/traffic")
    public RestResp customerTraffic(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp res = new RestResp();
        clientService.getClientTraffic(new Page(pageNum,pageSize),res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/allProduct")
    public RestListResp allProduct()
    {
        RestListResp res = new RestListResp();
        productService.getAllProduct(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/detail")
    public RestListResp productDetail()
    {
        RestListResp res = new RestListResp();
        productService.getProductRequestList(res);
        return res;
    }

    /**
     * 产品请求监控图（近1小时）
     */
    @LoginRequired
    @GetMapping(value = "/monitoring/product/request")
    public RestResp productRequest(@RequestParam(value = Field.PRODUCT_ID, required = false) String productId)
    {
        RestResp res = new RestResp();
        List<Long> productIdList = null;
        if(!StringUtils.isNullBlank(productId))
        {
            List<String> strings = Arrays.asList(productId.split(","));
            productIdList = new ArrayList<>();
            for(String item : strings)
            {
                productIdList.add(Long.valueOf(item));
            }
        }
        productService.getStatsProductRequestCache(productIdList, res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/ratio")
    public RestResp productRatio()
    {
        RestResp res = new RestResp();
        productService.getProductRatio(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/traffic")
    public RestResp productTraffic(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp res = new RestResp();
        productService.getProductTraffic(new Page(pageNum,pageSize),res);
        return res;
    }
}
