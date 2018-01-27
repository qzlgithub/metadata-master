package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.util.List;

public class ApiReqInfoListDTO
{
    private List<ApiReqInfoDTO> dataList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ApiReqInfoListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ApiReqInfoDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<ApiReqInfoDTO> dataList)
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
