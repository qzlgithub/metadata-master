package com.mingdong.bop.service;

import com.mingdong.bop.model.ClientVO;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClientService
{
    /**
     * 判断username是否已存在
     */
    void checkIfUsernameExist(String username, RestResp resp);

    /**
     * 根据客户的公司全称OR简称来获取客户信息列表
     */
    void getSimilarCorp(String name, Long clientId, RestListResp resp);

    /**
     * 根据条件获取客户信息列表
     */
    void getClientList(String keyword, Long parentIndustryId, Long industryId, Integer enabled, Long managerId,
            Page page, RestListResp res);

    /**
     * 更改客户状态同时修改帐号并新增客户帐号操作记录
     */
    void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, RestResp resp);

    /**
     * 根据客户ids软删客户
     */
    void deleteClient(List<Long> idList);

    /**
     * 批量重置客户主帐号的密码
     *
     * @param idList 客户ID列表
     */
    void resetPassword(List<Long> idList);

    /**
     * 根据客户id获取帐号列表
     */
    List<Map<String, Object>> getClientSubUserList(Long clientId);

    /**
     * 客户开通产品包时间形式，新增充值记录和客户产品记录
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp);

    /**
     * 客户开通产品计次形式，新增充值记录和客户产品记录
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp);

    /**
     * 根据客户产品id获取最后一次充值记录
     */
    void getProductRenewInfo(Long clientId, Long productId, RestResp resp);

    /**
     * 根据客户产品id续费按包时间形式，新增充值记录，更新客户产品信息
     */
    void renewProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, RestResp resp);

    /**
     * 根据客户产品id续费按计次形式，新增充值记录，更新客户产品信息
     */
    void renewProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, RestResp resp);

    /**
     * 查询客户帐号的管理员操作记录
     */
    void getClientOperateLog(Long clientId, Page page, RestListResp resp);

    /**
     * 判断合同号是否已存在
     */
    void checkIfContractExist(String contractNo, RestResp resp);

    void addClient(ClientVO vo, RestResp resp);

    void editClient(ClientVO vo, RestResp resp);

    /**
     * 获取客户的详细资料
     *
     * @param clientId 客户ID
     */
    Map<String, Object> getClientInfoAndProduct(Long clientId);

    Map<String, Object> getClientInfo(Long clientId);

    void selectCustomProduct(Long clientId, List<Long> productIds, RestResp resp);

    void removeCustomClientProduct(Long clientProductId, RestResp resp);

    Map<String, Object> getClientAccountDict(Long clientId);

    void getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate, Page page,
            RestListResp res);

    String getClientCorpName(Long clientId);

    void getProductRechargeList(Long clientId, Long productId, Date fromDate, Date toDate, Page page, RestListResp res);

    XSSFWorkbook createClientRequestXlsx(Long clientId, Long userId, Long productId, Date startTime, Date endTime,
            Page page);

    XSSFWorkbook createClientRechargeXlsx(Long clientId, Long productId, Date startTime, Date endTime, Page page);
}
