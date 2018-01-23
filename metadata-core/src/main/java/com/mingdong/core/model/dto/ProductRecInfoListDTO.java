package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductRecInfoListDTO implements Serializable
{
    private List<ProductRechargeInfoDTO> productRechargeDTOList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ProductRecInfoListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ProductRechargeInfoDTO> getProductRechargeDTOList()
    {
        return productRechargeDTOList;
    }

    public void setProductRechargeDTOList(List<ProductRechargeInfoDTO> productRechargeDTOList)
    {
        this.productRechargeDTOList = productRechargeDTOList;
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
