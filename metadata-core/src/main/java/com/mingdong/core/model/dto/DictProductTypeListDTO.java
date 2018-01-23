package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class DictProductTypeListDTO implements Serializable
{
    private int total;
    private int pages;
    private List<DictProductTypeDTO> dictProductTypeDTOList;
    private ResultDTO resultDTO;

    public DictProductTypeListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
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

    public List<DictProductTypeDTO> getDictProductTypeDTOList()
    {
        return dictProductTypeDTOList;
    }

    public void setDictProductTypeDTOList(List<DictProductTypeDTO> dictProductTypeDTOList)
    {
        this.dictProductTypeDTOList = dictProductTypeDTOList;
    }
}
