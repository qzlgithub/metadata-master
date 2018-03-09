package com.mingdong.core.model.dto.response;

import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.RequestDTO;

import java.util.List;

public class ClientUserDictResDTO extends RequestDTO
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
