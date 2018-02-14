package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.NewProductDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductInfoDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.service.RemoteProductService;
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
    private RemoteProductService remoteProductService;

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
            map.put(Field.CUSTOM, Custom.getById(product.getCustom()).getName());
            map.put(Field.CODE, product.getCode());
            map.put(Field.NAME, product.getName());
            map.put(Field.COST_AMT, NumberUtils.formatAmount(product.getCostAmt()));
            map.put(Field.REMARK, product.getRemark());
            map.put(Field.ENABLED, product.getEnabled());
            map.put(Field.CONTENT, productTxt == null ? "" : productTxt.getContent());
        }
        map.put(Field.CUSTOM_LIST, Custom.getAllList());
        map.put(Field.PROD_TYPE_DICT, ProdType.getProdTypeDict());
        return map;
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
    public void changeProductStatus(Long productId, Integer enabled, BLResp resp)
    {
        ResultDTO resultDTO = remoteProductService.changeProductStatus(productId, enabled);
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

    @Override
    public void getProductList(String keyword, Integer type, Integer custom, Integer status, Page page, ListRes res)
    {
        ListDTO<ProductDTO> dto = remoteProductService.getProductList(keyword, type, custom, status, page);
        res.setTotal(dto.getTotal());
        if(!CollectionUtils.isEmpty(dto.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(dto.getList().size());
            for(ProductDTO o : dto.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, o.getId() + "");
                map.put(Field.ADD_DATE, DateUtils.format(o.getCreateTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.CODE, o.getCode());
                map.put(Field.NAME, o.getName());
                map.put(Field.TYPE, ProdType.getNameById(o.getType()));
                map.put(Field.CUSTOM, o.getCustom());
                map.put(Field.COST_AMT, o.getCostAmt());
                map.put(Field.REMARK, o.getRemark());
                map.put(Field.STATUS, o.getEnabled());
                list.add(map);
            }
            res.setList(list);
        }
    }
}
