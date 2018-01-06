package com.mingdong.bop.model;

import java.util.List;

public class SecInfo
{
    private Long id;
    private String key;
    private List<String> priv;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public List<String> getPriv()
    {
        return priv;
    }

    public void setPriv(List<String> priv)
    {
        this.priv = priv;
    }
}
