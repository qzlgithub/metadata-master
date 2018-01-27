package com.mingdong.mis.component;

import com.mingdong.core.base.RedisBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao extends RedisBaseDao
{
    public boolean lockClientProduct(Long clientProductId, String lockName)
    {
        setExNx(DB.LOCK_CLIENT_PRODUCT, clientProductId + "", lockName, 60);
        String name = get(DB.LOCK_CLIENT_PRODUCT, clientProductId + "");
        return lockName.equals(name);
    }

    public void freeClientProduct(Long clientProductId, String lockName)
    {
        String name = get(DB.LOCK_CLIENT_PRODUCT, clientProductId + "");
        if(lockName.equals(name))
        {
            del(DB.LOCK_CLIENT_PRODUCT, clientProductId + "");
        }
    }

    interface DB
    {
        int LOCK_CLIENT_PRODUCT = 1;
    }

    interface Key
    {
    }
}
