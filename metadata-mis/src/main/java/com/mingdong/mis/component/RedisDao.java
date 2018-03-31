package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.mis.model.UserAuth;
import org.springframework.stereotype.Repository;

import java.util.Date;

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
        Long num = incr(DB.SEQUENCE, Key.RECHARGE_NO_PREFIX + dateStr);
        if(num == 1)
        {
            expire(DB.SEQUENCE, Key.RECHARGE_NO_PREFIX + dateStr, 86460L);
        }
        return "RO" + dateStr + String.format("%06d", num);
    }

    /**
     * 生成请求单号
     */
    public String getRequestNo(Date date)
    {
        String dateStr = DateUtils.format(date, "yyMMddHH");
        Long num = incr(DB.SEQUENCE, Key.REQUEST_NO_PREFIX + dateStr);
        if(num == 1)
        {
            expire(DB.SEQUENCE, Key.REQUEST_NO_PREFIX + dateStr, 3660L);
        }
        return "CO" + dateStr + String.format("%08d", num);
    }

    /**
     * 锁定客户产品账户
     */
    public boolean lockProductAccount(String account, String lockName)
    {
        setExNx(DB.SEQUENCE, account, lockName, 60);
        String name = get(DB.SEQUENCE, account);
        return lockName.equals(name);
    }

    /**
     * 释放客户产品账户
     */
    public void freeProductAccount(String account, String lockName)
    {
        String name = get(DB.SEQUENCE, account);
        if(lockName.equals(name))
        {
            del(DB.SEQUENCE, account);
        }
    }

    /**
     * 获取已缓存的大圣数据API请求凭证
     */
    public String getDSAuthToken()
    {
        return get(DB.OTHER, Key.DS_API_TOKEN);
    }

    /**
     * 保存大圣数据API请求凭证
     */
    public void setDSAuthToken(String token, long seconds)
    {
        setEx(DB.OTHER, Key.DS_API_TOKEN, token, seconds);
    }

    /**
     * 产品访问量监控
     */
    public void incProductTraffic(long timestamp, Long productId)
    {
        long l = timestamp - timestamp % 300;
        hIncrBy(DB.PRODUCT_TRAFFIC, String.valueOf(l), String.valueOf(productId), 1);
    }

    interface DB
    {
        // 客户token数据库
        int USER_AUTH = 1;
        // 系统序列
        int SEQUENCE = 2;
        // 产品请求监控数据库
        int PRODUCT_TRAFFIC = 3;
        // 其他数据
        int OTHER = 4;
    }

    interface Key
    {
        String DS_API_TOKEN = "ds_api_token";
        String RECHARGE_NO_PREFIX = "recharge_no:";
        String REQUEST_NO_PREFIX = "request_no:";
    }
}
