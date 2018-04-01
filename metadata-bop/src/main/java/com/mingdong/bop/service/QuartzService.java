package com.mingdong.bop.service;

import java.util.Date;

public interface QuartzService
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
}