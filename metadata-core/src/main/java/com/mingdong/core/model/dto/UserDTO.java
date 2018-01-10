package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

public class UserDTO extends BaseDTO
{
    private Long userId;
    private String name;
    private String managerQq;
    private Integer firstLogin;

    public UserDTO()
    {
        super(RestResult.SUCCESS);
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
