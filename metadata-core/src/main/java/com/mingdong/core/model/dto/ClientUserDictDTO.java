package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class ClientUserDictDTO implements Serializable
{
    private String corpName;
    private List<DictDTO> userDict;

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public List<DictDTO> getUserDict()
    {
        return userDict;
    }

    public void setUserDict(List<DictDTO> userDict)
    {
        this.userDict = userDict;
    }
}
