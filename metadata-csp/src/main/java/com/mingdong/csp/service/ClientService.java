package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;

public interface ClientService
{
    /**
     * 客户帐号登录
     */
    void userLogin(String username, String password, String sessionId, BLResp resp);

    /**
     * 注销
     */
    void userLogout(String sessionId);

    /**
     * 更改密码
     */
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
    void changeStatus(Long primaryAccountId, Long clientUserId, BLResp resp);

    /**
     * 编辑子账号
     */
    void editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password, String name,
            String phone, Integer enabled, BLResp resp);

    /**
     * 获取首页信息
     */
    void getHomeData(Long clientId, Long clientUserId, BLResp resp);

    /**
     * 获取客户消息
     */
    void getClientMessageList(Page page, ListRes res);

    /**
     * 软删客户子帐号
     */
    void setSubUserDeleted(Long primaryUserId, Long subUserId, BLResp resp);

    /**
     * 根据userId获取账户信息
     */
    void getAccountByUserId(Long userId, BLResp resp);

    /**
     * 获取token信息
     */
    void getUserCredential(Long userId, String password, Long productId, BLResp resp);

    /**
     * 获取token信息
     */
    void saveUserCredential(Long userId, Long productId, String appKey, String reqHost, BLResp resp);
}
