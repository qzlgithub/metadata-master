package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class RequestNumber
{
    @Field("product_id")
    private Long productId;
    @Field("client_id")
    private Long clientId;
    @Field("hit")
    private Integer hit;
    @Field("count")
    private Long count;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
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
