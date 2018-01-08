package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.util.List;
import java.util.Map;

public interface ManagerService
{
    void userLogin(String username, String password, String sessionId, BLResp resp);

    void userLogout(String sessionId);

    void changePassword(Long managerId, String oldPwd, String newPwd, BLResp resp);

    void getRoleList(Page page, BLResp resp);

    void addRole(String name, List<Long> privilege, BLResp resp);

    void editRole(Long roleId, String roleName, List<Long> privilege, BLResp resp);

    void getManagerList(Long roleId, Integer enabled, Page page, BLResp resp);

    void getManagerInfo(Long managerId, BLResp resp);

    void addManager(String username, String password, String name, String phone, String qq, Long roleId,
            Integer enabled, List<Long> privilege, BLResp resp);

    void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, BLResp resp);

    Map<String, Object> getRolePrivilegeDetail(Long roleId);

    List<String> getRolePrivilege(Long roleId);

    void changeStatus(Long roleId, BLResp resp);

    void changeManagerStatus(Long managerId, BLResp resp);
}
