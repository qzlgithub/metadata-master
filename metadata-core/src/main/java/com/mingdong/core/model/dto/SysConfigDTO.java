package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.base.ResponseDTO;

import java.io.Serializable;
import java.util.Date;

public class SysConfigDTO implements Serializable
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String name;
    private String value;
    private ResponseDTO responseDTO;

    public SysConfigDTO()
    {
        this.responseDTO = new ResponseDTO();
        responseDTO.setResult(RestResult.SUCCESS);
    }

    public ResponseDTO getResponseDTO()
    {
        return responseDTO;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
