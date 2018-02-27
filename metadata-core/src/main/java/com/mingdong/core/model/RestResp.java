package com.mingdong.core.model;

import com.alibaba.fastjson.JSON;
import com.mingdong.core.constant.RestResult;

import java.util.HashMap;
import java.util.Map;

public class RestResp
{
    private String code;
    private String message;
    private Map<String, Object> data;

    public RestResp()
    {
        code = RestResult.SUCCESS.getCode();
        message = RestResult.SUCCESS.getMessage();
    }

    public RestResp(RestResult restResult)
    {
        code = restResult.getCode();
        message = restResult.getMessage();
    }

    public static String getErrorResp(RestResult result)
    {
        return JSON.toJSONString(new RestResp(result));
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }

    public void setError(RestResult result)
    {
        if(result != null)
        {
            code = result.getCode();
            message = result.getMessage();
        }
    }

    public void addData(String k, Object v)
    {
        if(data == null)
        {
            data = new HashMap<>();
        }
        data.put(k, v);
    }

    public void addAllData(Map<String, Object> map)
    {
        if(map == null)
        {
            return;
        }
        for(Map.Entry<String, Object> entry : map.entrySet())
        {
            addData(entry.getKey(), entry.getValue());
        }
    }
}
