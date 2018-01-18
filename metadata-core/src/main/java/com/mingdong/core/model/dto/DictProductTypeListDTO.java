package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class DictProductTypeListDTO extends Base implements Serializable
{
    private String resCode;
    private int total;
    private int pages;
    private List<DictProductTypeDTO> dictProductTypeDTOList;

    public DictProductTypeListDTO()
    {
        resCode = RestResult.SUCCESS.getCode();
    }

    @Override
    public RestResult getResult()
    {
        return RestResult.getByCode(resCode);
    }

    @Override
    public void setResult(RestResult restResult)
    {
        if(restResult == null)
        {
            restResult = RestResult.SUCCESS;
        }
        resCode = restResult.getCode();
    }

    public String getResCode()
    {
        return resCode;
    }

    public void setResCode(String resCode)
    {
        this.resCode = resCode;
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
