package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.util.List;
import java.util.Map;

public interface SystemService
{
    List<Map<String, Object>> getValidRole();

    boolean checkIndustryCodeExist(String code);

    Map<String, String> getIndustryInfo(Long id);

    List<Map<String, Object>> getIndustryMap(Long parentId, Integer enabled);

    void addIndustryType(Long id, String code, String name, BLResp resp);

    void editIndustryInfo(Long id, String code, String name, BLResp resp);

    void dropRechargeType(Long rechargeTypeId, BLResp resp);

    void getRechargeTypeInfo(Long rechargeTypeId, BLResp resp);

    void updateRechargeType(Long id, String name, String remark, BLResp resp);

    List<Map<String, Object>> getProductListMap();

    List<Map<String, Object>> getHierarchyIndustry();

    List<Map<String, Object>> getHierarchyPrivilege();

    Map<String, Object> getPrivilegeInfo(Long id);

    void editPrivilegeInfo(Long privilegeId, String name, BLResp resp);

    List<Map<String, Object>> getRechargeDict();

    void getRechargeTypeList(Integer enabled, BLResp resp);

    void addRechargeType(String name, String remark, BLResp resp);

    BLResp editRechargeType(Long rechargeTypeId, String name, String remark, Integer enabled);

    BLResp getIndustryList(Integer enabled);

    List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled);

    BLResp editIndustry(Long industryId, String name, Integer enabled);

    BLResp getIndustryList(Page page);

    Map<String, Object> getInitIndustryMap();

    BLResp getModuleHierarchy();

    String getIndustryName(Long industryId);

    Map<String,String> cacheSystemModule();
}
