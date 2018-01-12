package com.mingdong.csp.service;

import com.mingdong.core.model.BLResp;

public interface UserService
{
    void userLogin(String username, String password, String sessionId, BLResp resp);

    void userLogout(String sessionId);

    void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp);

    /**
     * 添加子账号
     */
    void addChildAccount(Long clientId, String username, String password, String name, String phone, BLResp resp);

    /**
     * 子账号列表
     */
    BLResp getChildAccountMap(Long id);

    /**
     * 禁用启用子账号
     */
    void changeStatus(Long clientUserId, BLResp resp);

    /**
     * 编辑子账号
     */
    void editChildAccount(Long clientUserId, String username, String password, String name, String phone, BLResp resp);

    void getHomeData(Long clientId, Long clientUserId, BLResp resp);
}
