package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class NewManager implements Serializable
{
    private ManagerDTO managerDTO;
    private List<Long> privilege;

    public ManagerDTO getManagerDTO()
    {
        return managerDTO;
    }

    public void setManagerDTO(ManagerDTO managerDTO)
    {
        this.managerDTO = managerDTO;
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
