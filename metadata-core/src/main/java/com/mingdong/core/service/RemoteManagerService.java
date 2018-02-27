package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.NewRole;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;

public interface RemoteManagerService
{
    /**
     * 根据管理员id获取管理员信息
     */
    ManagerDTO getManagerById(Long managerId);

    /**
     * 根据username获取管理员信息
     */
    ManagerDTO getManagerByUsername(String username);

    /**
     * 更改管理员信息，null值不修改
     */
    ResultDTO updateManagerSkipNull(NewManager newManager);

    /**
     * 获取所有角色列表
     */
    RoleListDTO getRoleList(Page page);

    /**
     * 根据角色名称获取角色
     */
    RoleDTO getRoleByName(String name);

    /**
     * 根据角色id获取角色信息
     */
    RoleDTO getRoleById(Long roleId);

    /**
     * 根据角色信息，null值不修改
     */
    ResultDTO updateRoleSkipNull(NewRole newRole);

    /**
     * 根据条件获取管理员信息列表
     */
    ManagerInfoListDTO getManagerInfoList(Long roleId, Integer enabled, Page page);

    /**
     * 根据管理员id获取赋权信息列表
     */
    ManagerPrivilegeListDTO getManagerPrivilegeListByManagerId(Long managerId);

    /**
     * 根据角色id获取角色赋权信息列表
     */
    RolePrivilegeListDTO getRolePrivilegeListByRoleId(Long roleId);

    /**
     * 更改管理员密码
     */
    ResultDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd);

    /**
     * 新增角色并赋权
     */
    ResultDTO addRole(NewRole newRole);

    /**
     * 新增管理员并赋权
     */
    ResultDTO addManager(NewManager newManager);

    /**
     * 更新用户角色状态
     *
     * @param roleId 角色ID
     * @param status 状态：1-启用，2-禁用
     */
    ResultDTO changeRoleStatus(Long roleId, Integer status);
}
