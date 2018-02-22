package com.mingdong.mis.constant;

public enum Trade
{
    OPEN("OP"),
    RECHARGE("RC");

    private final String code;

    Trade(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
