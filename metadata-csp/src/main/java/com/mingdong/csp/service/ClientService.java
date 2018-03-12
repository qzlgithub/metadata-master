package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;

public interface ClientService
{
    /**
     * 客户帐号登录
     */
    void userLogin(String username, String password, String sessionId, RestResp resp);

    /**
     * 注销
     */
    void userLogout(String sessionId);

    /**
     * 更改密码
     */
    void changePassword(Long userId, String oldPwd, String newPwd, RestResp resp);

    /**
     * 添加子账号
     */
    void addAccount(Long primaryAccountId, String username, String password, String name, String phone, RestResp resp);

    /**
     * 子账号列表
     */
    void getClientSubAccountList(Page page, RestListResp res);

    /**
     * 禁用/启用客户子账号
     */
    void changeSubUserStatus(Long clientUserId, Integer enabled, RestResp resp);

    /**
     * 编辑子账号
     */
    void editSubUser(Long clientUserId, String username, String password, String name, String phone, Integer enabled,
            RestResp resp);

    /**
     * 获取首页信息
     */
    void getHomeData(Long clientId, Long clientUserId, RestResp resp);

    /**
     * 获取客户消息
     */
    void getClientMessageList(Page page, RestListResp res);

    /**
     * 软删客户子帐号
     */
    void setSubUserDeleted(Long primaryUserId, Long subUserId, RestResp resp);

    /**
     * 根据userId获取账户信息
     */
    void getClientUserInfo(Long userId, RestResp resp);

    /**
     * 获取token信息
     */
    void getUserCredential(Long userId, String password, Long productId, RestResp resp);

    /**
     * 获取token信息
     */
    void saveUserCredential(Long userId, Long productId, String appKey, String reqHost, RestResp resp);
}
