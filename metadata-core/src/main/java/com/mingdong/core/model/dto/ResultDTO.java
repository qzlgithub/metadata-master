package com.mingdong.core.model.dto;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;

import java.io.Serializable;

public class ResultDTO implements Serializable
{
    private String code;

    public ResultDTO()
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
}
