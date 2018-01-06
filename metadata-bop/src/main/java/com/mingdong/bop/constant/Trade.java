package com.mingdong.bop.constant;

public enum Trade
{
    PRODUCT_RECHARGE("PR");

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
