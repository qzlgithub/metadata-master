package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

public class DictDTO extends RequestDTO
{
    private String key;
    private String value;

    public DictDTO()
    {
    }

    public DictDTO(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
