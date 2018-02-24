package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDictDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.MessageDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.OpenClientProductDTO;
import com.mingdong.core.model.dto.ProductOpenDTO;
import com.mingdong.core.model.dto.RequestDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SubUserDTO;
import com.mingdong.core.model.dto.UpdateClientUserStatusDTO;
import com.mingdong.core.model.dto.UserDTO;

import java.util.Date;
import java.util.List;

public interface RemoteClientService
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
     * 查询客户子账号列表
     *
     * @param clientId 客户ID
     * @param userId   客户账户ID，非客户主账号返回空列表
     */
    ListDTO<SubUserDTO> getSubAccountList(Long clientId, Long userId);

    /**
     * 新增客户子帐号
     */
    ResultDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone);

    /**
     * 更改客户子帐号状态
     */
    UserDTO changeStatus(Long primaryAccountId, Long clientUserId);

    /**
     * 根据客户帐号id获取帐号信息
     */
    UserDTO getAccountByUserId(Long clientUserId);

    /**
     * 修改客户子帐号信息
     */
    UserDTO editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password, String name,
            String phone, Integer enabled);

    /**
     * 获取帐号token
     */
    CredentialDTO getUserCredential(Long userId, String password, Long productId);

    /**
     * 新增帐号token
     */
    ResultDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost);

    /**
     * 根据username获取帐号信息
     */
    UserDTO findByUsername(String username);

    /**
     * 根据客户的公司全称OR简称来获取客户信息列表
     */
    ClientInfoListDTO getSimilarCorpByName(String name, Long clientId);

    /**
     * 根据条件获取客户信息
     */
    ListDTO<ClientInfoDTO> getClientInfoListBy(String keyword, Long industryId, Integer enabled, Page page);

    /**
     * 根据客户id获取客户信息
     */
    ClientDTO getClientByClientId(Long clientId);

    /**
     * 根据条件获取客户帐号信息列表
     */
    ClientUserListDTO getListByClientAndStatus(Long clientId, Integer enabled, Integer deleted);

    /**
     * 根据客户ID将客户设置为删除
     */
    ResultDTO setClientDeleted(List<Long> idList);

    /**
     * 根据客户ids获取客户信息列表
     */
    ClientListDTO getClientListByIds(List<Long> idList);

    /**
     * 根据客户帐号ID重置密码
     */
    ResultDTO resetPasswordByIds(String newPassword, List<Long> idList);

    /**
     * 根据客户产品id获取客户产品信息
     */
    ClientProductDTO getClientProductById(Long clientProductId);

    /**
     * 根据客户帐号id获取客户帐号操作记录列表
     */
    ClientOperateInfoListDTO getClientOperateInfoListByUserId(Long userId, Page page);

    /**
     * 根据条件获取客户信息列表
     */
    ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page);

    /**
     * 根据条件获取客户消费信息列表
     */
    ApiReqInfoListDTO getClientBillListBy(String shortName, Long typeId, Long clientId, Long userId, Long productId,
            Date startDate, Date endDate, Page page);

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
    ResultDTO updateClientUserStatus(UpdateClientUserStatusDTO updateClientUserStatusDTO);

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
            Date toDate, Page page);

    /**
     * 开通客户产品服务
     */
    ResultDTO openProduct(ProductOpenDTO dto);

    ClientUserDictDTO getClientAccountDict(Long clientId);

    ListDTO<RequestDTO> getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate,
            Page page);
}
