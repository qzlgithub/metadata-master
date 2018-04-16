package com.mingdong.mis.mongo.entity;

public class ProductRequestCount
{
    private Long productId;
    private Long count;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }
}
