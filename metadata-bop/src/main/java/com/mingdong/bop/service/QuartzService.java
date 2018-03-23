package com.mingdong.bop.service;

import java.util.Date;

public interface QuartzService
{
    void statsByData(Date date);

    void statsRechargeByData(Date date);

    void quartzClientRemind(Date date);
}
