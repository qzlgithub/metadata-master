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
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ProductRpcService;
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
    private CommonRpcService commonRpcService;
    @Resource
    private ProductRpcService productRpcService;

    @Override
    public Map<String, Object> getProductInfoData(Long productId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.ID, productId + "");
        ProductDTO productDTO = productRpcService.getProductInfoData(productId);
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
        ResponseDTO responseDTO = productRpcService.editProduct(productDTO);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public void changeProductStatus(Long productId, Integer enabled, RestResp resp)
    {
        ResponseDTO responseDTO = productRpcService.changeProductStatus(productId, enabled);
        resp.setError(responseDTO.getResult());
    }

    @Override
    public List<Dict> getProductDict()
    {
        ListDTO<Dict> listDTO = commonRpcService.getProductDict();
        List<Dict> dict = listDTO.getList();
        return dict != null ? dict : new ArrayList<>();
    }

    @Override
    public void getProductList(String keyword, Integer type, Integer custom, Integer status, Page page,
            RestListResp res)
    {
        ListDTO<ProductDTO> dto = productRpcService.getProductList(keyword, type, custom, status, page);
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
