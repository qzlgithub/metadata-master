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

    public static RestResp build()
    {
        RestResp resp = new RestResp();
        return resp.result(RestResult.SUCCESS);
    }

    public static String getErrResp(RestResult result)
    {
        RestResp resp = new RestResp();
        resp.result(result);
        return JSON.toJSONString(resp);
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

    public RestResp result(RestResult result)
    {
        if(result == null)
        {
            result = RestResult.SUCCESS;
        }
        code = result.getCode();
        message = result.getMessage();
        return this;
    }

    public void addData(String k, Object v)
    {
        if(data == null)
        {
            data = new HashMap<>();
        }
        data.put(k, v);
    }

    public void addAllData(Map<String,Object> map){
        if(map == null){
            return;
        }
        for(Map.Entry<String,Object> entry : map.entrySet()){
            addData(entry.getKey(),entry.getValue());
        }
    }
}
