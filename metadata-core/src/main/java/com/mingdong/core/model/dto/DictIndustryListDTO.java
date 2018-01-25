package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class DictIndustryListDTO implements Serializable
{
    private List<DictIndustryDTO> dataList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public DictIndustryListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<DictIndustryDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<DictIndustryDTO> dataList)
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
