package com.mingdong.core.model.dto.request;

import com.mingdong.core.model.dto.RequestDTO;

import java.util.List;

public class GroupReqDTO extends RequestDTO
{
    private Long id;
    private String name;
    private Integer enabled;
    private List<Long> privilegeIdList;
    private List<String> moduleNameList;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public List<Long> getPrivilegeIdList()
    {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList)
    {
        this.privilegeIdList = privilegeIdList;
    }

    public List<String> getModuleNameList()
    {
        return moduleNameList;
    }

    public void setModuleNameList(List<String> moduleNameList)
    {
        this.moduleNameList = moduleNameList;
    }
}
