package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;

import java.util.List;
import java.util.Map;

public interface ManagerService
{
    /**
     * 管理员登录，更新seesionId
     *
     * @param username
     * @param password
     * @param sessionId
     * @param resp
     */
    void userLogin(String username, String password, String sessionId, BLResp resp);

    /**
     * 管理员注销
     *
     * @param sessionId
     */
    void userLogout(String sessionId);

    /**
     * 管理员修改密码
     *
     * @param managerId
     * @param oldPwd
     * @param newPwd
     * @param resp
     */
    void changePassword(Long managerId, String oldPwd, String newPwd, BLResp resp);

    /**
     * 获取角色列表
     *  @param page
     * @param res
     */
    void getRoleList(Page page, ListRes res);

    /**
     * 新增角色并赋权
     *
     * @param name
     * @param privilege
     * @param resp
     */
    void addRole(String name, List<Long> privilege, BLResp resp);

    /**
     * 修改角色并赋权
     *
     * @param roleId
     * @param roleName
     * @param privilege
     * @param resp
     */
    void editRole(Long roleId, String roleName, List<Long> privilege, BLResp resp);

    /**
     * 根据条件获取管理员列表
     *  @param roleId
     * @param enabled
     * @param page
     * @param res
     */
    void getManagerList(Long roleId, Integer enabled, Page page, ListRes res);

    /**
     * 根据管理员Id获取管理员信息
     *
     * @param managerId
     * @param resp
     */
    void getManagerInfo(Long managerId, BLResp resp);

    /**
     * 新增管理员信息并赋权
     *
     * @param username
     * @param password
     * @param name
     * @param phone
     * @param qq
     * @param roleId
     * @param enabled
     * @param privilege
     * @param resp
     */
    void addManager(String username, String password, String name, String phone, String qq, Long roleId,
            Integer enabled, List<Long> privilege, BLResp resp);

    /**
     * 修改管理员信息并赋权
     *
     * @param managerId
     * @param roleId
     * @param name
     * @param phone
     * @param qq
     * @param enabled
     * @param privilege
     * @param resp
     */
    void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, BLResp resp);

    /**
     * 根据角色id获取角色信息
     *
     * @param roleId
     * @return
     */
    Map<String, Object> getRolePrivilegeDetail(Long roleId);

    /**
     * 根据角色id获取赋权
     *
     * @param roleId
     * @return
     */
    List<String> getRolePrivilege(Long roleId);

    /**
     * 根据角色id更改角色状态
     *
     * @param roleId
     * @param resp
     */
    void changeStatus(Long roleId, BLResp resp);

    /**
     * 根据管理员id更改管理员状态
     *
     * @param managerId
     * @param resp
     */
    void changeManagerStatus(Long managerId, BLResp resp);

    /**
     * 判断角色名称是否存在
     *
     * @param name
     * @param resp
     */
    void checkIfRoleNameExist(String name, BLResp resp);

    /**
     * 根据enabled获取管理员信息列表
     *
     * @param enabled
     * @return
     */
    List<Map<String, Object>> getManagerListMap(Integer enabled);
}
