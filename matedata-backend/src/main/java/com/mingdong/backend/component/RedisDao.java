package com.mingdong.backend.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class RedisDao extends RedisBaseDao
{
    /**
     * 缓存客户及产品字典数据
     */
    public void cacheMetadata(Long clientId, String corpName, Long productId, String productName)
    {
        hSet(DB.METADATA, Key.CLIENT, String.valueOf(clientId), corpName);
        hSet(DB.METADATA, Key.PRODUCT, String.valueOf(productId), productName);
    }

    /**
     * 获取指定客户ID对应的企业名称
     */
    public Map<Long, String> getClientCorpName(List<Long> clientIdList)
    {
        Map<Long, String> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(clientIdList))
        {
            String[] ids = new String[clientIdList.size()];
            for(int i = 0; i < clientIdList.size(); i++)
            {
                ids[i] = String.valueOf(clientIdList.get(i));
            }
            List<String> list = hMGet(DB.METADATA, Key.CLIENT, ids);
            for(int i = 0; i < clientIdList.size(); i++)
            {
                map.put(clientIdList.get(i), list.get(i));
            }
        }
        return map;
    }

    /**
     * 获取指定产品ID对应的产品名称
     */
    public Map<Long, String> getProductName(List<Long> productIdList)
    {
        Map<Long, String> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(productIdList))
        {
            String[] ids = new String[productIdList.size()];
            for(int i = 0; i < productIdList.size(); i++)
            {
                ids[i] = String.valueOf(productIdList.get(i));
            }
            List<String> list = hMGet(DB.METADATA, Key.PRODUCT, ids);
            for(int i = 0; i < productIdList.size(); i++)
            {
                map.put(productIdList.get(i), list.get(i));
            }
        }
        return map;
    }

    /**
     * 产品访问量监控
     */
    public void realTimeTraffic(long timestamp, Long productId, Long clientId)
    {
        String s = String.valueOf(timestamp - timestamp % 300);
        hIncrBy(DB.PRODUCT_TRAFFIC, s, String.valueOf(productId), 1);
        hIncrBy(DB.CLIENT_TRAFFIC, s, String.valueOf(clientId), 1);
        hIncrBy(DB.PRODUCT_TRAFFIC, s, Key.ALL_COUNT, 1);
        hIncrBy(DB.CLIENT_TRAFFIC, s, Key.ALL_COUNT, 1);
        sAdd(DB.PRODUCT_CLIENT, s, productId + "," + clientId);
    }

    /**
     * 获取产品下请求的客户数量
     */
    public Map<Long, Integer> readProductClient(long... timestamp)
    {
        Map<Long, Integer> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        for(long item : timestamp)
        {
            keys.add(String.valueOf(item));
        }
        Map<Long, Set<Long>> mapTemp = new HashMap<>();
        Set<Long> setTemp;
        Set set = sUnion(DB.PRODUCT_CLIENT, keys.toArray(new String[0]));
        if(!CollectionUtils.isEmpty(set))
        {
            for(Object item : set)
            {
                String str = String.valueOf(item);
                String[] split = str.split(",");
                Long proId = Long.valueOf(split[0]);
                Long clientId = Long.valueOf(split[1]);
                setTemp = mapTemp.computeIfAbsent(proId, k -> new HashSet<>());
                setTemp.add(clientId);
            }
        }
        mapTemp.forEach((k, v) -> {
            map.put(k, v.size());
        });
        return map;
    }

    /**
     * 获取客户访问量
     */
    public List<Long> readClientTraffic(long timestamp, List<Long> clientIds)
    {
        List<String> clientIdsStr = new ArrayList<>();
        for(Long item : clientIds)
        {
            clientIdsStr.add(String.valueOf(item));
        }
        List countList = hMGet(DB.CLIENT_TRAFFIC, String.valueOf(timestamp), clientIdsStr.toArray(new String[0]));
        List<Long> longCountList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(countList))
        {
            for(Object item : countList)
            {
                longCountList.add(item == null ? 0 : Long.valueOf(String.valueOf(item)));
            }
        }
        return longCountList;
    }

    /**
     * 获取产品访问量
     */
    public List<Long> readProductTraffic(long timestamp, List<Long> productIds)
    {
        List<String> productIdsStr = new ArrayList<>();
        for(Long item : productIds)
        {
            productIdsStr.add(String.valueOf(item));
        }
        List countList = hMGet(DB.PRODUCT_TRAFFIC, String.valueOf(timestamp), productIdsStr.toArray(new String[0]));
        List<Long> longCountList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(countList))
        {
            for(Object item : countList)
            {
                longCountList.add(item == null ? 0 : Long.valueOf(String.valueOf(item)));
            }
        }
        return longCountList;
    }

    /**
     * 获取客户访问总量
     */
    public String readClientTrafficAll(long timestamp)
    {
        return hGet(DB.CLIENT_TRAFFIC, String.valueOf(timestamp), Key.ALL_COUNT);
    }

    /**
     * 获取产品访问总量
     */
    public String readProductTrafficAll(long timestamp)
    {
        return hGet(DB.PRODUCT_TRAFFIC, String.valueOf(timestamp), Key.ALL_COUNT);
    }

    /**
     * 流量数据定时清理
     *
     * @param timestamp 当前时间的Unix时间戳
     */
    public boolean cleanUpTraffic(long timestamp)
    {
        int[] dbs = new int[]{DB.PRODUCT_TRAFFIC, DB.CLIENT_TRAFFIC};
        try
        {
            for(int db : dbs)
            {
                Set allKeys = limitScan(db, 1000);
                List<String> keys = new ArrayList<>();
                String s;
                for(Object key : allKeys)
                {
                    s = String.valueOf(key);
                    if(timestamp - Long.parseLong(s) > (3600 * 24 + 300))
                    {
                        keys.add(s);
                    }
                }
                if(keys.size() > 0)
                {
                    del(db, keys.toArray(new String[keys.size()]));
                }
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * 获取当前时间下的所有请求量
     */
    public Map readProductTrafficHGetAll(long timestamp)
    {
        return hGetAll(DB.PRODUCT_TRAFFIC, String.valueOf(timestamp));
    }

    /**
     * 获取所有请求的产品名称
     */
    public Map<Long, String> getProductNameAll()
    {
        Map<Long, String> map = new HashMap<>();
        Map getMap = hGetAll(DB.METADATA, Key.PRODUCT);
        getMap.forEach((k, v) -> {
            map.put(Long.valueOf(String.valueOf(k)), String.valueOf(v));
        });
        return map;
    }

    /**
     * 实时请求
     */
    public void requestMessage(String host, String productName, String corpName, String msg)
    {
        RequestDetailResDTO requestDetailResDTO = new RequestDetailResDTO();
        requestDetailResDTO.setHost(host);
        requestDetailResDTO.setProductName(productName);
        requestDetailResDTO.setCorpName(corpName);
        requestDetailResDTO.setMsg(msg);
        rPush(DB.TRAFFIC_DETAIL, Key.PRODUCT_TRAFFIC_DETAIL, JSON.toJSONString(requestDetailResDTO));
        lTrim(DB.TRAFFIC_DETAIL, Key.PRODUCT_TRAFFIC_DETAIL, 0, 999);
        rPush(DB.TRAFFIC_DETAIL, Key.CLIENT_TRAFFIC_DETAIL, JSON.toJSONString(requestDetailResDTO));
        lTrim(DB.TRAFFIC_DETAIL, Key.CLIENT_TRAFFIC_DETAIL, 0, 999);
    }

    /**
     * 获取产品实时请求
     */
    public RequestDetailResDTO readProductRequestMessageFirst()
    {
        String value = lPop(DB.TRAFFIC_DETAIL, Key.PRODUCT_TRAFFIC_DETAIL);
        if(!StringUtils.isNullBlank(value))
        {
            return JSON.parseObject(value, RequestDetailResDTO.class);
        }
        return null;
    }

    /**
     * 获取客户实时请求
     */
    public RequestDetailResDTO readClientRequestMessageFirst()
    {
        String value = lPop(DB.TRAFFIC_DETAIL, Key.CLIENT_TRAFFIC_DETAIL);
        if(!StringUtils.isNullBlank(value))
        {
            return JSON.parseObject(value, RequestDetailResDTO.class);
        }
        return null;
    }

    interface DB
    {
        // 元数据 & 字典数据
        int METADATA = 1;
        // 产品请求计数实时监控数据库
        int PRODUCT_TRAFFIC = 2;
        // 客户请求计数实时监控数据库
        int CLIENT_TRAFFIC = 3;
        // 实时请求数据库
        int TRAFFIC_DETAIL = 4;
        // 产品下请求的客户
        int PRODUCT_CLIENT = 5;
    }

    public interface Key
    {
        String PRODUCT = "metadata:product";
        String CLIENT = "metadata:client";
        String ALL_COUNT = "all_count";
        String PRODUCT_TRAFFIC_DETAIL = "product_traffic_detail";
        String CLIENT_TRAFFIC_DETAIL = "client_traffic_detail";
    }
}
