package com.mingdong.bop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.DictIndustry;
import com.mingdong.bop.domain.entity.DictProductType;
import com.mingdong.bop.domain.entity.DictRechargeType;
import com.mingdong.bop.domain.entity.Privilege;
import com.mingdong.bop.domain.entity.Role;
import com.mingdong.bop.domain.mapper.DictIndustryMapper;
import com.mingdong.bop.domain.mapper.DictProductTypeMapper;
import com.mingdong.bop.domain.mapper.DictRechargeTypeMapper;
import com.mingdong.bop.domain.mapper.PrivilegeMapper;
import com.mingdong.bop.domain.mapper.RoleMapper;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
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
    private DictProductTypeMapper dictProductTypeMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DictIndustryMapper dictIndustryMapper;
    @Resource
    private DictRechargeTypeMapper dictRechargeTypeMapper;
    @Resource
    private PrivilegeMapper privilegeMapper;

    @Override
    public List<Map<String, Object>> getValidRole()
    {
        List<Role> roleList = roleMapper.getByStatus(TrueOrFalse.TRUE);
        List<Map<String, Object>> list = new ArrayList<>();
        for(Role role : roleList)
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
        DictIndustry industry = dictIndustryMapper.findByCode(code.toUpperCase());
        return industry != null;
    }

    @Override
    public Map<String, String> getIndustryInfo(Long id)
    {
        Map<String, String> map = new HashMap<>();
        map.put(Field.ID, id + "");
        DictIndustry industry = dictIndustryMapper.findById(id);
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
        List<DictIndustry> dataList = dictIndustryMapper.getByParentAndStatus(parentId, enabled);
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictIndustry industry : dataList)
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
        DictIndustry industry = dictIndustryMapper.findByCode(code);
        if(industry != null)
        {
            resp.result(RestResult.INDUSTRY_CODE_EXIST);
            return;
        }
        Date current = new Date();
        industry = new DictIndustry();
        industry.setCreateTime(current);
        industry.setUpdateTime(current);
        industry.setCode(code.toUpperCase());
        industry.setName(name);
        industry.setSeqNo(1); // TODO 序号
        industry.setParentId(id != null ? id : 0L);
        industry.setEnabled(TrueOrFalse.TRUE);
        dictIndustryMapper.add(industry);
    }

    @Override
    @Transactional
    public void editIndustryInfo(Long id, String code, String name, BLResp resp)
    {
        DictIndustry industry = dictIndustryMapper.findByCode(code);
        if(industry != null && !id.equals(industry.getId()))
        {
            resp.result(RestResult.INDUSTRY_CODE_EXIST);
            return;
        }
        industry = dictIndustryMapper.findById(id);
        if(industry == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        industry = new DictIndustry();
        industry.setId(id);
        industry.setUpdateTime(new Date());
        industry.setCode(code);
        industry.setName(name);
        dictIndustryMapper.updateSkipNull(industry);
    }

    @Override
    @Transactional
    public void dropRechargeType(Long rechargeTypeId, BLResp resp)
    {
        DictRechargeType rechargeType = dictRechargeTypeMapper.findById(rechargeTypeId);
        if(rechargeType != null && TrueOrFalse.FALSE.equals(rechargeType.getDeleted()))
        {
            rechargeType = new DictRechargeType();
            rechargeType.setId(rechargeTypeId);
            rechargeType.setUpdateTime(new Date());
            rechargeType.setDeleted(TrueOrFalse.TRUE);
            dictRechargeTypeMapper.updateSkipNull(rechargeType);
        }
    }

    @Override
    public void getRechargeTypeInfo(Long rechargeTypeId, BLResp resp)
    {
        DictRechargeType rechargeType = dictRechargeTypeMapper.findById(rechargeTypeId);
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
        DictRechargeType org = dictRechargeTypeMapper.findByName(name);
        if(org != null && !id.equals(org.getId()))
        {
            resp.result(RestResult.CATEGORY_NAME_EXIST);
            return;
        }
        DictRechargeType rechargeType = dictRechargeTypeMapper.findById(id);
        if(rechargeType != null)
        {
            rechargeType.setUpdateTime(new Date());
            rechargeType.setName(name);
            rechargeType.setRemark(remark);
            dictRechargeTypeMapper.updateById(rechargeType);
        }

    }

    @Override
    public List<Map<String, Object>> getProductListMap()
    {
        List<DictProductType> dataList = dictProductTypeMapper.getAll();
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictProductType dictProductType : dataList)
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
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, null);
        for(DictIndustry di : parentList)
        {
            Map<String, Object> p = new HashMap<>();
            p.put(Field.ID, di.getId() + "");
            p.put(Field.CODE, di.getCode());
            p.put(Field.NAME, di.getName());
            p.put(Field.ENABLED, di.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            List<DictIndustry> childList = dictIndustryMapper.getByParentAndStatus(di.getId(), null);
            for(DictIndustry ind : childList)
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
        List<Privilege> parentList = privilegeMapper.getByParentAndStatus(0L, null);
        for(Privilege pl : parentList)
        {
            Map<String, Object> p = new HashMap<>();
            p.put(Field.NAME, pl.getName());
            p.put(Field.ID, pl.getId() + "");
            p.put(Field.ENABLED, pl.getEnabled());
            List<Map<String, Object>> subList = new ArrayList<>();
            List<Privilege> childList = privilegeMapper.getByParentAndStatus(pl.getId(), null);
            for(Privilege ple : childList)
            {
                Map<String, Object> c = new HashMap<>();
                c.put(Field.NAME, ple.getName());
                c.put(Field.ID, ple.getId() + "");
                c.put(Field.ENABLED, ple.getEnabled());
                //subList.add(c);
                List<Map<String, Object>> thrList = new ArrayList<>();
                List<Privilege> thrChildList = privilegeMapper.getByParentAndStatus(ple.getId(), null);
                for(Privilege pleg : thrChildList)
                {
                    Map<String, Object> cc = new HashMap<>();
                    cc.put(Field.NAME, pleg.getName());
                    cc.put(Field.ID, pleg.getId() + "");
                    cc.put(Field.ENABLED, pleg.getEnabled());
                    thrList.add(cc);
                }
                //第三层写在这里
                c.put(Field.THR_LIST, thrList);
                subList.add(c);

            }
            p.put(Field.SUB_LIST, subList);
            list.add(p);
            /*for(int i=0;i<subList.size();i++){
                System.out.println(subList.get(i));
            }*/
        }
        return list;
    }

    @Override
    public Map<String, Object> getPrivilegeInfo(Long id)
    {
        Map<String, Object> privilegeMap = new HashMap<>();
        privilegeMap.put(Field.ID, id + "");
        Privilege privilege = privilegeMapper.findById(id);
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
        Privilege privilege = privilegeMapper.findById(privilegeId);
        if(privilege == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        if(name.equals(privilege.getName()))
        {
            return;
        }
        privilege = new Privilege();
        privilege.setId(privilegeId);
        privilege.setUpdateTime(new Date());
        privilege.setName(name);
        privilegeMapper.updateSkipNull(privilege);
        cacheSystemModule();
    }

    @Override
    public List<Map<String, Object>> getRechargeDict()
    {
        List<Map<String, Object>> list = new ArrayList<>();
        List<DictRechargeType> rechargeTypeList = dictRechargeTypeMapper.getByStatus(TrueOrFalse.TRUE);
        for(DictRechargeType drt : rechargeTypeList)
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
        List<DictRechargeType> dataList = dictRechargeTypeMapper.getByStatus(enabled);
        resp.addData(Field.TOTAL, dataList.size());
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(DictRechargeType type : dataList)
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
    @Transactional
    public void addRechargeType(String name, String remark, BLResp resp)
    {
        DictRechargeType type = dictRechargeTypeMapper.findByName(name);
        if(type != null && TrueOrFalse.FALSE.equals(type.getDeleted()))
        {
            resp.result(RestResult.CATEGORY_NAME_EXIST);
            return;
        }
        Date current = new Date();
        if(type == null)
        {
            type = new DictRechargeType();
            type.setCreateTime(current);
            type.setUpdateTime(current);
            type.setName(name);
            type.setRemark(remark);
            type.setEnabled(TrueOrFalse.TRUE);
            type.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.add(type);
        }
        else
        {
            type.setUpdateTime(current);
            type.setRemark(remark);
            type.setDeleted(TrueOrFalse.FALSE);
            dictRechargeTypeMapper.updateById(type);
        }
    }

    @Override
    @Transactional
    public BLResp editRechargeType(Long rechargeTypeId, String name, String remark, Integer enabled)
    {
        BLResp resp = BLResp.build();
        DictRechargeType type = dictRechargeTypeMapper.findById(rechargeTypeId);
        if(type == null)
        {
            return resp.result(RestResult.OBJECT_NOT_FOUND);
        }
        if(TrueOrFalse.TRUE.equals(enabled) || TrueOrFalse.FALSE.equals(enabled))
        {
            if(!type.getEnabled().equals(enabled))
            {
                type = new DictRechargeType();
                type.setId(rechargeTypeId);
                type.setUpdateTime(new Date());
                type.setEnabled(enabled);
                dictRechargeTypeMapper.updateSkipNull(type);
            }
        }
        else if(!StringUtils.isNullBlank(name))
        {
            type.setUpdateTime(new Date());
            type.setName(name);
            type.setRemark(remark);
            dictRechargeTypeMapper.updateById(type);
        }
        return resp;
    }

    @Override
    public BLResp getIndustryList(Integer enabled)
    {
        BLResp resp = BLResp.build();
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, enabled);
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictIndustry parent : parentList)
        {
            Map<String, Object> parentMap = new HashMap<>();
            parentMap.put(Field.ID, parent.getId() + "");
            parentMap.put(Field.NAME, parent.getName());
            if(enabled == null)
            {
                parentMap.put(Field.ENABLED, parent.getEnabled());
            }
            List<DictIndustry> childList = dictIndustryMapper.getByParentAndStatus(parent.getId(), enabled);
            List<Map<String, Object>> subList = new ArrayList<>(childList.size());
            for(DictIndustry child : childList)
            {
                Map<String, Object> childMap = new HashMap<>();
                childMap.put(Field.ID, child.getId() + "");
                childMap.put(Field.NAME, child.getName());
                if(enabled == null)
                {
                    childMap.put(Field.ENABLED, child.getEnabled());
                }
                subList.add(childMap);
            }
            parentMap.put(Field.SUB_LIST, subList);
            list.add(parentMap);
        }
        return resp.addData(Field.LIST, list);
    }

    @Override
    public List<Map<String, Object>> getIndustryList(Long parentId, Integer enabled)
    {
        List<DictIndustry> dataList = dictIndustryMapper.getByParentAndStatus(parentId, enabled);
        List<Map<String, Object>> list = new ArrayList<>();
        for(DictIndustry parent : dataList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, parent.getId() + "");
            map.put(Field.NAME, parent.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional
    public BLResp editIndustry(Long industryId, String name, Integer enabled)
    {
        BLResp resp = BLResp.build();
        DictIndustry industry = dictIndustryMapper.findById(industryId);
        if(industry == null)
        {
            return resp.result(RestResult.OBJECT_NOT_FOUND);
        }
        if(TrueOrFalse.TRUE.equals(enabled) || TrueOrFalse.FALSE.equals(enabled))
        {
            industry = new DictIndustry();
            industry.setId(industryId);
            industry.setUpdateTime(new Date());
            industry.setEnabled(enabled);
            dictIndustryMapper.updateById(industry);
        }
        else if(!industry.getName().equals(name))
        {
            industry = new DictIndustry();
            industry.setId(industryId);
            industry.setUpdateTime(new Date());
            industry.setName(name);
            dictIndustryMapper.updateById(industry);
        }
        return resp;
    }

    @Override
    public BLResp getIndustryList(Page page)
    {
        BLResp resp = BLResp.build();
        int total = dictIndustryMapper.countAll();
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<DictIndustry> industryList = dictIndustryMapper.getAll();
            List<Map<String, Object>> list = new ArrayList<>(industryList.size());
            for(DictIndustry industry : industryList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, industry.getId() + "");
                map.put(Field.ENABLED, industry.getEnabled() + "");
                map.put(Field.PARENT_ID, industry.getParentId() + "");
                map.put(Field.NAME, industry.getName());
                list.add(map);
            }
            resp.addData(Field.LIST, list);

        }

        return resp;
    }

    @Override
    public Map<String, Object> getInitIndustryMap()
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> parentIndustryList = new ArrayList<>();
        List<Map<String, Object>> industryList = new ArrayList<>();
        List<DictIndustry> parentList = dictIndustryMapper.getByParentAndStatus(0L, TrueOrFalse.TRUE);
        for(DictIndustry industry : parentList)
        {
            Map<String, Object> pm = new HashMap<>();
            pm.put(Field.ID, industry.getId() + "");
            pm.put(Field.NAME, industry.getName());
            parentIndustryList.add(pm);
        }
        if(!CollectionUtils.isEmpty(parentList))
        {
            List<DictIndustry> childList = dictIndustryMapper.getByParentAndStatus(parentList.get(0).getId(),
                    TrueOrFalse.TRUE);
            for(DictIndustry industry : childList)
            {
                Map<String, Object> pm = new HashMap<>();
                pm.put(Field.ID, industry.getId() + "");
                pm.put(Field.NAME, industry.getName());
                industryList.add(pm);
            }
        }
        map.put(Field.PARENT_INDUSTRY, parentIndustryList);
        map.put(Field.INDUSTRY, industryList);
        return map;
    }

    @Override
    public BLResp getModuleHierarchy()
    {
        BLResp resp = BLResp.build();
        List<Privilege> moduleList = privilegeMapper.getByParent(0L);
        List<Map<String, Object>> hierarchyList = new ArrayList<>(moduleList.size());
        for(Privilege module : moduleList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.MODULE_ID, module.getId() + "");
            map.put(Field.NAME, module.getName());
            map.put(Field.ENABLED, module.getEnabled());
            List<Map<String, Object>> subList = getSubModule(module.getId());
            if(!CollectionUtils.isEmpty(subList))
            {
                map.put(Field.SUB_MODULE, subList);
            }
            hierarchyList.add(map);
        }
        return resp.addData(Field.LIST, hierarchyList);
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
        List<Privilege> privilegeList = privilegeMapper.getListByLevel(3);
        for(Privilege p : privilegeList)
        {
            map.put("m" + p.getId(), p.getName());
        }
        redisDao.setSystemModule(map);
        return map;
    }

    private void cacheAllIndustryData()
    {
        List<DictIndustry> dataList = dictIndustryMapper.getIndustryInfo();
        for(DictIndustry industry : dataList)
        {
            redisDao.saveIndustryInfo(industry.getId(), industry.getName());
        }
    }

    private List<Map<String, Object>> getSubModule(Long parentModuleId)
    {
        List<Privilege> moduleList = privilegeMapper.getByParent(parentModuleId);
        if(!CollectionUtils.isEmpty(moduleList))
        {
            List<Map<String, Object>> list = new ArrayList<>();
            for(Privilege module : moduleList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.MODULE_ID, module.getId() + "");
                map.put(Field.NAME, module.getName());
                map.put(Field.ENABLED, module.getEnabled());
                List<Map<String, Object>> subList = getSubModule(module.getId());
                if(subList != null)
                {
                    map.put(Field.SUB_MODULE, list);
                }
                list.add(map);
            }
            return list;
        }
        return null;
    }
}
