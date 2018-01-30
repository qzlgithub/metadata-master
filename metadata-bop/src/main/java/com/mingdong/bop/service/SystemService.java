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
     *
     * @return
     */
    List<Map<String, Object>> getValidRole();

    /**
     * 判断是否存在该code数据
     *
     * @param code
     * @return
     */
    boolean checkIndustryCodeExist(String code);

    /**
     * 根据行业分类id获取行业分类
     *
     * @param id
     * @return
     */
    Map<String, String> getIndustryInfo(Long id);

    /**
     * 根据parentId和状态获取行业分类列表
     *
     * @param parentId
     * @param enabled
     * @return
     */
    List<Map<String, Object>> getIndustryMap(Long parentId, Integer enabled);

    /**
     * 新增行业分类信息
     *
     * @param id
     * @param code
     * @param name
     * @param resp
     */
    void addIndustryType(Long id, String code, String name, BLResp resp);

    /**
     * 修改行业分类信息
     *
     * @param id
     * @param code
     * @param name
     * @param resp
     */
    void editIndustryInfo(Long id, String code, String name, BLResp resp);

    /**
     * 软删充值类型
     *
     * @param rechargeTypeId
     * @param resp
     */
    void dropRechargeType(Long rechargeTypeId, BLResp resp);

    /**
     * 根据充值类型id获取充值类型信息
     *
     * @param rechargeTypeId
     * @param resp
     */
    void getRechargeTypeInfo(Long rechargeTypeId, BLResp resp);

    /**
     * 根据充值类型id更改充值类型信息
     *
     * @param id
     * @param name
     * @param remark
     * @param resp
     */
    void updateRechargeType(Long id, String name, String remark, BLResp resp);

    /**
     * 获取产品信息列表
     *
     * @return
     */
    List<Map<String, Object>> getProductListMap();

    /**
     * 获取顶级的行业分类列表
     *
     * @return
     */
    List<Map<String, Object>> getHierarchyIndustry();

    /**
     * 获取顶级的权限
     *
     * @return
     */
    List<Map<String, Object>> getHierarchyPrivilege();

    /**
     * 根据id获取权限信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getPrivilegeInfo(Long id);

    /**
     * 修改权限信息
     *
     * @param privilegeId
     * @param name
     * @param resp
     */
    void editPrivilegeInfo(Long privilegeId, String name, BLResp resp);

    /**
     * 获取充值类型列表
     *
     * @return
     */
    List<Map<String, Object>> getRechargeDict();

    /**
     * 根据状态获取充值类型列表
     *
     * @param enabled
     * @param resp
     */
    void getRechargeTypeList(Integer enabled, BLResp resp);

    /**
     * 根据状态获取充值类型列表
     *
     * @param enabled
     * @param deleted
     * @return
     */
    List<Map<String, Object>> getRechargeTypeList(Integer enabled, Integer deleted);

    /**
     * 新增充值类型
     *
     * @param name
     * @param remark
     * @param resp
     */
    void addRechargeType(String name, String remark, BLResp resp);

    /**
     * 根据条件获取行业分类列表
     *
     * @param parentId
     * @param enabled
     * @return
     */
    List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled);

    /**
     * 获取行业分类列表
     *
     * @param page
     * @return
     */
    BLResp getIndustryList(Page page);

    /**
     * 获取行业分类列表
     *
     * @return
     */
    Map<String, Object> getInitIndustryMap();

    /**
     * 根据行业分类id获取行业分类name
     *
     * @param industryId
     * @return
     */
    String getIndustryName(Long industryId);

    /**
     * 获取系统模块
     *
     * @return
     */
    Map<String, String> cacheSystemModule();

    /**
     * 修改系统参数
     *
     * @param sysConfigDTOList
     * @param resp
     */
    void setGlobalSetting(List<SysConfigDTO> sysConfigDTOList, BLResp resp);

    /**
     * 修改充值类型状态
     *
     * @param rechargeTypeId
     * @param enabled
     * @param resp
     */
    void changeRechargeStatus(Long rechargeTypeId, Integer enabled, BLResp resp);

    /**
     * 根据行业分类id更改状态
     *
     * @param industryTypeId
     * @param enabled
     * @param resp
     */
    void changeIndustryStatus(Long industryTypeId, Integer enabled, BLResp resp);

    /**
     * 根据权限id更改状态
     *
     * @param moduleIdList
     * @param status
     * @param resp
     */
    void setModuleStatus(List<Long> moduleIdList, Integer status, BLResp resp);

    /**
     * 获取系统参数
     *
     * @return
     */
    Map<String, Object> getSettings();
}
