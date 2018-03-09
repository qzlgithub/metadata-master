package com.mingdong.core.model.dto;

import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.base.RequestDTO;

import java.util.List;

public class ClientUserDictDTO extends RequestDTO
{
    private String corpName;
    private List<Dict> userDict;

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public List<Dict> getUserDict()
    {
        return userDict;
    }

    public void setUserDict(List<Dict> userDict)
    {
        this.userDict = userDict;
    }
}
