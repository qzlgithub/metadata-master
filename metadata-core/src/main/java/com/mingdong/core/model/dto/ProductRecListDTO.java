package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class ProductRecListDTO extends BaseDTO
{
    private List<ProductRechargeDTO> productRechargeDTOList;
    private int total;
    private int pages;

    public ProductRecListDTO(RestResult result){
        super(result);
    }

    public List<ProductRechargeDTO> getProductRechargeDTOList()
    {
        return productRechargeDTOList;
    }

    public void setProductRechargeDTOList(List<ProductRechargeDTO> productRechargeDTOList)
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
