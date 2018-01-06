package com.mingdong.bop.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.core.base.RedisBaseDao;
import com.movek.constant.DateFormat;
import com.movek.mis.constant.Trade;
import com.movek.mis.model.ManagerSession;
import com.movek.mis.model.SecInfo;
import com.movek.util.DateUtils;
import com.movek.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class RedisDao extends RedisBaseDao
{
    /**
     * 缓存用户安全信息
     */
    public void saveUserSecInfo(String userId, SecInfo secInfo)
    {
        String str = JSON.toJSONString(secInfo);
        set(DB.USER_SECURE, userId, str);
    }

    /**
     * 获取用户安全信息
     */
    public SecInfo getUserSecInfo(String userId)
    {
        String str = get(DB.USER_SECURE, userId);
        if(!StringUtils.isNullBlank(str))
        {
            return JSON.parseObject(str, SecInfo.class);
        }
        return null;
    }

    /**
     * 删除用户安全信息
     */
    public void dropUserSecInfo(String userId)
    {
        del(DB.USER_SECURE, userId);
    }

    public String getIndustryInfo(Long industryId)
    {
        return hGet(DB.SYSTEM, Key.INDUSTRY, industryId + "");
    }

    public void saveIndustryInfo(Long industryId, String industryName)
    {
        hSet(DB.SYSTEM, Key.INDUSTRY, industryId + "", industryName);
    }

    public void saveManagerSession(String sessionId, ManagerSession session)
    {
        String str = JSON.toJSONString(session);
        set(DB.USER_SECURE, sessionId, str);
    }

    public ManagerSession getManagerSession(String sessionId)
    {
        String str = get(DB.USER_SECURE, sessionId);
        if(!StringUtils.isNullBlank(str))
        {
            return JSON.parseObject(str, ManagerSession.class);
        }
        return null;
    }

    public void dropManagerSession(String sessionId)
    {
        del(DB.USER_SECURE, sessionId);
    }

    /**
     * 生成交易流水号
     *
     * @param trade 交易类型
     * @return 交易流水号
     */
    public String createTradeNo(Trade trade)
    {
        String dateStr = DateUtils.format(new Date(), DateFormat.YYYY_MM_DD);
        Long num = incr(DB.SYSTEM, trade.getCode() + dateStr);
        if(num == 1)
        {
            expire(DB.SYSTEM, trade.getCode() + dateStr, 86500L);
        }
        return trade.getCode() + dateStr + String.format("%06d", num);
    }

    interface DB
    {
        int USER_SECURE = 1; // 运营账号安全信息：登陆凭证及权限
        int SYSTEM = 2;
    }

    interface Key
    {
        String INDUSTRY = "industry";
    }
}
