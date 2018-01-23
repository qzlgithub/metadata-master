package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class HomeDTO implements Serializable
{
    private int totalAllowedQty;
    private List<UserDTO> subUsers;
    private List<ProductDTO> opened;
    private List<ProductDTO> toOpen;
    private ResultDTO resultDTO;

    public HomeDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public void setResultDTO(ResultDTO resultDTO)
    {
        this.resultDTO = resultDTO;
    }

    public int getTotalAllowedQty()
    {
        return totalAllowedQty;
    }

    public void setTotalAllowedQty(int totalAllowedQty)
    {
        this.totalAllowedQty = totalAllowedQty;
    }

    public List<UserDTO> getSubUsers()
    {
        return subUsers;
    }

    public void setSubUsers(List<UserDTO> subUsers)
    {
        this.subUsers = subUsers;
    }

    public List<ProductDTO> getOpened()
    {
        return opened;
    }

    public void setOpened(List<ProductDTO> opened)
    {
        this.opened = opened;
    }

    public List<ProductDTO> getToOpen()
    {
        return toOpen;
    }

    public void setToOpen(List<ProductDTO> toOpen)
    {
        this.toOpen = toOpen;
    }
}
