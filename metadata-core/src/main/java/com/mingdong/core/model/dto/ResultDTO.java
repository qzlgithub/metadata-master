package com.mingdong.core.model.dto;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;

public class ResultDTO
{
    private String code;

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
