package com.mingdong.bop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.mingdong.backend.service.BackendStatsService;
import com.mingdong.backend.service.BackendTrafficService;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ProductService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ProductReqDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.StatsProductRequestResDTO;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.core.util.DateCalculateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Resource
    private BackendTrafficService backendTrafficService;
    @Resource
    private BackendStatsService backendStatsService;

    @Override
    public Map<String, Object> getProductInfoData(Long productId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.ID, productId + "");
        ProductResDTO productResDTO = productRpcService.getProductInfoData(productId);
        if(productResDTO != null)
        {
            map.put(Field.CUSTOM, Custom.getById(productResDTO.getCustom()).getName());
            map.put(Field.TYPE, ProductType.getNameById(productResDTO.getType()));
            map.put(Field.CODE, productResDTO.getProductCode());
            map.put(Field.NAME, productResDTO.getName());
            map.put(Field.COST_AMT, NumberUtils.formatAmount(productResDTO.getCostAmt()));
            map.put(Field.REMARK, productResDTO.getRemark());
            map.put(Field.CONTENT, productResDTO.getContent());
            map.put(Field.ENABLED, productResDTO.getEnabled());
        }
        return map;
    }

    @Override
    public void editProduct(Long productId, String name, BigDecimal costAmt, String remark, String content,
            Integer enabled, RestResp resp)
    {
        ProductReqDTO reqDTO = new ProductReqDTO();
        reqDTO.setId(productId);
        reqDTO.setName(name);
        reqDTO.setCostAmt(costAmt);
        reqDTO.setRemark(remark);
        reqDTO.setContent(content);
        reqDTO.setEnabled(enabled);
        ResponseDTO responseDTO = productRpcService.editProduct(reqDTO);
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
        ListDTO<ProductResDTO> dto = productRpcService.getProductList(keyword, type, custom, status, page);
        res.setTotal(dto.getTotal());
        if(!CollectionUtils.isEmpty(dto.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(dto.getList().size());
            for(ProductResDTO o : dto.getList())
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, o.getId() + "");
                map.put(Field.ADD_DATE, DateUtils.format(o.getCreateTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.CODE, o.getProductCode());
                map.put(Field.NAME, o.getName());
                map.put(Field.TYPE, ProductType.getNameById(o.getType()));
                map.put(Field.CUSTOM, o.getCustom());
                map.put(Field.COST_AMT, NumberUtils.formatAmount(o.getCostAmt()));
                map.put(Field.REMARK, o.getRemark());
                map.put(Field.STATUS, o.getEnabled());
                list.add(map);
            }
            res.setList(list);
        }
    }

    @Override
    public void getAllProduct(RestListResp res)
    {
        ListDTO<ProductResDTO> listDTO = productRpcService.getAllProduct();
        List<ProductResDTO> dataList = listDTO.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dataList))
        {
            Map<String, Object> map;
            for(ProductResDTO item : dataList)
            {
                map = new HashMap<>();
                map.put(Field.PRODUCT_ID, item.getId() + "");
                map.put(Field.NAME, item.getName());
                list.add(map);
            }
        }
        res.setList(list);
    }

    @Override
    public void getStatsProductRequestCache(List<Long> productIdList, RestResp res)
    {
        ResponseDTO responseDTO = backendTrafficService.getStatsProductRequestCache(productIdList);
        String jsonStr = responseDTO.getExtradata().get(Field.DATA);
        res.addData(Field.DATA, jsonStr);
    }

    @Override
    public void getProductRatio(RestResp res)
    {
        ResponseDTO responseDTO = backendTrafficService.getStatsProductRatio();
        String jsonStr = responseDTO.getExtradata().get(Field.DATA);
        res.addData(Field.DATA, jsonStr);
    }

    @Override
    public void getProductRequestList(RestListResp res)
    {
        ListDTO<RequestDetailResDTO> listDTO = backendTrafficService.getProductRequestList(30);
        List<RequestDetailResDTO> list = listDTO.getList();
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list))
        {
            Map<String, Object> map;
            for(RequestDetailResDTO item : list)
            {
                map = new HashMap<>();
                map.put(Field.HOST, item.getHost());
                map.put(Field.PRODUCT_NAME, item.getProductName());
                map.put(Field.MSG, item.getMsg());
                mapList.add(map);
            }
        }
        res.setList(mapList);
    }

    @Override
    public void getProductTraffic(Page page, RestResp res)
    {
        ListDTO<ProductResDTO> listDTO = productRpcService.getProductList(null, null, null, null, page);
        List<ProductResDTO> dataList = listDTO.getList();
        res.addData(Field.PAGES, page.getPages(listDTO.getTotal()));
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Long> productIds = new ArrayList<>();
            for(ProductResDTO item : dataList)
            {
                productIds.add(item.getId());
            }
            Date date = new Date();
            Date afterDate = DateCalculateUtils.getCurrentDate(date);
            Date beforeDate = DateCalculateUtils.getBeforeDayDate(date, 7, true);
            ListDTO<StatsProductRequestResDTO> requestResDTOListDTO = backendStatsService.getProductTrafficByProductIds(
                    productIds, beforeDate, afterDate);
            List<StatsProductRequestResDTO> list = requestResDTOListDTO.getList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            JSONArray productData = new JSONArray();
            JSONArray xAxisData = new JSONArray();
            JSONArray seriesData = new JSONArray();
            JSONArray jsonArrayTemp;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beforeDate);
            while(beforeDate.before(afterDate))
            {
                xAxisData.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                beforeDate = calendar.getTime();
            }
            Map<Long, Map<String, Long>> map = new HashMap<>();//key productId
            Map<String, Long> mapTemp;//key yyyy-MM-dd
            if(!CollectionUtils.isEmpty(list))
            {
                for(StatsProductRequestResDTO item : list)
                {
                    mapTemp = map.computeIfAbsent(item.getProductId(), k -> new HashMap<>());
                    String format = sdf.format(item.getStatsDate());
                    Long count = mapTemp.computeIfAbsent(format, k -> 0l);
                    mapTemp.put(format, count + item.getRequest());
                }
            }
            for(ProductResDTO item : dataList)
            {
                productData.add(item.getName());
                mapTemp = map.get(item.getId());
                if(mapTemp == null)
                {
                    mapTemp = new HashMap<>();
                }
                jsonArrayTemp = new JSONArray();
                for(int i = 0; i < xAxisData.size(); i++)
                {
                    String dateStr = xAxisData.getString(i);
                    Long count = mapTemp.get(dateStr);
                    jsonArrayTemp.add(count == null ? 0 : count);
                }
                seriesData.add(jsonArrayTemp);
            }
            res.addData(Field.PRODUCT_DATA, productData);
            res.addData(Field.X_AXIS_DATA, xAxisData);
            res.addData(Field.SERIES_DATA, seriesData);
        }
    }

}
