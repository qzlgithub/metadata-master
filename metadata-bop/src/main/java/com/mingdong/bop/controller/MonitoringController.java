package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.constant.WarningType;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MonitoringController
{
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;
    @Resource
    private SystemService systemService;

    @LoginRequired
    @GetMapping(value = "/monitoring/client/allCustomer")
    public RestListResp allCustomer()
    {
        RestListResp res = new RestListResp();
        clientService.getAllClient(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/request")
    public RestResp customerRequest(@RequestParam(value = Field.CLIENT_ID, required = false) String clientId)
    {
        RestResp res = new RestResp();
        List<Long> clientIdList = convertStringToList(clientId);
        clientService.getStatsClientRequestCache(clientIdList, res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/detail")
    public RestListResp customerDetail()
    {
        RestListResp res = new RestListResp();
        clientService.getCustomerRequestList(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/traffic")
    public RestResp customerTraffic(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp res = new RestResp();
        clientService.getClientTraffic7d(new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/map")
    public RestResp customerTrafficMap()
    {
        RestResp res = new RestResp();
        clientService.getClientCity1h(res);
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
    @GetMapping(value = "/monitoring/product/type")
    public RestResp productType()
    {
        RestResp res = new RestResp();
        productService.getProductForType(res);
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
        List<Long> productIdList = convertStringToList(productId);
        productService.getStatsProductRequestCache(productIdList, res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/ratio")
    public RestResp productRatio()
    {
        RestResp res = new RestResp();
        productService.getProductRatio1h(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/traffic")
    public RestResp productTraffic(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp res = new RestResp();
        productService.getProductTraffic7d(new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/out")
    public RestListResp productOut()
    {
        RestListResp res = new RestListResp();
        systemService.getWarningOutListByWarningType(WarningType.PRODUCT.getId(), res, new Page(1, 8));
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/warning")
    public RestListResp productWarning()
    {
        RestListResp res = new RestListResp();
        systemService.getWarningManageListByWarningType(WarningType.PRODUCT.getId(), null, null, null,
                TrueOrFalse.FALSE, res, new Page(1, 4));
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/warning")
    public RestListResp clientWarning()
    {
        RestListResp res = new RestListResp();
        systemService.getWarningManageListByWarningType(WarningType.CLIENT.getId(), null, null, null, TrueOrFalse.FALSE,
                res, new Page(1, 4));
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/product/setting")
    public RestResp productSetting()
    {
        RestResp res = new RestListResp();
        res.addAllData(systemService.getWarningSettingListByWarningType(WarningType.PRODUCT.getId()));
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/client/setting")
    public RestResp clientSetting()
    {
        RestResp res = new RestListResp();
        res.addAllData(systemService.getWarningSettingListByWarningType(WarningType.CLIENT.getId()));
        return res;
    }

    private List<Long> convertStringToList(String str)
    {
        List<Long> idList = new ArrayList<>();
        if(!StringUtils.isNullBlank(str))
        {
            String[] arr = str.split(",");
            for(String s : arr)
            {
                idList.add(Long.valueOf(s));
            }
        }
        return idList;
    }
}
