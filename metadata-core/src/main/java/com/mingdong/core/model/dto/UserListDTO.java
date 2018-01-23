package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class UserListDTO implements Serializable
{
    private int allowedQty;
    private List<SubUserDTO> userList;
    private ResultDTO resultDTO;

    public UserListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
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
