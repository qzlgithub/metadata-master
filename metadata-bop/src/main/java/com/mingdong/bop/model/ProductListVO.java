package com.mingdong.bop.model;

import java.util.List;
import java.util.Map;

public class ProductListVO
{
    private String typeName;
    private List<Map<String, Object>> list;

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public List<Map<String, Object>> getList()
    {
        return list;
    }

    public void setList(List<Map<String, Object>> list)
    {
        this.list = list;
    }
}
