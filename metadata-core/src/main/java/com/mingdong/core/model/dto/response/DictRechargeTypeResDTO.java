package com.mingdong.core.model.dto.response;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.ResponseDTO;

import java.io.Serializable;
import java.util.Date;

public class DictRechargeTypeResDTO implements Serializable
{
    private Integer id;
    private Date createTime;
    private Date updateTime;
    private String name;
    private String remark;
    private Integer enabled;
    private Integer deleted;
    private ResponseDTO responseDTO;

    public DictRechargeTypeResDTO()
    {
        this.responseDTO = new ResponseDTO();
        responseDTO.setResult(RestResult.SUCCESS);
    }

    public ResponseDTO getResponseDTO()
    {
        return responseDTO;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }
}
