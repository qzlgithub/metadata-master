package com.mingdong.backend.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.domain.entity.WarningSetting;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import com.mingdong.core.model.dto.response.WarningManageDetailResDTO;
import com.mingdong.core.util.DateCalculateUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class RedisDao extends RedisBaseDao
{
    // 近24小时分段流量监视
    private static String currHourMin;

    private String getRequestHourMin(long timestamp)
    {
        Date date = new Date(timestamp - timestamp % 300000);
        return DateUtils.format(date, "HH:mm");
    }

    private List<String> getPeriodInOneDay()
    {
        int t = 288;
        Date date = new Date();
        long ts = date.getTime() - date.getTime() % 300000;
        List<String> list = new ArrayList<>(t);
        for(int i = t - 1; i >= 0; i--)
        {
            list.add(DateUtils.format(new Date(ts - 300000 * i), "HH:mm"));
        }
        return list;
    }

    /**
     * 获取有效时间
     */
    private List<String> validTimeHourMin()
    {
        List<String> list = new ArrayList<>();
        Long lastTimestamp = getLastTimestamp5min();
        if(lastTimestamp == null)
        {
            return list;
        }
        Date lastTime = new Date(lastTimestamp);
        Date currentTime = new Date();
        long currentTime5min = currentTime.getTime() - currentTime.getTime() % 300000;
        int dayDif = DateCalculateUtils.getBetweenDayDif(lastTime, currentTime);
        if(dayDif == 0)
        {
            List<String> periodInOneDay = getPeriodInOneDay();
            Calendar lastCalendar = Calendar.getInstance();
            lastCalendar.setTimeInMillis(lastTimestamp);
            while(lastCalendar.getTimeInMillis() <= currentTime5min)
            {
                lastCalendar.add(Calendar.MINUTE, 5);
                periodInOneDay.remove(getRequestHourMin(lastCalendar.getTimeInMillis()));
            }
            list.addAll(periodInOneDay);
        }
        else if(dayDif == 1)
        {
            Calendar lastCalendar = Calendar.getInstance();
            lastCalendar.setTimeInMillis(lastTimestamp);
            lastCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if(currentTime5min < lastCalendar.getTimeInMillis())
            {
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTimeInMillis(currentTime5min);
                while(currentCalendar.getTimeInMillis() <= lastCalendar.getTimeInMillis())
                {
                    currentCalendar.add(Calendar.MINUTE, 5);
                    list.add(getRequestHourMin(currentCalendar.getTimeInMillis()));
                }
            }
        }
        return list;
    }

    /**
     * 缓存客户及产品字典数据
     */
    public void cacheMetadata(Long clientId, String corpName, Long productId, String productName)
    {
        hSet(DB.METADATA, Key.CLIENT, String.valueOf(clientId), corpName);
        hSet(DB.METADATA, Key.PRODUCT, String.valueOf(productId), productName);
    }

    /**
     * 获取缓存中所有的客户字典数据
     */
    public Map<String, String> getAllClient()
    {
        return hGetAll(DB.METADATA, Key.CLIENT);
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
    public void realTimeTraffic(String hourMin, Long productId, Long clientId, String city)
    {
        checkCurrHourMin(hourMin);
        hIncrBy(DB.PRODUCT_TRAFFIC, hourMin, String.valueOf(productId), 1);
        hIncrBy(DB.CLIENT_TRAFFIC, hourMin, String.valueOf(clientId), 1);
        hIncrBy(DB.CLIENT_TRAFFIC_CITY, hourMin, city, 1);
        hIncrBy(DB.PRODUCT_TRAFFIC, hourMin, Key.ALL_COUNT, 1);
        hIncrBy(DB.CLIENT_TRAFFIC, hourMin, Key.ALL_COUNT, 1);
        sAdd(DB.PRODUCT_CLIENT, hourMin, productId + "," + clientId);
    }

    private void checkCurrHourMin(String realHourMin)
    {
        if(realHourMin.equals(currHourMin))
        {
            return;
        }
        synchronized(this)
        {
            if(!realHourMin.equals(currHourMin))
            {
                checkCurrHourMinNoLock(realHourMin);
                currHourMin = realHourMin;
            }
        }
    }

    private void checkCurrHourMinNoLock(String realHourMin)
    {
        del(DB.PRODUCT_TRAFFIC, realHourMin);
        del(DB.CLIENT_TRAFFIC, realHourMin);
        del(DB.CLIENT_TRAFFIC_CITY, realHourMin);
        del(DB.PRODUCT_CLIENT, realHourMin);
    }

    /**
     * 获取产品下请求的客户数量
     */
    public Map<Long, Integer> readProductClient(List<String> periodList)
    {
        List<String> validTimeHourMin = validTimeHourMin();
        Map<Long, Integer> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        for(String item : periodList)
        {
            if(validTimeHourMin.contains(item))
            {
                keys.add(item);
            }
        }
        if(keys.size() == 0)
        {
            return map;
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
     * 获取当前时间下的所有请求量
     */
    public Map readProductTrafficHGetAll(String period)
    {
        List<String> validTimeHourMin = validTimeHourMin();
        if(!validTimeHourMin.contains(period))
        {
            return new HashMap<String, String>();
        }
        return hGetAll(DB.PRODUCT_TRAFFIC, period);
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
    public void requestMessage(Long timestamp, String host, String productName, String corpName, String msg)
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
        synchronized(this)
        {
            Long lastTimestamp = getLastTimestamp5min();
            Long currentTimestamp5min = timestamp - timestamp % 300000;
            if(lastTimestamp != null)
            {
                Long lastTimestamp5min = lastTimestamp - lastTimestamp % 300000;
                if((currentTimestamp5min - lastTimestamp5min) >= 600000)
                {
                    Calendar lastCalendar = Calendar.getInstance();
                    lastCalendar.setTimeInMillis(lastTimestamp5min);
                    int number = (int) ((currentTimestamp5min - lastTimestamp5min) / 300000) - 1;
                    for(int i = 0; i < number; i++)
                    {
                        lastCalendar.add(Calendar.MINUTE, 5);
                        checkCurrHourMinNoLock(getRequestHourMin(lastCalendar.getTimeInMillis()));
                    }
                }
            }
            set(DB.TRAFFIC_DETAIL, Key.REQUEST_LAST_TIME, String.valueOf(currentTimestamp5min));
        }
    }

    private Long getLastTimestamp5min()
    {
        String lastTimestampStr = get(DB.TRAFFIC_DETAIL, Key.REQUEST_LAST_TIME);
        if(!StringUtils.isNullBlank(lastTimestampStr))
        {
            return Long.valueOf(lastTimestampStr);
        }
        return null;
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

    /**
     * 获取指定产品在某一时间区间内的实时流量
     */
    public List<Long> getProductTraffic(List<String> periodList, Long productId)
    {
        List<String> validTimeHourMin = validTimeHourMin();
        List<Long> list = new ArrayList<>(periodList.size());
        for(String p : periodList)
        {
            if(validTimeHourMin.contains(p))
            {
                String s = hGet(DB.PRODUCT_TRAFFIC, p, productId == null ? Key.ALL_COUNT : String.valueOf(productId));
                list.add(s == null ? 0L : Long.valueOf(s));
            }
            else
            {
                list.add(0L);
            }
        }
        return list;
    }

    /**
     * 获取指定客户在某一时间区间内的实时流量
     */
    public List<Long> getClientTraffic(List<String> periodList, Long clientId)
    {
        List<String> validTimeHourMin = validTimeHourMin();
        List<Long> list = new ArrayList<>(periodList.size());
        for(String p : periodList)
        {
            if(validTimeHourMin.contains(p))
            {
                String s = hGet(DB.CLIENT_TRAFFIC, p, clientId == null ? Key.ALL_COUNT : String.valueOf(clientId));
                list.add(s == null ? 0L : Long.valueOf(s));
            }
            else
            {
                list.add(0L);
            }
        }
        return list;
    }

    /**
     * 获取某一时间区间内的实时流量
     */
    public Map<String, Long> getClientTrafficCity(List<String> periodList)
    {
        List<String> validTimeHourMin = validTimeHourMin();
        Map<String, Long> maoCount = new HashMap<>();
        for(String p : periodList)
        {
            if(validTimeHourMin.contains(p))
            {
                Map map = hGetAll(DB.CLIENT_TRAFFIC_CITY, p);
                if(map != null)
                {
                    map.forEach((key, val) -> {
                        String strKey = String.valueOf(key);
                        Long count = maoCount.computeIfAbsent(strKey, k -> 0l);
                        maoCount.put(strKey, count += Long.valueOf(String.valueOf(val)));
                    });
                }
            }
        }
        return maoCount;
    }

    /**
     * 缓存警告设置
     */
    public void cacheWarningSetting(WarningSetting warningSetting)
    {
        set(DB.TRAFFIC_DETAIL, "warning:" + warningSetting.getCode(), JSON.toJSONString(warningSetting));
    }

    public WarningSetting getWarningSetting(String warningCode)
    {
        String value = get(DB.TRAFFIC_DETAIL, warningCode);
        if(!StringUtils.isNullBlank(value))
        {
            return JSON.parseObject(value, WarningSetting.class);
        }
        return null;
    }

    /**
     * 缓存警报详细信息 key productId
     */
    public Long cacheWarningProduct(WarningManageDetailResDTO warningManageDetailResDTO)
    {
        Long count = hIncrBy(DB.TRAFFIC_DETAIL, Key.ERROR_ITEMS,
                String.valueOf(warningManageDetailResDTO.getProductId()), 1);
        rPush(DB.TRAFFIC_DETAIL, String.valueOf(warningManageDetailResDTO.getProductId()),
                JSON.toJSONString(warningManageDetailResDTO));
        return count;
    }

    /**
     * 缓存警报详细信息 key clientId+","+productId
     */
    public Long cacheWarningClient(WarningManageDetailResDTO warningManageDetailResDTO)
    {
        Long count = hIncrBy(DB.TRAFFIC_DETAIL, Key.ERROR_ITEMS,
                warningManageDetailResDTO.getClientId() + "," + warningManageDetailResDTO.getProductId(), 1);
        rPush(DB.TRAFFIC_DETAIL,
                warningManageDetailResDTO.getClientId() + "," + warningManageDetailResDTO.getProductId(),
                JSON.toJSONString(warningManageDetailResDTO));
        return count;
    }

    /**
     * 获取警报详细
     */
    public List<WarningManageDetailResDTO> getWarningManageDetailListByProductId(Long productId)
    {
        List<WarningManageDetailResDTO> list = new ArrayList<>();
        boolean b = true;
        while(b)
        {
            String value = lPop(DB.TRAFFIC_DETAIL, String.valueOf(productId));
            if(!StringUtils.isNullBlank(value))
            {
                list.add(JSON.parseObject(value, WarningManageDetailResDTO.class));
            }
            else
            {
                b = false;
            }
        }
        return list;
    }

    /**
     * 获取警报详细
     */
    public List<WarningManageDetailResDTO> getWarningManageDetailListByClientIdAndProductId(Long clientId,
            Long productId)
    {
        List<WarningManageDetailResDTO> list = new ArrayList<>();
        boolean b = true;
        while(b)
        {
            String value = lPop(DB.TRAFFIC_DETAIL, clientId + "," + productId);
            if(!StringUtils.isNullBlank(value))
            {
                list.add(JSON.parseObject(value, WarningManageDetailResDTO.class));
            }
            else
            {
                b = false;
            }
        }
        return list;
    }

    /**
     * 清除警报缓存
     */
    public void delWarning(Long clientId, Long productId)
    {
        del(DB.TRAFFIC_DETAIL, String.valueOf(productId));
        del(DB.TRAFFIC_DETAIL, clientId + "," + productId);
        hDel(DB.TRAFFIC_DETAIL, Key.ERROR_ITEMS, String.valueOf(productId));
        hDel(DB.TRAFFIC_DETAIL, Key.ERROR_ITEMS, clientId + "," + productId);
    }

    interface DB
    {
        // 元数据 & 字典数据
        int METADATA = 1;
        // 产品请求计数实时监控数据库
        int PRODUCT_TRAFFIC = 2;
        // 客户请求计数实时监控数据库
        int CLIENT_TRAFFIC = 3;
        // 实时请求数据库 & 警报
        int TRAFFIC_DETAIL = 4;
        // 产品下请求的客户
        int PRODUCT_CLIENT = 5;
        // 客户请求-城市
        int CLIENT_TRAFFIC_CITY = 6;
    }

    public interface Key
    {
        String PRODUCT = "metadata:product";
        String CLIENT = "metadata:client";
        String ALL_COUNT = "all_count";
        String PRODUCT_TRAFFIC_DETAIL = "product_traffic_detail";
        String CLIENT_TRAFFIC_DETAIL = "client_traffic_detail";
        String ERROR_ITEMS = "error_items";
        String REQUEST_LAST_TIME = "request_last_time";
    }
}
