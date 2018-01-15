package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class ProductReqListDTO extends BaseDTO
{
    private List<ProductRequestDTO> productRequestDTOList;
    private int total;
    private int pages;

    public ProductReqListDTO(RestResult result){
        super(result);
    }

    public List<ProductRequestDTO> getProductRequestDTOList()
    {
        return productRequestDTOList;
    }

    public void setProductRequestDTOList(List<ProductRequestDTO> productRequestDTOList)
    {
        this.productRequestDTOList = productRequestDTOList;
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
