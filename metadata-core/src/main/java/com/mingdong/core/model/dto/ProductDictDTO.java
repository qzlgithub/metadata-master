package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ProductDictDTO extends Base implements Serializable
{
    private String resCode;
    private List<DictDTO> productDictList;

    public ProductDictDTO()
    {
        resCode = RestResult.SUCCESS.getCode();
    }

    public String getResCode()
    {
        return resCode;
    }

    public void setResCode(String resCode)
    {
        this.resCode = resCode;
    }

    public List<DictDTO> getProductDictList()
    {
        return productDictList;
    }

    public void setProductDictList(List<DictDTO> productDictList)
    {
        this.productDictList = productDictList;
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
}
