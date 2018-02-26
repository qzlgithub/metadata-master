package com.mingdong.bop.service;

import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
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
    void dropRechargeType(Integer rechargeTypeId);

    /**
     * 根据充值类型id更改充值类型信息
     */
    void editRechargeType(Integer rechargeTypeId, String name, String remark, BLResp resp);

    /**
     * 修改充值类型状态
     */
    void enableRechargeType(Integer rechargeTypeId, Integer enabled, BLResp resp);

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

    /**
     * 获取系统配置-充值类型字典列表
     */
    void getRechargeTypeList(ListRes res);
}
