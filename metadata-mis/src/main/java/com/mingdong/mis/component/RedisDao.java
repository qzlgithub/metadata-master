package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.mis.model.UserAuth;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    //************************* 用户池缓存 *************************
    public void setPersonCache(String phone, String personId)
    {
        setEx(DB.PERSON_POOL, phone, personId, 600);
    }

    public String findPersonByPhone(String phone)
    {
        return get(DB.PERSON_POOL, phone);
    }

    public Set getPersonKeys()
    {
        return keys(DB.PERSON_POOL, "*");
    }

    /**
     * 缓存号码数量
     */
    public void cachePersonStats(String key, int phoneCount)
    {
        PersonStats personStats = new PersonStats();
        personStats.setPhoneCount(phoneCount);
        String str = JSON.toJSONString(personStats);
        setEx(DB.SYSTEM, key, str, 3600L);
    }

    interface DB
    {
        // 系统数据
        int SYSTEM = 1;
        // 客户token数据库
        int USER_AUTH = 2;
        // 用户缓冲池
        int PERSON_POOL = 3;
    }

    interface Key
    {
        String RECHARGE_NO_PREFIX = "recharge_no:";
        String REQUEST_NO_PREFIX = "request_no:";
    }

    class PersonStats
    {
        private int phoneCount;

        public int getPhoneCount()
        {
            return phoneCount;
        }

        public void setPhoneCount(int phoneCount)
        {
            this.phoneCount = phoneCount;
        }

    }
}
