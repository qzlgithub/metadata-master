package com.mingdong.csp.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.csp.model.UserSession;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao extends RedisBaseDao
{
    public void saveUserSession(String sessionId, UserSession session)
    {
        String str = JSON.toJSONString(session);
        setEx(DB.USER_SECURE, sessionId, str, 1800L);
    }

    public UserSession getUserSession(String sessionId)
    {
        String str = get(DB.USER_SECURE, sessionId);
        if(!StringUtils.isNullBlank(str))
        {
            return JSON.parseObject(str, UserSession.class);
        }
        return null;
    }

    public void dropUserSession(String sessionId)
    {
        del(DB.USER_SECURE, sessionId);
    }

    interface DB
    {
        int USER_SECURE = 1;
        int SYSTEM = 2;
    }

    interface Key
    {
        String INDUSTRY = "industry";
        String SYSTEM_MODULE = "system_module";
    }
}
