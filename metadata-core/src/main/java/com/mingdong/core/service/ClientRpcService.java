package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ClientContactReqDTO;
import com.mingdong.core.model.dto.request.DisableClientReqDTO;
import com.mingdong.core.model.dto.request.NewClientReqDTO;
import com.mingdong.core.model.dto.request.SubUserReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientOperateLogResDTO;
import com.mingdong.core.model.dto.response.ClientUserDictResDTO;
import com.mingdong.core.model.dto.response.CredentialResDTO;
import com.mingdong.core.model.dto.response.MessageResDTO;
import com.mingdong.core.model.dto.response.ProductRechargeResDTO;
import com.mingdong.core.model.dto.response.Recharge1ResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.SubUserResDTO;
import com.mingdong.core.model.dto.response.UserResDTO;

import java.util.Date;
import java.util.List;

public interface ClientRpcService
{
    /**
     * 客户帐号登录
     */
    UserResDTO userLogin(String username, String password);

    /**
     * 更改客户帐号的密码
     */
    ResponseDTO changeUserPassword(Long userId, String orgPassword, String newPassword);

    /**
     * 根据客户id获取客户消息列表
     */
    ListDTO<MessageResDTO> getClientMessage(Long clientId, Page page);

    /**
     * 软删客户子帐号
     */
    ResponseDTO setSubUserDeleted(Long primaryUserId, Long subUserId);

    /**
     * 查询客户子账号列表，不包括已删除的账号
     *
     * @param clientId 客户ID
     * @param userId   客户账户ID，非客户主账号返回空列表
     */
    ListDTO<SubUserResDTO> getSubUserList(Long clientId, Long userId);

    /**
     * 查询客户子账号列表
     *
     * @param clientId       客户ID
     * @param includeDeleted 是否包含已删除账号
     */
    ListDTO<SubUserResDTO> getSubUserList(Long clientId, boolean includeDeleted);

    /**
     * 新增客户子帐号
     */
    ResponseDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone);

    /**
     * 更改客户子帐号状态
     */
    ResponseDTO changeSubUserStatus(Long clientId, Long clientUserId, Integer enabled);

    /**
     * 根据客户帐号id获取帐号信息
     */
    UserResDTO getAccountByUserId(Long clientUserId);

    /**
     * 修改客户子帐号信息
     */
    ResponseDTO editSubUser(SubUserReqDTO reqDTO);

    /**
     * 获取帐号token
     */
    CredentialResDTO getUserCredential(Long userId, String password, Long productId);

    /**
     * 新增帐号token
     */
    ResponseDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost);

    /**
     * 查看名称相似的客户信息列表
     */
    ListDTO<ClientInfoResDTO> getSimilarCorpByName(String name, Long clientId);

    /**
     * 根据条件获取客户信息
     */
    ListDTO<ClientInfoResDTO> getClientInfoListBy(String keyword, Long industryId, Integer enabled, Long managerId,
            Page page);

    /**
     * 批量将客户设置为删除
     */
    void setClientDeleted(List<Long> idList);

    /**
     * 批量设置客户主账号的密码
     *
     * @param idList   客户ID列表
     * @param password 新的密码
     */
    void setClientPassword(List<Long> idList, String password);

    /**
     * 根据条件获取客户信息列表
     */
    ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date startTime, Date endTime, Page page);

    /**
     * 新增客户
     */
    ResponseDTO addNewClient(NewClientReqDTO reqDTO);

    /**
     * 获取客户信息和帐号信息
     */
    ClientDetailResDTO getClientDetail(Long clientId);

    /**
     * 更改客户帐号状态
     */
    ResponseDTO changeClientStatus(DisableClientReqDTO reqDTO);

    /**
     * 获取客户信息
     */
    ClientDetailResDTO getClientInfoForEdit(Long clientId);

    ResponseDTO editClient(NewClientReqDTO clientDTO, List<ClientContactReqDTO> contacts, List<Long> delIds);

    ResponseDTO selectCustomProduct(Long clientId, List<Long> productIds);

    ResponseDTO removeCustomClientProduct(Long clientProductId);

    ListDTO<AccessResDTO> getClientBillListBy(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Long managerId, Page page);

    ClientUserDictResDTO getClientAccountDict(Long clientId);

    String getClientCorpName(Long clientId);

    // TODO （管理）客户消费记录
    ListDTO<AccessResDTO> getClientRequestList(Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page);

    // TODO （客户）客户消费记录
    ListDTO<AccessResDTO> getApiRequestRecord(Long clientId, Long userId, Long productId, Date startDate, Date endDate,
            Page page);

    // TODO （管理）客户充值记录
    ListDTO<Recharge1ResDTO> getClientRechargeList(Long clientId, Long productId, Date startDate, Date endDate,
            Page page);

    // TODO （客户）客户充值记录
    ListDTO<ProductRechargeResDTO> getProductRechargeRecord(Long clientId, Long productId, Date startDate, Date endDate,
            Page page);

    ListDTO<AccessResDTO> getRevenueList(Date beforeDate, Date currentDay, Page page);

    ListDTO<ClientOperateLogResDTO> getClientOperateLog(Long clientId, Page page);

    /**
     * 查询客户最近的充值记录信息
     */
    RechargeResDTO getLatestRechargeInfo(Long clientId, Long productId);
}
