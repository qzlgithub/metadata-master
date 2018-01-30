package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class NewRole implements Serializable
{
    private RoleDTO roleDTO;
    private List<Long> privilege;

    public RoleDTO getRoleDTO()
    {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO)
    {
        this.roleDTO = roleDTO;
    }

    public List<Long> getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(List<Long> privilege)
    {
        this.privilege = privilege;
    }
}
