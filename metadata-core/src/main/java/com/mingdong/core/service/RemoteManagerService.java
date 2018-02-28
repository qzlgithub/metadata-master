package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.UserInfoDTO;

public interface RemoteManagerService
{
    /**
     * 根据管理员id获取管理员信息
     */
    ManagerDTO getManagerById(Long managerId);

    UserInfoDTO getAccountInfo(Long userId);

    /**
     * 根据username获取管理员信息
     */
    ManagerDTO getManagerByUsername(String username);

    /**
     * 更改管理员信息，null值不修改
     */
    ResultDTO updateManagerSkipNull(NewManager newManager);

    /**
     * 检测角色名称是否已存在
     *
     * @return 1-存在，0-不存在
     */
    Integer isRoleNameExist(String name);

    /**
     * 根据条件获取管理员信息列表
     */
    ManagerInfoListDTO getManagerInfoList(Long roleId, Integer enabled, Page page);

    /**
     * 根据管理员id获取赋权信息列表
     */
    ManagerPrivilegeListDTO getManagerPrivilegeListByManagerId(Long managerId);

    /**
     * 更改管理员密码
     */
    ResultDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd);

    /**
     * 新增管理员并赋权
     */
    ResultDTO addManager(NewManager newManager);

    /**
     * 变更账户角色状态
     *
     * @param roleId 角色ID
     * @param status 状态：1-启用，2-禁用
     */
    ResultDTO changeRoleStatus(Long roleId, Integer status);

    /**
     * 编辑账户角色信息
     */
    ResultDTO editAccountRole(RoleDTO roleDTO);

    /**
     * 获取账户角色信息
     *
     * @param roleId 角色ID
     */
    RoleDTO getAccountRoleInfo(Long roleId);

    /**
     * 创建账户角色
     */
    ResultDTO addAccountRole(RoleDTO roleDTO);

    /**
     * 获取账户角色列表
     */
    ListDTO<RoleDTO> getAccountRoleList(Page page);

    ListDTO<DictDTO> getAccountRoleDict();
}
