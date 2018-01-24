package com.mingdong.mis.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class ManagerPrivilege
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long managerId;
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

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public Long getPrivilegeId()
    {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId)
    {
        this.privilegeId = privilegeId;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(managerId).append(privilegeId).toHashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof ManagerPrivilege))
        {
            return false;
        }
        ManagerPrivilege privilege = (ManagerPrivilege) o;
        return new EqualsBuilder().append(managerId, privilege.managerId)
                .append(privilegeId, privilege.privilegeId)
                .isEquals();
    }
}
