package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.base.RedisBaseDao;
import com.mingdong.mis.model.UserAuth;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao extends RedisBaseDao
{
    public boolean lockProductAccount(String account, String lockName)
    {
        setExNx(DB.LOCK_CLIENT_PRODUCT, account, lockName, 60);
        String name = get(DB.LOCK_CLIENT_PRODUCT, account);
        return lockName.equals(name);
    }

    public void freeProductAccount(String account, String lockName)
    {
        String name = get(DB.LOCK_CLIENT_PRODUCT, account);
        if(lockName.equals(name))
        {
            del(DB.LOCK_CLIENT_PRODUCT, account);
        }
    }

    public void saveUserAuth(String token, UserAuth userAuth, long seconds)
    {
        String str = JSON.toJSONString(userAuth);
        setEx(DB.USER_AUTH, token, str, seconds);
    }

    public void dropUserAuth(String token)
    {
        del(DB.USER_AUTH, token);
    }

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
     * 获取以缓存的大圣数据API请求凭证
     */
    public String getDSAuthToken()
    {
        return get(DB.THIRD_INFO, Key.DS_API_TOKEN);
    }

    public void setDSAuthToken(String token, long seconds)
    {
        setEx(DB.THIRD_INFO, Key.DS_API_TOKEN, token, seconds);
    }

    interface DB
    {
        int LOCK_CLIENT_PRODUCT = 1;
        int USER_AUTH = 2;
        int THIRD_INFO = 3;
    }

    interface Key
    {
        String DS_API_TOKEN = "ds_api_token";
    }
}
