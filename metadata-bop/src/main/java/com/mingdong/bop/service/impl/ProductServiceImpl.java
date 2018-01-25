package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.Param;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.DictProductTypeListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductInfoDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.IDUtils;
import org.apache.commons.collections4.CollectionUtils;
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
    private RemoteProductService remoteProductService;

    @Override
    public BLResp getProdCategory(Page page)
    {
        BLResp resp = BLResp.build();
        DictProductTypeListDTO dictProductTypeList = remoteProductService.getDictProductTypeList(null, page);
        resp.addData(Field.TOTAL, dictProductTypeList.getTotal());
        resp.addData(Field.PAGES, dictProductTypeList.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<DictProductTypeDTO> prodTypeList = dictProductTypeList.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(prodTypeList))
        {
            for(DictProductTypeDTO type : prodTypeList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, type.getId() + "");
                map.put(Field.CODE, type.getCode());
                map.put(Field.NAME, type.getName());
                map.put(Field.REMARK, type.getRemark());
                map.put(Field.ENABLED, type.getEnabled());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        return resp;
    }

    @Override
    @Transactional
    public void addProdCategory(String code, String name, String remark, BLResp resp)
    {
        DictProductTypeDTO type = remoteProductService.getDictProductTypeByCode(code);
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
        type = new DictProductTypeDTO();
        type.setCreateTime(curr);
        type.setUpdateTime(curr);
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(TrueOrFalse.TRUE);
        remoteProductService.saveDictProductType(type);
    }

    @Override
    @Transactional
    public BLResp editProdCategory(Long productId, String code, String name, String remark)
    {
        BLResp resp = BLResp.build();
        DictProductTypeDTO type = remoteProductService.getDictProductTypeById(productId);
        if(type == null)
        {
            return resp.result(RestResult.OBJECT_NOT_FOUND);
        }
        Integer enabled = type.getEnabled();
        type = remoteProductService.getDictProductTypeByCode(code);
        if(type != null && !productId.equals(type.getId()))
        {
            return resp.result(RestResult.CATEGORY_CODE_EXIST);
        }

        type = new DictProductTypeDTO();
        type.setId(productId);
        type.setUpdateTime(new Date());
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(enabled);
        remoteProductService.updateDictProductTypeSkipNull(type);
        return resp;
    }

    @Override
    public Map<String, Object> getProductInfo(Long productId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.ID, productId + "");
        ProductDTO product = remoteProductService.getProductById(productId);
        if(product != null)
        {
            ProductTxtDTO productTxt = remoteProductService.getProductTxtById(productId);
            map.put(Field.TYPE, product.getTypeId() + "");
            map.put(Field.CODE, product.getCode());
            map.put(Field.NAME, product.getName());
            map.put(Field.COST_AMT, product.getCostAmt());
            map.put(Field.REMARK, product.getRemark());
            map.put(Field.ENABLED, product.getEnabled());
            map.put(Field.CONTENT, productTxt == null ? "" : productTxt.getContent());
        }
        DictProductTypeListDTO dictProductTypeList = remoteProductService.getDictProductTypeList(null, null);
        List<DictProductTypeDTO> dictType = dictProductTypeList.getDataList();
        List<Map<String, Object>> productType = new ArrayList<>();
        for(DictProductTypeDTO type : dictType)
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
        DictProductTypeDTO type = remoteProductService.getDictProductTypeById(productType);
        if(type == null)
        {
            resp.result(RestResult.INVALID_PRODUCT_TYPE);
            return;
        }
        // 2. 校验产品编码是否重复
        ProductDTO product = remoteProductService.getProductByCode(code);
        if(product != null)
        {
            resp.result(RestResult.DUPLICATE_PRODUCT_CODE);
            return;
        }
        // 3. 校验产品名是否重复
        ProductDTO prod = remoteProductService.getProductByName(name);
        if(prod != null)
        {
            resp.result(RestResult.PRODUCT_NAME_EXIST);
            return;
        }
        Long productId = IDUtils.getProductId(param.getNodeId());
        Date curr = new Date();
        if(!StringUtils.isNullBlank(content))
        {
            ProductTxtDTO productTxt = new ProductTxtDTO();
            productTxt.setId(productId);
            productTxt.setCreateTime(curr);
            productTxt.setUpdateTime(curr);
            productTxt.setContent(content);
            remoteProductService.saveProductTxt(productTxt);
        }
        product = new ProductDTO();
        product.setId(productId);
        product.setCreateTime(curr);
        product.setUpdateTime(curr);
        product.setTypeId(productType.intValue());
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setRemark(remark);
        product.setEnabled(enabled);
        remoteProductService.saveProduct(product);
    }

    @Override
    @Transactional
    public void editProduct(Long id, Long productType, String code, String name, BigDecimal costAmt, Integer enabled,
            String remark, String content, BLResp resp)
    {
        ProductDTO prod = remoteProductService.getProductByCode(code);
        if(prod != null && !id.equals(prod.getId()))
        {
            resp.result(RestResult.DUPLICATE_PRODUCT_CODE);

        }
        ProductDTO product = remoteProductService.getProductById(id);
        if(product == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        product.setId(id);
        product.setUpdateTime(current);
        product.setTypeId(productType.intValue());
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setEnabled(enabled);
        product.setRemark(remark);
        remoteProductService.updateProductById(product);
        ProductTxtDTO productTxt = remoteProductService.getProductTxtById(id);
        if(productTxt == null)
        {
            if(!StringUtils.isNullBlank(content))
            {
                productTxt = new ProductTxtDTO();
                productTxt.setId(id);
                productTxt.setCreateTime(current);
                productTxt.setUpdateTime(current);
                productTxt.setContent(content);
                remoteProductService.saveProductTxt(productTxt);
            }
        }
        else
        {
            productTxt.setUpdateTime(current);
            productTxt.setContent(content);
            remoteProductService.updateProductTxtById(productTxt);
        }
    }

    @Override
    public BLResp getProductCategoryInfo(Long id)
    {
        BLResp resp = BLResp.build();
        DictProductTypeDTO dictProductType = remoteProductService.getDictProductTypeById(id);
        resp.addData(Field.ID, dictProductType.getId());
        resp.addData(Field.CODE, dictProductType.getCode());
        resp.addData(Field.NAME, dictProductType.getName());
        resp.addData(Field.REMARK, dictProductType.getRemark());
        return resp;

    }

    //产品类别启用禁用
    @Override
    @Transactional
    public void updateCateStatus(Long id, Integer enabled, BLResp resp)
    {
        DictProductTypeDTO productCategory = remoteProductService.getDictProductTypeById(id);
        if(productCategory == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(enabled.equals(productCategory.getEnabled()))
        {
            productCategory.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            productCategory.setUpdateTime(new Date());
            remoteProductService.updateDictProductTypeById(productCategory);
        }
    }

    @Override
    @Transactional
    public void updateProdStatus(Long id, Integer enabled, BLResp resp)
    {
        ProductDTO product = remoteProductService.getProductById(id);
        if(product == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(enabled.equals(product.getEnabled()))
        {
            product.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            product.setUpdateTime(new Date());
            remoteProductService.updateProductById(product);
        }
    }

    @Override
    public List<Map<String, Object>> getProductDict()
    {
        ProductListDTO productListByStatus = remoteProductService.getProductListByStatus(TrueOrFalse.TRUE);
        List<ProductDTO> productList = productListByStatus.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        for(ProductDTO p : productList)
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
        DictProductTypeListDTO dictProductTypeList = remoteProductService.getDictProductTypeList(enabled, null);
        List<DictProductTypeDTO> dataList = dictProductTypeList.getDataList();
        for(DictProductTypeDTO o : dataList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.NAME, o.getName());
            m.put(Field.ID, o.getId() + "");
            list.add(m);
        }
        return list;
    }

    @Override
    public BLResp getProductInfoList(Page page)
    {
        BLResp resp = BLResp.build();
        ProductInfoListDTO productInfoListDTO = remoteProductService.getProductInfoList(page);
        resp.addData(Field.TOTAL, productInfoListDTO.getTotal());
        resp.addData(Field.PAGES, productInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ProductInfoDTO> productInfoList = productInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(productInfoList.size());
        if(CollectionUtils.isNotEmpty(productInfoList))
        {
            for(ProductInfoDTO productInfo : productInfoList)
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
        }
        resp.addData(Field.LIST, list);
        return resp;
    }
}
