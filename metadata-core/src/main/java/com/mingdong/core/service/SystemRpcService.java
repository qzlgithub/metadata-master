package com.mingdong.core.service;

import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.RechargeTypeDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SysConfigDTO;

import java.util.List;
import java.util.Map;

public interface SystemRpcService
{
    /**
     * 根据条件获取行业分类信息列表
     */
    ListDTO<DictIndustryDTO> getIndustryList(Long parentIndustryId, Integer enabled);

    /**
     * 查询指定父级的子级功能列表
     */
    ListDTO<PrivilegeDTO> getPrivilegeListByParent(Long parentId);

    /**
     * 根据菜单功能等级获取功能列表
     */
    ListDTO<PrivilegeDTO> getPrivilegeByLevel(Integer level);

    /**
     * 根据状态获取充值类型列表
     */
    ListDTO<DictRechargeTypeDTO> getRechargeTypeList(Integer enabled, Integer deleted);

    /**
     * 获取行业分类列表
     */
    ListDTO<DictIndustryDTO> getDictIndustryInfoList();

    /**
     * 修改模块状态
     */
    void setModuleStatus(Integer status, List<Long> moduleIdList);

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
