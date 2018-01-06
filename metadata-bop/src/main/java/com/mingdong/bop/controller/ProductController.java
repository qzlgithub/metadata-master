package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.ProductInfo;
import com.mingdong.bop.model.BLResp;
import com.mingdong.bop.service.ProductService;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RequestThread;
import com.movek.model.Page;
import com.movek.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping(value = "/product")
public class ProductController
{
    @Resource
    private ProductService productService;
    // @Resource
    // private ProductInfoService productInfoService;

    @RequestMapping(value = "index.html")
    public ModelAndView productIndex()
    {
        ModelAndView view = new ModelAndView("product-manage/product-index");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/addition.html")
    public ModelAndView productAddition()
    {
        ModelAndView view = new ModelAndView("product-manage/product-add");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/edit.html")
    public ModelAndView productEdit(@RequestParam(Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("product-manage/product-edit");
        // TODO FIX...
        Map<String, Object> map = productService.getProductInfo(id);
        view.addAllObjects(map);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/category/index.html")
    public ModelAndView productCategoryIndex()
    {
        ModelAndView view = new ModelAndView("product-manage/product-category");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/productCategory/list", method = RequestMethod.GET)
    public Map<String, Object> getRechargeList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        BLResp resp = productService.getProdCategory(page);
        return resp.getDataMap();
    }

    @RequestMapping(value = "/categoryAdd", method = RequestMethod.POST)
    public BLResp addNewProductCategory(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        String remark = jsonReq.getString(Field.REMARK);
        if(StringUtils.isNullBlank(code) || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.addProdCategory(code, name, remark, resp);
        return resp;
    }

    @RequestMapping(value = "/updateCateStatus", method = RequestMethod.POST)
    public BLResp updateStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || id <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.updateCateStatus(id, enabled, resp);
        return resp;
    }

    @RequestMapping(value = "/updateProdStatus", method = RequestMethod.POST)
    public BLResp updateProdStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || id <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        System.out.println(id + "|" + enabled);
        productService.updateProdStatus(id, enabled, resp);
        return resp;
    }

    @RequestMapping(value = "/initProductCategory", method = RequestMethod.GET)
    public Map<String, Object> initFormDat(@RequestParam("id") String id)
    {
        System.out.println(id);
        BLResp resp = productService.getProductCategoryInfo(Long.valueOf(id));
        if(id == null)
        {
            resp.result(RestResult.KEY_FIELD_MISSING);
        }
        System.out.println(resp.getDataMap());
        return resp.getDataMap();
    }

    @RequestMapping(value = "/categoryUpdate", method = RequestMethod.POST)
    public BLResp categoryAdd(@RequestParam("id") String id, @RequestParam("code") String code,
            @RequestParam("name") String name, @RequestParam("remark") String remark)
    {
        BLResp resp = BLResp.build();
        if(StringUtils.isNullBlank(name) || StringUtils.isNullBlank(code))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        resp = productService.editProdCategory(Long.valueOf(id), code, name, remark);

        return resp;

    }

    @RequestMapping(value = "/product/management", method = RequestMethod.GET)
    public Map<String, Object> gotoProductManagementPage(
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        // TODO FIX...
        BLResp resp = productService.getProductInfoList(page);
        System.out.println(resp.getDataMap());
        return resp.getDataMap();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BLResp addNewProduct(@RequestBody ProductInfo productInfo)
    {

        BLResp resp = BLResp.build();
        System.out.println(productInfo.getTypeId() + "--------" + productInfo.getTypeName());
        Long typeId = productInfo.getTypeId();
        String code = productInfo.getCode();
        String name = productInfo.getName();
        BigDecimal costAmt = productInfo.getCostAmt();
        Integer enabled = productInfo.getEnabled();
        String content = productInfo.getContent();
        String remark = productInfo.getRemark();
        if(productInfo.getTypeId() == null || productInfo.getEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(productInfo.getName()) || StringUtils.isNullBlank(productInfo.getCode()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        System.out.println(typeId + "|||||" + code + "" + name);
        resp = productService.addProduct(typeId, code, name, costAmt, enabled, content, remark);
        return resp;
    }

    @RequestMapping(value = "modification", method = RequestMethod.POST)
    @ResponseBody
    public BLResp editClient(@RequestBody ProductInfo productInfo)
    {
        System.out.println(productInfo.getTypeId() + "|" + productInfo.getEnabled() + "|" + productInfo.getId());
        BLResp resp = BLResp.build();
        if(productInfo.getId() == null || productInfo.getTypeId() == null || productInfo.getEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(productInfo.getName()) || StringUtils.isNullBlank(productInfo.getCode()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }

        productService.editProduct(productInfo.getId(), productInfo.getTypeId(), productInfo.getCode(),
                productInfo.getName(), productInfo.getCostAmt(), productInfo.getContent(), productInfo.getRemark(),
                productInfo.getEnabled());
        return resp;

    }

}
