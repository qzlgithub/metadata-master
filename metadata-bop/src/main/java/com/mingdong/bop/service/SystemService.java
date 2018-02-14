package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.SysConfigDTO;

import java.util.List;
import java.util.Map;

public interface SystemService
{
    /**
     * 获取角色列表
     */
    List<Map<String, Object>> getValidRole();

    /**
     * 判断是否存在该code数据
     */
    boolean checkIndustryCodeExist(String code);

    /**
     * 根据行业分类id获取行业分类
     */
    Map<String, String> getIndustryInfo(Long id);

    /**
     * 根据parentId和状态获取行业分类列表
     */
    List<Map<String, Object>> getIndustryMap(Long parentId, Integer enabled);

    /**
     * 新增行业分类信息
     */
    void addIndustryType(Long id, String code, String name, BLResp resp);

    /**
     * 修改行业分类信息
     */
    void editIndustryInfo(Long id, String code, String name, BLResp resp);

    /**
     * 软删充值类型
     */
    void dropRechargeType(Long rechargeTypeId, BLResp resp);

    /**
     * 根据充值类型id获取充值类型信息
     */
    void getRechargeTypeInfo(Long rechargeTypeId, BLResp resp);

    /**
     * 根据充值类型id更改充值类型信息
     */
    void updateRechargeType(Long id, String name, String remark, BLResp resp);

    /**
     * 获取顶级的行业分类列表
     */
    List<Map<String, Object>> getHierarchyIndustry();

    /**
     * 获取顶级的权限
     */
    List<Map<String, Object>> getHierarchyPrivilege();

    /**
     * 根据id获取权限信息
     */
    Map<String, Object> getPrivilegeInfo(Long id);

    /**
     * 修改权限信息
     */
    void editPrivilegeInfo(Long privilegeId, String name, BLResp resp);

    /**
     * 获取充值类型列表
     */
    List<Map<String, Object>> getRechargeDict();

    /**
     * 根据状态获取充值类型列表
     */
    List<Map<String, Object>> getRechargeTypeList(Integer enabled, Integer deleted);

    /**
     * 新增充值类型
     */
    void addRechargeType(String name, String remark, BLResp resp);

    /**
     * 根据条件获取行业分类列表
     */
    List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled);

    /**
     * 获取行业分类列表
     */
    BLResp getIndustryList(Page page);

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
    void setGlobalSetting(List<SysConfigDTO> sysConfigDTOList, BLResp resp);

    /**
     * 修改充值类型状态
     */
    void changeRechargeStatus(Long rechargeTypeId, Integer enabled, BLResp resp);

    /**
     * 根据行业分类id更改状态
     */
    void changeIndustryStatus(Long industryTypeId, Integer enabled, BLResp resp);

    /**
     * 根据权限id更改状态
     */
    void setModuleStatus(List<Long> moduleIdList, Integer status, BLResp resp);

    /**
     * 获取系统参数
     */
    Map<String, Object> getSettings();
}
