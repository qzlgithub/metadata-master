package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.NewRole;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;

import java.util.List;

public interface RemoteManagerService
{
    /**
     * 根据管理员id获取管理员信息
     *
     * @param managerId
     * @return
     */
    ManagerDTO getManagerById(Long managerId);

    /**
     * 根据username获取管理员信息
     *
     * @param username
     * @return
     */
    ManagerDTO getManagerByUsername(String username);

    /**
     * 更改管理员信息，null值不修改
     *
     * @param newManager@return
     */
    ResultDTO updateManagerSkipNull(NewManager newManager);

    /**
     * 获取所有角色列表
     *
     * @param page
     * @return
     */
    RoleListDTO getRoleList(Page page);

    /**
     * 根据角色名称获取角色
     *
     * @param name
     * @return
     */
    RoleDTO getRoleByName(String name);

    /**
     * 新增角色赋权
     *
     * @param toAddList
     * @return
     */
    ResultDTO saveRolePrivilegeList(List<RolePrivilegeDTO> toAddList);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    ResultDTO saveRole(RoleDTO role);

    /**
     * 根据角色id获取角色信息
     *
     * @param roleId
     * @return
     */
    RoleDTO getRoleById(Long roleId);

    /**
     * 根据角色id删除赋权
     *
     * @param roleId
     * @return
     */
    ResultDTO deleteRolePrivilegeByRoleId(Long roleId);

    /**
     * 根据角色信息，null值不修改
     *
     * @param newRole
     * @return
     */
    ResultDTO updateRoleSkipNull(NewRole newRole);

    /**
     * 根据条件获取管理员信息列表
     *
     * @param roleId
     * @param enabled
     * @param page
     * @return
     */
    ManagerInfoListDTO getManagerInfoList(Long roleId, Integer enabled, Page page);

    /**
     * 根据管理员id获取赋权信息列表
     *
     * @param managerId
     * @return
     */
    ManagerPrivilegeListDTO getManagerPrivilegeListByManagerId(Long managerId);

    /**
     * 新增管理员赋权信息列表
     *
     * @param list
     * @return
     */
    ResultDTO saveManagerPrivilegeList(List<ManagerPrivilegeDTO> list);

    /**
     * 新增管理员
     *
     * @param manager
     * @return
     */
    ResultDTO saveManager(ManagerDTO manager);

    /**
     * 根据管理员id删除赋权信息
     *
     * @param managerId
     * @return
     */
    ResultDTO deleteManagerPrivilegeByManagerId(Long managerId);

    /**
     * 根据管理员id更改管理员信息
     *
     * @param manager
     * @return
     */
    ResultDTO updateManagerById(ManagerDTO manager);

    /**
     * 根据角色id获取角色赋权信息列表
     *
     * @param roleId
     * @return
     */
    RolePrivilegeListDTO getRolePrivilegeListByRoleId(Long roleId);

    /**
     * 更改管理员密码
     *
     * @param managerId
     * @param newPwd
     * @param oldPwd
     * @return
     */
    ResultDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd);

    /**
     * 新增角色并赋权
     *
     * @param newRole
     * @return
     */
    ResultDTO addRole(NewRole newRole);

    /**
     * 新增管理员并赋权
     *
     * @param newManager
     * @return
     */
    ResultDTO addManager(NewManager newManager);
}
