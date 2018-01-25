package com.mingdong.bop.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.mingdong.bop.component.Param;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.PrivilegeDTO;
import com.mingdong.core.model.dto.PrivilegeListDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.RoleListDTO;
import com.mingdong.core.model.dto.RolePrivilegeDTO;
import com.mingdong.core.model.dto.RolePrivilegeListDTO;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.service.RemoteSystemService;
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
    private RemoteManagerService remoteManagerService;
    @Resource
    private RemoteSystemService remoteSystemService;

    @Override
    @Transactional
    public void userLogin(String username, String password, String sessionId, BLResp resp)
    {
        // 1. 验证登陆信息正确性
        ManagerDTO manager = remoteManagerService.getManagerByUsername(username);
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
        ManagerDTO managerUpd = new ManagerDTO();
        managerUpd.setId(manager.getId());
        managerUpd.setUpdateTime(current);
        managerUpd.setSessionId(sessionId);
        remoteManagerService.updateManagerSkipNull(managerUpd);
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
        ManagerDTO manager = remoteManagerService.getManagerById(managerId);
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
            manager = new ManagerDTO();
            manager.setId(managerId);
            manager.setUpdateTime(new Date());
            manager.setPassword(newPassword);
            remoteManagerService.updateManagerSkipNull(manager);
        }
    }

    @Override
    public void getRoleList(Page page, BLResp resp)
    {
        RoleListDTO roleListDTO = remoteManagerService.getRoleList(page);
        resp.addData(Field.TOTAL, roleListDTO.getTotal());
        resp.addData(Field.PAGES, roleListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<RoleDTO> roleList = roleListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(roleList.size());
        if(CollectionUtils.isNotEmpty(roleList))
        {
            for(RoleDTO role : roleList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.ID, role.getId() + "");
                map.put(Field.NAME, role.getName());
                map.put(Field.PRIVILEGE, getRoleTopPrivilege(role.getId()));
                map.put(Field.ENABLED, role.getEnabled());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    @Transactional
    public void addRole(String name, List<Long> privilege, BLResp resp)
    {
        RoleDTO role = remoteManagerService.getRoleByName(name);
        if(role != null)
        {
            resp.result(RestResult.ROLE_NAME_EXIST);
            return;
        }
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        Date current = new Date();
        Long roleId = IDUtils.getRoleId(param.getNodeId());
        List<RolePrivilegeDTO> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            RolePrivilegeDTO rp = new RolePrivilegeDTO();
            rp.setCreateTime(current);
            rp.setUpdateTime(current);
            rp.setPrivilegeId(id);
            rp.setRoleId(roleId);
            toAddList.add(rp);
        }
        remoteManagerService.saveRolePrivilegeList(toAddList);
        role = new RoleDTO();
        role.setId(roleId);
        role.setCreateTime(current);
        role.setUpdateTime(current);
        role.setName(name);
        role.setEnabled(TrueOrFalse.TRUE);
        remoteManagerService.saveRole(role);
    }

    @Override
    @Transactional
    public void editRole(Long roleId, String roleName, List<Long> privilege, BLResp resp)
    {
        RoleDTO role = remoteManagerService.getRoleById(roleId);
        if(role == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        RoleDTO org = remoteManagerService.getRoleByName(roleName);
        if(org != null && !roleId.equals(org.getId()))
        {
            resp.result(RestResult.ROLE_NAME_EXIST);
            return;
        }
        remoteManagerService.deleteRolePrivilegeByRoleId(roleId);
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        Date current = new Date();
        List<RolePrivilegeDTO> toAddList = new ArrayList<>();
        for(Long id : allPrivilegeIdList)
        {
            RolePrivilegeDTO rp = new RolePrivilegeDTO();
            rp.setCreateTime(current);
            rp.setUpdateTime(current);
            rp.setRoleId(roleId);
            rp.setPrivilegeId(id);
            toAddList.add(rp);
        }
        remoteManagerService.saveRolePrivilegeList(toAddList);
        RoleDTO roleUpd = new RoleDTO();
        roleUpd.setId(roleId);
        roleUpd.setUpdateTime(new Date());
        roleUpd.setName(roleName);
        remoteManagerService.updateRoleSkipNull(roleUpd);
    }

    @Override
    public void getManagerList(Long roleId, Integer enabled, Page page, BLResp resp)
    {
        ManagerInfoListDTO managerListDTO = remoteManagerService.getManagerInfoList(roleId, enabled, page);
        resp.addData(Field.TOTAL, managerListDTO.getTotal());
        resp.addData(Field.PAGES, managerListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ManagerInfoDTO> managerList = managerListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(managerList.size());
        if(CollectionUtils.isNotEmpty(managerList))
        {
            for(ManagerInfoDTO manager : managerList)
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
        }
        resp.addData(Field.LIST, list);
    }

    @Override
    public void getManagerInfo(Long managerId, BLResp resp)
    {
        ManagerDTO manager = remoteManagerService.getManagerById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        ManagerPrivilegeListDTO managerPrivilegeListDTO = remoteManagerService.getManagerPrivilegeListByManagerId(
                managerId);

        List<ManagerPrivilegeDTO> dataList = managerPrivilegeListDTO.getDataList();
        List<String> privilege = new ArrayList<>(dataList.size());
        for(ManagerPrivilegeDTO mp : dataList)
        {
            privilege.add(mp.getPrivilegeId() + "");
        }
        RoleListDTO roleListDTO = remoteManagerService.getRoleList(null);
        List<RoleDTO> roleDataList = roleListDTO.getDataList();
        List<Map<String, Object>> roleList = new ArrayList<>(roleDataList.size());
        for(RoleDTO role : roleDataList)
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
        ManagerDTO manager = remoteManagerService.getManagerByUsername(username);
        if(manager != null)
        {
            resp.result(RestResult.USERNAME_EXIST);
            return;
        }
        Date current = new Date();
        Long managerId = IDUtils.getManagerId(param.getNodeId());
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        List<ManagerPrivilegeDTO> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            ManagerPrivilegeDTO mp = new ManagerPrivilegeDTO();
            mp.setUpdateTime(current);
            mp.setCreateTime(current);
            mp.setManagerId(managerId);
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        remoteManagerService.saveManagerPrivilegeList(list);
        manager = new ManagerDTO();
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
        remoteManagerService.saveManager(manager);

    }

    @Override
    @Transactional
    public void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, BLResp resp)
    {
        ManagerDTO manager = remoteManagerService.getManagerById(managerId);
        if(manager == null)
        {
            resp.result(RestResult.OBJECT_NOT_FOUND);
            return;
        }
        Date current = new Date();
        Set<Long> allPrivilegeIdList = getRelatedPrivilegeId(privilege);
        remoteManagerService.deleteManagerPrivilegeByManagerId(managerId);
        List<ManagerPrivilegeDTO> list = new ArrayList<>();
        for(Long privilegeId : allPrivilegeIdList)
        {
            ManagerPrivilegeDTO mp = new ManagerPrivilegeDTO();
            mp.setCreateTime(current);
            mp.setUpdateTime(current);
            mp.setManagerId(managerId);
            mp.setPrivilegeId(privilegeId);
            list.add(mp);
        }
        remoteManagerService.saveManagerPrivilegeList(list);
        //更新账号信息
        manager.setUpdateTime(current);
        manager.setRoleId(roleId);
        manager.setName(name);
        manager.setPhone(phone);
        manager.setQq(qq);
        manager.setEnabled(enabled);
        remoteManagerService.updateManagerById(manager);
    }

    @Override
    public Map<String, Object> getRolePrivilegeDetail(Long roleId)
    {
        Map<String, Object> map = new HashMap<>();
        RoleDTO role = remoteManagerService.getRoleById(roleId);
        map.put(Field.ROLE_ID, roleId + "");
        map.put(Field.ROLE_NAME, role != null ? role.getName() : "");
        RolePrivilegeListDTO rolePrivilegeListByRoleId = remoteManagerService.getRolePrivilegeListByRoleId(roleId);
        List<RolePrivilegeDTO> dataList = rolePrivilegeListByRoleId.getDataList();
        List<String> list = new ArrayList<>();
        for(RolePrivilegeDTO rp : dataList)
        {
            list.add(rp.getPrivilegeId() + "");
        }
        map.put(Field.PRIVILEGE, list);
        return map;
    }

    @Override
    public List<String> getRolePrivilege(Long roleId)
    {
        RolePrivilegeListDTO rolePrivilegeListByRoleId = remoteManagerService.getRolePrivilegeListByRoleId(roleId);
        List<RolePrivilegeDTO> dataList = rolePrivilegeListByRoleId.getDataList();
        List<String> list = new ArrayList<>();
        for(RolePrivilegeDTO rp : dataList)
        {
            list.add(rp.getPrivilegeId() + "");
        }
        return list;
    }

    @Override
    @Transactional
    public void changeStatus(Long roleId, BLResp resp)
    {
        RoleDTO role = remoteManagerService.getRoleById(roleId);
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
        RoleDTO roleUpd = new RoleDTO();
        roleUpd.setId(roleId);
        roleUpd.setUpdateTime(new Date());
        roleUpd.setEnabled(enabled);
        remoteManagerService.updateRoleSkipNull(roleUpd);
        resp.addData(Field.ENABLED, enabled);
    }

    @Override
    public void changeManagerStatus(Long managerId, BLResp resp)
    {
        ManagerDTO manager = remoteManagerService.getManagerById(managerId);
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
        ManagerDTO managerUpd = new ManagerDTO();
        managerUpd.setId(managerId);
        managerUpd.setUpdateTime(new Date());
        managerUpd.setEnabled(enabled);
        remoteManagerService.updateManagerSkipNull(managerUpd);
        resp.addData(Field.ENABLED, enabled);
    }

    @Override
    public void checkIfRoleNameExist(String name, BLResp resp)
    {
        RoleDTO role = remoteManagerService.getRoleByName(name);
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
            PrivilegeListDTO privilegeListByIds = remoteSystemService.getPrivilegeListByIds(new ArrayList<>(privilege));

            List<PrivilegeDTO> privilegeList = privilegeListByIds.getDataList();
            for(PrivilegeDTO p : privilegeList)
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
        ManagerPrivilegeListDTO managerPrivilegeListByManagerId =
                remoteManagerService.getManagerPrivilegeListByManagerId(managerId);
        List<ManagerPrivilegeDTO> dataList = managerPrivilegeListByManagerId.getDataList();
        List<String> list = new ArrayList<>(dataList.size());
        for(ManagerPrivilegeDTO mp : dataList)
        {
            list.add(mp.getPrivilegeId() + "");
        }
        return list;
    }

    private String getRoleTopPrivilege(Long roleId)
    {
        PrivilegeListDTO privilegeListDTO = remoteSystemService.getPrivilegeTopListByRoleId(roleId);
        List<PrivilegeDTO> privilegeList = privilegeListDTO.getDataList();
        StringBuilder sb = new StringBuilder();
        for(PrivilegeDTO p : privilegeList)
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
