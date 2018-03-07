package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.service.RemoteProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService
{
    @Resource
    private RemoteProductService remoteProductService;

    @Override
    public Map<String, Object> getProductInfoData(Long productId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.ID, productId + "");
        ProductDTO productDTO = remoteProductService.getProductInfoData(productId);
        if(productDTO != null)
        {
            map.put(Field.CUSTOM, Custom.getById(productDTO.getCustom()).getName());
            map.put(Field.TYPE, ProdType.getNameById(productDTO.getType()));
            map.put(Field.CODE, productDTO.getProductCode());
            map.put(Field.NAME, productDTO.getName());
            map.put(Field.COST_AMT, NumberUtils.formatAmount(productDTO.getCostAmt()));
            map.put(Field.REMARK, productDTO.getRemark());
            map.put(Field.CONTENT, productDTO.getContent());
            map.put(Field.ENABLED, productDTO.getEnabled());
        }
        return map;
    }

    @Override
    public void editProduct(Long productId, String name, BigDecimal costAmt, String remark, String content,
            Integer enabled, RestResp resp)
    {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setName(name);
        productDTO.setCostAmt(costAmt);
        productDTO.setRemark(remark);
        productDTO.setContent(content);
        productDTO.setEnabled(enabled);
        ResultDTO resultDTO = remoteProductService.editProduct(productDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void changeProductStatus(Long productId, Integer enabled, RestResp resp)
    {
        ResultDTO resultDTO = remoteProductService.changeProductStatus(productId, enabled);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public List<Dict> getProductDict()
    {
        List<Dict> list = new ArrayList<>();
        ListDTO<DictDTO> listDTO = remoteProductService.getProductDict();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictDTO o : listDTO.getList())
            {
                list.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        return list;
    }

    @Override
    public void getProductList(String keyword, Integer type, Integer custom, Integer status, Page page,
            RestListResp res)
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
                map.put(Field.CODE, o.getProductCode());
                map.put(Field.NAME, o.getName());
                map.put(Field.TYPE, ProdType.getNameById(o.getType()));
                map.put(Field.CUSTOM, o.getCustom());
                map.put(Field.COST_AMT, NumberUtils.formatAmount(o.getCostAmt()));
                map.put(Field.REMARK, o.getRemark());
                map.put(Field.STATUS, o.getEnabled());
                list.add(map);
            }
            res.setList(list);
        }
    }
}
