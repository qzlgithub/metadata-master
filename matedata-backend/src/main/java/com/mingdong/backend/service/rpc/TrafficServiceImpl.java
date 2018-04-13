package com.mingdong.backend.service.rpc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.Field;
import com.mingdong.backend.service.TrafficService;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.model.dto.response.ResponseDTO;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrafficServiceImpl implements TrafficService
{
    @Resource
    private RedisDao redisDao;

    @Override
    public void cleanTraffic(Date date)
    {
        redisDao.cleanUpTraffic(date.getTime() / 1000);
    }

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
                List<String> list = redisDao.readClientTraffic(beforeKey, clientIdList);
                for(int i = 0; i < clientIdList.size(); i++)
                {
                    mapTemp.get(clientIdList.get(i)).add(list.get(i) == null ? "0" : list.get(i));
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
                List<String> list = redisDao.readProductTraffic(beforeKey, productIdList);
                for(int i = 0; i < productIdList.size(); i++)
                {
                    mapTemp.get(productIdList.get(i)).add(list.get(i) == null ? "0" : list.get(i));
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
}
