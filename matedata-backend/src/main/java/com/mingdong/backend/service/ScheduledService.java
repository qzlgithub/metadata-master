package com.mingdong.backend.service;

import java.util.Date;

public interface ScheduledService
{
    /**
     * 总体统计
     */
    void statsByData(Date date);

    /**
     * 充值统计
     */
    void statsRechargeByData(Date date);

    /**
     * 客户服务提醒
     */
    void quartzClientRemind(Date date);

    /**
     * 清理定时缓存数据
     */
    void cleanTraffic(Date date);

    /**
     * 统计请求量
     */
    void statsRequest(Date date);
}
