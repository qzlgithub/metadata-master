package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class UserListDTO extends BaseDTO
{
    private int allowedQty;
    private List<SubUserDTO> userList;

    public UserListDTO(){

    }

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

    public List<SubUserDTO> getUserList()
    {
        return userList;
    }

    public void setUserList(List<SubUserDTO> userList)
    {
        this.userList = userList;
    }
}
