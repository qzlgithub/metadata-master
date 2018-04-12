package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.mis.model.UserAuth;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class RedisDao extends RedisBaseDao
{
    /**
     * 保存用户请求凭证
     */
    public void saveUserAuth(String token, UserAuth userAuth, long seconds)
    {
        String str = JSON.toJSONString(userAuth);
        setEx(DB.USER_AUTH, token, str, seconds);
    }

    /**
     * 获取用户请求凭证
     */
    public UserAuth findAuth(String token)
    {
        String str = get(DB.USER_AUTH, token);
        if(StringUtils.isNullBlank(str))
        {
            return null;
        }
        return JSON.parseObject(str, UserAuth.class);
    }

    /**
     * 删除用户请求凭证
     */
    public void dropUserAuth(String... token)
    {
        del(DB.USER_AUTH, token);
    }

    /**
     * 生成充值订单号
     */
    public String getRechargeNo()
    {
        String dateStr = DateUtils.format(new Date(), "yyMMdd");
        Long num = incr(DB.SYSTEM, Key.RECHARGE_NO_PREFIX + dateStr);
        if(num == 1)
        {
            expire(DB.SYSTEM, Key.RECHARGE_NO_PREFIX + dateStr, 86460L);
        }
        return "RO" + dateStr + String.format("%06d", num);
    }

    /**
     * 生成请求单号
     */
    public String getRequestNo(Date date)
    {
        String dateStr = DateUtils.format(date, "yyMMddHH");
        Long num = incr(DB.SYSTEM, Key.REQUEST_NO_PREFIX + dateStr);
        if(num == 1)
        {
            expire(DB.SYSTEM, Key.REQUEST_NO_PREFIX + dateStr, 3660L);
        }
        return "CO" + dateStr + String.format("%08d", num);
    }

    /**
     * 锁定客户产品账户
     */
    public boolean lockProductAccount(String account, String lockName)
    {
        setExNx(DB.SYSTEM, account, lockName, 60);
        String name = get(DB.SYSTEM, account);
        return lockName.equals(name);
    }

    /**
     * 释放客户产品账户
     */
    public void freeProductAccount(String account, String lockName)
    {
        String name = get(DB.SYSTEM, account);
        if(lockName.equals(name))
        {
            del(DB.SYSTEM, account);
        }
    }

    /**
     * 获取已缓存的大圣数据API请求凭证
     */
    public String getDSAuthToken()
    {
        return get(DB.SYSTEM, Key.DS_API_TOKEN);
    }

    /**
     * 保存大圣数据API请求凭证
     */
    public void setDSAuthToken(String token, long seconds)
    {
        setEx(DB.SYSTEM, Key.DS_API_TOKEN, token, seconds);
    }

    /**
     * 产品访问量监控
     */
    public void realTimeTraffic(long timestamp, Long productId, Long clientId)
    {
        long l = timestamp - timestamp % 300;
        hIncrBy(DB.PRODUCT_TRAFFIC, String.valueOf(l), String.valueOf(productId), 1);
        hIncrBy(DB.CLIENT_TRAFFIC, String.valueOf(l), String.valueOf(clientId), 1);
        hIncrBy(DB.PRODUCT_TRAFFIC, String.valueOf(l), Key.ALL_COUNT, 1);
        hIncrBy(DB.CLIENT_TRAFFIC, String.valueOf(l), Key.ALL_COUNT, 1);
    }

    /**
     * 获取客户访问量
     *
     * @param timestamp Unix时间戳
     */
    public List readClientTraffic(long timestamp, List<Long> clientIds)
    {
        List<String> clientIdsStr = new ArrayList<>();
        for(Long item : clientIds)
        {
            clientIdsStr.add(String.valueOf(item));
        }
        return hMGet(DB.CLIENT_TRAFFIC, String.valueOf(timestamp), clientIdsStr.toArray(new String[0]));
    }

    /**
     * 获取产品访问量
     *
     * @param timestamp Unix时间戳
     */
    public List readProductTraffic(long timestamp, List<Long> productIds)
    {
        List<String> productIdsStr = new ArrayList<>();
        for(Long item : productIds)
        {
            productIdsStr.add(String.valueOf(item));
        }
        return hMGet(DB.PRODUCT_TRAFFIC, String.valueOf(timestamp), productIdsStr.toArray(new String[0]));
    }

    /**
     * 获取客户访问总量
     *
     * @param timestamp Unix时间戳
     */
    public String readClientTrafficAll(long timestamp)
    {
        return hGet(DB.CLIENT_TRAFFIC, String.valueOf(timestamp), Key.ALL_COUNT);
    }

    /**
     * 获取产品访问总量
     *
     * @param timestamp Unix时间戳
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
                    if(timestamp - Long.parseLong(s) > 3900)
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

    //************************* 用户池缓存 *************************
    public void setPersonCache(String phone, String personId)
    {
        setEx(DB.PERSON_POOL, phone, personId, 600);
    }

    public String findPersonByPhone(String phone)
    {
        return get(DB.PERSON_POOL, phone);
    }

    interface DB
    {
        // 系统数据
        int SYSTEM = 1;
        // 客户token数据库
        int USER_AUTH = 2;
        // 用户缓冲池
        int PERSON_POOL = 3;
        // 产品请求实时监控数据库
        int PRODUCT_TRAFFIC = 4;
        // 产品请求实时监控数据库
        int CLIENT_TRAFFIC = 5;
    }

    interface Key
    {
        String DS_API_TOKEN = "ds_api_token";
        String RECHARGE_NO_PREFIX = "recharge_no:";
        String REQUEST_NO_PREFIX = "request_no:";
        String ALL_COUNT = "all_count";
    }
}
