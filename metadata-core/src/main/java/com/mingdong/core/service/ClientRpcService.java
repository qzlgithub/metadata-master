package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientUserDictDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.DisableClientDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.OpenClientProductDTO;
import com.mingdong.core.model.dto.ProductOpenDTO;
import com.mingdong.core.model.dto.RechargeDTO;
import com.mingdong.core.model.dto.RechargeInfoDTO;
import com.mingdong.core.model.dto.RequestDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UserDTO;

import java.util.Date;
import java.util.List;

public interface ClientRpcService
{
    /**
     * 客户帐号登录
     */
    UserDTO userLogin(String username, String password);

    /**
     * 更改客户帐号的密码
     */
    ResultDTO changeUserPassword(Long userId, String orgPassword, String newPassword);

    /**
     * 根据客户id获取客户消息列表
     */
    ListDTO<MessageDTO> getClientMessage(Long clientId, Page page);

    /**
     * 软删客户子帐号
     */
    ResultDTO setSubUserDeleted(Long primaryUserId, Long subUserId);

    /**
     * 查询客户子账号列表，不包括已删除的账号
     *
     * @param clientId 客户ID
     * @param userId   客户账户ID，非客户主账号返回空列表
     */
    ListDTO<SubUserDTO> getSubUserList(Long clientId, Long userId);

    /**
     * 查询客户子账号列表
     *
     * @param clientId       客户ID
     * @param includeDeleted 是否包含已删除账号
     */
    ListDTO<SubUserDTO> getSubUserList(Long clientId, boolean includeDeleted);

    /**
     * 新增客户子帐号
     */
    ResultDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone);

    /**
     * 更改客户子帐号状态
     */
    ResultDTO changeSubUserStatus(Long clientId, Long clientUserId, Integer enabled);

    /**
     * 根据客户帐号id获取帐号信息
     */
    UserDTO getAccountByUserId(Long clientUserId);

    /**
     * 修改客户子帐号信息
     */
    ResultDTO editSubUser(SubUserDTO subUserDTO);

    /**
     * 获取帐号token
     */
    CredentialDTO getUserCredential(Long userId, String password, Long productId);

    /**
     * 新增帐号token
     */
    ResultDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost);

    /**
     * 查看名称相似的客户信息列表
     */
    ListDTO<ClientInfoDTO> getSimilarCorpByName(String name, Long clientId);

    /**
     * 根据条件获取客户信息
     */
    ListDTO<ClientInfoDTO> getClientInfoListBy(String keyword, Long industryId, Integer enabled, Long managerId,
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
    ListDTO<ClientInfoDTO> getClientInfoListByDate(Date startTime, Date endTime, Page page);

    /**
     * 新增客户
     */
    ResultDTO addNewClient(NewClientDTO dto);

    /**
     * 获取客户信息和帐号信息
     */
    ClientDetailDTO getClientDetail(Long clientId);

    /**
     * 更改客户帐号状态
     */
    ResultDTO changeClientStatus(DisableClientDTO disableClientDTO);

    /**
     * 获取客户信息
     */
    ClientDetailDTO getClientInfoForEdit(Long clientId);

    /**
     * 客户续费产品
     */
    ResultDTO renewClientProduct(OpenClientProductDTO openClientProductDTO);

    ResultDTO editClient(NewClientDTO clientDTO, List<ClientContactDTO> contacts, List<Long> delIds);

    ResultDTO selectCustomProduct(Long clientId, List<Long> productIds);

    ResultDTO removeCustomClientProduct(Long clientProductId);

    ListDTO<ApiReqInfoDTO> getClientBillListBy(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Long managerId, Page page);

    /**
     * 开通客户产品服务
     */
    ResultDTO openProduct(ProductOpenDTO dto);

    ClientUserDictDTO getClientAccountDict(Long clientId);

    ListDTO<RequestDTO> getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate,
            Page page);

    String getClientCorpName(Long clientId);

    ListDTO<RechargeDTO> getClientRechargeList(Long clientId, Long productId, Date fromDate, Date toDate, Page page);

    ListDTO<ApiReqInfoDTO> getRevenueList(Date beforeDate, Date currentDay, Page page);

    ListDTO<ClientOperateLogDTO> getClientOperateLog(Long clientId, Page page);

    /**
     * 查询客户最近的充值记录信息
     */
    RechargeInfoDTO getLatestRechargeInfo(Long clientProductId);
}
