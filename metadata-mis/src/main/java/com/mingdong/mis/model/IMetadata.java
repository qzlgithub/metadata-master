package com.mingdong.mis.model;

import java.util.Map;

public interface IMetadata
{
    /**
     * 判断请求是否命中
     */
    boolean isHit();

    /**
     * 获取请求结果
     */
    Map<String, Object> response();

    /**
     * 获取第三方接口的请求编号
     */
    String getRequestNo();
}
