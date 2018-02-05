package com.mingdong.mis.model.metadata;

import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.model.IMetadata;

import java.util.HashMap;
import java.util.Map;

public class Blacklist implements IMetadata
{
    private String orderNo;
    private Integer hit;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    @Override
    public boolean isHit()
    {
        return TrueOrFalse.TRUE.equals(hit);
    }

    @Override
    public Map<String, Object> response()
    {
        Map<String, Object> m = new HashMap<>();
        m.put(Field.ORDER_NO, orderNo);
        m.put(Field.IS_HIT, hit);
        return m;
    }

    @Override
    public String getRequestNo()
    {
        return orderNo;
    }
}
