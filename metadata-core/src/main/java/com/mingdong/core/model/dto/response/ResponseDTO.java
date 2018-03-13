package com.mingdong.core.model.dto.response;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseDTO implements Serializable
{
    private String code;
    private Map<String, String> extradata;

    public ResponseDTO()
    {
        code = RestResult.SUCCESS.getCode();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public RestResult getResult()
    {
        return StringUtils.isNullBlank(code) ? RestResult.SUCCESS : RestResult.getByCode(code);
    }

    public void setResult(RestResult result)
    {
        code = result != null ? result.getCode() : null;
    }

    public Map<String, String> getExtradata()
    {
        return extradata;
    }

    @SuppressWarnings("unused")
    public void setExtradata(Map<String, String> extradata)
    {
        this.extradata = extradata;
    }

    public void addExtra(String k, String v)
    {
        if(extradata == null)
        {
            extradata = new HashMap<>();
        }
        extradata.put(k, v);
    }
}
