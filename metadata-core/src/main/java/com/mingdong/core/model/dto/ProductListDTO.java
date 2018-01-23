package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductListDTO implements Serializable
{
    private List<ProductDTO> opened;
    private List<ProductDTO> toOpen;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ProductListDTO()
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

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getPages()
    {
        return pages;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }
}
