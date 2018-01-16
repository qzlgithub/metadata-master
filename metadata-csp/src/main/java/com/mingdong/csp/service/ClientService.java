package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

public interface ClientService
{
    void userLogin(String username, String password, String sessionId, BLResp resp);

    void userLogout(String sessionId);

    void changePassword(Long userId, String oldPwd, String newPwd, BLResp resp);

    /**
     * 添加子账号
     */
    void addAccount(Long primaryAccountId, String username, String password, String name, String phone, BLResp resp);

    /**
     * 子账号列表
     */
    void getAccountList(Long clientId, Long primaryUserId, BLResp resp);

    /**
     * 禁用启用子账号
     */
    void changeStatus(Long primaryAccountId,Long clientUserId, BLResp resp);

    /**
     * 编辑子账号
     */
    void editChildAccount(Long primaryAccountId,Long clientUserId, String username, String password, String name, String phone, Integer enabled, BLResp resp);

    void getHomeData(Long clientId, Long clientUserId, BLResp resp);

    void getClientMessage(Long clientId, Page page, BLResp resp);

    void setSubUserDeleted(Long primaryUserId, Long subUserId, BLResp resp);

    void getAccountByUserId(Long userId, BLResp resp);

    void getUserCredential(Long userId, String password, Long productId, BLResp resp);
}
