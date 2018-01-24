package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;

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

    ManagerInfoListDTO getManagerInfoList(Long roleId, Integer enabled, Page page);

    ManagerPrivilegeListDTO getManagerPrivilegeListByManagerId(Long managerId);

    ResultDTO saveManagerPrivilegeList(List<ManagerPrivilegeDTO> list);

    ResultDTO saveManager(ManagerDTO manager);

    ResultDTO deleteManagerPrivilegeByManagerId(Long managerId);

    ResultDTO updateManagerById(ManagerDTO manager);

    RolePrivilegeListDTO getRolePrivilegeListByRoleId(Long roleId);

    PrivilegeListDTO getPrivilegeListByIds(List<Long> ids);

    PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId);
}
