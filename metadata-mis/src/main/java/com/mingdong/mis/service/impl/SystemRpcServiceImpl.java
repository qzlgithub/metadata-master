package com.mingdong.mis.service.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.request.DictIndustryReqDTO;
import com.mingdong.core.model.dto.request.PrivilegeReqDTO;
import com.mingdong.core.model.dto.request.RechargeTypeReqDTO;
import com.mingdong.core.model.dto.request.SysConfigReqDTO;
import com.mingdong.core.model.dto.response.DictIndustryResDTO;
import com.mingdong.core.model.dto.response.DictRechargeTypeResDTO;
import com.mingdong.core.model.dto.response.IndustryResDTO;
import com.mingdong.core.model.dto.response.PrivilegeResDTO;
import com.mingdong.core.model.dto.response.RechargeTypeResDTO;
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
    public ListDTO<DictIndustryResDTO> getIndustryList(Long parentIndustryId, Integer enabled)
    {
        ListDTO<DictIndustryResDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dataList = dictIndustryMapper.getByParentAndStatus(parentIndustryId, enabled);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<DictIndustryResDTO> list = new ArrayList<>(dataList.size());
            for(DictIndustry o : dataList)
            {
                DictIndustryResDTO di = new DictIndustryResDTO();
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
    public ListDTO<PrivilegeResDTO> getPrivilegeListByParent(Long parentId)
    {
        ListDTO<PrivilegeResDTO> listDTO = new ListDTO<>();
        if(parentId == null)
        {
            parentId = 0L;
        }
        List<Function> functionList = functionMapper.getListByParent(parentId);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeResDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeResDTO p = new PrivilegeResDTO();
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
    public ListDTO<PrivilegeResDTO> getPrivilegeByLevel(Integer level)
    {
        ListDTO<PrivilegeResDTO> listDTO = new ListDTO<>();
        List<Function> functionList = functionMapper.getListByLevel(level);
        if(!CollectionUtils.isEmpty(functionList))
        {
            List<PrivilegeResDTO> list = new ArrayList<>(functionList.size());
            for(Function o : functionList)
            {
                PrivilegeResDTO p = new PrivilegeResDTO();
                p.setPrivilegeId(o.getId());
                p.setName(o.getName());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<DictRechargeTypeResDTO> getRechargeTypeList(Integer enabled, Integer deleted)
    {
        ListDTO<DictRechargeTypeResDTO> listDTO = new ListDTO<>();
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getListByStatus(enabled, deleted);
        if(!CollectionUtils.isEmpty(rechargeTypeList))
        {
            List<DictRechargeTypeResDTO> list = new ArrayList<>();
            for(DictRechargeType o : rechargeTypeList)
            {
                DictRechargeTypeResDTO drt = new DictRechargeTypeResDTO();
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
    public ListDTO<DictIndustryResDTO> getDictIndustryInfoList()
    {
        ListDTO<DictIndustryResDTO> listDTO = new ListDTO<>();
        List<DictIndustry> dictIndustryList = dictIndustryMapper.getIndustryInfo();
        if(!CollectionUtils.isEmpty(dictIndustryList))
        {
            List<DictIndustryResDTO> list = new ArrayList<>(dictIndustryList.size());
            for(DictIndustry o : dictIndustryList)
            {
                DictIndustryResDTO di = new DictIndustryResDTO();
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
    public IndustryResDTO getIndustryDictOfTarget(Long industryId)
    {
        IndustryResDTO dto = new IndustryResDTO();
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
    public ResponseDTO addIndustryType(DictIndustryReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        DictIndustry byCode = dictIndustryMapper.findByCode(reqDTO.getCode());
        if(byCode != null)
        {
            responseDTO.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return responseDTO;
        }
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(reqDTO, dictIndustry);
        dictIndustryMapper.add(dictIndustry);
        responseDTO.setResult(RestResult.SUCCESS);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO editIndustryInfo(DictIndustryReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        DictIndustry byCode = dictIndustryMapper.findByCode(reqDTO.getCode());
        if(byCode != null && !byCode.getId().equals(reqDTO.getId()))
        {
            res.setResult(RestResult.INDUSTRY_CODE_EXIST);
            return res;
        }
        DictIndustry byId = dictIndustryMapper.findById(reqDTO.getId());
        if(byId == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        DictIndustry dictIndustry = new DictIndustry();
        EntityUtils.copyProperties(reqDTO, dictIndustry);
        dictIndustryMapper.updateSkipNull(dictIndustry);
        res.setResult(RestResult.SUCCESS);
        return res;
    }

    @Override
    @Transactional
    public ResponseDTO editPrivilegeInfo(PrivilegeReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Function function = functionMapper.findById(reqDTO.getPrivilegeId());
        if(function == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Function temp = new Function();
        temp.setId(reqDTO.getPrivilegeId());
        temp.setUpdateTime(new Date());
        temp.setName(reqDTO.getName());
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
    public ResponseDTO addOrUpdateSetting(List<SysConfigReqDTO> sysConfigReqDTOList)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Date current = new Date();
        Sistem sistem;
        for(SysConfigReqDTO item : sysConfigReqDTOList)
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
    public ListDTO<RechargeTypeResDTO> getRechargeList()
    {
        ListDTO<RechargeTypeResDTO> res = new ListDTO<>();
        List<DictRechargeType> dataList = dictRechargeTypeMapper.getAvailableList();
        res.setTotal(dataList.size());
        List<RechargeTypeResDTO> list = new ArrayList<>();
        for(DictRechargeType o : dataList)
        {
            RechargeTypeResDTO dto = new RechargeTypeResDTO();
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
    public ResponseDTO editRechargeType(RechargeTypeReqDTO reqDTO)
    {
        ResponseDTO res = new ResponseDTO();
        DictRechargeType objUpd = dictRechargeTypeMapper.findById(reqDTO.getId());
        if(objUpd == null)
        {
            res.setResult(RestResult.OBJECT_NOT_FOUND);
            return res;
        }
        if(!StringUtils.isNullBlank(reqDTO.getName()))
        {
            DictRechargeType temp = dictRechargeTypeMapper.findByName(reqDTO.getName());
            if(temp != null && !temp.getId().equals(objUpd.getId()))
            {
                res.setResult(RestResult.CATEGORY_NAME_EXIST);
                return res;
            }
            objUpd.setName(reqDTO.getName());
            objUpd.setRemark(reqDTO.getRemark());
        }
        if(TrueOrFalse.TRUE.equals(reqDTO.getEnabled()) || TrueOrFalse.FALSE.equals(
                reqDTO.getEnabled()))
        {
            objUpd.setEnabled(reqDTO.getEnabled());
        }
        objUpd.setUpdateTime(new Date());
        dictRechargeTypeMapper.updateById(objUpd);
        return res;
    }
}
