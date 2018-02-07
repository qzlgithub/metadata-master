package com.mingdong.bop.service;

import com.mingdong.bop.model.NewClientVO;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClientService
{
    /**
     * 判断username是否已存在
     *
     * @param username
     * @param resp
     */
    void checkIfUsernameExist(String username, BLResp resp);

    /**
     * 根据客户的公司全称OR简称来获取客户信息列表
     *
     * @param name     左右模糊匹配
     * @param clientId
     * @param resp
     */
    void getSimilarCorp(String name, Long clientId, BLResp resp);

    /**
     * 根据条件获取客户信息列表
     *
     * @param enabled
     * @param account
     * @param cropName
     * @param shortName
     * @param parentIndustryId
     * @param industryId
     * @param page
     * @return
     */
    BLResp getCorp(Integer enabled, String account, String cropName, String shortName, Long parentIndustryId,
            Long industryId, Page page);

    void getClientInfoForEdit(Long clientId, BLResp resp);

    /**
     * 更改客户状态同时修改帐号并新增客户帐号操作记录
     *
     * @param clientIdList
     * @param enabled
     * @param reason
     * @param managerId
     * @param resp
     */
    void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, Long managerId, BLResp resp);

    /**
     * 根据客户ids软删客户
     *
     * @param idList
     * @param resp
     */
    void setClientDeleted(List<Long> idList, BLResp resp);

    /**
     * 根据客户ids重置主帐号的密码
     *
     * @param idList
     * @param resp
     */
    void resetClientPassword(List<Long> idList, BLResp resp);

    /**
     * 根据客户项目id获取客户项目
     *
     * @param clientProductId
     * @return
     */
    Map<String, Object> getClientProductInfo(Long clientProductId);

    /**
     * 根据客户id获取帐号列表
     *
     * @param clientId
     * @return
     */
    List<Map<String, Object>> getSubAccountList(Long clientId);

    /**
     * 重置帐号的密码
     *
     * @param clientUserId
     * @param resp
     */
    void resetClientUserPassword(Long clientUserId, BLResp resp);

    /**
     * 客户开通产品包时间形式，新增充值记录和客户产品记录
     *
     * @param clientId
     * @param productId
     * @param contractNo
     * @param billPlan
     * @param rechargeType
     * @param amount
     * @param startDate
     * @param endDate
     * @param remark
     * @param resp
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    /**
     * 客户开通产品计次形式，新增充值记录和客户产品记录
     *
     * @param clientId
     * @param productId
     * @param contractNo
     * @param billPlan
     * @param rechargeType
     * @param amount
     * @param unitAmt
     * @param remark
     * @param resp
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    /**
     * 根据客户产品id获取最后一次充值记录
     *
     * @param clientProductId
     * @param resp
     */
    void getProductRenewInfo(Long clientProductId, BLResp resp);

    /**
     * 根据客户产品id续费按包时间形式，新增充值记录，更新客户产品信息
     *
     * @param clientProductId
     * @param contractNo
     * @param billPlan
     * @param rechargeType
     * @param amount
     * @param startDate
     * @param endDate
     * @param remark
     * @param resp
     */
    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    /**
     * 根据客户产品id续费按计次形式，新增充值记录，更新客户产品信息
     *
     * @param clientProductId
     * @param contractNo
     * @param billPlan
     * @param rechargeType
     * @param amount
     * @param unitAmt
     * @param remark
     * @param resp
     */
    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    /**
     * 根据客户id获取客户帐号操作记录
     *
     * @param clientId
     * @param page
     * @param resp
     */
    void getClientOperateLog(Long clientId, Page page, BLResp resp);

    /**
     * 根据条件导出充值记录
     *
     * @param clientId
     * @param productId
     * @param startTime
     * @param endTime
     * @return
     */
    XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date startTime, Date endTime);

    /**
     * 判断合同号是否已存在
     *
     * @param contractNo
     * @param resp
     */
    void checkIfContractExist(String contractNo, BLResp resp);

    void addClient(NewClientVO vo, BLResp resp);

    void editClient(NewClientVO vo, BLResp resp);

    void findClientDetail(Long clientId, BLResp resp);

    void selectCustomProduct(Long clientId, List<Long> productIds, BLResp resp);

    void removeCustomClientProduct(Long clientProductId, BLResp resp);
}
