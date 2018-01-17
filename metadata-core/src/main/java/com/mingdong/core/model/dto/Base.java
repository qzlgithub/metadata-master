package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

abstract class Base
{
    public abstract RestResult getResult();

    public abstract void setResult(RestResult restResult);
}
