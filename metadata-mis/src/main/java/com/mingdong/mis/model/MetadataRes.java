package com.mingdong.mis.model;

import com.mingdong.mis.constant.MetadataResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MetadataRes
{
    private int code;
    private Date timestamp;
    private Map<String, Object> metadata;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getMetadata()
    {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata)
    {
        this.metadata = metadata;
    }

    public void setResult(MetadataResult mr)
    {
        code = mr.getCode();
    }

    public void add(String k, Object v)
    {
        if(metadata == null)
        {
            metadata = new HashMap<>();
        }
        metadata.put(k, v);
    }

    public static MetadataRes create()
    {
        MetadataRes o = new MetadataRes();
        o.setCode(MetadataResult.RC_0.getCode());
        o.setTimestamp(new Date());
        return o;
    }
}
