package com.mingdong.bop.service;

import com.mingdong.bop.model.ManagerVO;
import com.mingdong.bop.model.NewManagerVO;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;

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
    void getAccountRoleList(Page page, RestListResp res);

    /**
     * 新增角色并赋权
     */
    void addAccountRole(String name, List<Long> privilegeIdList, RestResp resp);

    /**
     * 修改角色并赋权
     */
    void editRole(Long groupId, String groupName, List<Long> privilege, RestResp resp);

    /**
     * 根据条件获取管理员列表
     */
    void getManagerList(Integer roleType, Integer enabled, Page page, RestListResp res);

    /**
     * 获取用户信息
     */
    Map<String, Object> getAccountInfoData(Long userId);

    /**
     * 新增管理员信息并赋权
     */
    void addManager(NewManagerVO newManagerVO, RestResp resp);

    /**
     * 修改管理员信息并赋权
     */
    void editManager(ManagerVO vo, RestResp resp);

    /**
     * 根据角色id获取角色信息
     */
    Map<String, Object> getAccountRoleInfo(Long groupId);

    /**
     * 根据角色id获取赋权
     */
    void getRolePrivilege(Long groupId, RestResp resp);

    /**
     * 根据角色id更改角色状态
     */
    void changeRoleStatus(Long groupId, Integer status, RestResp resp);

    /**
     * 根据管理员id更改管理员状态
     */
    void changeManagerStatus(Long userId, Integer status, RestResp resp);

    /**
     * 判断角色名称是否存在
     */
    void checkIfGroupExist(String name, RestResp resp);

    /**
     * 根据enabled获取管理员信息列表
     */
    List<Map<String, Object>> getManagerListMap(Integer enabled);
}
