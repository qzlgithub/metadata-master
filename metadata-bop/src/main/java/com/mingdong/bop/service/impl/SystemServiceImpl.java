package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.RechargeTypeDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.RemoteSystemService;
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
    private RemoteSystemService remoteSystemService;

    @Override
    public List<Map<String, Object>> getValidRole()
    {
        RoleListDTO roleListByStatus = remoteSystemService.getRoleListByStatus(TrueOrFalse.TRUE);
        List<RoleDTO> roleList = roleListByStatus.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        for(RoleDTO role : roleList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ROLE_ID, role.getId() + "");
            map.put(Field.ROLE_NAME, role.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public boolean checkIndustryCodeExist(String code)
    {
        DictIndustryDTO industry = remoteSystemService.getDictIndustryByCode(code.toUpperCase());
        return industry != null;
    }

    @Override
    public Map<String, String> getIndustryInfo(Long id)
    {
        Map<String, String> map = new HashMap<>();
        map.put(Field.ID, id + "");
        DictIndustryDTO industry = remoteSystemService.getDictIndustryById(id);
        if(industry != null)
        {
            map.put(Field.CODE, industry.getCode());
            map.put(Field.NAME, industry.getName());
        }
        return map;
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
        ResultDTO resultDTO = remoteSystemService.addIndustryType(industry);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void editIndustryInfo(Long id, String code, String name, RestResp resp)
    {
        DictIndustryDTO industry = new DictIndustryDTO();
        industry.setId(id);
        industry.setUpdateTime(new Date());
        industry.setCode(code);
        industry.setName(name);
        ResultDTO resultDTO = remoteSystemService.editIndustryInfo(industry);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void dropRechargeType(Integer rechargeTypeId)
    {
        remoteSystemService.setRechargeTypeDeleted(rechargeTypeId);
    }

    @Override
    public void editRechargeType(Integer rechargeTypeId, String name, String remark, RestResp resp)
    {
        RechargeTypeDTO rt = new RechargeTypeDTO();
        rt.setId(rechargeTypeId);
        rt.setName(name);
        rt.setRemark(remark);
        ResultDTO res = remoteSystemService.editRechargeType(rt);
        resp.result(res.getResult());
    }

    @Override
    public void enableRechargeType(Integer rechargeTypeId, Integer enabled, RestResp resp)
    {
        RechargeTypeDTO rt = new RechargeTypeDTO();
        rt.setId(rechargeTypeId);
        rt.setEnabled(enabled);
        ResultDTO res = remoteSystemService.editRechargeType(rt);
        resp.result(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getHierarchyIndustry()//TODO HERE
    {
        List<Map<String, Object>> list = new ArrayList<>();
        DictIndustryListDTO dictIndustryListDTO = remoteSystemService.getDictIndustryListByParentAndStatus(0L, null);
        List<DictIndustryDTO> parentList = dictIndustryListDTO.getDataList();
        for(DictIndustryDTO di : parentList)
        {
            Map<String, Object> p = new HashMap<>();
            p.put(Field.ID, di.getId() + "");
            p.put(Field.CODE, di.getCode());
            p.put(Field.NAME, di.getName());
            p.put(Field.ENABLED, di.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            DictIndustryListDTO dictIndustryListByParentAndStatus =
                    remoteSystemService.getDictIndustryListByParentAndStatus(di.getId(), null);
            List<DictIndustryDTO> childList = dictIndustryListByParentAndStatus.getDataList();
            for(DictIndustryDTO ind : childList)
            {
                Map<String, Object> c = new HashMap<>();
                c.put(Field.ID, ind.getId() + "");
                c.put(Field.CODE, ind.getCode());
                c.put(Field.NAME, ind.getName());
                c.put(Field.ENABLED, ind.getEnabled());
                subList.add(c);
            }
            p.put(Field.SUB_LIST, subList);
            list.add(p);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getHierarchyPrivilege()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        PrivilegeListDTO privilegeListDTO = remoteSystemService.getPrivilegeListByParentAndStatus(0L, null);
        List<PrivilegeDTO> parentList = privilegeListDTO.getDataList();
        for(PrivilegeDTO pl : parentList)
        {
            Map<String, Object> p = new HashMap<>();
            p.put(Field.NAME, pl.getName());
            p.put(Field.ID, pl.getId() + "");
            p.put(Field.ENABLED, pl.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            PrivilegeListDTO childPrivilegeListDTO = remoteSystemService.getPrivilegeListByParentAndStatus(pl.getId(),
                    null);
            List<PrivilegeDTO> childList = childPrivilegeListDTO.getDataList();
            for(PrivilegeDTO ple : childList)
            {
                Map<String, Object> c = new HashMap<>();
                c.put(Field.NAME, ple.getName());
                c.put(Field.ID, ple.getId() + "");
                c.put(Field.ENABLED, ple.getEnabled());
                //subList.add(c);
                List<Map<String, Object>> thrList = new ArrayList<>();
                PrivilegeListDTO thrChildPrivilegeListDTO = remoteSystemService.getPrivilegeListByParentAndStatus(
                        ple.getId(), null);
                List<PrivilegeDTO> thrChildList = thrChildPrivilegeListDTO.getDataList();
                for(PrivilegeDTO pleg : thrChildList)
                {
                    Map<String, Object> cc = new HashMap<>();
                    cc.put(Field.NAME, pleg.getName());
                    cc.put(Field.ID, pleg.getId() + "");
                    cc.put(Field.ENABLED, pleg.getEnabled());
                    thrList.add(cc);
                }
                c.put(Field.THR_LIST, thrList);
                subList.add(c);

            }
            p.put(Field.SUB_LIST, subList);
            list.add(p);
        }
        return list;
    }

    @Override
    public Map<String, Object> getPrivilegeInfo(Long id)
    {
        Map<String, Object> privilegeMap = new HashMap<>();
        privilegeMap.put(Field.ID, id + "");
        PrivilegeDTO privilege = remoteSystemService.getPrivilegeById(id);
        if(privilege != null)
        {
            privilegeMap.put(Field.NAME, privilege.getName());
        }
        return privilegeMap;
    }

    @Override
    public void editPrivilegeInfo(Long privilegeId, String name, RestResp resp)
    {
        PrivilegeDTO privilege = new PrivilegeDTO();
        privilege.setId(privilegeId);
        privilege.setUpdateTime(new Date());
        privilege.setName(name);
        ResultDTO resultDTO = remoteSystemService.editPrivilegeInfo(privilege);
        resp.result(resultDTO.getResult());
    }

    @Override
    public List<Map<String, Object>> getRechargeDict()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        DictRechargeTypeListDTO dictRechargeTypeListDTO = remoteSystemService.getDictRechargeTypeListByStatus(
                TrueOrFalse.TRUE, null);
        List<DictRechargeTypeDTO> rechargeTypeList = dictRechargeTypeListDTO.getDataList();
        for(DictRechargeTypeDTO drt : rechargeTypeList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, drt.getId() + "");
            map.put(Field.NAME, drt.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getRechargeTypeList(Integer enabled, Integer deleted)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        DictRechargeTypeListDTO dictRechargeTypeListDTO = remoteSystemService.getDictRechargeTypeListByStatus(enabled,
                deleted);
        List<DictRechargeTypeDTO> dataList = dictRechargeTypeListDTO.getDataList();
        for(DictRechargeTypeDTO o : dataList)
        {
            Map<String, Object> m = new HashMap<>();
            m.put(Field.ID, o.getId() + "");
            m.put(Field.NAME, o.getName());
            m.put(Field.ENABLED, o.getEnabled());
            m.put(Field.REMARK, o.getRemark());
            list.add(m);
        }
        return list;
    }

    @Override
    public void addRechargeType(String name, String remark, RestResp resp)
    {
        ResultDTO res = remoteSystemService.addRechargeType(name, remark);
        resp.result(res.getResult());
    }

    @Override
    public List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled)
    {
        DictIndustryListDTO dictIndustryListDTO = remoteSystemService.getDictIndustryListByParentAndStatus(parentId,
                enabled);
        List<DictIndustryDTO> dataList = dictIndustryListDTO.getDataList();
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
        DictIndustryListDTO dictIndustryListDTO = remoteSystemService.getDictIndustryListByParentAndStatus(0L,
                TrueOrFalse.TRUE);
        List<DictIndustryDTO> parentList = dictIndustryListDTO.getDataList();
        for(DictIndustryDTO industry : parentList)
        {
            Map<String, Object> pm = new HashMap<>();
            parentIndustryList.add(pm);
            pm.put(Field.ID, industry.getId() + "");
            pm.put(Field.NAME, industry.getName());
        }
        if(!CollectionUtils.isEmpty(parentList))
        {
            DictIndustryListDTO childDictIndustryListDTO = remoteSystemService.getDictIndustryListByParentAndStatus(
                    parentList.get(0).getId(), TrueOrFalse.TRUE);
            List<DictIndustryDTO> childList = childDictIndustryListDTO.getDataList();
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
        PrivilegeListDTO privilegeByLevel = remoteSystemService.getPrivilegeByLevel(3);
        List<PrivilegeDTO> privilegeList = privilegeByLevel.getDataList();
        for(PrivilegeDTO p : privilegeList)
        {
            map.put("m" + p.getId(), p.getName());
        }
        redisDao.setSystemModule(map);
        return map;
    }

    @Override
    public void setGlobalSetting(List<SysConfigDTO> sysConfigDTOList, RestResp resp)
    {
        ResultDTO resultDTO = remoteSystemService.addOrUpdateSetting(sysConfigDTOList);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void changeIndustryStatus(Long industryTypeId, Integer enabled, RestResp resp)
    {
        ResultDTO resultDTO = remoteSystemService.changeIndustryStatus(industryTypeId, enabled);
        resp.result(resultDTO.getResult());
    }

    @Override
    public void setModuleStatus(List<Long> moduleIdList, Integer status, RestResp resp)
    {
        remoteSystemService.setModuleStatus(status, moduleIdList);
    }

    @Override
    public Map<String, Object> getSettings()
    {
        return remoteSystemService.getSettingData();
    }

    @Override
    public void getRechargeTypeList(ListRes res)
    {
        ListDTO<RechargeTypeDTO> dto = remoteSystemService.getRechargeList();
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

    private void cacheAllIndustryData()
    {
        DictIndustryListDTO dictIndustryInfoList = remoteSystemService.getDictIndustryInfoList();
        List<DictIndustryDTO> dataList = dictIndustryInfoList.getDataList();
        for(DictIndustryDTO industry : dataList)
        {
            redisDao.saveIndustryInfo(industry.getId(), industry.getName());
        }
    }

}
