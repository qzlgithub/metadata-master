package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductRechargeInfoListDTO implements Serializable
{
    private List<ProductRechargeInfoDTO> dataList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ProductRechargeInfoListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ProductRechargeInfoDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<ProductRechargeInfoDTO> dataList)
    {
        this.dataList = dataList;
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
