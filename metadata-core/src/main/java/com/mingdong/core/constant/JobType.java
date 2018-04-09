package com.mingdong.core.constant;

public enum JobType
{
    STATS_ALL("STATS_ALL", "统计各维度"),
    STATS_RECHARGE("STATS_RECHARGE", "统计充值"),
    CLIENT_REMIND("CLIENT_REMIND", "客户服务提醒"),
    CLEAN_TRAFFIC("CLEAN_TRAFFIC", "清理定时缓存数据");

    private final String code;
    private final String name;

    JobType(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
}
