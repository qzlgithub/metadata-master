package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ProductVO;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProductController
{
    @Resource
    private ProductService productService;

    /**
     * 产品 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/product/list")
    public ListRes getProductList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.TYPE, required = false) Integer type,
            @RequestParam(value = Field.CUSTOM, required = false) Integer custom,
            @RequestParam(value = Field.STATUS, required = false) Integer status,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        Page page = new Page(pageNum, pageSize);
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        if(!TrueOrFalse.TRUE.equals(custom) && !TrueOrFalse.FALSE.equals(custom))
        {
            custom = null;
        }
        if(!TrueOrFalse.TRUE.equals(status) && !TrueOrFalse.FALSE.equals(status))
        {
            status = null;
        }
        productService.getProductList(keyword, type, custom, status, page, res);
        return res;
    }

    /**
     * 产品 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/product")
    public BLResp editClient(@RequestBody ProductVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getId() == null || StringUtils.isNullBlank(vo.getName()) || vo.getCostAmt() == null ||
                vo.getEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.editProduct(vo.getId(), vo.getProductType(), vo.getCode(), vo.getName(), vo.getCostAmt(),
                vo.getEnabled(), vo.getCustom(), vo.getRemark(), vo.getContent(), resp);
        return resp;
    }

    /**
     * 产品 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/product/status")
    public BLResp changeProductStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long productId = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(productId == null || productId <= 0 || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(
                enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        productService.changeProductStatus(productId, enabled, resp);
        return resp;
    }
}
