package com.mingdong.mis.model;

import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.constant.ResCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MDResp
{
    private Integer code; // 请求状态：0-成功，其他-失败
    private long timestamp;
    private String requestNo;
    private Integer resCode;
    private Map<String, Object> resData;

    private MDResp()
    {
    }

    public static MDResp create()
    {
        MDResp o = new MDResp();
        o.setCode(MDResult.OK.getCode());
        o.setTimestamp(System.currentTimeMillis() / 1000);
        o.setResCode(ResCode.NORMAL);
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

    public Integer getResCode()
    {
        return resCode;
    }

    public void setResCode(Integer resCode)
    {
        this.resCode = resCode;
    }

    public Map<String, Object> getResData()
    {
        return resData;
    }

    public void setResData(Map<String, Object> resData)
    {
        this.resData = resData;
    }

    public void addResData(String k, Object v)
    {
        if(resData == null)
        {
            resData = new HashMap<>();
        }
        resData.put(k, v);
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