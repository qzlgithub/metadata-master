package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class IndustryDTO extends ResultDTO implements Serializable
{
    private Long parentId;
    private List<DictDTO> parents;
    private List<DictDTO> peers;

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public List<DictDTO> getParents()
    {
        return parents;
    }

    public void setParents(List<DictDTO> parents)
    {
        this.parents = parents;
    }

    public List<DictDTO> getPeers()
    {
        return peers;
    }

    public void setPeers(List<DictDTO> peers)
    {
        this.peers = peers;
    }
}
