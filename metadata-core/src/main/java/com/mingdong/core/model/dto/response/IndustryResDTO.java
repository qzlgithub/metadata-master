package com.mingdong.core.model.dto.response;

import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ResponseDTO;

import java.util.List;

public class IndustryResDTO extends ResponseDTO
{
    private Long parentId;
    private List<Dict> parents;
    private List<Dict> peers;

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public List<Dict> getParents()
    {
        return parents;
    }

    public void setParents(List<Dict> parents)
    {
        this.parents = parents;
    }

    public List<Dict> getPeers()
    {
        return peers;
    }

    public void setPeers(List<Dict> peers)
    {
        this.peers = peers;
    }
}
