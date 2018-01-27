package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.RemoteSystemService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.DictRechargeType;
import com.mingdong.mis.domain.entity.Privilege;
import com.mingdong.mis.domain.entity.Role;
import com.mingdong.mis.domain.entity.SysConfig;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.DictRechargeTypeMapper;
import com.mingdong.mis.domain.mapper.PrivilegeMapper;
import com.mingdong.mis.domain.mapper.RoleMapper;
import com.mingdong.mis.domain.mapper.SysConfigMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteSystemServiceImpl implements RemoteSystemService
{
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private DictRechargeTypeMapper dictRechargeTypeMapper;
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public RoleListDTO getRoleListByStatus(Integer aTrue)
    {
        RoleListDTO roleListDTO = new RoleListDTO();
        List<RoleDTO> dataList = new ArrayList<>();
        roleListDTO.setDataList(dataList);
        RoleDTO roleDTO;
        List<Role> roleList = roleMapper.getByStatus(TrueOrFalse.TRUE);
        if(CollectionUtils.isNotEmpty(roleList))
        {
            for(Role item : roleList)
            {
                roleDTO = new RoleDTO();
                EntityUtils.copyProperties(item, roleDTO);
                dataList.add(roleDTO);
            }
        }
        return roleListDTO;
    }

    @Override
    public DictIndustryListDTO getDictIndustryListByParentAndStatus(Long parentIndustryId, Integer trueOrFalse)
    {
        DictIndustryListDTO dictIndustryListDTO = new DictIndustryListDTO();
        List<DictIndustryDTO> dataList = new ArrayList<>();
        dictIndustryListDTO.setDataList(dataList);
        List<DictIndustry> dictIndustryList = dictIndustryMapper.getByParentAndStatus(parentIndustryId, trueOrFalse);
        if(CollectionUtils.isNotEmpty(dictIndustryList))
        {
            DictIndustryDTO dictIndustryDTO;
            for(DictIndustry item : dictIndustryList)
            {
                dictIndustryDTO = new DictIndustryDTO();
                EntityUtils.copyProperties(item, dictIndustryDTO);
                dataList.add(dictIndustryDTO);
            }
            findDictIndustryDTO(dictIndustryList, dataList);
        }
        return dictIndustryListDTO;
    }

    @Override
    public DictIndustryDTO getDictIndustryById(Long industryId)
    {
        DictIndustryDTO dictIndustryDTO = new DictIndustryDTO();
        DictIndustry byId = dictIndustryMapper.findById(industryId);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, dictIndustryDTO);
        return dictIndustryDTO;
    }

    @Override
    public DictIndustryDTO getDictIndustryByCode(String code)
    {
        DictIndustryDTO dictIndustryDTO = new DictIndustryDTO();
        DictIndustry byId = dictIndustryMapper.findByCode(code);
        if(byId == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byId, dictIndustryDTO);
        return dictIndustryDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveDictIndustry(DictIndustryDTO dictProductTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(dictProductTypeDTO, dictIndustry);
        dictIndustryMapper.add(dictIndustry);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateDictIndustrySkipNull(DictIndustryDTO dictIndustryDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(dictIndustryDTO, dictIndustry);
        dictIndustryMapper.updateSkipNull(dictIndustry);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public DictRechargeTypeDTO getDictRechargeTypeById(Long rechargeTypeId)
    {
        DictRechargeTypeDTO dictRechargeTypeDTO = new DictRechargeTypeDTO();
        DictRechargeType rechargeType = dictRechargeTypeMapper.findById(rechargeTypeId);
        if(rechargeType == null)
        {
            return null;
        }
        EntityUtils.copyProperties(rechargeType, dictRechargeTypeDTO);
        return dictRechargeTypeDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateDictRechargeTypeSkipNull(DictRechargeTypeDTO dictRechargeTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictRechargeType dictRechargeType = new DictRechargeType();
        EntityUtils.copyProperties(dictRechargeTypeDTO, dictRechargeType);
        dictRechargeTypeMapper.updateSkipNull(dictRechargeType);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public DictRechargeTypeDTO getDictRechargeTypeByName(String name)
    {
        DictRechargeTypeDTO dictRechargeTypeDTO = new DictRechargeTypeDTO();
        DictRechargeType rechargeType = dictRechargeTypeMapper.findByName(name);
        if(rechargeType == null)
        {
            return null;
        }
        EntityUtils.copyProperties(rechargeType, dictRechargeTypeDTO);
        return dictRechargeTypeDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateDictRechargeTypeById(DictRechargeTypeDTO dictRechargeTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictRechargeType dictRechargeType = new DictRechargeType();
        EntityUtils.copyProperties(dictRechargeTypeDTO, dictRechargeType);
        dictRechargeTypeMapper.updateById(dictRechargeType);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeListByIds(List<Long> ids)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        List<Privilege> privilegeList = privilegeMapper.getParentIdByChildId(ids);
        if(CollectionUtils.isNotEmpty(privilegeList))
        {
            findPrivilegeDTO(privilegeList, dataList);
        }
        return privilegeListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeTopListByRoleId(Long roleId)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        List<Privilege> privilegeList = privilegeMapper.getTopListByRole(roleId);
        if(CollectionUtils.isNotEmpty(privilegeList))
        {
            findPrivilegeDTO(privilegeList, dataList);
        }
        return privilegeListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeListByParentAndStatus(Long parentId, Integer enabled)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        List<Privilege> privilegeList = privilegeMapper.getByParentAndStatus(parentId, enabled);
        if(CollectionUtils.isNotEmpty(privilegeList))
        {
            findPrivilegeDTO(privilegeList, dataList);
        }
        return privilegeListDTO;
    }

    @Override
    public PrivilegeDTO getPrivilegeById(Long id)
    {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        Privilege privilege = privilegeMapper.findById(id);
        if(privilege == null)
        {
            return null;
        }
        EntityUtils.copyProperties(privilege, privilegeDTO);
        return privilegeDTO;
    }

    @Override
    @Transactional
    public ResultDTO updatePrivilegeSkipNull(PrivilegeDTO privilegeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Privilege privilege = new Privilege();
        EntityUtils.copyProperties(privilegeDTO, privilege);
        privilegeMapper.updateSkipNull(privilege);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public DictRechargeTypeListDTO getDictRechargeTypeListByStatus(Integer enabled, Integer deleted)
    {
        DictRechargeTypeListDTO dictRechargeTypeListDTO = new DictRechargeTypeListDTO();
        List<DictRechargeTypeDTO> dataList = new ArrayList<>();
        dictRechargeTypeListDTO.setDataList(dataList);
        DictRechargeTypeDTO dictRechargeTypeDTO;
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getListByStatus(enabled, deleted);
        if(CollectionUtils.isNotEmpty(rechargeTypeList))
        {
            for(DictRechargeType item : rechargeTypeList)
            {
                dictRechargeTypeDTO = new DictRechargeTypeDTO();
                EntityUtils.copyProperties(item, dictRechargeTypeDTO);
                dataList.add(dictRechargeTypeDTO);
            }
        }
        return dictRechargeTypeListDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveDictRechargeType(DictRechargeTypeDTO dictRechargeTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictRechargeType dictRechargeType = new DictRechargeType();
        EntityUtils.copyProperties(dictRechargeTypeDTO, dictRechargeType);
        dictRechargeTypeMapper.add(dictRechargeType);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public DictIndustryListDTO getDictIndustryList(Page page)
    {
        DictIndustryListDTO dictIndustryListDTO = new DictIndustryListDTO();
        List<DictIndustryDTO> dataList = new ArrayList<>();
        dictIndustryListDTO.setDataList(dataList);
        if(page == null)
        {
            List<DictIndustry> dictIndustryList = dictIndustryMapper.getAll();
            if(CollectionUtils.isNotEmpty(dictIndustryList))
            {
                findDictIndustryDTO(dictIndustryList, dataList);
            }
        }
        else
        {
            int total = dictIndustryMapper.countAll();
            int pages = page.getTotalPage(total);
            dictIndustryListDTO.setPages(pages);
            dictIndustryListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<DictIndustry> dictIndustryList = dictIndustryMapper.getAll();
                if(CollectionUtils.isNotEmpty(dictIndustryList))
                {
                    findDictIndustryDTO(dictIndustryList, dataList);
                }
            }
        }
        return dictIndustryListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeByLevel(Integer level)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        privilegeListDTO.setDataList(dataList);
        List<Privilege> privilegeList = privilegeMapper.getListByLevel(level);
        if(CollectionUtils.isNotEmpty(privilegeList))
        {
            findPrivilegeDTO(privilegeList, dataList);
        }
        return privilegeListDTO;
    }

    @Override
    public SysConfigDTO getSysConfigByName(String name)
    {
        SysConfigDTO sysConfigDTO = new SysConfigDTO();
        SysConfig sysConfig = sysConfigMapper.findByName(name);
        if(sysConfig == null)
        {
            return null;
        }
        EntityUtils.copyProperties(sysConfig, sysConfigDTO);
        return sysConfigDTO;
    }

    @Override
    @Transactional
    public ResultDTO saveSysConfig(SysConfigDTO sysConfigDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        SysConfig sysConfig = new SysConfig();
        EntityUtils.copyProperties(sysConfigDTO, sysConfig);
        sysConfigMapper.add(sysConfig);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateSysConfigById(SysConfigDTO sysConfigDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        SysConfig sysConfig = new SysConfig();
        EntityUtils.copyProperties(sysConfigDTO, sysConfig);
        sysConfigMapper.updateById(sysConfig);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public DictIndustryListDTO getDictIndustryInfoList()
    {
        DictIndustryListDTO dictIndustryListDTO = new DictIndustryListDTO();
        List<DictIndustryDTO> dataList = new ArrayList<>();
        dictIndustryListDTO.setDataList(dataList);
        List<DictIndustry> dictIndustryList = dictIndustryMapper.getIndustryInfo();
        if(CollectionUtils.isNotEmpty(dictIndustryList))
        {
            findDictIndustryDTO(dictIndustryList, dataList);
        }
        return dictIndustryListDTO;
    }

    @Override
    public PrivilegeListDTO getPrivilegeByParent(Long parentModuleId)
    {
        PrivilegeListDTO privilegeListDTO = new PrivilegeListDTO();
        List<PrivilegeDTO> dataList = new ArrayList<>();
        List<Privilege> privilegeList = privilegeMapper.getByParent(parentModuleId);
        if(CollectionUtils.isNotEmpty(privilegeList))
        {
            findPrivilegeDTO(privilegeList, dataList);
        }
        return privilegeListDTO;
    }

    @Override
    @Transactional
    public void setModuleStatus(Integer status, List<Long> moduleIdList)
    {
        privilegeMapper.updateModuleStatusByIds(status, new Date(), moduleIdList);
    }

    private void findDictIndustryDTO(List<DictIndustry> dictIndustryList, List<DictIndustryDTO> dataList)
    {
        DictIndustryDTO dictIndustryDTO;
        for(DictIndustry item : dictIndustryList)
        {
            dictIndustryDTO = new DictIndustryDTO();
            EntityUtils.copyProperties(item, dictIndustryDTO);
            dataList.add(dictIndustryDTO);
        }
    }

    private void findPrivilegeDTO(List<Privilege> privilegeList, List<PrivilegeDTO> dataList)
    {
        PrivilegeDTO privilegeDTO;
        for(Privilege item : privilegeList)
        {
            privilegeDTO = new PrivilegeDTO();
            EntityUtils.copyProperties(item, privilegeDTO);
            dataList.add(privilegeDTO);
        }
    }
}
