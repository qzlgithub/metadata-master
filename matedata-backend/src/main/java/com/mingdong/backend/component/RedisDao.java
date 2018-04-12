package com.mingdong.backend.component;

import com.mingdong.core.base.RedisBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class RedisDao extends RedisBaseDao
{
    /**
     * 产品访问量监控
     */
    public void realTimeTraffic(long timestamp, Long productId, Long clientId)
    {
        long l = timestamp - timestamp % 300;
        hIncrBy(DB.PRODUCT_TRAFFIC, String.valueOf(l), String.valueOf(productId), 1);
        hIncrBy(DB.CLIENT_TRAFFIC, String.valueOf(l), String.valueOf(clientId), 1);
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

   interface DB
    {
        // 产品请求实时监控数据库
        int PRODUCT_TRAFFIC = 4;
        // 产品请求实时监控数据库
        int CLIENT_TRAFFIC = 5;
    }
}
