package com.mingdong.core.model;

import com.alibaba.fastjson.JSON;
import com.mingdong.core.constant.RestResult;

import java.util.HashMap;
import java.util.Map;

public class RestResp
{
    private String errCode;
    private String errMsg;
    private Map<String, Object> dataMap;

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

    public String getErrCode()
    {
        return errCode;
    }

    public void setErrCode(String errCode)
    {
        this.errCode = errCode;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public Map<String, Object> getDataMap()
    {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap)
    {
        this.dataMap = dataMap;
    }

    public RestResp result(RestResult result)
    {
        if(result == null)
        {
            result = RestResult.SUCCESS;
        }
        errCode = result.getCode();
        errMsg = result.getMessage();
        return this;
    }

    public void addData(String k, Object v)
    {
        if(dataMap == null)
        {
            dataMap = new HashMap<>();
        }
        dataMap.put(k, v);
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
