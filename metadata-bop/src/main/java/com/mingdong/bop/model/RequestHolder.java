package com.mingdong.bop.model;

import java.util.List;
import java.util.Map;

public class RequestHolder
{
    private Long iId;
    private Integer roleType;
    private String iName;
    private String iModule;
    private List<String> iPrivilege;
    private Map<String, String> iSystem;

    public Long getiId()
    {
        return iId;
    }

    public void setiId(Long iId)
    {
        this.iId = iId;
    }

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
    }

    public String getiName()
    {
        return iName;
    }

    public void setiName(String iName)
    {
        this.iName = iName;
    }

    public String getiModule()
    {
        return iModule;
    }

    public void setiModule(String iModule)
    {
        this.iModule = iModule;
    }

    public List<String> getiPrivilege()
    {
        return iPrivilege;
    }

    public void setiPrivilege(List<String> iPrivilege)
    {
        this.iPrivilege = iPrivilege;
    }

    public Map<String, String> getiSystem()
    {
        return iSystem;
    }

    public void setiSystem(Map<String, String> iSystem)
    {
        this.iSystem = iSystem;
    }
}
