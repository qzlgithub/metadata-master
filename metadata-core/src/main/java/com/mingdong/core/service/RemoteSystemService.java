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
    RoleListDTO getRoleListByStatus(Integer aTrue);

    DictIndustryListDTO getDictIndustryListByParentAndStatus(Long parentIndustryId, Integer trueOrFalse);

    DictIndustryDTO getDictIndustryById(Long industryId);

    DictIndustryDTO getDictIndustryByCode(String code);

    ResultDTO saveDictIndustry(DictIndustryDTO industry);

    ResultDTO updateDictIndustrySkipNull(DictIndustryDTO industry);

    DictRechargeTypeDTO getDictRechargeTypeById(Long rechargeTypeId);

    ResultDTO updateDictRechargeTypeSkipNull(DictRechargeTypeDTO rechargeType);

    DictRechargeTypeDTO getDictRechargeTypeByName(String name);

    ResultDTO updateDictRechargeTypeById(DictRechargeTypeDTO rechargeType);

    PrivilegeListDTO getPrivilegeListByIds(List<Long> ids);

    PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId);

    PrivilegeListDTO getPrivilegeListByParentAndStatus(Long parentId, Integer enabled);

    PrivilegeDTO getPrivilegeById(Long id);

    ResultDTO updatePrivilegeSkipNull(PrivilegeDTO privilege);

    DictRechargeTypeListDTO getDictRechargeTypeListByStatus(Integer enabled, Integer deleted);

    ResultDTO saveDictRechargeType(DictRechargeTypeDTO type);

    DictIndustryListDTO getDictIndustryList(Page page);

    PrivilegeListDTO getPrivilegeByLevel(Integer level);

    SysConfigDTO getSysConfigByName(String name);

    ResultDTO saveSysConfig(SysConfigDTO sysConfig);

    ResultDTO updateSysConfigById(SysConfigDTO sysConfig);

    DictIndustryListDTO getDictIndustryInfoList();

    PrivilegeListDTO getPrivilegeByParent(Long parentModuleId);

    void setModuleStatus(Integer status, List<Long> moduleIdList);

    Map<String, Object> getSettingData();

    IndustryDTO getIndustryDictOfTarget(Long industryId);
}
