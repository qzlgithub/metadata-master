package com.mingdong.backend.service.rpc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.Field;
import com.mingdong.backend.model.LegendDTO;
import com.mingdong.backend.model.LineDiagramDTO;
import com.mingdong.backend.service.BackendTrafficService;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackendTrafficServiceImpl implements BackendTrafficService
{
    @Resource
    private RedisDao redisDao;

    @Override
    public LineDiagramDTO getStatsClientRequestCache(List<Long> clientIdList, Date date)
    {
        LineDiagramDTO res = new LineDiagramDTO();
        List<String> periodList = getPeriodInOneHour(date);
        res.setxAxis(periodList);
        List<LegendDTO> legendList = new ArrayList<>();
        List<Long> trafficList;
        LegendDTO legend;
        if(CollectionUtils.isEmpty(clientIdList))
        {
            trafficList = redisDao.getClientTraffic(periodList, null);
            legend = new LegendDTO();
            legend.setName("总体");
            legend.setSeries(trafficList);
            legendList.add(legend);
        }
        else
        {
            Map<Long, String> corpNameMap = redisDao.getClientCorpName(clientIdList);
            for(Long clientId : clientIdList)
            {
                trafficList = redisDao.getClientTraffic(periodList, clientId);
                legend = new LegendDTO();
                legend.setName(corpNameMap.get(clientId));
                legend.setSeries(trafficList);
                legendList.add(legend);
            }
        }
        res.setLegendList(legendList);
        return res;
    }

    @Override
    public LineDiagramDTO getStatsProductRequestCache(List<Long> productIdList, Date date)
    {
        LineDiagramDTO res = new LineDiagramDTO();
        List<String> periodList = getPeriodInOneHour(date);
        res.setxAxis(periodList);
        List<LegendDTO> legendList = new ArrayList<>();
        List<Long> trafficList;
        LegendDTO legend;
        if(CollectionUtils.isEmpty(productIdList))
        {
            trafficList = redisDao.getProductTraffic(periodList, null);
            legend = new LegendDTO();
            legend.setName("总体");
            legend.setSeries(trafficList);
            legendList.add(legend);
        }
        else
        {
            Map<Long, String> productNameMap = redisDao.getProductName(productIdList);
            for(Long productId : productIdList)
            {
                trafficList = redisDao.getProductTraffic(periodList, productId);
                legend = new LegendDTO();
                legend.setName(productNameMap.get(productId));
                legend.setSeries(trafficList);
                legendList.add(legend);
            }
        }
        res.setLegendList(legendList);
        return res;
    }

    @Override
    public ResponseDTO getStatsProductRatio(Date date)
    {
        ResponseDTO responseDTO = new ResponseDTO();
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
        List<String> periodList = getPeriodInOneHour(date);
        for(String item : periodList)
        {
            mapTemp = redisDao.readProductTrafficHGetAll(item);
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
        }
        Map<Long, Integer> productClientCount = redisDao.readProductClient(periodList);
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

    private List<String> getPeriodInOneHour(Date date)
    {
        int t = 12;
        long ts = date.getTime() - date.getTime() % 300000;
        List<String> list = new ArrayList<>(t);
        for(int i = t - 1; i >= 0; i--)
        {
            list.add(DateUtils.format(new Date(ts - 300000 * i), "HH:mm"));
        }
        return list;
    }
}
