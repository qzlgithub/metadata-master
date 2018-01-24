package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;

import java.io.Serializable;
import java.util.List;

public class ClientUserListDTO implements Serializable
{
    private List<ClientUserDTO> dataList;
    private ResultDTO resultDTO;

    public ClientUserListDTO()
    {
        this.resultDTO = new ResultDTO();
        resultDTO.setResult(RestResult.SUCCESS);
    }

    public ResultDTO getResultDTO()
    {
        return resultDTO;
    }

    public List<ClientUserDTO> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<ClientUserDTO> dataList)
    {
        this.dataList = dataList;
    }
}
