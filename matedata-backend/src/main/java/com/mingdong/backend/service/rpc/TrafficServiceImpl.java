package com.mingdong.backend.service.rpc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.Field;
import com.mingdong.backend.service.TrafficService;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrafficServiceImpl implements TrafficService
{
    @Resource
    private RedisDao redisDao;

    @Override
    public ResponseDTO getStatsClientRequestCache(List<Long> clientIdList)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        long currentTime = System.currentTimeMillis() / 1000;
        long afterKey = currentTime - currentTime % 300;
        long beforeKey = afterKey - 3600;
        JSONArray legendData = new JSONArray();
        JSONArray xAxisData = new JSONArray();
        JSONArray seriesData = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if(!CollectionUtils.isEmpty(clientIdList))
        {
            Map<Long, JSONArray> mapTemp = new LinkedHashMap<>();
            Map<Long, String> clientMap = redisDao.getClientCorpName(clientIdList);
            for(Long clientId : clientIdList)
            {
                legendData.add(clientMap.get(clientId));
                mapTemp.put(clientId, new JSONArray());
            }
            while(beforeKey <= afterKey)
            {
                xAxisData.add(sdf.format(new Date(beforeKey * 1000)));
                List<Long> list = redisDao.readClientTraffic(beforeKey, clientIdList);
                for(int i = 0; i < clientIdList.size(); i++)
                {
                    mapTemp.get(clientIdList.get(i)).add(list.get(i) == null ? "0" : list.get(i) + "");
                }
                beforeKey += 300;
            }
            mapTemp.forEach((k, v) -> {
                seriesData.add(v);
            });
        }
        else
        {
            legendData.add("总体");
            JSONArray jsonArrayTemp = new JSONArray();
            while(beforeKey <= afterKey)
            {
                xAxisData.add(sdf.format(new Date(beforeKey * 1000)));
                String count = redisDao.readClientTrafficAll(beforeKey);
                jsonArrayTemp.add(StringUtils.isNullBlank(count) ? "0" : count);
                beforeKey += 300;
            }
            seriesData.add(jsonArrayTemp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Field.LEGEND_DATA, legendData);
        jsonObject.put(Field.X_AXIS_DATA, xAxisData);
        jsonObject.put(Field.SERIES_DATA, seriesData);
        responseDTO.addExtra(Field.DATA, jsonObject.toJSONString());
        return responseDTO;
    }

    @Override
    public ResponseDTO getStatsProductRequestCache(List<Long> productIdList)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        long currentTime = System.currentTimeMillis() / 1000;
        long afterKey = currentTime - currentTime % 300;
        long beforeKey = afterKey - 3600;
        JSONArray legendData = new JSONArray();
        JSONArray xAxisData = new JSONArray();
        JSONArray seriesData = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if(!CollectionUtils.isEmpty(productIdList))
        {
            Map<Long, JSONArray> mapTemp = new LinkedHashMap<>();
            Map<Long, String> prodMap = redisDao.getProductName(productIdList);
            for(Long productId : productIdList)
            {
                legendData.add(prodMap.get(productId));
                mapTemp.put(productId, new JSONArray());
            }
            while(beforeKey <= afterKey)
            {
                xAxisData.add(sdf.format(new Date(beforeKey * 1000)));
                List<Long> list = redisDao.readProductTraffic(beforeKey, productIdList);
                for(int i = 0; i < productIdList.size(); i++)
                {
                    mapTemp.get(productIdList.get(i)).add(list.get(i) == null ? "0" : list.get(i) + "");
                }
                beforeKey += 300;
            }
            mapTemp.forEach((k, v) -> seriesData.add(v));
        }
        else
        {
            legendData.add("总体");
            JSONArray jsonArrayTemp = new JSONArray();
            while(beforeKey <= afterKey)
            {
                xAxisData.add(sdf.format(new Date(beforeKey * 1000)));
                String count = redisDao.readProductTrafficAll(beforeKey);
                jsonArrayTemp.add(StringUtils.isNullBlank(count) ? "0" : count);
                beforeKey += 300;
            }
            seriesData.add(jsonArrayTemp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Field.LEGEND_DATA, legendData);
        jsonObject.put(Field.X_AXIS_DATA, xAxisData);
        jsonObject.put(Field.SERIES_DATA, seriesData);
        responseDTO.addExtra(Field.DATA, jsonObject.toJSONString());
        return responseDTO;
    }

    @Override
    public ResponseDTO getStatsProductRatio()
    {
        ResponseDTO responseDTO = new ResponseDTO();
        long currentTime = System.currentTimeMillis() / 1000;
        long afterKey = currentTime - currentTime % 300;
        long beforeKey = afterKey - 3600;
        JSONArray legendData = new JSONArray();
        JSONArray seriesData = new JSONArray();
        JSONArray jsonArrayTemp;
        JSONArray jsonArrayTemp2;
        String productType = ProductType.INTERNET_FINANCE.getName();
        legendData.add(productType);
        long requestAllCount = 0;
        //产品请求量
        Map<String, Map<Long, Long>> productTypeRequestCount = new HashMap<>();//key productType key2 productId
        //产品请求占比
        Map<String, Map<Long, Integer>> productTypeRequestPro = new HashMap<>();//key productType key2 productId
        Map<Long, Long> productRequestCountTemp;//key productId
        Map<Long, Integer> productRequestProTemp;//key productId
        Map<String, String> mapTemp;
        Map<Long, String> prodMap = redisDao.getProductNameAll();
        List<Long> keys = new ArrayList<>();
        while(beforeKey <= afterKey)
        {
            keys.add(beforeKey);
            mapTemp = redisDao.readProductTrafficHGetAll(beforeKey);
            for(Map.Entry<String, String> entry : mapTemp.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                if(RedisDao.Key.ALL_COUNT.equals(key))
                {
                    requestAllCount += Long.valueOf(value);
                }
                else
                {
                    Long longKey = Long.valueOf(key);
                    productRequestCountTemp = productTypeRequestCount.computeIfAbsent(productType,
                            k -> new HashMap<>());
                    Long count = productRequestCountTemp.computeIfAbsent(longKey, k -> 0l);
                    productRequestCountTemp.put(longKey, count += Long.valueOf(value));
                }
            }
            beforeKey += 300;
        }
        Map<Long, Integer> productClientCount = redisDao.readProductClient(
                keys.stream().mapToLong(t -> t.longValue()).toArray());
        for(Map.Entry<String, Map<Long, Long>> entry : productTypeRequestCount.entrySet())
        {
            String key = entry.getKey();
            productRequestCountTemp = entry.getValue();
            for(Map.Entry<Long, Long> entry2 : productRequestCountTemp.entrySet())
            {
                productRequestProTemp = productTypeRequestPro.computeIfAbsent(key, k -> new HashMap<>());
                productRequestProTemp.put(entry2.getKey(), (int) (entry2.getValue() * 100 / requestAllCount));
            }
        }
        for(Map.Entry<String, Map<Long, Integer>> entry : productTypeRequestPro.entrySet())
        {
            String key = entry.getKey();
            productRequestProTemp = entry.getValue();
            jsonArrayTemp = new JSONArray();
            productRequestCountTemp = productTypeRequestCount.get(key);
            for(Map.Entry<Long, Integer> entry2 : productRequestProTemp.entrySet())
            {
                Long productId = entry2.getKey();
                Integer intValue = entry2.getValue();
                jsonArrayTemp2 = new JSONArray();
                jsonArrayTemp2.add(productRequestCountTemp.get(productId) + "");
                jsonArrayTemp2.add(intValue + "");
                jsonArrayTemp2.add(
                        productClientCount.get(productId) != null ? productClientCount.get(productId) + "" : 99);
                jsonArrayTemp2.add(prodMap.get(productId));
                jsonArrayTemp2.add(productType);
                jsonArrayTemp.add(jsonArrayTemp2);
            }
            seriesData.add(jsonArrayTemp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Field.LEGEND_DATA, legendData);
        jsonObject.put(Field.SERIES_DATA, seriesData);
        responseDTO.addExtra(Field.DATA, jsonObject.toJSONString());
        return responseDTO;
    }

    @Override
    public ListDTO<RequestDetailResDTO> getProductRequestList(Integer size)
    {
        ListDTO<RequestDetailResDTO> listDTO = new ListDTO<>();
        List<RequestDetailResDTO> list = new ArrayList<>();
        listDTO.setList(list);
        RequestDetailResDTO requestDetailResDTO;
        for(int i = 0; i < size; i++)
        {
            requestDetailResDTO = redisDao.readProductRequestMessageFirst();
            if(requestDetailResDTO == null)
            {
                break;
            }
            list.add(requestDetailResDTO);
        }
        return listDTO;
    }

    @Override
    public ListDTO<RequestDetailResDTO> getClientRequestList(Integer size)
    {
        ListDTO<RequestDetailResDTO> listDTO = new ListDTO<>();
        List<RequestDetailResDTO> list = new ArrayList<>();
        listDTO.setList(list);
        RequestDetailResDTO requestDetailResDTO;
        for(int i = 0; i < size; i++)
        {
            requestDetailResDTO = redisDao.readClientRequestMessageFirst();
            if(requestDetailResDTO == null)
            {
                break;
            }
            list.add(requestDetailResDTO);
        }
        return listDTO;
    }
}
