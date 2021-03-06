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
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
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
    public RestListResp getProductList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.TYPE, required = false) Integer type,
            @RequestParam(value = Field.CUSTOM, required = false) Integer custom,
            @RequestParam(value = Field.STATUS, required = false) Integer status,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
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
    public RestResp editClient(@RequestBody ProductVO vo)
    {
        RestResp resp = new RestResp();
        if(vo.getId() == null || StringUtils.isNullBlank(vo.getName()) || vo.getCostAmt() == null ||
                (!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled())))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        productService.editProduct(vo.getId(), vo.getName(), vo.getCostAmt(), vo.getRemark(), vo.getContent(),
                vo.getEnabled(), resp);
        return resp;
    }

    /**
     * 产品 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/product/status")
    public RestResp changeProductStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Long productId = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(productId == null || productId <= 0 || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(
                enabled)))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        productService.changeProductStatus(productId, enabled, resp);
        return resp;
    }
}
