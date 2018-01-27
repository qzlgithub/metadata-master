package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class DictRechargeTypeListDTO implements Serializable
{
    private List<DictRechargeTypeDTO> dataList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public DictRechargeTypeListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<DictRechargeTypeDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<DictRechargeTypeDTO> dataList)
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