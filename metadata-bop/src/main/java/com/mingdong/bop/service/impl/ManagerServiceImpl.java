package com.mingdong.bop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.Manager;
import com.mingdong.bop.domain.entity.ManagerInfo;
import com.mingdong.bop.domain.entity.ManagerPrivilege;
import com.mingdong.bop.domain.entity.Privilege;
import com.mingdong.bop.domain.entity.Role;
import com.mingdong.bop.domain.entity.RolePrivilege;
import com.mingdong.bop.domain.mapper.ManagerInfoMapper;
import com.mingdong.bop.domain.mapper.ManagerMapper;
import com.mingdong.bop.domain.mapper.ManagerPrivilegeMapper;
import com.mingdong.bop.domain.mapper.PrivilegeMapper;
import com.mingdong.bop.domain.mapper.RoleMapper;
import com.mingdong.bop.domain.mapper.RolePrivilegeMapper;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.bop.component.Param;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.util.IDUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ManagerServiceImpl implements ManagerService
{
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private ManagerMapper managerMapper;
    @Resource
    private ManagerInfoMapper managerInfoMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private RolePrivilegeMapper rolePrivilegeMapper;
    @Resource
    private ManagerPrivilegeMapper managerPrivilegeMapper;

    @Override
    @Transactional
    public void userLogin(String username, String password, String sessionId, BLResp resp)
    {
        // 1. 验证登陆信息正确性
        Manager manager = managerMapper.findByUsername(username);
        if(manager == null)
        {
            resp.result(RestResult.ACCOUNT_NOT_EXIST);
            return;
        }
        else if(TrueOrFalse.FALSE.equals(manager.getEnabled()))
        {
            resp.result(RestResult.ACCOUNT_DISABLED);
            return;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(password)))
        {
            resp.result(RestResult.INVALID_PASSCODE);
            return;
        }
        else if(!TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            resp.result(RestResult.ACCOUNT_DISABLED);
            return;
        }
        // 删除用户旧的sessionId
        if(!StringUtils.isNullBlank(manager.getSessionId()))
        {
            redisDao.dropManagerSession(manager.getSessionId());
        }
        Date current = new Date();
        // 更新用户的sessionId
        Manager managerUpd = new Manager();
        managerUpd.setId(manager.getId());
        managerUpd.setUpdateTime(current);
        managerUpd.setSessionId(sessionId);
        managerMapper.updateSkipNull(managerUpd);
        // 缓存用户的账号及权限
        List<String> privilegeList = getManagerPrivilegeIdList(manager.getId());
        ManagerSession ms = new ManagerSession();
        ms.setManagerId(manager.getId());
        ms.setName(manager.getName());
        ms.setPrivileges(privilegeList);
        ms.setAddAt(current.getTime());
        redisDao.saveManagerSession(sessionId, ms);
    }

    @Override
    public void userLogout(String sessionId)
    {
        redisDao.dropManagerSession(sessionId);
    }

    /*@Override
    @Transactional
    public BLResp changePasscode(Long userId, String password)
    {
        BLResp resp = BLResp.build();
        Manager manager = managerMapper.findById(userId);
        if(manager == null)
        {
            return resp.result(RestResult.OBJECT_NOT_FOUND);
        }
        String newPwd = Md5Utils.encrypt(password);
        if(!newPwd.equals(manager.getPassword()))
        {
            manager = new Manager();
            manager.setId(userId);
            manager.setUpdateTime(new Date());
            manager.setPassword(newPwd);
            managerMapper.updateSkipNull(manager);
        }
        return resp;
    }*/

    @Override
    @Transactional
    public void changePassword(Long managerId, String oldPwd, String newPwd, BLResp resp)
    {
        Manager manager = managerMapper.findById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(oldPwd)))
        {
            resp.result(RestResult.INVALID_PASSCODE);
            return;
        }
        String newPassword = Md5Utils.encrypt(newPwd);
        if(!manager.getPassword().equals(newPassword))
        {
            manager = new Manager();
            manager.setId(managerId);
            manager.setUpdateTime(new Date());
            manager.setPassword(newPassword);
            managerMapper.updateSkipNull(manager);
        }
    }

    @Override
    public void getRoleList(Page page, BLResp resp)
    {
        int total = roleMapper.countAll();
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<Role> roleList = roleMapper.getList();
            List<Map<String, Object>> list = new ArrayList<>(roleList.size());
            for(Role role : roleList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, role.getId() + "");
                map.put(Field.NAME, role.getName());
                map.put(Field.PRIVILEGE, getRoleTopPrivilege(role.getId()));
                map.put(Field.ENABLED, role.getEnabled());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
    }

    @Override
    @Transactional
    public void addRole(String name, List<Long> privilege, BLResp resp)
    {
        Role role = roleMapper.findByName(name);
        if(role != null)
        {
            resp.result(RestResult.ROLE_NAME_EXIST);
            return;
        }
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        Date current = new Date();
        Long roleId = IDUtils.getRoleId(param.getNodeId());
        List<RolePrivilege> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            RolePrivilege rp = new RolePrivilege();
            rp.setCreateTime(current);
            rp.setUpdateTime(current);
            rp.setRoleId(roleId);
            rp.setPrivilegeId(id);
            toAddList.add(rp);
        }
        rolePrivilegeMapper.addList(toAddList);
        role = new Role();
        role.setId(roleId);
        role.setCreateTime(current);
        role.setUpdateTime(current);
        role.setName(name);
        role.setEnabled(TrueOrFalse.TRUE);
        roleMapper.add(role);
    }

    @Override
    @Transactional
    public void editRole(Long roleId, String roleName, List<Long> privilege, BLResp resp)
    {
        Role role = roleMapper.findById(roleId);
        if(role == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Role org = roleMapper.findByName(roleName);
        if(org != null && !roleId.equals(org.getId()))
        {
            resp.result(RestResult.ROLE_NAME_EXIST);
            return;
        }
        rolePrivilegeMapper.deleteByRole(roleId);
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        Date current = new Date();
        List<RolePrivilege> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            RolePrivilege rp = new RolePrivilege();
            rp.setCreateTime(current);
            rp.setUpdateTime(current);
            rp.setRoleId(roleId);
            rp.setPrivilegeId(id);
            toAddList.add(rp);
        }
        rolePrivilegeMapper.addList(toAddList);
        Role roleUpd = new Role();
        roleUpd.setId(roleId);
        roleUpd.setUpdateTime(new Date());
        roleUpd.setName(roleName);
        roleMapper.updateSkipNull(roleUpd);
    }

    @Override
    public void getManagerList(Long roleId, Integer enabled, Page page, BLResp resp)
    {
        int total = managerMapper.countBy(roleId, enabled);
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ManagerInfo> managerList = managerInfoMapper.getListBy(roleId, enabled);
            List<Map<String, Object>> list = new ArrayList<>(managerList.size());
            for(ManagerInfo manager : managerList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, manager.getManagerId() + "");
                map.put(Field.USERNAME, manager.getUsername());
                map.put(Field.ROLE_NAME, manager.getRoleName());
                map.put(Field.NAME, manager.getName());
                map.put(Field.PHONE, manager.getPhone());
                map.put(Field.REGISTER_DATE, DateUtils.format(manager.getRegisterTime(), DateFormat.YYYY_MM_DD));
                map.put(Field.ENABLED, manager.getEnabled());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
    }

    @Override
    public void getManagerInfo(Long managerId, BLResp resp)
    {
        Manager manager = managerMapper.findById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        List<ManagerPrivilege> dataList = managerPrivilegeMapper.getPrivilegeIdListByManager(managerId);
        List<String> privilege = new ArrayList<>(dataList.size());
        for(ManagerPrivilege mp : dataList)
        {
            privilege.add(mp.getPrivilegeId() + "");
        }
        List<Role> roleDataList = roleMapper.getList();
        List<Map<String, Object>> roleList = new ArrayList<>(roleDataList.size());
        for(Role role : roleDataList)
        {
            Map<String, Object> map = new HashMap<>();
            map.put(Field.ID, role.getId() + "");
            map.put(Field.NAME, role.getName());
            roleList.add(map);
        }
        resp.addData(Field.MANAGER_ID, managerId + "");
        resp.addData(Field.ROLE_ID, manager.getRoleId() + "");
        resp.addData(Field.USERNAME, manager.getUsername());
        resp.addData(Field.NAME, manager.getName());
        resp.addData(Field.PHONE, manager.getPhone());
        resp.addData(Field.QQ, manager.getQq());
        resp.addData(Field.ENABLED, manager.getEnabled());
        resp.addData(Field.PRIVILEGE, privilege);
        resp.addData(Field.ROLE_LIST, roleList);
    }

    @Override
    @Transactional
    public void addManager(String username, String password, String name, String phone, String qq, Long roleId,
            Integer enabled, List<Long> privilege, BLResp resp)
    {
        Manager manager = managerMapper.findByUsername(username);
        if(manager != null)
        {
            resp.result(RestResult.USERNAME_EXIST);
            return;
        }
        Date current = new Date();
        Long managerId = IDUtils.getManagerId(param.getNodeId());
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        List<ManagerPrivilege> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            ManagerPrivilege mp = new ManagerPrivilege();
            mp.setCreateTime(current);
            mp.setUpdateTime(current);
            mp.setManagerId(managerId);
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        managerPrivilegeMapper.addList(list);
        manager = new Manager();
        manager.setId(managerId);
        manager.setCreateTime(current);
        manager.setUpdateTime(current);
        manager.setUsername(username);
        manager.setPassword(Md5Utils.encrypt(password));
        manager.setName(name);
        manager.setPhone(phone);
        manager.setQq(qq);
        manager.setRoleId(roleId);
        manager.setEnabled(enabled);
        managerMapper.add(manager);
    }

    @Override
    @Transactional
    public void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, BLResp resp)
    {
        Manager manager = managerMapper.findById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        managerPrivilegeMapper.deleteByManager(managerId);
        List<ManagerPrivilege> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            ManagerPrivilege mp = new ManagerPrivilege();
            mp.setCreateTime(current);
            mp.setUpdateTime(current);
            mp.setManagerId(managerId);
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        managerPrivilegeMapper.addList(list);
        //更新账号信息
        manager.setUpdateTime(current);
        manager.setRoleId(roleId);
        manager.setName(name);
        manager.setPhone(phone);
        manager.setQq(qq);
        manager.setEnabled(enabled);
        managerMapper.updateById(manager);
    }

    @Override
    public Map<String, Object> getRolePrivilegeDetail(Long roleId)
    {
        Map<String, Object> map = new HashMap<>();
        Role role = roleMapper.findById(roleId);
        map.put(Field.ROLE_ID, roleId + "");
        map.put(Field.ROLE_NAME, role != null ? role.getName() : "");
        List<RolePrivilege> dataList = rolePrivilegeMapper.getByRole(roleId);
        List<String> list = new ArrayList<>();
        for(RolePrivilege rp : dataList)
        {
            list.add(rp.getPrivilegeId() + "");
        }
        map.put(Field.PRIVILEGE, list);
        return map;
    }

    @Override
    public List<String> getRolePrivilege(Long roleId)
    {
        List<RolePrivilege> dataList = rolePrivilegeMapper.getByRole(roleId);
        List<String> list = new ArrayList<>();
        for(RolePrivilege rp : dataList)
        {
            list.add(rp.getPrivilegeId() + "");
        }
        return list;
    }

    @Override
    @Transactional
    public void changeStatus(Long roleId, BLResp resp)
    {
        Role role = roleMapper.findById(roleId);
        if(role == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Integer enabled = TrueOrFalse.TRUE;
        if(TrueOrFalse.TRUE.equals(role.getEnabled()))
        {
            enabled = TrueOrFalse.FALSE;
        }
        Role roleUpd = new Role();
        roleUpd.setId(roleId);
        roleUpd.setUpdateTime(new Date());
        roleUpd.setEnabled(enabled);
        roleMapper.updateSkipNull(roleUpd);
        resp.addData(Field.ENABLED, enabled);
    }

    @Override
    public void changeManagerStatus(Long managerId, BLResp resp)
    {
        Manager manager = managerMapper.findById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Integer enabled = TrueOrFalse.TRUE;
        if(TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            enabled = TrueOrFalse.FALSE;
        }
        Manager managerUpd = new Manager();
        managerUpd.setId(managerId);
        managerUpd.setUpdateTime(new Date());
        managerUpd.setEnabled(enabled);
        managerMapper.updateSkipNull(managerUpd);
        resp.addData(Field.ENABLED, enabled);
    }

    @Override
    public void checkIfRoleNameExist(String name, BLResp resp)
    {
        Role role = roleMapper.findByName(name);
        resp.addData(Field.EXIST, role == null ? 0 : 1);
    }

    /**
     * 查询权限列表及其父级权限的ID
     */
    private Set<Long> getRelatedPrivilegeId(List<Long> privilege)
    {
        Set<Long> idSet = new HashSet<>(privilege);
        Set<Long> parentIdSet = getParentId(idSet);
        idSet.addAll(parentIdSet);
        return idSet;
    }

    private Set<Long> getParentId(Set<Long> privilege)
    {
        Set<Long> set = new HashSet<>();
        if(!CollectionUtils.isEmpty(privilege))
        {
            List<Privilege> privilegeList = privilegeMapper.getParentIdByChildId(new ArrayList<>(privilege));
            for(Privilege p : privilegeList)
            {
                if(p.getParentId() != null && p.getParentId() != 0)
                {
                    set.add(p.getParentId());
                }
            }
            set.addAll(getParentId(set));
        }
        return set;
    }

    private List<String> getManagerPrivilegeIdList(Long managerId)
    {
        List<ManagerPrivilege> dataList = managerPrivilegeMapper.getPrivilegeIdListByManager(managerId);
        List<String> list = new ArrayList<>(dataList.size());
        for(ManagerPrivilege mp : dataList)
        {
            list.add(mp.getPrivilegeId() + "");
        }
        return list;
    }

    private String getRoleTopPrivilege(Long roleId)
    {
        List<Privilege> privilegeList = privilegeMapper.getTopListByRole(roleId);
        StringBuilder sb = new StringBuilder();
        for(Privilege p : privilegeList)
        {
            sb.append(",").append(p.getName());
        }
        if(sb.length() > 0)
        {
            return sb.substring(1);
        }
        return "";
    }
}
