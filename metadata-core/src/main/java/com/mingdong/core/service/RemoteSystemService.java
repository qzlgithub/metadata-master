package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.SysConfigDTO;

import java.util.List;
import java.util.Map;

public interface RemoteSystemService
{
    /**
     * 根据状态获取角色信息列表
     *
     * @param aTrue
     * @return
     */
    RoleListDTO getRoleListByStatus(Integer aTrue);

    /**
     * 根据条件获取行业分类信息列表
     *
     * @param parentIndustryId
     * @param trueOrFalse
     * @return
     */
    DictIndustryListDTO getDictIndustryListByParentAndStatus(Long parentIndustryId, Integer trueOrFalse);

    /**
     * 根据行业分类id获取行业分类
     *
     * @param industryId
     * @return
     */
    DictIndustryDTO getDictIndustryById(Long industryId);

    /**
     * 根据行业分类code获取行业分类
     *
     * @param code
     * @return
     */
    DictIndustryDTO getDictIndustryByCode(String code);

    /**
     * 新增行业分类
     *
     * @param industry
     * @return
     */
    ResultDTO saveDictIndustry(DictIndustryDTO industry);

    /**
     * 更改行业分类信息，null值不修改
     *
     * @param industry
     * @return
     */
    ResultDTO updateDictIndustrySkipNull(DictIndustryDTO industry);

    /**
     * 根据充值类型id获取充值类型
     *
     * @param rechargeTypeId
     * @return
     */
    DictRechargeTypeDTO getDictRechargeTypeById(Long rechargeTypeId);

    /**
     * 更改充值类型，null值不修改
     *
     * @param rechargeType
     * @return
     */
    ResultDTO updateDictRechargeTypeSkipNull(DictRechargeTypeDTO rechargeType);

    /**
     * 根据name获取充值类型
     *
     * @param name
     * @return
     */
    DictRechargeTypeDTO getDictRechargeTypeByName(String name);

    /**
     * 根据充值类型id更改充值类型
     *
     * @param rechargeType
     * @return
     */
    ResultDTO updateDictRechargeTypeById(DictRechargeTypeDTO rechargeType);

    /**
     * 根据栏目ids获取权限信息列表
     *
     * @param ids
     * @return
     */
    PrivilegeListDTO getPrivilegeListByIds(List<Long> ids);

    /**
     * 根据角色id获取权限信息列表
     *
     * @param roleId
     * @return
     */
    PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId);

    /**
     * 根据parentId和状态获取权限信息列表
     *
     * @param parentId
     * @param enabled
     * @return
     */
    PrivilegeListDTO getPrivilegeListByParentAndStatus(Long parentId, Integer enabled);

    /**
     * 根据权限id获取权限信息
     *
     * @param id
     * @return
     */
    PrivilegeDTO getPrivilegeById(Long id);

    /**
     * 更改权限信息，null值不修改
     *
     * @param privilege
     * @return
     */
    ResultDTO updatePrivilegeSkipNull(PrivilegeDTO privilege);

    /**
     * 根据状态获取充值类型列表
     *
     * @param enabled
     * @param deleted
     * @return
     */
    DictRechargeTypeListDTO getDictRechargeTypeListByStatus(Integer enabled, Integer deleted);

    /**
     * 新增充值类型
     *
     * @param type
     * @return
     */
    ResultDTO saveDictRechargeType(DictRechargeTypeDTO type);

    /**
     * 获取行业分类列表
     *
     * @param page
     * @return
     */
    DictIndustryListDTO getDictIndustryList(Page page);

    /**
     * 根据级数获取权限列表
     *
     * @param level
     * @return
     */
    PrivilegeListDTO getPrivilegeByLevel(Integer level);

    /**
     * 根据name获取系统参数
     *
     * @param name
     * @return
     */
    SysConfigDTO getSysConfigByName(String name);

    /**
     * 新增系统参数
     *
     * @param sysConfig
     * @return
     */
    ResultDTO saveSysConfig(SysConfigDTO sysConfig);

    /**
     * 根据系统参数id更改系统参数
     *
     * @param sysConfig
     * @return
     */
    ResultDTO updateSysConfigById(SysConfigDTO sysConfig);

    /**
     * 获取行业分类列表
     *
     * @return
     */
    DictIndustryListDTO getDictIndustryInfoList();

    /**
     * 修改模块状态
     *
     * @param status
     * @param moduleIdList
     */
    ResultDTO setModuleStatus(Integer status, List<Long> moduleIdList);

    /**
     * 获取系统参数信息 key为name
     *
     * @return
     */
    Map<String, Object> getSettingData();

    IndustryDTO getIndustryDictOfTarget(Long industryId);

    /**
     * 新增行业分类，判断code
     *
     * @param industry
     * @return
     */
    ResultDTO addIndustryType(DictIndustryDTO industry);

    /**
     * 修改行业分类，判断code
     *
     * @param industry
     * @return
     */
    ResultDTO editIndustryInfo(DictIndustryDTO industry);

    /**
     * 软删充值类型
     *
     * @param rechargeType
     * @return
     */
    ResultDTO dropRechargeType(DictRechargeTypeDTO rechargeType);

    /**
     * 修改权限信息
     *
     * @param privilege
     * @return
     */
    ResultDTO editPrivilegeInfo(PrivilegeDTO privilege);

    /**
     * 新增or修改充值类型备注
     *
     * @param name
     * @param remark
     * @return
     */
    ResultDTO addOrUpdateRechargeTypeByName(String name, String remark);

    /**
     * 新增or修改系统参数
     *
     * @param sysConfigDTOList
     * @return
     */
    ResultDTO addOrUpdateSetting(List<SysConfigDTO> sysConfigDTOList);

    /**
     * 修改充值类型状态
     *
     * @param rechargeTypeId
     * @param enabled
     * @return
     */
    ResultDTO changeRechargeStatus(Long rechargeTypeId, Integer enabled);

    /**
     * 修改行业分类状态
     *
     * @param industryTypeId
     * @param enabled
     * @return
     */
    ResultDTO changeIndustryStatus(Long industryTypeId, Integer enabled);
}
