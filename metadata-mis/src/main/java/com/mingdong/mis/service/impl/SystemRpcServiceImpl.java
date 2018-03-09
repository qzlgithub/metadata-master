package com.mingdong.mis.service.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.IndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.RechargeTypeDTO;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;
import com.mingdong.core.service.SystemRpcService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.DictIndustry;
import com.mingdong.mis.domain.entity.DictRechargeType;
import com.mingdong.mis.domain.entity.Function;
import com.mingdong.mis.domain.entity.Sistem;
import com.mingdong.mis.domain.mapper.DictIndustryMapper;
import com.mingdong.mis.domain.mapper.DictRechargeTypeMapper;
import com.mingdong.mis.domain.mapper.FunctionMapper;
import com.mingdong.mis.domain.mapper.SistemMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemRpcServiceImpl implements SystemRpcService
{
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private FunctionMapper functionMapper;
    @Resource
    private DictRechargeTypeMapper dictRechargeTypeMapper;
    @Resource
    private SistemMapper sistemMapper;

    @Override
    public ListDTO<DictIndustryDTO> getIndustryList(Long parentIndustryId, Integer enabled)
    {
        ListDTO<DictIndustryDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dataList = dictIndustryMapper.getByParentAndStatus(parentIndustryId, enabled);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<DictIndustryDTO> list = new ArrayList<>(dataList.size());
            for(DictIndustry o : dataList)
            {
                DictIndustryDTO di = new DictIndustryDTO();
                di.setId(o.getId());
                di.setCode(o.getCode());
                di.setName(o.getName());
                di.setEnabled(o.getEnabled());
                di.setParentId(o.getParentId());
                list.add(di);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<PrivilegeDTO> getPrivilegeListByParent(Long parentId)
    {
        ListDTO<PrivilegeDTO> listDTO = new ListDTO<>();
        if(parentId == null)
        {
            parentId = 0L;
        }
        List<Function> functionList = functionMapper.getListByParent(parentId);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeDTO p = new PrivilegeDTO();
                p.setPrivilegeId(o.getId());
                p.setName(o.getName());
                p.setEnabled(o.getEnabled());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<PrivilegeDTO> getPrivilegeByLevel(Integer level)
    {
        ListDTO<PrivilegeDTO> listDTO = new ListDTO<>();
        List<Function> functionList = functionMapper.getListByLevel(level);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeDTO p = new PrivilegeDTO();
                p.setPrivilegeId(o.getId());
                p.setName(o.getName());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictRechargeTypeDTO> getRechargeTypeList(Integer enabled, Integer deleted)
    {
        ListDTO<DictRechargeTypeDTO> listDTO = new ListDTO<>();
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getListByStatus(enabled, deleted);
        if(!CollectionUtils.isEmpty(rechargeTypeList))
        {
            List<DictRechargeTypeDTO> list = new ArrayList<>();
            for(DictRechargeType o : rechargeTypeList)
            {
                DictRechargeTypeDTO drt = new DictRechargeTypeDTO();
                drt.setId(o.getId());
                drt.setName(o.getName());
                drt.setRemark(o.getRemark());
                drt.setEnabled(o.getEnabled());
                list.add(drt);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictIndustryDTO> getDictIndustryInfoList()
    {
        ListDTO<DictIndustryDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dictIndustryList = dictIndustryMapper.getIndustryInfo();
        if(!CollectionUtils.isEmpty(dictIndustryList))
        {
            List<DictIndustryDTO> list = new ArrayList<>(dictIndustryList.size());
            for(DictIndustry o : dictIndustryList)
            {
                DictIndustryDTO di = new DictIndustryDTO();
                di.setId(o.getId());
                di.setName(o.getName());
                list.add(di);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    @Transactional
    public void setModuleStatus(Integer status, List<Long> moduleIdList)
    {
        functionMapper.updateModuleStatusByIds(status, new Date(), moduleIdList);
    }

    @Override
    public Map<String, Object> getSettingData()
    {
        List<Sistem> list = sistemMapper.getAll();
        Map<String, Object> map = new HashMap<>();
        for(Sistem o : list)
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
        List<Dict> peers = new ArrayList<>();
        for(DictIndustry d : peerList)
        {
            peers.add(new Dict(d.getId() + "", d.getName()));
        }
        dto.setPeers(peers);
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, TrueOrFalse.TRUE);
        List<Dict> parents = new ArrayList<>();
        for(DictIndustry d : parentList)
        {
            parents.add(new Dict(d.getId() + "", d.getName()));
        }
        dto.setParents(parents);
        return dto;
    }

    @Override
    @Transactional
    public ResponseDTO addIndustryType(DictIndustryDTO industry)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        DictIndustry byCode = dictIndustryMapper.findByCode(industry.getCode());
        if(byCode != null)
        {
            responseDTO.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return responseDTO;
        }
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(industry, dictIndustry);
        dictIndustryMapper.add(dictIndustry);
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editIndustryInfo(DictIndustryDTO dictIndustryDTO)
    {
        ResponseDTO res = new ResponseDTO();
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
    public ResponseDTO editPrivilegeInfo(PrivilegeDTO privilegeDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Function function = functionMapper.findById(privilegeDTO.getPrivilegeId());
        if(function == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Function temp = new Function();
        temp.setId(privilegeDTO.getPrivilegeId());
        temp.setUpdateTime(new Date());
        temp.setName(privilegeDTO.getName());
        functionMapper.updateSkipNull(temp);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addRechargeType(String name, String remark)
    {
        Date current = new Date();
        ResponseDTO res = new ResponseDTO();
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
    public ResponseDTO addOrUpdateSetting(List<SysConfigDTO> sysConfigDTOList)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Date current = new Date();
        Sistem sistem;
        for(SysConfigDTO item : sysConfigDTOList)
        {
            sistem = sistemMapper.findByName(item.getName());
            if(sistem == null)
            {
                sistem = new Sistem();
                sistem.setCreateTime(current);
                sistem.setUpdateTime(current);
                sistem.setName(item.getName());
                sistem.setValue(item.getValue());
                sistemMapper.add(sistem);
            }
            else if(!sistem.getValue().equals(item.getValue()))
            {
                sistem.setUpdateTime(current);
                sistem.setValue(item.getValue());
                sistemMapper.updateById(sistem);
            }
        }
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeIndustryStatus(Long industryTypeId, Integer enabled)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        DictIndustry byId = dictIndustryMapper.findById(industryTypeId);
        if(byId != null && !enabled.equals(byId.getEnabled()))
        {
            DictIndustry updObj = new DictIndustry();
            updObj.setId(industryTypeId);
            updObj.setUpdateTime(new Date());
            updObj.setEnabled(enabled);
            dictIndustryMapper.updateSkipNull(updObj);
        }
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
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
    public ResponseDTO editRechargeType(RechargeTypeDTO rechargeTypeDTO)
    {
        ResponseDTO res = new ResponseDTO();
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
}
