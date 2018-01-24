package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ClientInfoListDTO implements Serializable
{
    private List<ClientInfoDTO> dataList;
    private int total;
    private int pages;
    private ResultDTO resultDTO;

    public ClientInfoListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ClientInfoDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<ClientInfoDTO> dataList)
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
