package com.mingdong.core.model;

import java.util.List;

public class RequestHolder
{
    private Long iId;
    private String iName;
    private List<String> iPrivilege;
    private String iModule;

    public Long getiId()
    {
        return iId;
    }

    public void setiId(Long iId)
    {
        this.iId = iId;
    }

    public String getiName()
    {
        return iName;
    }

    public void setiName(String iName)
    {
        this.iName = iName;
    }

    public List<String> getiPrivilege()
    {
        return iPrivilege;
    }

    public void setiPrivilege(List<String> iPrivilege)
    {
        this.iPrivilege = iPrivilege;
    }

    public String getiModule()
    {
        return iModule;
    }

    public void setiModule(String iModule)
    {
        this.iModule = iModule;
    }
}
