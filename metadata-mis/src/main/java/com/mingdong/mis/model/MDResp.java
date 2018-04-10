package com.mingdong.mis.model;

import com.mingdong.mis.constant.MDResult;

import java.util.Date;
import java.util.Map;

public class MDResp
{
    private Integer code; // 请求状态：0-成功，其他-失败
    private long timestamp;
    private String requestNo;
    private MDRes result;

    private MDResp()
    {
    }

    public static MDResp create()
    {
        MDResp o = new MDResp();
        o.setCode(MDResult.OK.getCode());
        o.setTimestamp(System.currentTimeMillis() / 1000);
        return o;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getRequestNo()
    {
        return requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public MDRes getResult()
    {
        return result;
    }

    public void setResult(MDRes result)
    {
        this.result = result;
    }

    public void setResultStatus(Integer status)
    {
        if(result == null)
        {
            result = new MDRes(status);
        }
        else
        {
            result.setStatus(status);
        }
    }

    public void setResultData(Map<String, Object> data)
    {
        if(result == null)
        {
            result = new MDRes();
        }
        result.setData(data);
    }

    public void addResultData(String k, Object v)
    {
        if(result == null)
        {
            result = new MDRes();
        }
        result.addData(k, v);
    }

    public void response(MDResult mdResult)
    {
        code = mdResult.getCode();
    }

    public Date requestAt()
    {
        return new Date(timestamp * 1000);
    }
}