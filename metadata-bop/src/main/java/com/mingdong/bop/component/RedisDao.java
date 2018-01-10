package com.mingdong.bop.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Trade;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RedisDao extends RedisBaseDao
{
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

    public void setSystemModule(Map<String, String> systemModule)
    {
        String str = JSON.toJSONString(systemModule);
        set(DB.SYSTEM, Key.SYSTEM_MODULE, str);
    }

    public Map<String, String> getSystemModule()
    {
        String str = get(DB.SYSTEM, Key.SYSTEM_MODULE);
        if(StringUtils.isNullBlank(str))
        {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        JSONObject json = JSON.parseObject(str);
        for(String key : json.keySet())
        {
            map.put(key, json.getString(key));
        }
        return map;
    }

    /**
     * 生成交易流水号
     *
     * @param trade 交易类型
     * @return 交易流水号
     */
    public String createTradeNo(Trade trade)
    {
        String dateStr = DateUtils.format(new Date(), DateFormat.YYYYMMDD);
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
        String SYSTEM_MODULE = "system_module";
    }
}
