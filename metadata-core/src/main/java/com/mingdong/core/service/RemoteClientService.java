package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.ClientContactDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientDetailDTO;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.NewClientDTO;
import com.mingdong.core.model.dto.OpenClientProductDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UpdateClientUserStatusDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RemoteClientService
{
    /**
     * 客户帐号登录
     *
     * @param username
     * @param password
     * @return
     */
    UserDTO userLogin(String username, String password);

    /**
     * 更改客户帐号的密码
     *
     * @param userId
     * @param orgPassword
     * @param newPassword
     * @return
     */
    ResultDTO changeUserPassword(Long userId, String orgPassword, String newPassword);

    /**
     * 根据客户id获取客户消息列表
     *
     * @param clientId
     * @param page
     * @return
     */
    MessageListDTO getClientMessage(Long clientId, Page page);

    /**
     * 软删客户子帐号
     *
     * @param primaryUserId
     * @param subUserId
     * @return
     */
    ResultDTO setSubUserDeleted(Long primaryUserId, Long subUserId);

    /**
     * 获取客户子帐号列表
     *
     * @param clientId
     * @param primaryUserId
     * @return
     */
    UserListDTO getSubUserList(Long clientId, Long primaryUserId);

    /**
     * 新增客户子帐号
     *
     * @param primaryAccountId
     * @param username
     * @param password
     * @param name
     * @param phone
     * @return
     */
    ResultDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone);

    /**
     * 更改客户子帐号状态
     *
     * @param primaryAccountId
     * @param clientUserId
     * @return
     */
    UserDTO changeStatus(Long primaryAccountId, Long clientUserId);

    /**
     * 根据客户帐号id获取帐号信息
     *
     * @param clientUserId
     * @return
     */
    UserDTO getAccountByUserId(Long clientUserId);

    /**
     * 修改客户子帐号信息
     *
     * @param primaryAccountId
     * @param clientUserId
     * @param username
     * @param password
     * @param name
     * @param phone
     * @param enabled
     * @return
     */
    UserDTO editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password, String name,
            String phone, Integer enabled);

    /**
     * 获取帐号token
     *
     * @param userId
     * @param password
     * @param productId
     * @return
     */
    CredentialDTO getUserCredential(Long userId, String password, Long productId);

    /**
     * 新增帐号token
     *
     * @param userId
     * @param productId
     * @param appKey
     * @param reqHost
     * @return
     */
    ResultDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost);

    /**
     * 根据username获取帐号信息
     *
     * @param username
     * @return
     */
    UserDTO findByUsername(String username);

    /**
     * 根据客户的公司全称OR简称来获取客户信息列表
     *
     * @param name     左右模糊匹配
     * @param clientId
     * @return
     */
    ClientInfoListDTO getSimilarCorpByName(String name, Long clientId);

    /**
     * 根据条件获取客户信息
     */
    ListDTO<ClientInfoDTO> getClientInfoListBy(String keyword, List<Long> industryList, Integer enabled, Page page);

    /**
     * 根据客户id获取客户信息
     *
     * @param clientId
     * @return
     */
    ClientDTO getClientByClientId(Long clientId);

    /**
     * 根据客户帐号id获取帐号信息
     *
     * @param userId
     * @return
     */
    ClientUserDTO getClientUserByUserId(Long userId);

    /**
     * 根据条件获取客户帐号信息列表
     *
     * @param clientId
     * @param enabled
     * @param deleted
     * @return
     */
    ClientUserListDTO getListByClientAndStatus(Long clientId, Integer enabled, Integer deleted);

    /**
     * 根据客户ids软删客户
     *
     * @param idList
     * @return
     */
    ResultDTO setClientDeleted(List<Long> idList);

    /**
     * 根据客户ids获取客户信息列表
     *
     * @param idList
     * @return
     */
    ClientListDTO getClientListByIds(List<Long> idList);

    /**
     * 根据客户帐号ids更改密码
     *
     * @param pwd
     * @param idList
     * @return
     */
    ResultDTO resetPasswordByIds(String pwd, List<Long> idList);

    /**
     * 根据客户产品id获取客户产品信息
     *
     * @param clientProductId
     * @return
     */
    ClientProductDTO getClientProductById(Long clientProductId);

    /**
     * 更改客户帐号信息null值不修改
     *
     * @param clientUserDTO
     * @return
     */
    ResultDTO updateClientUserSkipNull(ClientUserDTO clientUserDTO);

    /**
     * 根据客户帐号id获取客户帐号操作记录列表
     *
     * @param userId
     * @param page
     * @return
     */
    ClientOperateInfoListDTO getClientOperateInfoListByUserId(Long userId, Page page);

    /**
     * 根据条件获取客户信息列表
     *
     * @param date
     * @param currentDay
     * @param page
     * @return
     */
    ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page);

    /**
     * 根据条件获取客户消费信息列表
     *
     * @param shortName
     * @param typeId
     * @param clientId
     * @param userId
     * @param productId
     * @param startDate
     * @param endDate
     * @param page
     * @return
     */
    ApiReqInfoListDTO getClientBillListBy(String shortName, Long typeId, Long clientId, Long userId, Long productId,
            Date startDate, Date endDate, Page page);

    /**
     * 根据条件获取总消费数额
     *
     * @param shortName
     * @param typeId
     * @param clientId
     * @param userId
     * @param productId
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal getClientBillFeeSum(String shortName, Long typeId, Long clientId, Long userId, Long productId,
            Date startDate, Date endDate);

    /**
     * 新增客户
     *
     * @param dto
     * @return
     */
    ResultDTO addNewClient(NewClientDTO dto);

    /**
     * 获取客户信息和帐号信息
     *
     * @param clientId
     * @return
     */
    ClientDetailDTO getClientDetail(Long clientId);

    /**
     * 更改客户帐号状态
     *
     * @param updateClientUserStatusDTO
     * @return
     */
    ResultDTO updateClientUserStatus(UpdateClientUserStatusDTO updateClientUserStatusDTO);

    /**
     * 获取客户信息
     *
     * @param clientId
     * @return
     */
    ClientDetailDTO getClientInfoForEdit(Long clientId);

    /**
     * 客户开通产品
     *
     * @param openClientProductDTO
     * @return
     */
    ResultDTO openClientProduct(OpenClientProductDTO openClientProductDTO);

    /**
     * 客户续费产品
     *
     * @param openClientProductDTO
     * @return
     */
    ResultDTO renewClientProduct(OpenClientProductDTO openClientProductDTO);

    ResultDTO editClient(NewClientDTO clientDTO, List<ClientContactDTO> contacts, List<Long> delIds);

    ResultDTO selectCustomProduct(Long clientId, List<Long> productIds);

    ResultDTO removeCustomClientProduct(Long clientProductId);
}
