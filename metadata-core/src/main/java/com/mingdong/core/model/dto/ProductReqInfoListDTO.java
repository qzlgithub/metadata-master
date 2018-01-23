package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductReqInfoListDTO implements Serializable
{
    private List<ProductRequestInfoDTO> productRequestDTOList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ProductReqInfoListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ProductRequestInfoDTO> getProductRequestDTOList()
    {
        return productRequestDTOList;
    }

    public void setProductRequestDTOList(List<ProductRequestInfoDTO> productRequestDTOList)
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
