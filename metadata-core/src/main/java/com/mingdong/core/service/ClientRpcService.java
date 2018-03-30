package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ClientContactReqDTO;
import com.mingdong.core.model.dto.request.ClientReqDTO;
import com.mingdong.core.model.dto.request.ClientUserReqDTO;
import com.mingdong.core.model.dto.request.DisableClientReqDTO;
import com.mingdong.core.model.dto.request.IntervalReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ClientDetailResDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.ClientOperateLogResDTO;
import com.mingdong.core.model.dto.response.ClientRemindResInfoDTO;
import com.mingdong.core.model.dto.response.ClientUserDictResDTO;
import com.mingdong.core.model.dto.response.CredentialResDTO;
import com.mingdong.core.model.dto.response.MessageResDTO;
import com.mingdong.core.model.dto.response.RechargeInfoResDTO;
import com.mingdong.core.model.dto.response.RechargeResDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.StatsDateInfoResDTO;
import com.mingdong.core.model.dto.response.SubUserResDTO;
import com.mingdong.core.model.dto.response.UserResDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClientRpcService
{
    /**
     * 客户帐号登录
     */
    UserResDTO userLogin(String username, String password);

    /**
     * 更改客户帐号的密码
     */
    ResponseDTO changeUserPassword(Long userId, String orgPassword, String newPassword, String repeatPassword);

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
    ListDTO<SubUserResDTO> getSubUserList(Long clientId, Long userId, Page page);

    /**
     * 查询客户子账号列表
     *
     * @param clientId 客户ID
     */
    ListDTO<SubUserResDTO> getSubUserList(Long clientId, Page page);

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
    ResponseDTO editSubUser(ClientUserReqDTO reqDTO);

    /**
     * 获取帐号token
     */
    CredentialResDTO getUserCredential(Long userId, String password, Long productId);

    /**
     * 新增帐号token
     */
    ResponseDTO saveUserProductCredential(Long userId, Long productId, String reqHost);

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
     * 新增客户
     */
    ResponseDTO addClient(ClientReqDTO reqDTO);

    /**
     * 编辑客户
     */
    ResponseDTO editClient(ClientReqDTO clientDTO, List<ClientContactReqDTO> contacts, List<Long> delIds);

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
    ClientDetailResDTO getClientInfo(Long clientId);

    ResponseDTO selectCustomProduct(Long clientId, List<Long> productIds);

    ResponseDTO removeCustomClientProduct(Long clientProductId);

    ListDTO<AccessResDTO> getClientBillListBy(String keyword, Long productId, Integer billPlan, Date fromDate,
            Date toDate, Page page);

    ClientUserDictResDTO getClientAccountDict(Long clientId);

    String getClientCorpName(Long clientId);

    /**
     * 客户接口请求记录
     */
    ListDTO<AccessResDTO> getClientRequestRecord(Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page);

    /**
     * 客户产品充值记录
     */
    ListDTO<RechargeResDTO> getClientRechargeRecord(String keyword, Long clientId, Long productId, Long managerId,
            Integer rechargeType, Date startDate, Date endDate, Page page);

    ListDTO<RechargeResDTO> getRechargeInfoListBy(Date fromDate, Date toDate);

    ListDTO<AccessResDTO> getRevenueList(Date beforeDate, Date currentDay, Page page);

    ListDTO<ClientOperateLogResDTO> getClientOperateLog(Long clientId, Page page);

    /**
     * 查询客户最近的充值记录信息
     */
    RechargeInfoResDTO getLatestRechargeInfo(Long clientId, Long productId);

    ResponseDTO saveUserCredential(ClientUserReqDTO clientUserReqDTO);

    int getClientIncrementFrom(Date date);

    Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit);

    Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit);

    ListDTO<ClientRemindResInfoDTO> getClientRemindList(Long managerId, Integer type, Date date, Integer dispose,
            Page page);

    void quartzClientRemind(Date date);

    ResponseDTO updateClientRemind(Long remindId, String remark);

    /**
     * 获取总客户数量
     */
    Integer getAllClientCount();

    /**
     * 根据日期获取客户数量
     */
    Integer getClientCountByDate(Date fromDate, Date toDate);

    /**
     * 根据条件获取客户信息列表
     */
    ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date startTime, Date endTime, Page page);

    ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date startTime, Date endTime);

    /**
     * 根据日期获取充值总额
     */
    BigDecimal getClientRechargeStatsByDate(Date fromDate, Date toDate);

    /**
     * 获取充值总额
     */
    BigDecimal getClientRechargeStatsAll();

    /**
     * 根据日期获取客户充值次数
     */
    Integer getClientRechargeCountByDate(Date fromDate, Date toDate);

    ListDTO<StatsDateInfoResDTO> getRequestListStats(Date fromDate, Date toDate, String name, Long productId);

    ListDTO<StatsDateInfoResDTO> getRevenueListStats(Date fromDate, Date toDate);

    void statsByData(Date date);

    void clientAccessTrend(List<Long> clientIdList, List<IntervalReqDTO> intervalList);

    void statsRechargeByData(Date date);

    BigDecimal getClientRechargeStatsByDate(Date fromDate, Date toDate, Long managerId);

    Integer getAllClientCount(Long managerId);

    Integer getClientCountByDate(Date fromDate, Date toDate, Long managerId);
}
