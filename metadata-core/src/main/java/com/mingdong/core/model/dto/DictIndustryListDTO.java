package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class DictIndustryListDTO implements Serializable
{
    private List<DictIndustryDTO> dataList;
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
}
