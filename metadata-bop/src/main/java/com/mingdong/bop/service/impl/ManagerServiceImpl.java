package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.Param;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ManagerDTO;
import com.mingdong.core.model.dto.ManagerInfoDTO;
import com.mingdong.core.model.dto.ManagerInfoListDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeDTO;
import com.mingdong.core.model.dto.ManagerPrivilegeListDTO;
import com.mingdong.core.model.dto.NewManager;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.RoleDTO;
import com.mingdong.core.model.dto.UserInfoDTO;
import com.mingdong.core.service.RemoteManagerService;
import com.mingdong.core.util.IDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService
{
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private RemoteManagerService remoteManagerService;

    @Override
    public void userLogin(String username, String password, String sessionId, RestResp resp)
    {
        // 1. 验证登陆信息正确性
        ManagerDTO manager = remoteManagerService.getManagerByUsername(username);
        if(manager == null)
        {
            resp.setError(RestResult.ACCOUNT_NOT_EXIST);
            return;
        }
        else if(TrueOrFalse.FALSE.equals(manager.getEnabled()))
        {
            resp.setError(RestResult.ACCOUNT_DISABLED);
            return;
        }
        else if(!manager.getPassword().equals(Md5Utils.encrypt(password)))
        {
            resp.setError(RestResult.INVALID_PASSCODE);
            return;
        }
        else if(!TrueOrFalse.TRUE.equals(manager.getEnabled()))
        {
            resp.setError(RestResult.ACCOUNT_DISABLED);
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
        NewManager newManager = new NewManager();
        newManager.setManagerDTO(managerUpd);
        remoteManagerService.updateManagerSkipNull(newManager);
        // 缓存用户的账号及权限
        List<String> privilegeList = getManagerPrivilegeIdList(manager.getId());
        ManagerSession ms = new ManagerSession();
        ms.setManagerId(manager.getId());
        ms.setName(manager.getName());
        ms.setPrivileges(privilegeList);
        ms.setAddAt(current.getTime());
        redisDao.saveManagerSession(sessionId, ms);
        resp.addData(Field.NAME, manager.getName());
    }

    @Override
    public void userLogout(String sessionId)
    {
        redisDao.dropManagerSession(sessionId);
    }

    @Override
    public void changePassword(Long managerId, String oldPwd, String newPwd, RestResp resp)
    {
        ResultDTO resultDTO = remoteManagerService.updateManagerPwd(managerId, newPwd, oldPwd);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getAccountRoleList(Page page, RestListResp res)
    {
        ListDTO<RoleDTO> listDTO = remoteManagerService.getAccountRoleList(null);
        res.setTotal(listDTO.getTotal());
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            List<Map<String, Object>> list = new ArrayList<>(listDTO.getList().size());
            for(RoleDTO o : listDTO.getList())
            {
                Map<String, Object> m = new HashMap<>();
                m.put(Field.ID, o.getId() + "");
                m.put(Field.NAME, o.getName());
                if(CollectionUtils.isEmpty(o.getModuleNameList()))
                {
                    m.put(Field.MODULE, "");
                }
                else
                {
                    m.put(Field.MODULE, StringUtils.join(o.getModuleNameList().toArray(), "，"));
                }
                m.put(Field.ENABLED, o.getEnabled());
                list.add(m);
            }
            res.setList(list);
        }
    }

    @Override
    public void addAccountRole(String name, List<Long> privilegeIdList, RestResp resp)
    {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(name);
        roleDTO.setPrivilegeIdList(privilegeIdList);
        ResultDTO resultDTO = remoteManagerService.addAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editRole(Long roleId, String roleName, List<Long> privilege, RestResp resp)
    {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleId);
        roleDTO.setName(roleName);
        roleDTO.setPrivilegeIdList(privilege);
        ResultDTO resultDTO = remoteManagerService.editAccountRole(roleDTO);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void getManagerList(Long roleId, Integer enabled, Page page, RestListResp res)
    {
        ManagerInfoListDTO managerListDTO = remoteManagerService.getManagerInfoList(roleId, enabled, page);
        res.setTotal(managerListDTO.getTotal());
        List<ManagerInfoDTO> managerList = managerListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(managerList.size());
        if(!CollectionUtils.isEmpty(managerList))
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
        res.setList(list);
    }

    @Override
    public Map<String, Object> getAccountInfoData(Long userId)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(Field.MANAGER_ID, userId + "");
        // 查询账户的基本信息及权限配置信息
        UserInfoDTO userInfoDTO = remoteManagerService.getAccountInfo(userId);
        data.put(Field.USERNAME, userInfoDTO.getUsername());
        data.put(Field.NAME, userInfoDTO.getName());
        data.put(Field.PHONE, userInfoDTO.getPhone());
        data.put(Field.QQ, userInfoDTO.getQq());
        data.put(Field.ROLE_ID, userInfoDTO.getRoleId());
        data.put(Field.ENABLED, userInfoDTO.getEnabled());
        List<String> privilege = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userInfoDTO.getPrivilegeIdList()))
        {
            for(Long o : userInfoDTO.getPrivilegeIdList())
            {
                privilege.add(o + "");
            }
        }
        data.put(Field.PRIVILEGE, privilege);
        // 获取系统账户的角色字典
        ListDTO<DictDTO> listDTO = remoteManagerService.getAccountRoleDict();
        List<Dict> roleDict = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listDTO.getList()))
        {
            for(DictDTO o : listDTO.getList())
            {
                roleDict.add(new Dict(o.getKey(), o.getValue()));
            }
        }
        data.put(Field.ROLE_DICT, roleDict);
        return data;
    }

    @Override
    public void addManager(String username, String password, String name, String phone, String qq, Long roleId,
            Integer enabled, List<Long> privilege, RestResp resp)
    {
        NewManager newManager = new NewManager();
        ManagerDTO manager = new ManagerDTO();
        Long managerId = IDUtils.getManagerId(param.getNodeId());
        Date current = new Date();
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
        newManager.setManagerDTO(manager);
        newManager.setPrivilege(privilege);
        ResultDTO resultDTO = remoteManagerService.addManager(newManager);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void editManager(Long managerId, Long roleId, String name, String phone, String qq, Integer enabled,
            List<Long> privilege, RestResp resp)
    {
        NewManager newManager = new NewManager();
        ManagerDTO manager = new ManagerDTO();
        Date current = new Date();
        manager.setId(managerId);
        manager.setUpdateTime(current);
        manager.setRoleId(roleId);
        manager.setName(name);
        manager.setPhone(phone);
        manager.setQq(qq);
        manager.setEnabled(enabled);
        newManager.setManagerDTO(manager);
        newManager.setPrivilege(privilege);
        ResultDTO resultDTO = remoteManagerService.updateManagerSkipNull(newManager);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public Map<String, Object> getAccountRoleInfo(Long roleId)
    {
        Map<String, Object> map = new HashMap<>();
        RoleDTO roleDTO = remoteManagerService.getAccountRoleInfo(roleId);
        map.put(Field.ROLE_ID, roleId + "");
        map.put(Field.ROLE_NAME, roleDTO.getName());
        List<String> privilegeList = new ArrayList<>();
        for(Long privilegeId : roleDTO.getPrivilegeIdList())
        {
            privilegeList.add(privilegeId + "");
        }
        map.put(Field.PRIVILEGE_LIST, privilegeList);
        return map;
    }

    @Override
    public void getRolePrivilege(Long roleId, RestResp resp)
    {
        List<String> privilegeList = new ArrayList<>();
        RoleDTO roleDTO = remoteManagerService.getAccountRoleInfo(roleId);
        if(!CollectionUtils.isEmpty(roleDTO.getPrivilegeIdList()))
        {
            for(Long o : roleDTO.getPrivilegeIdList())
            {
                privilegeList.add(o + "");
            }
        }
        resp.addData(Field.PRIVILEGE_LIST, privilegeList);
    }

    @Override
    public void changeRoleStatus(Long roleId, Integer status, RestResp resp)
    {
        ResultDTO resultDTO = remoteManagerService.changeRoleStatus(roleId, status);
        resp.setError(resultDTO.getResult());
    }

    @Override
    public void changeManagerStatus(Long managerId, RestResp resp)
    {
        ManagerDTO manager = remoteManagerService.getManagerById(managerId);
        if(manager == null)
        {
            resp.setError(RestResult.OBJECT_NOT_FOUND);
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
        NewManager newManager = new NewManager();
        newManager.setManagerDTO(managerUpd);
        remoteManagerService.updateManagerSkipNull(newManager);
        resp.addData(Field.ENABLED, enabled);
    }

    @Override
    public void checkIfRoleNameExist(String name, RestResp resp)
    {
        resp.addData(Field.EXIST, remoteManagerService.isRoleNameExist(name));
    }

    @Override
    public List<Map<String, Object>> getManagerListMap(Integer enabled)
    {
        ManagerInfoListDTO managerInfoList = remoteManagerService.getManagerInfoList(null, enabled, null);
        List<ManagerInfoDTO> dataList = managerInfoList.getDataList();
        List<Map<String, Object>> dataListMap = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dataList))
        {
            Map<String, Object> dataMap;
            for(ManagerInfoDTO item : dataList)
            {
                dataMap = new HashMap<>();
                dataListMap.add(dataMap);
                dataMap.put(Field.ID, item.getManagerId() + "");
                dataMap.put(Field.NAME, item.getName());
            }
        }
        return dataListMap;
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
}
