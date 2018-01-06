package com.mingdong.bop.model;

import com.mingdong.core.constant.RestResult;

import java.util.HashMap;
import java.util.Map;

public class BLResp
{
    private String errCode;
    private String errMsg;
    private Map<String, Object> dataMap;

    public static BLResp build()
    {
        BLResp resp = new BLResp();
        return resp.result(RestResult.SUCCESS);
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

    public BLResp result(RestResult result)
    {
        if(result == null)
        {
            result = RestResult.SUCCESS;
        }
        errCode = result.getCode();
        errMsg = result.getMessage();
        return this;
    }

    public BLResp addData(String k, Object v)
    {
        if(dataMap == null)
        {
            dataMap = new HashMap<>();
        }
        dataMap.put(k, v);
        return this;
    }
}
