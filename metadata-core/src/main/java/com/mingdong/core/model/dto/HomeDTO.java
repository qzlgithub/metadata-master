package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class HomeDTO extends BaseDTO
{
    private int totalAllowedQty;
    private List<SubUserDTO> subUsers;
    private List<ProdDTO> opened;
    private List<ProdDTO> toOpen;

    public HomeDTO(RestResult result)
    {
        super(result);
    }

    public int getTotalAllowedQty()
    {
        return totalAllowedQty;
    }

    public void setTotalAllowedQty(int totalAllowedQty)
    {
        this.totalAllowedQty = totalAllowedQty;
    }

    public List<SubUserDTO> getSubUsers()
    {
        return subUsers;
    }

    public void setSubUsers(List<SubUserDTO> subUsers)
    {
        this.subUsers = subUsers;
    }

    public List<ProdDTO> getOpened()
    {
        return opened;
    }

    public void setOpened(List<ProdDTO> opened)
    {
        this.opened = opened;
    }

    public List<ProdDTO> getToOpen()
    {
        return toOpen;
    }

    public void setToOpen(List<ProdDTO> toOpen)
    {
        this.toOpen = toOpen;
    }
}
