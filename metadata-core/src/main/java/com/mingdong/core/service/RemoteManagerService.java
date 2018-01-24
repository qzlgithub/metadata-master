package com.mingdong.core.service;

import com.mingdong.core.model.dto.ManagerDTO;

public interface RemoteManagerService
{
    ManagerDTO getManagerById(Long managerId);

    ManagerDTO getManagerByUsername(String username);

}
