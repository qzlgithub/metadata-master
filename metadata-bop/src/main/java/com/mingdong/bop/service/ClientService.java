package com.mingdong.bop.service;

import com.mingdong.bop.model.NewClientVO;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
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
    void checkIfUsernameExist(String username, BLResp resp);

    /**
     * 根据客户的公司全称OR简称来获取客户信息列表
     */
    void getSimilarCorp(String name, Long clientId, BLResp resp);

    /**
     * 根据条件获取客户信息列表
     */
    void getClientList(String keyword, Long parentIndustryId, Long industryId, Integer enabled, Page page, ListRes res);

    void getClientInfoForEdit(Long clientId, BLResp resp);

    /**
     * 更改客户状态同时修改帐号并新增客户帐号操作记录
     */
    void changeClientStatus(List<Long> clientIdList, Integer enabled, String reason, Long managerId, BLResp resp);

    /**
     * 根据客户ids软删客户
     */
    void deleteClient(List<Long> idList);

    /**
     * 根据客户ids重置主帐号的密码
     */
    void resetPassword(List<Long> idList, BLResp resp);

    /**
     * 根据客户id获取帐号列表
     */
    List<Map<String, Object>> getSubAccountList(Long clientId);

    /**
     * 客户开通产品包时间形式，新增充值记录和客户产品记录
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    /**
     * 客户开通产品计次形式，新增充值记录和客户产品记录
     */
    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    /**
     * 根据客户产品id获取最后一次充值记录
     */
    void getProductRenewInfo(Long clientProductId, BLResp resp);

    /**
     * 根据客户产品id续费按包时间形式，新增充值记录，更新客户产品信息
     */
    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    /**
     * 根据客户产品id续费按计次形式，新增充值记录，更新客户产品信息
     */
    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    /**
     * 根据客户id获取客户帐号操作记录
     */
    void getClientOperateLog(Long clientId, Page page, BLResp resp);

    /**
     * 判断合同号是否已存在
     */
    void checkIfContractExist(String contractNo, BLResp resp);

    void addClient(NewClientVO vo, BLResp resp);

    void editClient(NewClientVO vo, BLResp resp);

    void findClientDetail(Long clientId, BLResp resp);

    void selectCustomProduct(Long clientId, List<Long> productIds, BLResp resp);

    void removeCustomClientProduct(Long clientProductId, BLResp resp);

    Map<String, Object> getClientAccountDict(Long clientId);

    void getClientRequestList(Long clientId, Long userId, Long productId, Date fromDate, Date toDate, Page page,
            ListRes res);

    String getClientCorpName(Long clientId);

    void getProductRechargeList(Long clientId, Long productId, Date fromDate, Date toDate, Page page, ListRes res);

    XSSFWorkbook createClientRequestXlsx(Long clientId, Long userId, Long productId, Date startTime, Date endTime,
            Page page);

    XSSFWorkbook createClientRechargeXlsx(Long clientId, Long productId, Date startTime, Date endTime, Page page);
}
