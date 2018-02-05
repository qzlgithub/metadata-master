package com.mingdong.mis.model.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;

public class AccessVO<T extends AbstractAccessBean>
{
    private static Logger logger = LoggerFactory.getLogger(AccessVO.class);
    private Long timestamp;
    private String sign;
    private T payload;

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public T getPayload()
    {
        return payload;
    }

    public void setPayload(T payload)
    {
        this.payload = payload;
    }

    public boolean checkSign(String appSecret)
    {
        JSONObject json = (JSONObject) JSON.toJSON(payload);
        json.put(Field.TIMESTAMP, timestamp);
        SortedMap sortedMap = MapUtils.sortKey(json);
        String str = JSON.toJSONString(sortedMap);
        try
        {
            String newSign = SignUtils.sign(str, appSecret);
            return sign != null && sign.equals(newSign);
        }
        catch(MetadataCoreException e)
        {
            logger.error("Error occurred while check digital signature, data: {}, key: {}", str, appSecret);
            return false;
        }
    }
}
