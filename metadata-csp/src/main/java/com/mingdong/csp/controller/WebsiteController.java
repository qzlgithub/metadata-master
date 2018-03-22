package com.mingdong.csp.controller;

import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.ProductService;
import com.mingdong.csp.service.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WebsiteController
{

    @Resource
    private ProductService productService;
    @Resource
    private SystemService systemService;

    @GetMapping(value = "/api/product/type")
    public RestResp getProductType()
    {
        RestResp resp = new RestResp();
        resp.addData(Field.LIST, ProductType.getProductTypeDict());
        return resp;
    }

    @GetMapping(value = "/api/product/list")
    public RestListResp getProductList(@RequestParam(value = Field.PRODUCT_TYPE, required = false) String productType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp resp = new RestListResp();
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
        productService.getProductListAll(productTypeList, new Page(pageNum, pageSize), resp);
        return resp;
    }

    @GetMapping(value = "/api/news/list")
    public RestListResp getNewsList(@RequestParam(value = Field.TYPE, required = false) Integer type,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp resp = new RestListResp();
        systemService.getNewsList(type, new Page(pageNum, pageSize), resp);
        return resp;
    }

    @GetMapping(value = "/api/news/detail")
    public RestResp getNewsList(@RequestParam(value = Field.ID, required = true) Long id)
    {
        RestResp resp = new RestResp();
        systemService.getNewsDetail(id, resp);
        return resp;
    }

}
