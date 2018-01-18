package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class ProductListDTO extends BaseDTO
{
    private List<ProductDTO> opened;
    private List<ProductDTO> toOpen;
    private int total;
    private int pages;

    public ProductListDTO()
    {
        super(RestResult.SUCCESS);
    }

    public ProductListDTO(RestResult result)
    {
        super(result);
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
