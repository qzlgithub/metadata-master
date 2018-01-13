package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class UserListDTO extends BaseDTO
{
    private int allowedQty;
    private List<UserDTO> userList;

    public UserListDTO(RestResult result)
    {
        super(result);
    }

    public int getAllowedQty()
    {
        return allowedQty;
    }

    public void setAllowedQty(int allowedQty)
    {
        this.allowedQty = allowedQty;
    }

    public List<UserDTO> getUserList()
    {
        return userList;
    }

    public void setUserList(List<UserDTO> userList)
    {
        this.userList = userList;
    }
}
