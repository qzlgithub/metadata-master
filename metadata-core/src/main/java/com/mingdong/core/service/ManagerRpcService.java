package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.response.AdminSessionResDTO;
import com.mingdong.core.model.dto.request.AdminUserReqDTO;
import com.mingdong.core.model.dto.request.GroupReqDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.LoginReqDTO;
import com.mingdong.core.model.dto.response.ManagerInfoResDTO;
import com.mingdong.core.model.dto.response.UserInfoResDTO;
import com.mingdong.core.model.dto.ResponseDTO;

public interface ManagerRpcService
{
    /**
     * 管理账号登陆
     */
    AdminSessionResDTO adminLogin(LoginReqDTO loginReqDTO);

    UserInfoResDTO getAccountInfo(Long userId);

    /**
     * 编辑管理账号的基本信息及权限配置
     */
    ResponseDTO editAdminUser(AdminUserReqDTO userDTO);

    ListDTO<ManagerInfoResDTO> getAdminUserList(Integer roleType, Integer enabled, Page page);

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
    ResponseDTO editAccountRole(GroupReqDTO groupReqDTO);

    /**
     * 获取账户角色信息
     *
     * @param groupId 角色ID
     */
    GroupReqDTO getAccountRoleInfo(Long groupId);

    /**
     * 创建账户角色
     */
    ResponseDTO addAccountRole(GroupReqDTO groupReqDTO);

    /**
     * 获取账户角色列表
     */
    ListDTO<GroupReqDTO> getAccountGroupList(Page page);

    /**
     * 变更用户账户的状态
     *
     * @param userId 管理用户ID
     * @param status 1-启用，0-禁用
     */
    ResponseDTO changeUserStatus(Long userId, Integer status);
}
