package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;

public class BaseDTO implements Serializable
{
    private String code;

    public BaseDTO()
    {
        code = RestResult.SUCCESS.getCode();
    }

    public BaseDTO(RestResult result)
    {
        code = result.getCode();
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
        return RestResult.getByCode(code);
    }
}
