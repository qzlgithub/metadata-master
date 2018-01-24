package com.mingdong.mis.service.impl;

import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.Manager;
import com.mingdong.mis.domain.mapper.ManagerMapper;

import javax.annotation.Resource;

public class RemoteManagerServiceImpl implements RemoteManagerService
{
    @Resource
    private ManagerMapper managerMapper;

    @Override
    public ManagerDTO getManagerById(Long managerId)
    {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager byId = managerMapper.findById(managerId);
        if(byId == null){
            return null;
        }
        EntityUtils.copyProperties(byId,managerDTO);
        return managerDTO;
    }

    @Override
    public ManagerDTO getManagerByUsername(String username)
    {
        ManagerDTO managerDTO = new ManagerDTO();
        Manager byId = managerMapper.findByUsername(username);
        if(byId == null){
            return null;
        }
        EntityUtils.copyProperties(byId,managerDTO);
        return managerDTO;
    }
}
