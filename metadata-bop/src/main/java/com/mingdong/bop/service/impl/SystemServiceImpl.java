package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.RechargeTypeDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.CommonRpcService;
import com.mingdong.core.service.SystemRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private CommonRpcService commonRpcService;
    @Resource
    private SystemRpcService systemRpcService;

    @Override
    public void checkIfIndustryExist(String code, RestResp resp)
    {
        resp.addData(Field.EXIST, commonRpcService.checkIfIndustryExist(code.toUpperCase()));
    }

    @Override
    public void addIndustryType(Long id, String code, String name, RestResp resp)
    {
        Date current = new Date();
        DictIndustryDTO industry = new DictIndustryDTO();
        industry.setCreateTime(current);
        industry.setUpdateTime(current);
        industry.setCode(code.toUpperCase());
        industry.setName(name);
        industry.setSeqNo(1); // TODO 序号
        industry.setParentId(id != null ? id : 0L);
        industry.setEnabled(TrueOrFalse.TRUE);
        ResultDTO resultDTO = systemRpcService.addIndustryType(industry);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editIndustryInfo(Long id, String code, String name, RestResp resp)
    {
        DictIndustryDTO industry = new DictIndustryDTO();
        industry.setId(id);
        industry.setUpdateTime(new Date());
        industry.setCode(code);
        industry.setName(name);
        ResultDTO resultDTO = systemRpcService.editIndustryInfo(industry);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editRechargeType(Integer rechargeTypeId, String name, String remark, RestResp resp)
    {
        RechargeTypeDTO rt = new RechargeTypeDTO();
        rt.setId(rechargeTypeId);
        rt.setName(name);
        rt.setRemark(remark);
        ResultDTO res = systemRpcService.editRechargeType(rt);
        resp.setError(res.getResult());
    }

    @Override
    public void enableRechargeType(Integer rechargeTypeId, Integer enabled, RestResp resp)
    {
        RechargeTypeDTO rt = new RechargeTypeDTO();
        rt.setId(rechargeTypeId);
        rt.setEnabled(enabled);
        ResultDTO res = systemRpcService.editRechargeType(rt);
        resp.setError(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getHierarchyIndustry()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<DictIndustryDTO> listDTO1 = systemRpcService.getIndustryList(0L, null);
        if(!CollectionUtils.isEmpty(listDTO1.getList()))
        {
            for(DictIndustryDTO o : listDTO1.getList())
            {
                Map<String, Object> p = new HashMap<>();
                p.put(Field.ID, o.getId() + "");
                p.put(Field.CODE, o.getCode());
                p.put(Field.NAME, o.getName());
                p.put(Field.ENABLED, o.getEnabled());
                ListDTO<DictIndustryDTO> listDTO2 = systemRpcService.getIndustryList(o.getId(), null);
                List<Map<String, Object>> subList = new ArrayList<>();
                if(!CollectionUtils.isEmpty(listDTO2.getList()))
                {
                    for(DictIndustryDTO ind : listDTO2.getList())
                    {
                        Map<String, Object> c = new HashMap<>();
                        c.put(Field.ID, ind.getId() + "");
                        c.put(Field.CODE, ind.getCode());
                        c.put(Field.NAME, ind.getName());
                        c.put(Field.ENABLED, ind.getEnabled());
                        c.put(Field.PARENT_ID, ind.getParentId());
                        subList.add(c);
                    }
                }
                p.put(Field.SUB_LIST, subList);
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getHierarchyPrivilege()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        ListDTO<PrivilegeDTO> listDTO1 = systemRpcService.getPrivilegeListByParent(0L);
        for(PrivilegeDTO o1 : listDTO1.getList())
        {
            Map<String, Object> m1 = new HashMap<>();
            m1.put(Field.NAME, o1.getName());
            m1.put(Field.ID, o1.getPrivilegeId() + "");
            m1.put(Field.ENABLED, o1.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            ListDTO<PrivilegeDTO> listDTO2 = systemRpcService.getPrivilegeListByParent(o1.getPrivilegeId());
            for(PrivilegeDTO o2 : listDTO2.getList())
            {
                Map<String, Object> m2 = new HashMap<>();
                m2.put(Field.NAME, o2.getName());
                m2.put(Field.ID, o2.getPrivilegeId() + "");
                m2.put(Field.ENABLED, o2.getEnabled());
                List<Map<String, Object>> thrList = new ArrayList<>();
                ListDTO<PrivilegeDTO> listDTO3 = systemRpcService.getPrivilegeListByParent(o2.getPrivilegeId());
                for(PrivilegeDTO o3 : listDTO3.getList())
                {
                    Map<String, Object> m3 = new HashMap<>();
                    m3.put(Field.NAME, o3.getName());
                    m3.put(Field.ID, o3.getPrivilegeId() + "");
                    m3.put(Field.ENABLED, o3.getEnabled());
                    thrList.add(m3);
                }
                m2.put(Field.THR_LIST, thrList);
                subList.add(m2);

            }
            m1.put(Field.SUB_LIST, subList);
            list.add(m1);
        }
        return list;
    }

    @Override
    public void editPrivilegeInfo(Long privilegeId, String name, RestResp resp)
    {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setPrivilegeId(privilegeId);
        privilegeDTO.setName(name);
        ResultDTO resultDTO = systemRpcService.editPrivilegeInfo(privilegeDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public List<Dict> getRechargeDict()
    {
        List<Dict> list = new ArrayList<>();
        ListDTO<DictDTO> listDTO = commonRpcService.getRechargeTypeDict();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictDTO o : listDTO.getList())
            {
                list.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        return list;
    }

    @Override
    public void addRechargeType(String name, String remark, RestResp resp)
    {
        ResultDTO res = systemRpcService.addRechargeType(name, remark);
        resp.setError(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled)
    {
        ListDTO<DictIndustryDTO> listDTO = systemRpcService.getIndustryList(parentId, enabled);
        List<DictIndustryDTO> dataList = listDTO.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictIndustryDTO parent : dataList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, parent.getId() + "");
            map.put(Field.NAME, parent.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String, Object> getInitIndustryMap()
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> parentIndustryList = new ArrayList<>();
        List<Map<String, Object>> industryList = new ArrayList<>();
        ListDTO<DictIndustryDTO> listDTO = systemRpcService.getIndustryList(0L, TrueOrFalse.TRUE);
        List<DictIndustryDTO> parentList = listDTO.getList();
        for(DictIndustryDTO industry : parentList)
        {
            Map<String, Object> pm = new HashMap<>();
            parentIndustryList.add(pm);
            pm.put(Field.ID, industry.getId() + "");
            pm.put(Field.NAME, industry.getName());
        }
        if(!CollectionUtils.isEmpty(parentList))
        {
            ListDTO<DictIndustryDTO> listDTO2 = systemRpcService.getIndustryList(parentList.get(0).getId(),
                    TrueOrFalse.TRUE);
            List<DictIndustryDTO> childList = listDTO2.getList();
            for(DictIndustryDTO industry : childList)
            {
                Map<String, Object> pm = new HashMap<>();
                pm.put(Field.NAME, industry.getName());
                industryList.add(pm);
                pm.put(Field.ID, industry.getId() + "");
            }
        }
        map.put(Field.PARENT_INDUSTRY, parentIndustryList);
        map.put(Field.INDUSTRY, industryList);
        return map;
    }

    @Override
    public String getIndustryName(Long industryId)
    {
        String industryName = redisDao.getIndustryInfo(industryId);
        if(StringUtils.isNullBlank(industryName))
        {
            cacheAllIndustryData();
            industryName = redisDao.getIndustryInfo(industryId);
        }
        return industryName == null ? "" : industryName;
    }

    @Override
    public Map<String, String> cacheSystemModule()
    {
        Map<String, String> map = new HashMap<>();
        ListDTO<PrivilegeDTO> listDTO = systemRpcService.getPrivilegeByLevel(3);
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(PrivilegeDTO p : listDTO.getList())
            {
                map.put("m" + p.getPrivilegeId(), p.getName());
            }
        }
        redisDao.setSystemModule(map);
        return map;
    }

    @Override
    public void setGlobalSetting(List<SysConfigDTO> sysConfigDTOList, RestResp resp)
    {
        ResultDTO resultDTO = systemRpcService.addOrUpdateSetting(sysConfigDTOList);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void changeIndustryStatus(Long industryTypeId, Integer enabled, RestResp resp)
    {
        ResultDTO resultDTO = systemRpcService.changeIndustryStatus(industryTypeId, enabled);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void setModuleStatus(List<Long> moduleIdList, Integer status)
    {
        systemRpcService.setModuleStatus(status, moduleIdList);
    }

    @Override
    public Map<String, Object> getSettings()
    {
        return systemRpcService.getSettingData();
    }

    @Override
    public void getRechargeTypeList(RestListResp res)
    {
        ListDTO<RechargeTypeDTO> dto = systemRpcService.getRechargeList();
        res.setTotal(dto.getTotal());
        List<Map<String, Object>> list = new ArrayList<>();
        for(RechargeTypeDTO o : dto.getList())
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId());
            m.put(Field.NAME, o.getName());
            m.put(Field.REMARK, o.getRemark());
            m.put(Field.STATUS, o.getEnabled());
            m.put(Field.ADD_AT, DateUtils.format(o.getAddAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            list.add(m);
        }
        res.setList(list);
    }

    @Override
    public List<Dict> getAccountRoleDict()
    {
        List<Dict> roleDict = new ArrayList<>();
        ListDTO<DictDTO> listDTO = commonRpcService.getAdminGroupDict();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictDTO o : listDTO.getList())
            {
                roleDict.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        return roleDict;
    }

    private void cacheAllIndustryData()
    {
        ListDTO<DictIndustryDTO> listDTO = systemRpcService.getDictIndustryInfoList();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictIndustryDTO o : listDTO.getList())
            {
                redisDao.saveIndustryInfo(o.getId(), o.getName());
            }
        }
    }
}
