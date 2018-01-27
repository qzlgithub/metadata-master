package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.SysParam;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.DictProductTypeListDTO;
import com.mingdong.core.model.dto.DictRechargeTypeDTO;
import com.mingdong.core.model.dto.DictRechargeTypeListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.service.RemoteSystemService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private RemoteProductService remoteProductService;

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
    public List<Map<String, Object>> getIndustryMap(Long parentId, Integer enabled)
    {
        DictIndustryListDTO dictIndustryListDTO = remoteSystemService.getDictIndustryListByParentAndStatus(parentId,
                enabled);
        List<DictIndustryDTO> dataList = dictIndustryListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictIndustryDTO industry : dataList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, industry.getId() + "");
            map.put(Field.NAME, industry.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional
    public void addIndustryType(Long id, String code, String name, BLResp resp)
    {
        DictIndustryDTO industry = remoteSystemService.getDictIndustryByCode(code);
        if(industry != null)
        {
            resp.result(RestResult.INDUSTRY_CODE_EXIST);
            return;
        }
        Date current = new Date();
        industry = new DictIndustryDTO();
        industry.setCreateTime(current);
        industry.setUpdateTime(current);
        industry.setCode(code.toUpperCase());
        industry.setName(name);
        industry.setSeqNo(1); // TODO 序号
        industry.setParentId(id != null ? id : 0L);
        industry.setEnabled(TrueOrFalse.TRUE);
        remoteSystemService.saveDictIndustry(industry);
    }

    @Override
    @Transactional
    public void editIndustryInfo(Long id, String code, String name, BLResp resp)
    {
        DictIndustryDTO industry = remoteSystemService.getDictIndustryByCode(code);
        if(industry != null && !id.equals(industry.getId()))
        {
            resp.result(RestResult.INDUSTRY_CODE_EXIST);
            return;
        }
        industry = remoteSystemService.getDictIndustryById(id);
        if(industry == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        industry = new DictIndustryDTO();
        industry.setId(id);
        industry.setUpdateTime(new Date());
        industry.setCode(code);
        industry.setName(name);
        remoteSystemService.updateDictIndustrySkipNull(industry);
    }

    @Override
    @Transactional
    public void dropRechargeType(Long rechargeTypeId, BLResp resp)
    {
        DictRechargeTypeDTO rechargeType = remoteSystemService.getDictRechargeTypeById(rechargeTypeId);
        if(rechargeType != null && TrueOrFalse.FALSE.equals(rechargeType.getDeleted()))
        {
            rechargeType = new DictRechargeTypeDTO();
            rechargeType.setId(rechargeTypeId);
            rechargeType.setUpdateTime(new Date());
            rechargeType.setDeleted(TrueOrFalse.TRUE);
            remoteSystemService.updateDictRechargeTypeSkipNull(rechargeType);
        }
    }

    @Override
    public void getRechargeTypeInfo(Long rechargeTypeId, BLResp resp)
    {
        DictRechargeTypeDTO rechargeType = remoteSystemService.getDictRechargeTypeById(rechargeTypeId);
        resp.addData(Field.ID, rechargeTypeId + "");
        if(rechargeType != null)
        {
            resp.addData(Field.NAME, rechargeType.getName());
            resp.addData(Field.REMARK, rechargeType.getRemark());
        }
    }

    @Override
    public void updateRechargeType(Long id, String name, String remark, BLResp resp)
    {
        DictRechargeTypeDTO org = remoteSystemService.getDictRechargeTypeByName(name);
        if(org != null && !id.equals(org.getId()))
        {
            resp.result(RestResult.CATEGORY_NAME_EXIST);
            return;
        }
        DictRechargeTypeDTO rechargeType = remoteSystemService.getDictRechargeTypeById(id);
        if(rechargeType != null)
        {
            rechargeType.setUpdateTime(new Date());
            rechargeType.setName(name);
            rechargeType.setRemark(remark);
            remoteSystemService.updateDictRechargeTypeById(rechargeType);
        }

    }

    @Override
    public List<Map<String, Object>> getProductListMap()
    {
        DictProductTypeListDTO dictProductTypeList = remoteProductService.getDictProductTypeList(null, null);
        List<DictProductTypeDTO> dataList = dictProductTypeList.getDataList();
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictProductTypeDTO dictProductType : dataList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.TYPE_NAME, dictProductType.getName());
            map.put(Field.ID, dictProductType.getId());
            list.add(map);
        }
        return list;
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
    @Transactional
    public void editPrivilegeInfo(Long privilegeId, String name, BLResp resp)
    {
        PrivilegeDTO privilege = remoteSystemService.getPrivilegeById(privilegeId);
        if(privilege == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(name.equals(privilege.getName()))
        {
            return;
        }
        privilege = new PrivilegeDTO();
        privilege.setId(privilegeId);
        privilege.setUpdateTime(new Date());
        privilege.setName(name);
        remoteSystemService.updatePrivilegeSkipNull(privilege);
        cacheSystemModule();
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
    public void getRechargeTypeList(Integer enabled, BLResp resp)
    {
        DictRechargeTypeListDTO dictRechargeTypeListDTO = remoteSystemService.getDictRechargeTypeListByStatus(enabled,
                null);
        List<DictRechargeTypeDTO> dataList = dictRechargeTypeListDTO.getDataList();
        resp.addData(Field.TOTAL, dataList.size());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(DictRechargeTypeDTO type : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, type.getId() + "");
                map.put(Field.NAME, type.getName());
                if(enabled == null)
                {
                    map.put(Field.REMARK, type.getRemark());
                    map.put(Field.ENABLED, type.getEnabled());
                }
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
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
    @Transactional
    public void addRechargeType(String name, String remark, BLResp resp)
    {
        DictRechargeTypeDTO type = remoteSystemService.getDictRechargeTypeByName(name);
        if(type != null && TrueOrFalse.FALSE.equals(type.getDeleted()))
        {
            resp.result(RestResult.CATEGORY_NAME_EXIST);
            return;
        }
        Date current = new Date();
        if(type == null)
        {
            type = new DictRechargeTypeDTO();
            type.setCreateTime(current);
            type.setUpdateTime(current);
            type.setName(name);
            type.setRemark(remark);
            type.setEnabled(TrueOrFalse.TRUE);
            type.setDeleted(TrueOrFalse.FALSE);
            remoteSystemService.saveDictRechargeType(type);
        }
        else
        {
            type.setUpdateTime(current);
            type.setRemark(remark);
            type.setDeleted(TrueOrFalse.FALSE);
            remoteSystemService.updateDictRechargeTypeById(type);
        }
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
            map.put(Field.NAME, parent.getName());
            map.put(Field.ID, parent.getId() + "");
            list.add(map);
        }
        return list;
    }

    @Override
    public BLResp getIndustryList(Page page)
    {
        BLResp resp = BLResp.build();
        DictIndustryListDTO dictIndustryList = remoteSystemService.getDictIndustryList(page);
        resp.addData(Field.TOTAL, dictIndustryList.getTotal());
        resp.addData(Field.PAGES, dictIndustryList.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<DictIndustryDTO> industryList = dictIndustryList.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(industryList.size());
        if(CollectionUtils.isNotEmpty(industryList))
        {
            for(DictIndustryDTO industry : industryList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, industry.getId() + "");
                map.put(Field.ENABLED, industry.getEnabled() + "");
                map.put(Field.PARENT_ID, industry.getParentId() + "");
                map.put(Field.NAME, industry.getName());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
        return resp;
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
    @Transactional
    public void setGlobalSetting(Integer subUserQty, String serviceQQ, BLResp resp)
    {
        Date current = new Date();
        SysConfigDTO sysConfig = remoteSystemService.getSysConfigByName(SysParam.CLIENT_SUB_USER_QTY);
        if(sysConfig == null)
        {
            sysConfig = new SysConfigDTO();
            sysConfig.setCreateTime(current);
            sysConfig.setUpdateTime(current);
            sysConfig.setName(SysParam.CLIENT_SUB_USER_QTY);
            sysConfig.setValue(subUserQty + "");
            remoteSystemService.saveSysConfig(sysConfig);
        }
        else if(!sysConfig.getValue().equals(subUserQty + ""))
        {
            sysConfig.setUpdateTime(current);
            sysConfig.setValue(subUserQty + "");
            remoteSystemService.updateSysConfigById(sysConfig);
        }
        sysConfig = remoteSystemService.getSysConfigByName(SysParam.SERVICE_QQ);
        if(sysConfig == null)
        {
            sysConfig = new SysConfigDTO();
            sysConfig.setCreateTime(current);
            sysConfig.setUpdateTime(current);
            sysConfig.setName(SysParam.SERVICE_QQ);
            sysConfig.setValue(serviceQQ);
            remoteSystemService.saveSysConfig(sysConfig);
        }
        else if(!sysConfig.getValue().equals(serviceQQ))
        {
            sysConfig.setUpdateTime(current);
            sysConfig.setValue(serviceQQ);
            remoteSystemService.updateSysConfigById(sysConfig);
        }
    }

    @Override
    @Transactional
    public void changeRechargeStatus(Long rechargeTypeId, Integer enabled, BLResp resp)
    {
        DictRechargeTypeDTO rechargeType = remoteSystemService.getDictRechargeTypeById(rechargeTypeId);
        if(rechargeType != null && !enabled.equals(rechargeType.getEnabled()))
        {
            DictRechargeTypeDTO updObj = new DictRechargeTypeDTO();
            updObj.setId(rechargeTypeId);
            updObj.setUpdateTime(new Date());
            updObj.setEnabled(enabled);
            remoteSystemService.updateDictRechargeTypeSkipNull(updObj);
        }
    }

    @Override
    @Transactional
    public void changeIndustryStatus(Long industryTypeId, Integer enabled)
    {
        DictIndustryDTO di = remoteSystemService.getDictIndustryById(industryTypeId);
        if(di != null && !enabled.equals(di.getEnabled()))
        {
            DictIndustryDTO updObj = new DictIndustryDTO();
            updObj.setId(industryTypeId);
            updObj.setUpdateTime(new Date());
            updObj.setEnabled(enabled);
            remoteSystemService.updateDictIndustrySkipNull(updObj);
        }
    }

    @Override
    @Transactional
    public void setModuleStatus(List<Long> moduleIdList, Integer status, BLResp resp)
    {
        remoteSystemService.setModuleStatus(status, moduleIdList);
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
