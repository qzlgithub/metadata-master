package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;

import java.util.List;
import java.util.Map;

public interface ManagerService
{
    /**
     * 管理员登录，更新sessionId
     */
    void userLogin(String username, String password, String sessionId, RestResp resp);

    /**
     * 管理员注销
     */
    void userLogout(String sessionId);

    /**
     * 管理员修改密码
     */
    void changePassword(Long managerId, String oldPwd, String newPwd, RestResp resp);

    /**
     * 获取角色列表
     */
    void getRoleList(Page page, ListRes res);

    /**
     * 新增角色并赋权
     */
    void addRole(String name, List<Long> privilege, RestResp resp);

    /**
     * 修改角色并赋权
     */
    void editRole(Long roleId, String roleName, List<Long> privilege, RestResp resp);

    /**
     * 根据条件获取管理员列表
     */
    void getManagerList(Long roleId, Integer enabled, Page page, ListRes res);

    /**
     * 根据管理员Id获取管理员信息
     */
    void getManagerInfo(Long managerId, RestResp resp);

    /**
     * 新增管理员信息并赋权
     */
    void addManager(String username, String password, String name, String phone, String qq, Long roleId,
            Integer enabled, List<Long> privilege, RestResp resp);

    /**
     * 修改管理员信息并赋权
     */
    void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, RestResp resp);

    /**
     * 根据角色id获取角色信息
     */
    Map<String, Object> getRolePrivilegeDetail(Long roleId);

    /**
     * 根据角色id获取赋权
     */
    List<String> getRolePrivilege(Long roleId);

    /**
     * 根据角色id更改角色状态
     */
    void changeStatus(Long roleId, RestResp resp);

    /**
     * 根据管理员id更改管理员状态
     */
    void changeManagerStatus(Long managerId, RestResp resp);

    /**
     * 判断角色名称是否存在
     */
    void checkIfRoleNameExist(String name, RestResp resp);

    /**
     * 根据enabled获取管理员信息列表
     */
    List<Map<String, Object>> getManagerListMap(Integer enabled);
}
