package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.configurer.Param;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.DictProductType;
import com.mingdong.bop.domain.entity.Product;
import com.mingdong.bop.domain.entity.ProductInfo;
import com.mingdong.bop.domain.entity.ProductTxt;
import com.mingdong.bop.domain.mapper.DictProductTypeMapper;
import com.mingdong.bop.domain.mapper.ProductInfoMapper;
import com.mingdong.bop.domain.mapper.ProductMapper;
import com.mingdong.bop.domain.mapper.ProductTxtMapper;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.util.IDUtils;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService
{
    @Resource
    private Param param;
    @Resource
    private DictProductTypeMapper dictProductTypeMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductTxtMapper productTxtMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;

    @Override
    public BLResp getProdCategory(Page page)
    {
        BLResp resp = BLResp.build();
        int total = dictProductTypeMapper.countAll();
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());

        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<DictProductType> prodTypeList = dictProductTypeMapper.getAll();
            List<Map<String, Object>> list = new ArrayList<>(prodTypeList.size());
            for(DictProductType type : prodTypeList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, type.getId() + "");
                map.put(Field.CODE, type.getCode());
                map.put(Field.NAME, type.getName());
                map.put(Field.REMARK, type.getRemark());
                map.put(Field.ENABLED, type.getEnabled());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        return resp;
    }

    @Override
    @Transactional
    public void addProdCategory(String code, String name, String remark, BLResp resp)
    {
        DictProductType type = dictProductTypeMapper.findByCode(code);
        if(type != null)
        {
            resp.result(RestResult.CATEGORY_CODE_EXIST);
            return;
        }
        if(code == null)
        {
            resp.result(RestResult.INVALID_PRODUCT_TYPE);
            return;
        }
        if(name == null)
        {
            resp.result(RestResult.INVALID_PRODUCT_NAME);
            return;
        }
        Date curr = new Date();
        type = new DictProductType();
        type.setCreateTime(curr);
        type.setUpdateTime(curr);
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(TrueOrFalse.TRUE);
        dictProductTypeMapper.add(type);
    }

    @Override
    @Transactional
    public BLResp editProdCategory(Long productId, String code, String name, String remark)
    {
        BLResp resp = BLResp.build();
        DictProductType type = dictProductTypeMapper.findById(productId);
        if(type == null)
        {
            return resp.result(RestResult.OBJECT_NOT_FOUND);
        }
        Integer enabled = type.getEnabled();
        type = dictProductTypeMapper.findByCode(code);
        if(type != null && !productId.equals(type.getId()))
        {
            return resp.result(RestResult.CATEGORY_CODE_EXIST);
        }

        type = new DictProductType();
        type.setId(productId);
        type.setUpdateTime(new Date());
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(enabled);
        dictProductTypeMapper.updateSkipNull(type);
        return resp;
    }

    @Override
    public Map<String, Object> getProductInfo(Long productId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.ID, productId + "");
        Product product = productMapper.findById(productId);
        if(product != null)
        {
            ProductTxt productTxt = productTxtMapper.findById(productId);
            map.put(Field.TYPE, product.getTypeId() + "");
            map.put(Field.CODE, product.getCode());
            map.put(Field.NAME, product.getName());
            map.put(Field.COST_AMT, product.getCostAmt());
            map.put(Field.REMARK, product.getRemark());
            map.put(Field.ENABLED, product.getEnabled());
            map.put(Field.CONTENT, productTxt == null ? "" : productTxt.getContent());
        }
        List<DictProductType> dictType = dictProductTypeMapper.getAll();
        List<Map<String, Object>> productType = new ArrayList<>();
        for(DictProductType type : dictType)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, type.getId() + "");
            m.put(Field.NAME, type.getName());
            productType.add(m);
        }
        map.put(Field.PRODUCT_TYPE_DICT, productType);
        return map;
    }

    @Override
    @Transactional
    public void addProduct(Long productType, String code, String name, BigDecimal costAmt, Integer enabled,
            String remark, String content, BLResp resp)
    {
        // 1. 校验产品类型是否有效
        DictProductType type = dictProductTypeMapper.findById(productType);
        if(type == null)
        {
            resp.result(RestResult.INVALID_PRODUCT_TYPE);
            return;
        }
        // 2. 校验产品编码是否重复
        Product product = productMapper.findByCode(code);
        if(product != null)
        {
            resp.result(RestResult.DUPLICATE_PRODUCT_CODE);
            return;
        }
        // 3. 校验产品名是否重复
        Product prod = productMapper.findByName(name);
        if(prod != null)
        {
            resp.result(RestResult.PRODUCT_NAME_EXIST);
            return;
        }
        Long productId = IDUtils.getProductId(param.getNodeId());
        Date curr = new Date();
        if(!StringUtils.isNullBlank(content))
        {
            ProductTxt productTxt = new ProductTxt();
            productTxt.setId(productId);
            productTxt.setCreateTime(curr);
            productTxt.setUpdateTime(curr);
            productTxt.setContent(content);
            productTxtMapper.add(productTxt);
        }
        product = new Product();
        product.setId(productId);
        product.setCreateTime(curr);
        product.setUpdateTime(curr);
        product.setTypeId(productType);
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setRemark(remark);
        product.setEnabled(enabled);
        productMapper.add(product);
    }

    @Override
    @Transactional
    public void editProduct(Long id, Long productType, String code, String name, BigDecimal costAmt, Integer enabled,
            String remark, String content, BLResp resp)
    {
        Product prod = productMapper.findByCode(code);
        if(prod != null)
        {
            resp.result(RestResult.DUPLICATE_PRODUCT_CODE);

        }

        Product product = productMapper.findById(id);
        if(product == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        product.setId(id);
        product.setUpdateTime(current);
        product.setTypeId(productType);
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setEnabled(enabled);
        product.setRemark(remark);
        productMapper.updateById(product);
        ProductTxt productTxt = productTxtMapper.findById(id);
        if(productTxt == null)
        {
            if(!StringUtils.isNullBlank(content))
            {
                productTxt = new ProductTxt();
                productTxt.setId(id);
                productTxt.setCreateTime(current);
                productTxt.setUpdateTime(current);
                productTxt.setContent(content);
                productTxtMapper.add(productTxt);
            }
        }
        else
        {
            productTxt.setUpdateTime(current);
            productTxt.setContent(content);
            productTxtMapper.updateById(productTxt);
        }
    }

    @Override
    public BLResp getProductCategoryInfo(Long id)
    {
        BLResp resp = BLResp.build();
        DictProductType dictProductType = dictProductTypeMapper.findById(id);
        return resp.addData(Field.ID, dictProductType.getId()).addData(Field.CODE, dictProductType.getCode()).addData(
                Field.NAME, dictProductType.getName()).addData(Field.REMARK, dictProductType.getRemark());

    }

    //产品类别启用禁用
    @Override
    @Transactional
    public void updateCateStatus(Long id, Integer enabled, BLResp resp)
    {
        DictProductType productCategory = dictProductTypeMapper.findById(id);
        if(productCategory == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(enabled.equals(productCategory.getEnabled()))
        {
            productCategory.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            productCategory.setUpdateTime(new Date());
            dictProductTypeMapper.updateById(productCategory);
        }
    }

    @Override
    @Transactional
    public void updateProdStatus(Long id, Integer enabled, BLResp resp)
    {
        Product product = productMapper.findById(id);
        if(product == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(enabled.equals(product.getEnabled()))
        {
            product.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            product.setUpdateTime(new Date());
            productMapper.updateById(product);
        }
    }

    @Override
    public List<Map<String, Object>> getProductDict()
    {
        List<Product> productList = productMapper.getListByStatus(TrueOrFalse.TRUE);
        List<Map<String, Object>> list = new ArrayList<>();
        for(Product p : productList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, p.getId() + "");
            map.put(Field.NAME, p.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getProductTypeDict(Integer enabled)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        List<DictProductType> dataList = dictProductTypeMapper.getListByStatus(enabled);
        for(DictProductType o : dataList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId() + "");
            m.put(Field.NAME, o.getName());
            list.add(m);
        }
        return list;
    }

    @Override
    public BLResp getProductInfoList(Page page)
    {
        BLResp resp = BLResp.build();
        int total = productInfoMapper.countAll();
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductInfo> productInfoList = productInfoMapper.getAll();
            List<Map<String, Object>> list = new ArrayList<>(productInfoList.size());
            for(ProductInfo productInfo : productInfoList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, productInfo.getId() + "");
                map.put(Field.CODE, productInfo.getCode());
                map.put(Field.NAME, productInfo.getName());
                map.put(Field.REMARK, productInfo.getRemark());
                map.put(Field.ENABLED, productInfo.getEnabled() + "");
                map.put(Field.TYPE_NAME, productInfo.getTypeName());
                map.put(Field.COST_AMT, productInfo.getCostAmt());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        return resp;
    }
}
