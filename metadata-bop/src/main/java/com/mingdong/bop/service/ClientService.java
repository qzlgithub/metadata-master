package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClientService
{
    void checkIfUsernameExist(String username, BLResp resp);

    void getSimilarCorp(String corpName, Long clientId, BLResp resp);

    BLResp getCorp(Integer enabled, String account, String cropName, String shortName, Long parentIndustryId,
            Long industryId, Page page);

    void addCorp(String username, String password, String corpName, String shortName, Long industryId, String name,
            String phone, String email, String license, Integer enabled, BLResp resp);

    void editClientInfo(Long clientId, String corpName, String shortName, String license, Long industryId, String name,
            String phone, String email, Integer userEnabled, Integer accountEnabled, BLResp resp);

    Map<String, Object> findClientInfo(Long clientId);

    Map<String, Object> findClientDetail(Long clientId);

    void changeClientStatus(List<Long> clientIdList, Integer enabled, BLResp resp);

    void setClientDeleted(List<Long> idList, BLResp resp);

    void resetClientPassword(List<Long> idList, BLResp resp);

    Map<String, Object> getClientProductInfo(Long clientProductId);

    List<Map<String, Object>> getSubAccountList(Long clientId);

    void resetClientUserPassword(Long clientUserId, BLResp resp);

    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    void openProductService(Long clientId, Long productId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    void getProductRenewInfo(Long clientProductId, BLResp resp);

    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, Date startDate, Date endDate, String remark, BLResp resp);

    void renewProductService(Long clientProductId, String contractNo, Integer billPlan, Integer rechargeType,
            BigDecimal amount, BigDecimal unitAmt, String remark, BLResp resp);

    void getClientOperateLog(Long clientId, Page page, BLResp resp);
}
