package com.mingdong.core.model.dto.response;

public class UserResDTO extends ResponseDTO
{
    private Long clientId;
    private Long userId;
    private Integer primary;
    private String username;
    private String name;
    private String phone;
    private Integer enabled;
    private String managerQq;
    private Integer firstLogin;
    private String appKey;
    private String serviceQq;

    public String getServiceQq()
    {
        return serviceQq;
    }

    public void setServiceQq(String serviceQq)
    {
        this.serviceQq = serviceQq;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getPrimary()
    {
        return primary;
    }

    public void setPrimary(Integer primary)
    {
        this.primary = primary;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public String getManagerQq()
    {
        return managerQq;
    }

    public void setManagerQq(String managerQq)
    {
        this.managerQq = managerQq;
    }

    public Integer getFirstLogin()
    {
        return firstLogin;
    }

    public void setFirstLogin(Integer firstLogin)
    {
        this.firstLogin = firstLogin;
    }
}
