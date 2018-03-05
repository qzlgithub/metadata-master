package com.mingdong.mis.domain.entity;

import java.util.Date;

public class GroupFunction
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long groupId;
    private Long privilegeId;

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

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getPrivilegeId()
    {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId)
    {
        this.privilegeId = privilegeId;
    }
}


