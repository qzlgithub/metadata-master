package com.mingdong.mis.component;

import com.alibaba.fastjson.JSONObject;

public abstract class BusinessAbstract<T>
{
    public abstract T getSuccessData(JSONObject res);

}
