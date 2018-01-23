package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductDictDTO implements Serializable
{
    private List<DictDTO> productDictList;
    private ResultDTO resultDTO;

    public ProductDictDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public void setResultDTO(ResultDTO resultDTO)
    {
        this.resultDTO = resultDTO;
    }

    public List<DictDTO> getProductDictList()
    {
        return productDictList;
    }

    public void setProductDictList(List<DictDTO> productDictList)
    {
        this.productDictList = productDictList;
    }

}
