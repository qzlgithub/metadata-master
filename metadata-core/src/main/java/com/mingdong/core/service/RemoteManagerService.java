package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;

import java.util.List;

public interface RemoteManagerService
{
    ManagerDTO getManagerById(Long managerId);

    ManagerDTO getManagerByUsername(String username);

    ResultDTO updateManagerSkipNull(ManagerDTO managerUpd);

    RoleListDTO getRoleList(Page page);

    RoleDTO getRoleByName(String name);

    ResultDTO saveRolePrivilegeList(List<RolePrivilegeDTO> toAddList);

    ResultDTO saveRole(RoleDTO role);

    RoleDTO getRoleById(Long roleId);

    ResultDTO deleteRolePrivilegeByRoleId(Long roleId);

    ResultDTO updateRoleSkipNull(RoleDTO roleDTO);
}
