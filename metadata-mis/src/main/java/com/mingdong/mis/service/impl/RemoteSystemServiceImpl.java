package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.DictDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        DictIndustry byCode = dictIndustryMapper.findByCode(code);
        if(byCode == null)
        {
            return null;
        }
        EntityUtils.copyProperties(byCode, dictIndustryDTO);
        return dictIndustryDTO;
    }

    @Override
    public DictRechargeTypeDTO getDictRechargeTypeById(Integer rechargeTypeId)
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
    @Transactional
    public ResultDTO setModuleStatus(Integer status, List<Long> moduleIdList)
    {
        ResultDTO resultDTO = new ResultDTO();
        privilegeMapper.updateModuleStatusByIds(status, new Date(), moduleIdList);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public Map<String, Object> getSettingData()
    {
        List<SysConfig> list = sysConfigMapper.getAll();
        Map<String, Object> map = new HashMap<>();
        for(SysConfig o : list)
        {
            map.put(o.getName(), o.getValue());
        }
        return map;
    }

    @Override
    public IndustryDTO getIndustryDictOfTarget(Long industryId)
    {
        IndustryDTO dto = new IndustryDTO();
        DictIndustry self = dictIndustryMapper.findById(industryId);
        if(self == null)
        {
            dto.setResult(RestResult.OBJECT_NOT_FOUND);
            return dto;
        }
        dto.setParentId(self.getParentId());
        List<DictIndustry> peerList = dictIndustryMapper.getByParentAndStatus(self.getParentId(), TrueOrFalse.TRUE);
        List<DictDTO> peers = new ArrayList<>();
        for(DictIndustry d : peerList)
        {
            peers.add(new DictDTO(d.getId() + "", d.getName()));
        }
        dto.setPeers(peers);
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, TrueOrFalse.TRUE);
        List<DictDTO> parents = new ArrayList<>();
        for(DictIndustry d : parentList)
        {
            parents.add(new DictDTO(d.getId() + "", d.getName()));
        }
        dto.setParents(parents);
        return dto;
    }

    @Override
    @Transactional
    public ResultDTO addIndustryType(DictIndustryDTO industry)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictIndustry byCode = dictIndustryMapper.findByCode(industry.getCode());
        if(byCode != null)
        {
            resultDTO.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return resultDTO;
        }
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(industry, dictIndustry);
        dictIndustryMapper.add(dictIndustry);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO editIndustryInfo(DictIndustryDTO dictIndustryDTO)
    {
        ResultDTO res = new ResultDTO();
        DictIndustry byCode = dictIndustryMapper.findByCode(dictIndustryDTO.getCode());
        if(byCode != null && !byCode.getId().equals(dictIndustryDTO.getId()))
        {
            res.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return res;
        }
        DictIndustry byId = dictIndustryMapper.findById(dictIndustryDTO.getId());
        if(byId == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(dictIndustryDTO, dictIndustry);
        dictIndustryMapper.updateSkipNull(dictIndustry);
        res.setResult(RestResult.SUCCESS);
        return res;
    }

    @Override
    @Transactional
    public void setRechargeTypeDeleted(Integer rechargeTypeId)
    {
        DictRechargeType obj = new DictRechargeType();
        obj.setId(rechargeTypeId);
        obj.setUpdateTime(new Date());
        obj.setDeleted(TrueOrFalse.TRUE);
        dictRechargeTypeMapper.updateSkipNull(obj);
    }

    @Override
    @Transactional
    public ResultDTO editPrivilegeInfo(PrivilegeDTO privilegeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Privilege privilege = privilegeMapper.findById(privilegeDTO.getId());
        if(privilege == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        privilege = new Privilege();
        EntityUtils.copyProperties(privilegeDTO, privilege);
        privilegeMapper.updateSkipNull(privilege);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO addRechargeType(String name, String remark)
    {
        Date current = new Date();
        ResultDTO res = new ResultDTO();
        DictRechargeType rechargeType = dictRechargeTypeMapper.findByName(name);
        if(rechargeType != null && TrueOrFalse.FALSE.equals(rechargeType.getDeleted()))
        {
            res.setResult(RestResult.CATEGORY_NAME_EXIST);
            return res;
        }
        if(rechargeType == null)
        {
            rechargeType = new DictRechargeType();
            rechargeType.setCreateTime(current);
            rechargeType.setUpdateTime(current);
            rechargeType.setName(name);
            rechargeType.setRemark(remark);
            rechargeType.setEnabled(TrueOrFalse.TRUE);
            rechargeType.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.add(rechargeType);
        }
        else
        {
            rechargeType.setUpdateTime(current);
            rechargeType.setRemark(remark);
            rechargeType.setEnabled(TrueOrFalse.TRUE);
            rechargeType.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.updateById(rechargeType);
        }
        return res;
    }

    @Override
    @Transactional
    public ResultDTO addOrUpdateSetting(List<SysConfigDTO> sysConfigDTOList)
    {
        ResultDTO resultDTO = new ResultDTO();
        Date current = new Date();
        SysConfig sysConfig;
        for(SysConfigDTO item : sysConfigDTOList)
        {
            sysConfig = sysConfigMapper.findByName(item.getName());
            if(sysConfig == null)
            {
                sysConfig = new SysConfig();
                sysConfig.setCreateTime(current);
                sysConfig.setUpdateTime(current);
                sysConfig.setName(item.getName());
                sysConfig.setValue(item.getValue());
                sysConfigMapper.add(sysConfig);
            }
            else if(!sysConfig.getValue().equals(item.getValue()))
            {
                sysConfig.setUpdateTime(current);
                sysConfig.setValue(item.getValue());
                sysConfigMapper.updateById(sysConfig);
            }
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO changeIndustryStatus(Long industryTypeId, Integer enabled)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictIndustry byId = dictIndustryMapper.findById(industryTypeId);
        if(byId != null && !enabled.equals(byId.getEnabled()))
        {
            DictIndustry updObj = new DictIndustry();
            updObj.setId(industryTypeId);
            updObj.setUpdateTime(new Date());
            updObj.setEnabled(enabled);
            dictIndustryMapper.updateSkipNull(updObj);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    public ListDTO<RechargeTypeDTO> getRechargeList()
    {
        ListDTO<RechargeTypeDTO> res = new ListDTO<>();
        List<DictRechargeType> dataList = dictRechargeTypeMapper.getAvailableList();
        res.setTotal(dataList.size());
        List<RechargeTypeDTO> list = new ArrayList<>();
        for(DictRechargeType o : dataList)
        {
            RechargeTypeDTO dto = new RechargeTypeDTO();
            dto.setId(o.getId());
            dto.setName(o.getName());
            dto.setRemark(o.getRemark());
            dto.setEnabled(o.getEnabled());
            dto.setAddAt(o.getCreateTime());
            list.add(dto);
        }
        res.setList(list);
        return res;
    }

    @Override
    @Transactional
    public ResultDTO editRechargeType(RechargeTypeDTO rechargeTypeDTO)
    {
        ResultDTO res = new ResultDTO();
        DictRechargeType objUpd = dictRechargeTypeMapper.findById(rechargeTypeDTO.getId());
        if(objUpd == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        if(!StringUtils.isNullBlank(rechargeTypeDTO.getName()))
        {
            DictRechargeType temp = dictRechargeTypeMapper.findByName(rechargeTypeDTO.getName());
            if(temp != null && !temp.getId().equals(objUpd.getId()))
            {
                res.setResult(RestResult.CATEGORY_NAME_EXIST);
                return res;
            }
            objUpd.setName(rechargeTypeDTO.getName());
            objUpd.setRemark(rechargeTypeDTO.getRemark());
        }
        if(TrueOrFalse.TRUE.equals(rechargeTypeDTO.getEnabled()) || TrueOrFalse.FALSE.equals(
                rechargeTypeDTO.getEnabled()))
        {
            objUpd.setEnabled(rechargeTypeDTO.getEnabled());
        }
        objUpd.setUpdateTime(new Date());
        dictRechargeTypeMapper.updateById(objUpd);
        return res;
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
