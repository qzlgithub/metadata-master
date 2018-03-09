package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.AdminSessionResDTO;
import com.mingdong.core.model.dto.AdminUserReqDTO;
import com.mingdong.core.model.dto.GroupDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.LoginDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;

public interface ManagerRpcService
{
    /**
     * 管理账号登陆
     */
    AdminSessionResDTO adminLogin(LoginDTO loginDTO);

    UserInfoDTO getAccountInfo(Long userId);

    /**
     * 编辑管理账号的基本信息及权限配置
     */
    ResponseDTO editAdminUser(AdminUserReqDTO userDTO);

    ListDTO<ManagerInfoDTO> getAdminUserList(Integer roleType, Integer enabled, Page page);

    /**
     * 根据管理员id获取赋权信息列表
     */
    ListDTO<String> getManagerPrivilegeListByManagerId(Long managerId);

    /**
     * 更改管理员密码
     */
    ResponseDTO updateManagerPwd(Long managerId, String newPwd, String oldPwd);

    /**
     * 新增管理员并赋权
     */
    ResponseDTO addAdminUser(AdminUserReqDTO userDTO);

    /**
     * 变更账户角色状态
     *
     * @param groupId 角色ID
     * @param status  状态：1-启用，2-禁用
     */
    ResponseDTO changeRoleStatus(Long groupId, Integer status);

    /**
     * 编辑账户角色信息
     */
    ResponseDTO editAccountRole(GroupDTO groupDTO);

    /**
     * 获取账户角色信息
     *
     * @param groupId 角色ID
     */
    GroupDTO getAccountRoleInfo(Long groupId);

    /**
     * 创建账户角色
     */
    ResponseDTO addAccountRole(GroupDTO groupDTO);

    /**
     * 获取账户角色列表
     */
    ListDTO<GroupDTO> getAccountGroupList(Page page);

    /**
     * 变更用户账户的状态
     *
     * @param userId 管理用户ID
     * @param status 1-启用，0-禁用
     */
    ResponseDTO changeUserStatus(Long userId, Integer status);
}
