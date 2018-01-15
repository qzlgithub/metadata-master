package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

public class UserDTO extends BaseDTO
{
    private Long userId;
    private String username;
    private String name;
    private String phone;
    private Integer enabled;
    private String managerQq;
    private Integer firstLogin;

    public UserDTO()
    {
    }

    public UserDTO(RestResult result)
    {
        super(result);
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
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
