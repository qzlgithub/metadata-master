package com.test.mis;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.util.SignUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class UtilTest
{
    public static void main(String[] args) throws MetadataCoreException
    {
        long ts = System.currentTimeMillis() / 1000;
        System.out.println("timestamp: " + ts);
        Map<String, Object> m = new HashMap<>();
        m.put("phone", "13335758543");
        m.put("name", "冯宝宝");
        m.put("idNo", "330721198910115417");
        m.put("timestamp", ts);
        SortedMap sm = MapUtils.sortKey(m);
        String str = JSON.toJSONString(sm);
        System.out.println("str: " + str);
        String[] keys = {"0d593dc3cacb49b1a090d0b14b1e2c6a"};
        for(String key : keys)
        {
            String sign = SignUtils.sign(str, key);
            System.out.println("sign: " + sign);
        }
    }
}
