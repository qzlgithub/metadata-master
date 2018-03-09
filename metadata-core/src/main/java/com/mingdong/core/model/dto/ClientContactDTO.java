package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

public class ClientContactDTO extends RequestDTO
{
    private Long id;
    private String name;
    private String position;
    private String phone;
    private String email;
    private Integer general;

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

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getGeneral()
    {
        return general;
    }

    public void setGeneral(Integer general)
    {
        this.general = general;
    }
}
