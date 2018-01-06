package com.mingdong.bop.constant;

public enum ProductStatus
{
    NORMAL(1);

    private final int id;

    ProductStatus(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
