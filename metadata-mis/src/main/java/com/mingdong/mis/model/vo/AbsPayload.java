package com.mingdong.mis.model.vo;

import com.alibaba.fastjson.JSON;

public abstract class AbsPayload
{
    public abstract boolean check();

    public String toRequest()
    {
        return JSON.toJSONString(this);
    }
}
