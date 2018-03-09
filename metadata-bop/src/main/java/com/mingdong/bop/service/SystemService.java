package com.mingdong.bop.service;

import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.request.SysConfigReqDTO;

import java.util.List;
import java.util.Map;

public interface SystemService
{
    /**
     * 判断是否存在该code数据
     */
    void checkIfIndustryExist(String code, RestResp resp);

    /**
     * 新增行业分类信息
     */
    void addIndustryType(Long id, String code, String name, RestResp resp);

    /**
     * 修改行业分类信息
     */
    void editIndustryInfo(Long id, String code, String name, RestResp resp);

    /**
     * 根据充值类型id更改充值类型信息
     */
    void editRechargeType(Integer rechargeTypeId, String name, String remark, RestResp resp);

    /**
     * 修改充值类型状态
     */
    void enableRechargeType(Integer rechargeTypeId, Integer enabled, RestResp resp);

    /**
     * 获取顶级的行业分类列表
     */
    List<Map<String, Object>> getHierarchyIndustry();

    /**
     * 获取顶级的权限
     */
    List<Map<String, Object>> getHierarchyPrivilege();

    /**
     * 修改权限信息
     */
    void editPrivilegeInfo(Long privilegeId, String name, RestResp resp);

    /**
     * 获取充值类型列表
     */
    List<Dict> getRechargeDict();

    /**
     * 新增充值类型
     */
    void addRechargeType(String name, String remark, RestResp resp);

    /**
     * 根据条件获取行业分类列表
     */
    List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled);

    /**
     * 获取行业分类列表
     */
    Map<String, Object> getInitIndustryMap();

    /**
     * 根据行业分类id获取行业分类name
     */
    String getIndustryName(Long industryId);

    /**
     * 获取系统模块
     */
    Map<String, String> cacheSystemModule();

    /**
     * 修改系统参数
     */
    void setGlobalSetting(List<SysConfigReqDTO> sysConfigReqDTOList, RestResp resp);

    /**
     * 根据行业分类id更改状态
     */
    void changeIndustryStatus(Long industryTypeId, Integer enabled, RestResp resp);

    /**
     * 根据权限id更改状态
     */
    void setModuleStatus(List<Long> moduleIdList, Integer status);

    /**
     * 获取系统参数
     */
    Map<String, Object> getSettings();

    /**
     * 获取系统配置-充值类型字典列表
     */
    void getRechargeTypeList(RestListResp res);

    List<Dict> getAccountRoleDict();
}
