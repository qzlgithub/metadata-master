package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ProductVO;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
public class ProductController
{
    @Resource
    private ProductService productService;

    @GetMapping(value = "/productCategory/list")
    @ResponseBody
    public Map<String, Object> getRechargeList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        BLResp resp = productService.getProdCategory(page);
        return resp.getDataMap();
    }

    @PostMapping(value = "/categoryAdd")
    @ResponseBody
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

    @PostMapping(value = "/updateCateStatus")
    @ResponseBody
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

    @PostMapping(value = "/updateProdStatus")
    @ResponseBody
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
        productService.updateProdStatus(id, enabled, resp);
        return resp;
    }

    @GetMapping(value = "/initProductCategory")
    @ResponseBody
    public Map<String, Object> initFormDat(@RequestParam(Field.ID) Long id)
    {
        BLResp resp = productService.getProductCategoryInfo(id);
        if(id == null)
        {
            resp.result(RestResult.KEY_FIELD_MISSING);
        }
        return resp.getDataMap();
    }

    @PostMapping(value = "/categoryUpdate")
    @ResponseBody
    public BLResp categoryAdd(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        String remark = jsonReq.getString(Field.REMARK);
        if(id == null || id <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(name) || StringUtils.isNullBlank(code))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        resp = productService.editProdCategory(id, code, name, remark);

        return resp;

    }

    @GetMapping(value = "/product/management")
    @ResponseBody
    public Map<String, Object> gotoProductManagementPage(
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        BLResp resp = productService.getProductInfoList(page);
        return resp.getDataMap();
    }

   /* @PostMapping(value = "/addition")
    @ResponseBody
    public BLResp addNewProduct(@RequestBody ProductVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getProductType() == null || StringUtils.isNullBlank(vo.getCode()) || StringUtils.isNullBlank(
                vo.getName()) || vo.getCostAmt() == null || vo.getEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.addProduct(vo.getProductType(), vo.getCode(), vo.getName(), vo.getCostAmt(), vo.getEnabled(), vo.getCustom(),
                vo.getRemark(), vo.getContent(), resp);
        return resp;
    }*/

    @PostMapping(value = "modification")
    @ResponseBody
    public BLResp editClient(@RequestBody ProductVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getId() == null || StringUtils.isNullBlank(vo.getName()) ||
                vo.getCostAmt() == null || vo.getEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.editProduct(vo.getId(), vo.getProductType(), vo.getCode(), vo.getName(), vo.getCostAmt(),
                vo.getEnabled(), vo.getCustom(), vo.getRemark(), vo.getContent(), resp);
        return resp;
    }
}
