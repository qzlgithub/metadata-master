package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.Param;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.DictProductTypeListDTO;
import com.mingdong.core.model.dto.NewProductDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductInfoDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.IDUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
    public void addProdCategory(String code, String name, String remark, BLResp resp)
    {
        DictProductTypeDTO type = new DictProductTypeDTO();
        Date curr = new Date();
        type.setCreateTime(curr);
        type.setUpdateTime(curr);
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(TrueOrFalse.TRUE);
        ResultDTO resultDTO = remoteProductService.addProductType(type);
        resp.result(resultDTO.getResult());
    }

    @Override
    public BLResp editProdCategory(Long productId, String code, String name, String remark)
    {
        BLResp resp = BLResp.build();
        DictProductTypeDTO type = new DictProductTypeDTO();
        type.setId(productId);
        type.setUpdateTime(new Date());
        type.setCode(code);
        type.setName(name);
        type.setRemark(remark);
        type.setEnabled(TrueOrFalse.TRUE);
        ResultDTO resultDTO = remoteProductService.updateDictProductTypeSkipNull(type);
        resp.result(resultDTO.getResult());
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
            map.put(Field.TYPE, ProdType.getById(product.getType()).getName());
            map.put(Field.CUSTOM,Custom.getById(product.getCustom()).getName());
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
        map.put(Field.CUSTOM_LIST, Custom.getAllList());
        map.put(Field.PROD_TYPE_DICT, ProdType.getProdTypeDict());
        return map;
    }

    @Override
    public void addProduct(Integer productType, String code, String name, BigDecimal costAmt, Integer enabled,
            Integer custom, String remark, String content, BLResp resp)
    {
        Long productId = IDUtils.getProductId(param.getNodeId());
        Date curr = new Date();
        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setCreateTime(curr);
        product.setUpdateTime(curr);
        product.setType(productType);
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setRemark(remark);
        product.setEnabled(enabled);
        product.setCustom(custom);
        ProductTxtDTO productTxt = null;
        if(!StringUtils.isNullBlank(content))
        {
            productTxt = new ProductTxtDTO();
            productTxt.setId(productId);
            productTxt.setCreateTime(curr);
            productTxt.setUpdateTime(curr);
            productTxt.setContent(content);
        }
        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setProductDTO(product);
        newProductDTO.setProductTxtDTO(productTxt);
        ResultDTO resultDTO = remoteProductService.addProduct(newProductDTO);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void editProduct(Long id, Integer productType, String code, String name, BigDecimal costAmt, Integer enabled,
            Integer custom, String remark, String content, BLResp resp)
    {
        NewProductDTO newProductDTO = new NewProductDTO();
        Date current = new Date();
        ProductDTO product = new ProductDTO();
        product.setId(id);
        product.setUpdateTime(current);
        product.setType(productType);
        product.setCode(code);
        product.setName(name);
        product.setCostAmt(costAmt);
        product.setEnabled(enabled);
        product.setRemark(remark);
        product.setCustom(custom);
        newProductDTO.setProductDTO(product);
        ProductTxtDTO productTxt = new ProductTxtDTO();
        productTxt.setId(id);
        productTxt.setCreateTime(current);
        productTxt.setUpdateTime(current);
        productTxt.setContent(content);
        newProductDTO.setProductTxtDTO(productTxt);
        ResultDTO resultDTO = remoteProductService.updateProductSkipNull(newProductDTO);
        resp.result(resultDTO.getResult());
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
    public void updateCateStatus(Long id, Integer enabled, BLResp resp)
    {
        ResultDTO resultDTO = remoteProductService.updateDictProductTypeStatusById(id, enabled);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void updateProdStatus(Long id, Integer enabled, BLResp resp)
    {
        ResultDTO resultDTO = remoteProductService.updateProductStatusById(id, enabled);
        resp.result(resultDTO.getResult());
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
        ProductInfoListDTO productInfoListDTO = remoteProductService.getProductInfoList(null, page);
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
                map.put(Field.CUSTOM,productInfo.getCustomName());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        return resp;
    }

    @Override
    public List<Map<String, Object>> getProductInfoListMap(Integer enabled)
    {
        ProductInfoListDTO productInfoListDTO = remoteProductService.getProductInfoList(enabled, null);
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
                map.put(Field.COST_AMT, productInfo.getCostAmt());
                map.put(Field.TYPE_NAME, productInfo.getTypeName());
                list.add(map);
            }
        }
        return list;
    }
}
