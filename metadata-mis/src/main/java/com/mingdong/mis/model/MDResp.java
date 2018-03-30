package com.mingdong.mis.model;

import com.mingdong.mis.constant.MDResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MDResp
{
    private Integer code; // 请求状态：0-成功，其他-失败
    private Integer status; // 查询状态：0-正常命中，1-未命中
    private long timestamp;
    private String requestNo;
    private Map<String, Object> data;

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

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
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

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }

    public void setResult(MDResult mr)
    {
        code = mr.getCode();
    }

    public void add(String k, Object v)
    {
        if(data == null)
        {
            data = new HashMap<>();
        }
        data.put(k, v);
    }

    public void addAll(Map<String, Object> map)
    {
        if(data == null)
        {
            data = map;
        }
        else
        {
            data.putAll(map);
        }
    }

    public Date requestAt()
    {
        return new Date(timestamp * 1000);
    }
}
