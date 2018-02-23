package com.mingdong.core.service;

import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.RechargeTypeDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.SysConfigDTO;

import java.util.List;
import java.util.Map;

public interface RemoteSystemService
{
    /**
     * 根据状态获取角色信息列表
     */
    RoleListDTO getRoleListByStatus(Integer aTrue);

    /**
     * 根据条件获取行业分类信息列表
     */
    DictIndustryListDTO getDictIndustryListByParentAndStatus(Long parentIndustryId, Integer trueOrFalse);

    /**
     * 根据行业分类id获取行业分类
     */
    DictIndustryDTO getDictIndustryById(Long industryId);

    /**
     * 根据行业分类code获取行业分类
     */
    DictIndustryDTO getDictIndustryByCode(String code);

    /**
     * 根据充值类型id获取充值类型
     */
    DictRechargeTypeDTO getDictRechargeTypeById(Integer rechargeTypeId);

    /**
     * 根据角色id获取权限信息列表
     */
    PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId);

    /**
     * 根据parentId和状态获取权限信息列表
     */
    PrivilegeListDTO getPrivilegeListByParentAndStatus(Long parentId, Integer enabled);

    /**
     * 根据权限id获取权限信息
     */
    PrivilegeDTO getPrivilegeById(Long id);

    /**
     * 根据状态获取充值类型列表
     */
    DictRechargeTypeListDTO getDictRechargeTypeListByStatus(Integer enabled, Integer deleted);

    /**
     * 根据级数获取权限列表
     */
    PrivilegeListDTO getPrivilegeByLevel(Integer level);

    /**
     * 获取行业分类列表
     */
    DictIndustryListDTO getDictIndustryInfoList();

    /**
     * 修改模块状态
     */
    ResultDTO setModuleStatus(Integer status, List<Long> moduleIdList);

    /**
     * 获取系统参数信息 key为name
     */
    Map<String, Object> getSettingData();

    IndustryDTO getIndustryDictOfTarget(Long industryId);

    /**
     * 新增行业分类，判断code
     */
    ResultDTO addIndustryType(DictIndustryDTO industry);

    /**
     * 修改行业分类，判断code
     */
    ResultDTO editIndustryInfo(DictIndustryDTO industry);

    /**
     * 软删充值类型
     */
    void setRechargeTypeDeleted(Integer rechargeTypeId);

    /**
     * 修改权限信息
     */
    ResultDTO editPrivilegeInfo(PrivilegeDTO privilege);

    /**
     * 新增or修改充值类型备注
     */
    ResultDTO addRechargeType(String name, String remark);

    /**
     * 新增or修改系统参数
     */
    ResultDTO addOrUpdateSetting(List<SysConfigDTO> sysConfigDTOList);

    /**
     * 修改行业分类状态
     */
    ResultDTO changeIndustryStatus(Long industryTypeId, Integer enabled);

    /**
     * 查询充值类型列表
     */
    ListDTO<RechargeTypeDTO> getRechargeList();

    /**
     * 根据ID编辑充值类型
     */
    ResultDTO editRechargeType(RechargeTypeDTO rechargeTypeDTO);
}
